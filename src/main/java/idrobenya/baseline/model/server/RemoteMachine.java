package idrobenya.baseline.model.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * @author Ilya Drabenia
 * @since 08.09.12
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RemoteMachine {
    private String host;
    private int port;
    private String login;
    private String password;
    private int shellDelay;

    private RemoteMachine() {

    }

    public RemoteMachine(String host, int port, String login, String password, int shellDelay) {
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
        this.shellDelay = shellDelay;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public int getShellDelay() {
        return shellDelay;
    }

}
