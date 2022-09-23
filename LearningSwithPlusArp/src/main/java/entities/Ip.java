package entities;

import java.util.Objects;
import java.util.regex.Pattern;

public class Ip {

    private int part1;

    private int part2;

    private int part3;

    private int part4;

    private final char SEPARATOR = '.';

    public Ip(String ip) {

        var ipSplit = ip.split(Pattern.quote(Character.toString(SEPARATOR)));

        if(ipSplit.length == 4)
        {
            try {
                part1 = Integer.parseInt(ipSplit[0]);
                part2 = Integer.parseInt(ipSplit[1]);
                part3 = Integer.parseInt(ipSplit[2]);
                part4 = Integer.parseInt(ipSplit[3]);
            }
            catch(NumberFormatException ex)
            {
                throw ex;
            }
        }
        else
        {
            throw new IllegalArgumentException("Invalid ip address");
        }
    }

    public void setPart4(int part4) {
        this.part4 = part4;
    }

    @Override
    public String toString() {
        return String.valueOf(part1) + SEPARATOR + part2 + SEPARATOR + part3 + SEPARATOR + part4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ip ip = (Ip) o;
        return part1 == ip.part1 && part2 == ip.part2 && part3 == ip.part3 && part4 == ip.part4 && SEPARATOR == ip.SEPARATOR;
    }

    @Override
    public int hashCode() {
        return Objects.hash(part1, part2, part3, part4, SEPARATOR);
    }
}
