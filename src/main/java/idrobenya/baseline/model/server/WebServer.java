package idrobenya.baseline.model.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * @author Ilya Drabenia
 * @since 08.09.12
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class WebServer {
    private RemoteMachine remoteMachine;
    private String deploymentPath;

    private WebServer() {

    }

    public WebServer(RemoteMachine remoteMachine, String deploymentPath) {
        this.remoteMachine = remoteMachine;
        this.deploymentPath = deploymentPath;
    }

    public RemoteMachine getRemoteMachine() {
        return remoteMachine;
    }

    public String getDeploymentPath() {
        return deploymentPath;
    }

}
