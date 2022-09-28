package entities;

import java.util.Random;

public class Package {
    private int id;

    private Package type;

    public Package() {
        this.id = new Random().nextInt() & Integer.MAX_VALUE;
    }

    public int getId() {
        return id;
    }

    public Package getType() {
        return type;
    }

    public void setType(Package type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Package{" +
                "id=" + id +
                ", type=" + type +
                '}';
    }
}
