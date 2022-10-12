package extensions;

import entities.Ip;
import entities.Network;

import java.util.ArrayList;
import java.util.List;

public class NetworkExtension {

    public static Network ipNetworkOwner(List<Network> networks, Ip ip)
    {
        Network owner = null;

        List<Network> ownersList = new ArrayList<>();

        for(Network network : networks)
        {
            if(network.ipBelongsToNetwork(ip))
            {
                ownersList.add(network);
            }
        }

        if(ownersList.size() == 1)
        {
            owner = ownersList.get(0);
        }
        else if(ownersList.size() > 1)
        {
            int distinctCount = 0;

            for(Network currentOwner : ownersList)
            {
                String maskBits = String.format("%-" + ip.getMaxBits() + "s", "1".repeat(currentOwner.getMask())).replace(' ', '0');

                String networkPartsBits = Long.toBinaryString(Long.parseLong(currentOwner.getIp().getPartsBits(4), 2)
                        & Long.parseLong(maskBits, 2));
                String ipPartsBits = Long.toBinaryString(Long.parseLong(ip.getPartsBits(4), 2));

                int currentCount = 0;

                for(int index = 0; index < networkPartsBits.length(); index++)
                {
                    if(networkPartsBits.toCharArray()[index] != ipPartsBits.toCharArray()[index])
                    {
                        currentCount++;
                    }
                }

                if(distinctCount == 0)
                {
                    owner = currentOwner;
                    distinctCount = currentCount;
                }
                else if(currentCount < distinctCount)
                {
                    owner = currentOwner;
                    distinctCount = currentCount;
                }
                else if(currentCount == distinctCount)
                {
                    if(owner.getMask() < currentOwner.getMask())
                    {
                        owner = currentOwner;
                    }
                }
            }
        }

        return owner;
    }
}
