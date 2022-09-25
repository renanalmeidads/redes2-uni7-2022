package entities;

public class Port {

    private int id;

    private boolean connected = false;

    public Port(int id) {
        this.id = id;
    }

    public boolean connect()
    {
        return connected = true;
    }

    public boolean disconnect()
    {
        return connected = false;
    }

    public boolean isConnected() { return connected; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
