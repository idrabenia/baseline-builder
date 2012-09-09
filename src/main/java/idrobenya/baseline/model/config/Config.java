package idrobenya.baseline.model.config;

import idrobenya.baseline.Main;
import idrobenya.baseline.model.server.WebServer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;

/**
 * @author Ilya Drabenia
 * @since 08.09.12
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Config {
    private static Config instance;
    private static final String CONFIG_FILE_NAME = "config.xml";

    private WebServer webServer;

    private Config() {
    }

    public static Config getInstance() {
        if (instance == null) {
            try {
                instance = load();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        return instance;
    }

    public WebServer getWebServer() {
        return webServer;
    }

    private static Config load() throws Exception {
        JAXBContext context = JAXBContext.newInstance(Config.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        return (Config) unmarshaller.unmarshal(getConfigFile());
    }

    private static File getConfigFile() {
        File sourceLocation =  new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());

        String applicationFolder;
        if (sourceLocation.isDirectory()) {
            applicationFolder = sourceLocation.getAbsolutePath();
        } else {
            applicationFolder = sourceLocation.getParent();
        }

        File configFile = new File(applicationFolder, CONFIG_FILE_NAME);

        if (!configFile.exists()) {
            throw new RuntimeException(configFile.getAbsolutePath() + " do not exists");
        }

        return configFile;
    }

}
