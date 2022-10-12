package entities;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Network {

    private final Ip ip;

    private int mask;

    private final char SEPARATOR = '/';

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

    public int getMask() {
        return mask;
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

            if((ip.getMaxBits() - getMask()) < (ip.getBitsBlockAmount() + ipRangesAmount))
            {
                throw new InvalidParameterException("The network cannot provide " + subnetsAmount + " ip ranges.");
            }

            String maskBits = String.format("%-" + this.ip.getMaxBits() + "s", "1".repeat(getMask())).replace(' ', '0');

            String ipPartsBits = Long.toBinaryString(Long.parseLong(this.ip.getPartsBits(4), 2) & Long.parseLong(maskBits, 2));

            long subnetIp = Long.parseLong(ipPartsBits, 2);

            for(int i = 0; i < Math.pow(2, ipRangesAmount); i++)
            {
                Ip ip = new Ip();

                int defaultSum = i > 0 ? ip.getMaxBlockValue() + 1 : 0;

                subnetIp = subnetIp + defaultSum;

                ip.setPartsByBits(Long.toBinaryString(subnetIp));

                Network network = new Network(ip, 24);

                Ip lastIp = network.getLastIp();

                IpRange<Ip, Ip> ipRange = new IpRange<>(ip, lastIp);

                ipRanges.add(ipRange);
            }
        }

        return ipRanges;
    }

    public boolean ipBelongsToNetwork(Ip ip)
    {
        String maskBits = String.format("%-" + this.ip.getMaxBits() + "s", "1".repeat(getMask())).replace(' ', '0');

        String networkPartsBits = Long.toBinaryString(Long.parseLong(this.ip.getPartsBits(4), 2) & Long.parseLong(maskBits, 2));
        String ipPartsBits = Long.toBinaryString(Long.parseLong(ip.getPartsBits(4), 2) & Long.parseLong(maskBits, 2));

        return networkPartsBits.equals(ipPartsBits);
    }

    @Override
    public String toString() {
        return  ip + Character.toString(SEPARATOR) + mask;
    }
}
