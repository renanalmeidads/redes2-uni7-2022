package entities.tcp;

import entities.Package;
import entities.Port;

public class TcpPackage extends Package {

    private Port portSource;

    private Port portDestination;

    private String payload;

    public TcpPackage(Port portSource, Port portDestination, String payload) {
        this.portSource = portSource;
        this.portDestination = portDestination;
        this.payload = payload;
    }

    public Port getPortSource() {
        return portSource;
    }

    public Port getPortDestination() {
        return portDestination;
    }

    public String getPayload() {
        return payload;
    }
}
