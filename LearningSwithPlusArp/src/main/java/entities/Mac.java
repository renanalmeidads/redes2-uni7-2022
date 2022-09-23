package entities;

public class Mac {
    private String part1;

    private String part2;

    private String part3;

    private String part4;

    private String part5;

    private String part6;

    private final char SEPARATOR = '-';

    public Mac(String mac) {

        var macSplit = mac.split(Character.toString(SEPARATOR));

        if(macSplit.length == 6)
        {
            part1 = macSplit[0];
            part2 = macSplit[1];
            part3 = macSplit[2];
            part4 = macSplit[3];
            part5 = macSplit[4];
            part6 = macSplit[5];
        }
        else
        {
            throw new IllegalArgumentException("Invalid mac address");
        }
    }

    @Override
    public String toString() {
        return part1 + SEPARATOR + part2 + SEPARATOR + part3 + SEPARATOR + part4 + SEPARATOR + part5 + SEPARATOR + part6;
    }
}
