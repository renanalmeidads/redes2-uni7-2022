package entities.arp;

import entities.Ip;
import entities.Mac;

public class ArpPackageResponse extends ArpPackage {

    public ArpPackageResponse(Ip ipSource, Mac macSource, Ip ipDestination, Mac macDestination) {
        super(ipSource, macSource, ipDestination, macDestination);

        setOpCodeResponse();
    }
}
