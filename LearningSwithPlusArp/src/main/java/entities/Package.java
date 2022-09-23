package entities;

import java.util.Random;

public class Package {
    private int id;
    private Ip ipDestino;
    private String payload;

    public Package() {
        this.id = new Random().nextInt();
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public int getId() {
        return id;
    }

    public Ip getIpDestino() {
        return ipDestino;
    }

    public void setIpDestino(Ip ipDestino) {
        this.ipDestino = ipDestino;
    }
}
