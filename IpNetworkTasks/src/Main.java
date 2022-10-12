import entities.Ip;
import entities.IpRange;
import entities.Network;
import extensions.NetworkExtension;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Network n = new Network(new Ip("192.168.47.33"), 20);

        Ip lastIp = n.getLastIp();

        List<IpRange<Ip, Ip>> ipRanges = n.getIpRanges(16);

        boolean ipBelongs = n.ipBelongsToNetwork(new Ip("192.168.45.1"));

        List<Network> networks = new ArrayList<>();
        networks.add(new Network(new Ip("192.168.47.33"), 20));
        networks.add(new Network(new Ip("192.168.45.33"), 24));
        networks.add(new Network(new Ip("192.168.45.33"), 25));

        Network ownerNetwork = NetworkExtension.ipNetworkOwner(networks, new Ip("192.168.45.1"));
    }
}