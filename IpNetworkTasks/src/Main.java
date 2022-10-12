import entities.Ip;
import entities.IpRange;
import entities.Network;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Network n = new Network(new Ip("192.168.48.33"), 14);

        Ip lastIp = n.getLastIp();

        List<IpRange<Ip, Ip>> ipRanges = n.getIpRanges(1024);

        //Comentário
    }
}