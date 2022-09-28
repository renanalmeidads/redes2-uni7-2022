package entities.ip;

import entities.Ip;
import entities.Package;

public class IpPackage extends Package {

    private Ip ipSource;

    private Ip ipDestination;

    public IpPackage(Ip ipSource, Ip ipDestination, Package type) {
        this.ipSource = ipSource;
        this.ipDestination = ipDestination;
        setType(type);
    }

    public Ip getIpSource() {
        return ipSource;
    }

    public Ip getIpDestination() {
        return ipDestination;
    }

    @Override
    public String toString() {
        return "IpPackage{" +
                "ipSource=" + ipSource +
                ", ipDestination=" + ipDestination +
                ", type=" + this.getType() +
                '}';
    }
}
