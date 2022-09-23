package entities;

import java.util.ArrayList;

public class Host extends Equipment {

    public Host(Mac macAddress) {
        super(macAddress);

        ArrayList ports = new ArrayList();

        ports.add(new Port(1));

        setPorts(ports);
    }

    @Override
    public void send(Package pack) throws Exception {

        if(pack != null)
        {
            for(Port port : getPorts())
            {
                if(port.isConnected())
                {
                    Link link = getLinkConnectedByPort(port);

                    link.send(this, pack);
                }
                else {
                    System.out.println("Porta " + port.getId() + " desconectada");
                }
            }
        }
        else
        {
            throw new Exception(this.getMacAddress() + " - Package inválido.");
        }
    }

    @Override
    public void receive(Package pack) throws Exception {

        if(pack != null)
        {
            if(pack instanceof ArpPackage)
            {
                if(pack.getIpDestino() != null)
                {
                    if(getIpAddress() == pack.getIpDestino()) {
                        System.out.println(getMacAddress() + " - " + getIpAddress() + " - ARP aceito.");

                        ((ArpPackage) pack).setMac(getMacAddress());

                        send(pack);
                    }
                    else {
                        System.out.println(getMacAddress() + " - " + getIpAddress() + " - ARP ignorado.");
                    }
                }
                else
                {
                    System.out.println("O IP da requisição ARP está nulo.");
                }
            }
            else if(pack instanceof TcpPackage)
            {
                System.out.println(getMacAddress() + " - Payload: " + pack.getPayload());
            }
        }
    }
}
