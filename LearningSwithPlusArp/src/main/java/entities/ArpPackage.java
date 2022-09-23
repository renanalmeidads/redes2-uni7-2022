package entities;

public class ArpPackage extends Package {

    private Ip ip;

    private Mac mac;

    public Ip getIpDestino() {
        return ip;
    }

    public void setIpDestino(Ip ipDestino) {
        this.ip = ipDestino;
    }

    public Mac getMac() {
        return mac;
    }

    public void setMac(Mac mac) {
        this.mac = mac;
    }
}
