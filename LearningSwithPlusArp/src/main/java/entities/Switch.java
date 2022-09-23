package entities;

import javax.management.InstanceNotFoundException;
import java.nio.file.ProviderNotFoundException;
import java.util.*;

public class Switch extends Equipment implements ProviderEquipment {
    private Map<Mac, Port> forwardingTable;

    private Map<Ip, Mac> ipMacMap;

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

        ipMacMap = new HashMap<>();

        mapPack = new HashMap<>();
    }

    public Map<Mac, Port> getForwardingTable() {
        return forwardingTable;
    }

    public void setForwardingTable(Map<Mac, Port> forwardingTable) {
        this.forwardingTable = forwardingTable;
    }

    private void addToForwardingTable(Mac mac, Port port)
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

                addToForwardingTable(link.getEquipment1().getMacAddress(), getConnections().get(link));
            }

            if (link.getEquipment2() != null && !(link.getEquipment2() instanceof ProviderEquipment)) {
                link.getEquipment2().setIpAddress(getNextIp());
                System.out.println(link.getEquipment2().getMacAddress() + " - IP " + link.getEquipment2().getIpAddress().toString() + " atribuído do equipamento.");

                addToForwardingTable(link.getEquipment2().getMacAddress(), getConnections().get(link));
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

    @Override
    public void receive(Package pack) throws Exception {

        if(pack != null)
        {
            if(pack instanceof ArpPackage)
            {
                if(pack.getIpDestino() != null && ((ArpPackage) pack).getMac() != null)
                {
                    ipMacMap.put(pack.getIpDestino(), ((ArpPackage) pack).getMac());

                    receive(mapPack.get(pack.getIpDestino()));
                }
            }
            else if (pack instanceof TcpPackage) {

                if (pack.getIpDestino() != null) {

                    if(!mapPack.containsKey(pack.getIpDestino())) {
                        mapPack.put(pack.getIpDestino(), pack);
                    }

                    if (ipMacMap.containsKey(pack.getIpDestino())) {

                        Package sendPackage = mapPack.get(pack.getIpDestino());

                        send(sendPackage, ipMacMap.get(pack.getIpDestino()));

                        mapPack.remove(pack.getIpDestino());
                    } else {
                        ARP(pack.getIpDestino());
                    }
                } else {
                    throw new Exception("O IP de destino do package deve ser informado.");
                }
            }
        }
    }

    private void send(Package pack, Mac mac) throws Exception {

        if(mac != null) {

            Port port = forwardingTable.get(mac);

            if(port != null) {
                Link link = getLinkConnectedByPort(port);

                link.send(this, pack);
            }
            else
            {
                System.out.println("Nenhuma porta encontrada para o mac informado.");
            }
        }
    }

    private void ARP(Ip ip) throws Exception {

        ArpPackage pack = new ArpPackage();

        pack.setIpDestino(ip);

        Broadcast(pack);
    }

    private void Broadcast(ArpPackage arpPack) throws Exception {

        for(Link link : getConnections().keySet())
        {
            link.send(this, arpPack);
        }
    }
}
