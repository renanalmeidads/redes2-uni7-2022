package entities;

import entities.arp.ArpPackage;
import entities.ethernet.EthernetPackage;

import java.util.ArrayList;

public class Host extends Equipment {

    public Host(Mac macAddress) {
        super(macAddress);

        ArrayList ports = new ArrayList();

        ports.add(new Port(1));

        setPorts(ports);
    }

    @Override
    public void addToForwardingTable(Mac mac, Port port) {

    }

    @Override
    public void forwardPackage(Package pack, Link link) throws Exception {

        if(pack instanceof EthernetPackage)
        {
            EthernetPackage ethernetPackage = (EthernetPackage) pack;

            if(pack.getType() instanceof ArpPackage)
            {
                ArpPackage arpPackage = (ArpPackage) pack.getType();

                if (arpPackage.isArpResponse())
                {
                    if(this.getMacAddress() == arpPackage.getMacDestination())
                    {
                        System.out.println(getMacAddress() + " - " + getIpAddress() + " - ARP response recebido");

                        EthernetPackage packStack = getPackStack();

                        packStack.setMacDestination(arpPackage.getMacSource());

                        System.out.println(getMacAddress() + " - " + getIpAddress() + " - Continuando envio do pacote original...");

                        link.send(this, packStack);
                    }
                }
                else if(arpPackage.isArpRequest())
                {
                    System.out.println(getMacAddress() + " - " + getIpAddress() + " - ARP ignorado.");
                }
            }
        }
    }


}
