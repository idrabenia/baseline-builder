package idrobenya.baseline.model.ssh;

import com.jcraft.jsch.*;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author Ilya Drabenia
 * @since 09.09.12
 */
public abstract class SshTemplate<T> {

    public SshTemplate() {
    }

    public T execute() {
        Session session = null;

        try {
            return processWithSession(SshSession.getInstance());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void executeCommand(Session session, String command) throws Exception {
        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        channel.setPty(true);
        channel.setAgentForwarding(true);
        channel.setXForwarding(true);
        channel.setCommand(command);
//
//        channel.connect();
//        Thread.sleep(2000);
//        channel.disconnect();

//        ChannelShell channel = (ChannelShell) session.openChannel("shell");

        channel.setInputStream(null);

        InputStream in = channel.getInputStream();
        channel.connect();

//        PrintStream output = new PrintStream(channel.getOutputStream());
//        output.println(command);
//        output.flush();

        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) break;
                System.out.print(new String(tmp, 0, i));
            }
            if (channel.isClosed()) {
                System.out.println("exit-status: " + channel.getExitStatus());
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception ee) {
            }
        }
        channel.disconnect();
        System.out.println("DONE");
    }

    public ChannelSftp getSftpChannel(Session session) throws Exception {
        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();
        return channelSftp;
    }

    protected abstract T processWithSession(Session session) throws Exception;
}
