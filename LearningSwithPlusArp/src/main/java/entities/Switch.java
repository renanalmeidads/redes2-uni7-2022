package entities;

import entities.arp.ArpPackage;
import entities.ethernet.EthernetPackage;
import factory.PackageFactory;

import javax.management.InstanceNotFoundException;
import java.nio.file.ProviderNotFoundException;
import java.util.*;

public class Switch extends Equipment implements ProviderEquipment {
    private Map<Mac, Port> forwardingTable;

    private Map<Ip,Package> mapPack;

    private Set<Ip> ipsSet;
    private final int MAX_EQUIPMENTS_CONNECTED = 254;

    private final Ip DEFAULT_IP = new Ip("192.168.0.1");

    public Switch(Mac macAddress, Ip ip) {
        super(macAddress);

        ArrayList ports = new ArrayList();

        ports.add(new Port(20));
        ports.add(new Port(21));
        ports.add(new Port(22));
        ports.add(new Port(23));
        ports.add(new Port(24));
        ports.add(new Port(25));
        ports.add(new Port(26));
        ports.add(new Port(27));
        ports.add(new Port(28));
        ports.add(new Port(29));

        setPorts(ports);

        ipsSet = new HashSet<>();

        if(ip != null)
            setIpAddress(ip);
        else
            setIpAddress(this.DEFAULT_IP);

        ipsSet.add(getIpAddress());

        forwardingTable = new HashMap<>();

        mapPack = new HashMap<>();
    }

    public Port getFromForwardingTable(Mac mac) {

        Port port = null;

        if(!this.forwardingTable.isEmpty())
        {
            port = this.forwardingTable.get(mac);
        }

        return port;
    }

    public void setForwardingTable(Map<Mac, Port> forwardingTable) {
        this.forwardingTable = forwardingTable;
    }

    public void addToForwardingTable(Mac mac, Port port)
    {
        if(mac != null && port != null) {
            if (!this.forwardingTable.containsKey(mac)) {
                this.forwardingTable.put(mac, port);
            }
        }
    }

    private void removeFromForwardingTable(Mac mac)
    {
        if(mac != null && forwardingTable.containsKey(mac)) {
            forwardingTable.remove(mac);
        }
    }

    @Override
    public void connect(Link link) throws Exception {
        super.connect(link);

        if(link != null) {
            if (link.getEquipment1() != null && !(link.getEquipment1() instanceof ProviderEquipment)) {
                link.getEquipment1().setIpAddress(getNextIp());
                System.out.println(link.getEquipment1().getMacAddress() + " - IP " + link.getEquipment1().getIpAddress().toString() + " atribuído do equipamento.");
            }

            if (link.getEquipment2() != null && !(link.getEquipment2() instanceof ProviderEquipment)) {
                link.getEquipment2().setIpAddress(getNextIp());
                System.out.println(link.getEquipment2().getMacAddress() + " - IP " + link.getEquipment2().getIpAddress().toString() + " atribuído do equipamento.");
            }
        }
    }

    @Override
    public void disconnect(Link link) throws InstanceNotFoundException
    {
        super.disconnect(link);

        if(link != null)
        {
            Equipment eq = link.getEquipment1();

            removeIpEquipment(eq);

            eq = link.getEquipment2();

            removeIpEquipment(eq);

            removeFromForwardingTable(eq.getMacAddress());
        }
    }

    @Override
    public void forwardPackage(Package pack, Link link) throws Exception {

        if(pack instanceof EthernetPackage)
        {
            EthernetPackage ethernetPackage = (EthernetPackage) pack;

            ethernetPackage.setMacSource(link.getOtherEquipment(this).getMacAddress());

            if(pack.getType() instanceof ArpPackage)
            {
                ArpPackage arpPackage = (ArpPackage) pack.getType();

                addToForwardingTable(arpPackage.getMacSource(), getConnections().get(link));

                if(arpPackage.isArpRequest())
                {
                    this.arp(ethernetPackage, link);
                }
                else if (arpPackage.isArpResponse())
                {
                    if(this.getMacAddress() == arpPackage.getMacDestination())
                    {
                        System.out.println(getMacAddress() + " - " + getIpAddress() + " - ARP response recebido");

                        EthernetPackage packStack = getPackStack();

                        this.send(packStack, link);
                    }
                    else
                    {
                        System.out.println(getMacAddress() + " - " + getIpAddress() + " - ARP ignorado. Encaminhando...");

                        Port port = getFromForwardingTable(arpPackage.getMacDestination());

                        getLinkConnectedByPort(port).send(this, ethernetPackage);
                    }
                }
            }
            else
            {
                Port port = getFromForwardingTable(ethernetPackage.getMacDestination());

                getLinkConnectedByPort(port).send(this, ethernetPackage);
            }
        }
    }

    private void removeIpEquipment(Equipment eq)
    {
        if(eq != null)
        {
            if(eq.getIpAddress() != null)
            {
                System.out.println(eq.getMacAddress() + " - IP " + eq.getIpAddress().toString() + " removido do equipamento.");

                ipsSet.remove(eq.getIpAddress());
                eq.setIpAddress(null);
            }
        }
    }

    public Ip getNextIp() throws ProviderNotFoundException
    {
        Ip currentIp = null;

        removeIpsNotUsed();

        for(int ipLastDigit = 1; ipLastDigit <= MAX_EQUIPMENTS_CONNECTED; ipLastDigit++)
        {
            currentIp = new Ip(getIpAddress().toString());

            currentIp.setPart4(ipLastDigit);

            if(!ipsSet.contains(currentIp))
            {
                ipsSet.add(currentIp);

                return currentIp;
            }
        }

        throw new ProviderNotFoundException("Não existe IP disponível.");
    }

    private void removeIpsNotUsed()
    {
        Set<Ip> ipsNotUsed = new HashSet<>();

        for(Ip ip : ipsSet)
        {
            boolean usingIp = false;

            for(Link link : getConnections().keySet())
            {
                if ((link.getEquipment1() != null && link.getEquipment1().getIpAddress() != null && link.getEquipment1().getIpAddress() == ip) ||
                    (link.getEquipment2() != null && link.getEquipment2().getIpAddress() != null && link.getEquipment2().getIpAddress() == ip))
                {
                    usingIp = true;

                    break;
                }
            }

            if(!usingIp)
            {
                ipsNotUsed.add(ip);
            }
        }

        for(Ip ip : ipsNotUsed)
        {
            ipsSet.remove(ip);
        }
    }
}
