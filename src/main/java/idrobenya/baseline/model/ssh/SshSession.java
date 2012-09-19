package idrobenya.baseline.model.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import idrobenya.baseline.model.config.Config;
import idrobenya.baseline.model.server.RemoteMachine;

/**
 * @author Ilya Drabenia
 */
public class SshSession {
    private static Session session;

    public synchronized static Session getInstance() {
        if (session == null || !session.isConnected()) {
            session = makeNewSession();
        }

        return session;
    }

    public synchronized static void close() {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

    private static Session makeNewSession() {
        Config config = Config.getInstance();
        try {
            JSch jsch = new JSch();

            RemoteMachine machineConfig = config.getWebServer().getRemoteMachine();
            session = jsch.getSession(machineConfig.getLogin(), machineConfig.getHost(), machineConfig.getPort());
            session.setPassword(machineConfig.getPassword());

            java.util.Properties sessionProperties = new java.util.Properties();
            sessionProperties.put("StrictHostKeyChecking", "no");
            session.setConfig(sessionProperties);

            session.connect();

            return session;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
