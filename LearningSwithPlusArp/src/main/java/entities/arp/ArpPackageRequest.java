package entities.arp;

import entities.Ip;
import entities.Mac;

public class ArpPackageRequest extends ArpPackage {

    public ArpPackageRequest(Ip ipSource, Mac macSource, Ip ipDestination, Mac macDestination) {
        super(ipSource, macSource, ipDestination, macDestination);

        setOpCodeRequest();
    }

    @Override
    public String toString() {
        return "ArpPackageRequest{" +
                "ipSource=" + this.getIpSource() +
                ", macSource=" + this.getMacSource() +
                ", ipDestination=" + this.getIpDestination() +
                ", macDestination=" + this.getMacDestination() +
                ", opCode=" + this.getOpCode() +
                '}';
    }
}
