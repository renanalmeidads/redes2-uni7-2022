package entities;

import entities.arp.ArpPackage;
import entities.ethernet.EthernetPackage;
import entities.ip.IpPackage;
import entities.tcp.TcpPackage;
import factory.PackageFactory;

import javax.management.InstanceNotFoundException;
import java.util.*;

public abstract class Equipment {
    private Ip ipAddress;

    private Mac macAddress;

    private ArrayList<Port> ports;

    private Map<Link, Port> connections;

    private Map<Ip, Mac> ipMacMap;

    private Stack<EthernetPackage> packStack;

    public Equipment(Mac macAddress) {
        this.macAddress = macAddress;

        ipMacMap = new HashMap<>();
    }

    public void setPorts(ArrayList<Port> ports) {
        this.ports = ports;
    }

    public ArrayList<Port> getPorts() {
        return ports;
    }

    public Link getLinkConnectedByPort(Port p)
    {
        Link link = null;

        if(!connections.isEmpty())
        {
            link = connections.entrySet().stream().filter(e -> p == e.getValue()).map(Map.Entry::getKey).findFirst().get();
        }

        return link;
    }

    public Mac getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(Mac macAddress) {
        this.macAddress = macAddress;
    }

    public void setIpAddress(Ip ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Ip getIpAddress() {
        return ipAddress;
    }

    public void addIpMacMap(Ip ip, Mac mac) {
        if(ip != null && mac != null)
        {
            if(!this.ipMacMap.containsKey(ip))
            {
                this.ipMacMap.put(ip, mac);
            }
        }
    }

    public boolean ipMacExists(Ip ip)
    {
        boolean ipMacExists = false;

        if(ip != null)
        {
            if(this.ipMacMap.containsKey(ip))
            {
                ipMacExists = true;
            }
        }

        return ipMacExists;
    }

    public Mac getMacFromIp(Ip ip)
    {
        Mac mac = null;

        if(ip != null)
        {
            if(ipMacExists(ip))
            {
                mac = this.ipMacMap.get(ip);
            }
        }

        return mac;
    }

    public void addPackStack(EthernetPackage pack)
    {
        if(packStack == null)
            packStack = new Stack<>();

        if(pack != null)
            packStack.add(pack);
    }

    public EthernetPackage getPackStack()
    {
        EthernetPackage pack;

        if(packStack == null || packStack.isEmpty())
            return null;

        pack = packStack.pop();

        return pack;
    }

    public Map<Link, Port> getConnections() {
        return connections;
    }

    public void connect(Link link) throws Exception {

        Port port = null;

        if (connections == null)
            connections = new HashMap<>();

        if (!ports.isEmpty()) {
            for (Port p : ports) {
                if (!p.isConnected()) {
                    port = p;

                    break;
                }
            }
        }

        if (link != null && port != null) {
            link.connect(this);

            port.connect();

            connections.put(link, port);

            System.out.println(this.getMacAddress() + " - Conexão realizada com sucesso.");

        } else if (port == null) {
            throw new Exception(this.getMacAddress() + " - O equipamento não tem mais portas disponíveis.");
        }

        if(link.getOtherEquipment(this) instanceof ProviderEquipment)
        {
            this.setIpAddress(((ProviderEquipment) link.getOtherEquipment(this)).getNextIp());
        }
    }

    public void disconnect(Link link) throws InstanceNotFoundException {

        if(link != null)
        {
            if(connections != null && !connections.isEmpty()) {
                Port p = connections.get(link);

                if(p != null) {
                    p.disconnect();

                    link.disconnect(this);

                    connections.remove(link);

                    System.out.println(this.getMacAddress() + " - Desconexão realizada com sucesso.");
                }
                else
                {
                    throw new InstanceNotFoundException(this.getMacAddress() + " - Não foi encontrada conexão para o link fornecido.");
                }
            }
            else
            {
                System.out.println(this.getMacAddress() + " - Disconnect - Nenhuma conexão com este host.");
            }
        }
    }

    public void send(Package pack) throws Exception {
        this.send(pack, null);
    }

    public void send(Package pack, Link link) throws Exception {

        if(pack != null)
        {
            if(pack instanceof EthernetPackage) {

                EthernetPackage ethernetPackage = (EthernetPackage) pack;

                if (pack.getType() instanceof IpPackage) {
                    IpPackage ipPack = (IpPackage) pack.getType();

                    if (ipMacExists(ipPack.getIpDestination())) {

                        ethernetPackage.setMacDestination(getMacFromIp(ipPack.getIpDestination()));

                        if(ipPack.getType() instanceof TcpPackage)
                        {
                            TcpPackage tcpPack = (TcpPackage) ipPack.getType();

                            link.send(this, pack);
                        }
                    } else {
                        addPackStack((EthernetPackage) pack);

                        arp(ipPack.getIpDestination(), link);
                    }
                } else if (pack.getType() instanceof ArpPackage) {
                    ArpPackage arpPackage = (ArpPackage) pack.getType();

                    if(arpPackage.isArpRequest()) {
                        addIpMacMap(arpPackage.getIpSource(), arpPackage.getMacSource());

                        arp(ethernetPackage, link);
                    }
                    else if(arpPackage.isArpResponse())
                    {
                        forwardPackage(ethernetPackage, link);
                    }
                }
            }
        }
        else
        {
            throw new Exception(this.getMacAddress() + " - Package inválido.");
        }
    }

    public void receive(Package pack, Link link) throws Exception {

        if(pack instanceof EthernetPackage)
        {
            EthernetPackage ethernetPackage = (EthernetPackage) pack;

            if(pack.getType() instanceof ArpPackage)
            {
                ArpPackage arpPackage = (ArpPackage) pack.getType();

                if(arpPackage.getIpDestination() != null)
                {
                    if(arpPackage.isArpRequest()) {

                        addIpMacMap(arpPackage.getIpSource(), arpPackage.getMacSource());

                        if (getIpAddress() == arpPackage.getIpDestination()) {
                            System.out.println(getMacAddress() + " - " + getIpAddress() + " - ARP aceito.");

                            EthernetPackage arpPackageResponse = (EthernetPackage) PackageFactory.getArpResponsePackage(this, arpPackage.getIpSource(), arpPackage.getMacSource());

                            System.out.println(getMacAddress() + " - " + getIpAddress() + " - Enviando ARP response...");

                            link.send(this, arpPackageResponse);
                        } else {
                            forwardPackage(pack, link);
                        }
                    }
                    else if(arpPackage.isArpResponse())
                    {
                        addIpMacMap(arpPackage.getIpSource(), arpPackage.getMacSource());

                        forwardPackage(pack, link);
                    }
                }
                else
                {
                    System.out.println("O IP da requisição ARP está nulo.");
                }
            }
            else if(ethernetPackage.getType() instanceof IpPackage)
            {
                IpPackage ipPackage = (IpPackage) ethernetPackage.getType();

                if(ipPackage.getType() instanceof TcpPackage)
                {
                    if(this.getMacAddress() == ethernetPackage.getMacDestination())
                    {
                        TcpPackage tcpPackage = (TcpPackage) ipPackage.getType();

                        System.out.println(getMacAddress() + " - " + getIpAddress() + " - Payload: " + tcpPackage.getPayload());
                    }
                    else
                    {
                        forwardPackage(pack, link);
                    }
                }
            }
        }
    }

    public void arp(Ip ip, Link link) throws Exception {

        EthernetPackage pack = (EthernetPackage) PackageFactory.getArpRequestPackage(this, ip);

        arp(pack, link);
    }

    public void arp(EthernetPackage pack, Link link) throws Exception {

        if(pack.isBroadcast())
        {
            broadcast(pack);
        }
        else
        {
            this.send(pack, link);
        }
    }

    public void broadcast(Package pack) throws Exception {

        for(Link link : getConnections().keySet())
        {
            if(pack instanceof EthernetPackage)
            {
                EthernetPackage ethernetPackage = (EthernetPackage) pack;

                Equipment eq = link.getOtherEquipment(this);

                if(eq != null) {
                    if (eq.getMacAddress() != ethernetPackage.getMacSource()) {
                        link.send(this, pack);
                    }
                }
                else
                {
                    System.out.println(this.getMacAddress() + " - Link - " + link.getId() + " não conectado à outro equipamento.");
                }
            }
            else {
                link.send(this, pack);
            }
        }
    }

    public abstract void addToForwardingTable(Mac mac, Port port);

    public abstract void forwardPackage(Package pack, Link link) throws Exception;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipment equipment = (Equipment) o;
        return macAddress.equals(equipment.macAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(macAddress);
    }
}
