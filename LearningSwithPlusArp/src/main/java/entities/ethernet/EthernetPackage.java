package entities.ethernet;

import entities.Mac;
import entities.Package;

public class EthernetPackage extends Package {

    private Mac macDestination;

    private Mac macSource;

    private final Mac MAC_BROADCAST = new Mac("FF-FF-FF-FF-FF-FF");

    public EthernetPackage(Mac macSource, Mac macDestination, Package type) {

        if(macDestination == null)
            setBroadcastMac();
        else
            this.macDestination = macDestination;

        this.macSource = macSource;
        setType(type);
    }

    public void setBroadcastMac()
    {
        this.macDestination = MAC_BROADCAST;
    }

    public boolean isBroadcast()
    {
        return this.macDestination == MAC_BROADCAST;
    }

    public Mac getMacDestination() {
        return macDestination;
    }

    public Mac getMacSource() {
        return macSource;
    }

    public void setMacSource(Mac macSource) {
        this.macSource = macSource;
    }

    public void setMacDestination(Mac macDestination) {
        this.macDestination = macDestination;
    }
}
