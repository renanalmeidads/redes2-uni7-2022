package entities;

public class Link {
    private Equipment equipment1;
    private Equipment equipment2;

    public Equipment getEquipment1() {
        return equipment1;
    }

    public void setEquipment1(Equipment equipment1) {
        this.equipment1 = equipment1;
    }

    public Equipment getEquipment2() {
        return equipment2;
    }

    public void setEquipment2(Equipment equipment2) {
        this.equipment2 = equipment2;
    }

    public void connect(Equipment equipment) throws Exception {

        if(equipment1 == null)
        {
            equipment1 = equipment;
        }
        else if(equipment2 == null)
        {
            equipment2 = equipment;
        }
        else
        {
            throw new Exception("O link já está conectado à dois equipamentos.");
        }
    }

    public void disconnect(Equipment equipment)
    {
        if(equipment != null) {
            if (equipment1 == equipment) {
                equipment1 = null;
            } else if (equipment2 == equipment) {
                equipment2 = null;
            }
            else
            {
                System.out.println("O equipamento não está conectado à este link.");
            }
        }
    }

    public Equipment getOtherEquipment(Equipment equipment)
    {
        if(equipment1 == equipment)
        {
            return equipment2;
        }
        else if(equipment2 == equipment)
        {
            return equipment1;
        }
        else
        {
            return null;
        }
    }

    public void send(Equipment equipment, Package pack) throws Exception {

        var otherEquipment = getOtherEquipment(equipment);

        if(otherEquipment != null)
        {
            otherEquipment.receive(pack);
        }
    }
}
