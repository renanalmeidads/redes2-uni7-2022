package entities;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Network {

    private final Ip ip;

    private int mask;

    private final char SEPARATOR = '/';

    private List<Network> subNetworks;

    public Network(Ip ip, int mask)
    {
        this.ip = ip;

        setMask(mask);
    }

    private void setMask(int mask)
    {
        if(mask <= ip.getMaxBits())
        {
            this.mask = mask;
        }
    }

    public Ip getIp() {
        return ip;
    }

    public Ip getLastIp()
    {
        Ip lastIp = new Ip();

        int compBitsAmount = ip.getMaxBits() - mask;

        for(int blockPosition = ip.getMaxBlocksAmount(); blockPosition > 0; blockPosition--){

            int bitsOperationAmount = compBitsAmount >= ip.getBitsBlockAmount() ? ip.getBitsBlockAmount() : compBitsAmount % ip.getBitsBlockAmount();

            String bitsOperation = bitsOperationAmount > 0 ? "1".repeat(bitsOperationAmount) : "0";

            int blockValue = ip.getPosition(blockPosition) | Integer.parseInt(bitsOperation, 2);

            lastIp.setPosition(blockPosition, blockValue);

            compBitsAmount -= bitsOperationAmount;
        }

        return lastIp;
    }

    public Ip getFirstIp()
    {
        return getIp();
    }

    public List<IpRange<Ip, Ip>> getIpRanges(int subnetsAmount)
    {
        List<IpRange<Ip, Ip>> ipRanges = new ArrayList<>();

        if(subnetsAmount > 0)
        {
            int ipRangesAmount = (int)Math.ceil(Math.log(subnetsAmount) / Math.log(2));

            if((ip.getMaxBits() - this.mask) < (ip.getBitsBlockAmount() + ipRangesAmount))
            {
                throw new InvalidParameterException("The network cannot provide " + subnetsAmount + " ip ranges.");
            }

            for(int i = 0; i < Math.pow(2, ipRangesAmount); i++)
            {
                Ip ip = new Ip();

                String ipPartsBits = this.ip.getPartsBits(3);

                int subnetIp = Integer.parseInt(ipPartsBits, 2) + i;

                ip.setPartsByBits(Integer.toBinaryString(subnetIp));

                Ip startIp = ip;

                startIp.setPart4(this.ip.getPart4());

                Network network = new Network(startIp, 24);

                Ip lastIp = network.getLastIp();

                IpRange<Ip, Ip> ipRange = new IpRange<>(startIp, lastIp);

                ipRanges.add(ipRange);
            }
        }

        return ipRanges;
    }

    @Override
    public String toString() {
        return  ip + Character.toString(SEPARATOR) + mask;
    }
}
