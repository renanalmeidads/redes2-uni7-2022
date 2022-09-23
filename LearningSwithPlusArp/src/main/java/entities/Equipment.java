package entities;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class Equipment {
    private Ip ipAddress;

    private Mac macAddress;

    private ArrayList<Port> ports;

    private Map<Link, Port> connections;

    public Equipment(Mac macAddress) {
        this.macAddress = macAddress;
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

            port.Connect();

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
                    p.Disconnect();

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

    public void send(Package pack) throws Exception {}

    public void receive(Package pack) throws Exception {}

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
