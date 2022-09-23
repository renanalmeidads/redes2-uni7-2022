package entities;

public class Port {

    private int id;

    private boolean Connected = false;

    public Port(int id) {
        this.id = id;
    }

    public boolean Connect()
    {
        return Connected = true;
    }

    public boolean Disconnect()
    {
        return Connected = false;
    }

    public boolean isConnected() { return Connected; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
