package services;

import entities.*;
import entities.Package;
import factory.PackageFactory;

public class Application {

    public static void main(String[] args){

        var h = new Host(new Mac("D2-64-B1-C6-C9-18"));
        var h2 = new Host(new Mac("F6-D3-F0-7B-95-A6"));
        var h3 = new Host(new Mac("BF-6E-F4-15-D5-CA"));
        var h4 = new Host(new Mac("E3-C2-5A-80-EA-6D"));
        var h5 = new Host(new Mac("4C-77-F3-38-C1-A8"));
        var h6 = new Host(new Mac("2D-3F-44-FE-15-91"));
        var h7 = new Host(new Mac("F0-E4-A2-8E-86-BB"));
        var h8 = new Host(new Mac("F3-22-F2-4A-1F-95"));
        var s2 = new Switch(new Mac("C4-FF-86-DD-ED-1E"), new Ip("192.168.0.2"));
        var s = new Switch(new Mac("06-0A-83-04-BA-E6"), new Ip("192.168.0.1"));
        var s3 = new Switch(new Mac("A9-5D-A2-84-2B-4D"), new Ip("192.168.0.3"));
        var l1 = new Link();
        var l2 = new Link();
        var l3 = new Link();
        var l4 = new Link();
        var l5 = new Link();
        var l6 = new Link();
        var l7 = new Link();
        var l8 = new Link();
        var l9 = new Link();
        var l10 = new Link();
        var l11 = new Link();

        var h9 = new Host(new Mac("35-3D-85-11-4B-BE"));
        var h10 = new Host(new Mac("1B-71-36-60-87-DF"));

        try {
            h.connect(l1);
            s.connect(l1);
            h2.connect(l2);
            s.connect(l2);
            h3.connect(l3);
            s.connect(l3);
            h4.connect(l4);
            s.connect(l4);
            s2.connect(l5);
            h5.connect(l5);
            s.connect(l6);
            s2.connect(l6);
            h6.connect(l7);
            s2.connect(l7);
            s3.connect(l8);
            h7.connect(l8);
            s3.connect(l9);
            h8.connect(l9);
            s2.connect(l10);
            s3.connect(l10);

            h9.connect(l11);
            h10.connect(l11);
            h9.setIpAddress(new Ip("192.168.0.1"));
            h10.setIpAddress(new Ip("192.168.0.2"));

            Package pack = PackageFactory.getTcpPackage(h, h8, l1, "Teste de envio pack");

            h.send(pack);

            Package pack2 = PackageFactory.getTcpPackage(h9, h10, l11, "Teste de envio pack2");

            h9.send(pack2);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
