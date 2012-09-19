package idrobenya.baseline.model;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import idrobenya.baseline.model.config.Config;
import idrobenya.baseline.model.ssh.SshTemplate;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import org.apache.commons.lang.Validate;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * @author Ilya Drabenia
 * @since 01.09.12
 */
public class Baseline {
    private static Logger LOG = Logger.getLogger(Baseline.class.getName());

    private String name;
    private String pathToDirectory;

    public Baseline() {

    }

    public Baseline(String name) {
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathToDirectory() {
        return pathToDirectory;
    }

    public void setPathToDirectory(String pathToDirectory) {
        this.pathToDirectory = pathToDirectory;
    }

    public void makeBaseline() {
        try {
            File archive = compressWireframesFolder();
            deployBaseline(archive);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private File compressWireframesFolder() throws Exception {
        String archivePath = makeTempFilePath(name, ".zip");

        ZipFile zipFile = new ZipFile(archivePath);
        zipFile.setRunInThread(false);

        ZipParameters zipParams = new ZipParameters();
        zipParams.setRootFolderInZip(name);
        zipFile.setFileNameCharset("UTF-8");
        zipParams.setIncludeRootFolder(false);
        zipFile.createZipFileFromFolder(this.pathToDirectory, zipParams, false, -1);

        File archiveFile = zipFile.getFile();
        archiveFile.deleteOnExit();
        return archiveFile;
    }

    private String makeTempFilePath(String prefix, String suffix) {
        String tempFolder = System.getProperty("java.io.tmpdir");

        String name = prefix + System.currentTimeMillis() + suffix;

        return tempFolder + name;
    }

    private void deployBaseline(final File wireframesArchive) {
        Validate.notNull(wireframesArchive);

        SshTemplate<Void> deployTemplate = new SshTemplate<Void>() {
            @Override
            protected Void processWithSession(Session session) throws Exception {
                return deployBaselineUsingSsh(this, session, wireframesArchive);
            }
        };
        deployTemplate.execute();
    }

    private Void deployBaselineUsingSsh(SshTemplate<Void> template, Session session, File wireframesArchive)
            throws Exception {
        Validate.isTrue(wireframesArchive.exists());
        final String DEPLOYMENT_PATH = Config.getInstance().getWebServer().getDeploymentPath();

        ChannelSftp channelSftp = template.getSftpChannel(session);
        channelSftp.cd(DEPLOYMENT_PATH);
        channelSftp.put(new FileInputStream(wireframesArchive), wireframesArchive.getName());
        channelSftp.disconnect();

        Thread.sleep(1000);

        template.executeCommand(session, "unzip -o " + DEPLOYMENT_PATH + "/" + wireframesArchive.getName()
                + " -d " + DEPLOYMENT_PATH);

        template.executeCommand(session, "rm -f " + DEPLOYMENT_PATH + "/" + wireframesArchive.getName());
//        template.executeCommand(session, "make_baseline.rb " + DEPLOYMENT_PATH + " " + wireframesArchive.getName());

        return null;
    }

    public void delete() {
        SshTemplate<Void> deleteTemplate = new SshTemplate<Void>() {
            @Override
            protected Void processWithSession(Session session) throws Exception {
                return executeDeleteCommandUsingSsh(this, session);
            }
        };

        deleteTemplate.execute();
    }

    private Void executeDeleteCommandUsingSsh(SshTemplate<Void> template, Session session) throws Exception {
        final String DEPLOYMENT_PATH = Config.getInstance().getWebServer().getDeploymentPath();
        template.executeCommand(session, "nohup rm -rf " + DEPLOYMENT_PATH + "/" + this.name);

        return null;
    }

    public static List<Baseline> list() {
        final Config SERVER_CONFIG = Config.getInstance();

        SshTemplate<Vector> foldersTemplate = new SshTemplate<Vector>() {
            @Override
            protected Vector processWithSession(Session session) throws Exception {
                return listDirectoriesUsingSsh(this, session);
            }
        };

        List<Baseline> baselines = new ArrayList<>();
        for (Object curFolder : foldersTemplate.execute()) {
            String name = ((ChannelSftp.LsEntry) curFolder).getFilename();
            baselines.add(new Baseline(name));
        }

        return baselines;
    }

    private static Vector listDirectoriesUsingSsh(SshTemplate template, Session session) throws Exception {
        Config SERVER_CONFIG = Config.getInstance();
        ChannelSftp channelSftp = template.getSftpChannel(session);

        channelSftp.cd(SERVER_CONFIG.getWebServer().getDeploymentPath());
        return channelSftp.ls("*");
    }

    @Override
    public String toString() {
        return name;
    }
}
