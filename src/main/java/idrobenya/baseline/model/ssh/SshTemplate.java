package idrobenya.baseline.model.ssh;

import com.jcraft.jsch.*;
import idrobenya.baseline.model.config.Config;
import idrobenya.baseline.model.server.RemoteMachine;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author Ilya Drabenia
 * @since 09.09.12
 */
public abstract class SshTemplate<T> {
    private Config config;

    public SshTemplate(Config config) {
        this.config = config;
    }

    public T execute() {
        Session session = null;

        try {
            JSch jsch = new JSch();

            RemoteMachine machineConfig = config.getWebServer().getRemoteMachine();
            session = jsch.getSession(machineConfig.getLogin(), machineConfig.getHost(), machineConfig.getPort());
            session.setPassword(machineConfig.getPassword());

            java.util.Properties sessionProperties = new java.util.Properties();
            sessionProperties.put("StrictHostKeyChecking", "no");
            session.setConfig(sessionProperties);

            session.connect();

            return processWithSession(session);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (session != null) {
                session.disconnect();
            }
        }
    }

    public void executeCommand(Session session, String command) throws Exception {
        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand(command);

        channel.connect();
        Thread.sleep(2000);
        channel.disconnect();
    }

    public ChannelSftp getSftpChannel(Session session) throws Exception {
        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();
        return channelSftp;
    }

    protected abstract T processWithSession(Session session) throws Exception;
}
