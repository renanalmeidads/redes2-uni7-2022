package factory;

import entities.*;
import entities.Package;
import entities.arp.ArpPackage;
import entities.arp.ArpPackageRequest;
import entities.arp.ArpPackageResponse;
import entities.ethernet.EthernetPackage;
import entities.ip.IpPackage;
import entities.tcp.TcpPackage;

public class PackageFactory {

    public static Package getEthernetPackage(Equipment eq1, Equipment eq2, Link link, String payload)
    {
        TcpPackage tcpPackage = new TcpPackage(eq1.getConnections().get(link), null, payload);
        IpPackage ipPackage = new IpPackage(eq1.getIpAddress(), eq2.getIpAddress(), tcpPackage);
        EthernetPackage ethernetPackage = new EthernetPackage(eq1.getMacAddress(), null, ipPackage);

        return ethernetPackage;
    }

    public static Package getArpPackage(Equipment eq1, Equipment eq2)
    {
        ArpPackage arpPackage = new ArpPackage(eq1.getIpAddress(), eq1.getMacAddress(), eq2.getIpAddress(), null);
        EthernetPackage ethernetPackage = new EthernetPackage(eq1.getMacAddress(), null, arpPackage);

        return ethernetPackage;
    }

    public static Package getArpRequestPackage(Equipment eq1, Ip ip)
    {
        ArpPackage arpPackage = new ArpPackageRequest(eq1.getIpAddress(), eq1.getMacAddress(), ip, null);
        EthernetPackage ethernetPackage = new EthernetPackage(eq1.getMacAddress(), null, arpPackage);

        return ethernetPackage;
    }

    public static Package getArpResponsePackage(Equipment eq1, Ip ip, Mac macDestination)
    {
        ArpPackage arpPackage = new ArpPackageResponse(eq1.getIpAddress(), eq1.getMacAddress(), ip, macDestination);
        EthernetPackage ethernetPackage = new EthernetPackage(eq1.getMacAddress(), macDestination, arpPackage);

        return ethernetPackage;
    }
}
