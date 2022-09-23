package services;

import entities.*;

public class Application {

    public static void main(String[] args){

        var h = new Host(new Mac("D2-64-B1-C6-C9-18"));
        var h2 = new Host(new Mac("F6-D3-F0-7B-95-A6"));
        var h3 = new Host(new Mac("BF-6E-F4-15-D5-CA"));
        var h4 = new Host(new Mac("E3-C2-5A-80-EA-6D"));
        var s = new Switch(new Mac("06-0A-83-04-BA-E6"), new Ip("192.168.0.1"));
        var l1 = new Link();
        var l2 = new Link();
        var l3 = new Link();

        try {
            h.connect(l1);
            s.connect(l1);
            h2.connect(l2);
            s.connect(l2);

            TcpPackage pack = new TcpPackage();
            pack.setIpDestino(h2.getIpAddress());
            pack.setPayload("Teste de envio pack1");

            TcpPackage pack2 = new TcpPackage();
            pack2.setIpDestino(h.getIpAddress());
            pack2.setPayload("Teste de envio pack2");

            TcpPackage pack3 = new TcpPackage();
            pack3.setIpDestino(h2.getIpAddress());
            pack3.setPayload("Teste de envio pack3");


            h.send(pack);
            h2.send(pack2);
            h.send(pack3);
            h2.send(pack2);
            h.send(pack);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
