package entities;

public class Port {

    private int id;

    private boolean Connected = false;

    public Port(int id) {
        this.id = id;
    }

    public boolean connect()
    {
        return Connected = true;
    }

    public boolean disconnect()
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