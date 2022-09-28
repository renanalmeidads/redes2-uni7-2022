package entities.arp;

import entities.Ip;
import entities.Mac;
import entities.Package;

public class ArpPackage extends Package {

    private Ip ipSource;

    private Mac macSource;

    private Ip ipDestination;

    private Mac macDestination;

    private int opCode;

    private final Mac MAC_ZEROES = new Mac("00-00-00-00-00-00");

    public ArpPackage(Ip ipSource, Mac macSource, Ip ipDestination, Mac macDestination) {
        this.ipSource = ipSource;
        this.macSource = macSource;
        this.ipDestination = ipDestination;

        if(macDestination == null)
            setMacDestinationZeros();
        else
            this.macDestination = macDestination;
    }

    protected void setOpCodeRequest()
    {
        this.opCode = 1;
    }

    protected void setOpCodeResponse()
    {
        this.opCode = 2;
    }

    public boolean isArpRequest()
    {
        return this.opCode == 1;
    }

    public boolean isArpResponse()
    {
        return this.opCode == 2;
    }

    protected int getOpCode() {
        return opCode;
    }

    private void setMacDestinationZeros()
    {
        this.macDestination = MAC_ZEROES;
    }

    public Ip getIpSource() {
        return ipSource;
    }

    public void setIpSource(Ip ipSource) {
        this.ipSource = ipSource;
    }

    public Mac getMacSource() {
        return macSource;
    }

    public void setMacSource(Mac macSource) {
        this.macSource = macSource;
    }

    public Ip getIpDestination() {
        return ipDestination;
    }

    public void setIpDestination(Ip ipDestination) {
        this.ipDestination = ipDestination;
    }

    public Mac getMacDestination() {
        return macDestination;
    }

    public void setMacDestination(Mac macDestination) {
        this.macDestination = macDestination;
    }

    @Override
    public String toString() {
        return "ArpPackage{" +
                "ipSource=" + ipSource +
                ", macSource=" + macSource +
                ", ipDestination=" + ipDestination +
                ", macDestination=" + macDestination +
                ", opCode=" + opCode +
                '}';
    }
}
