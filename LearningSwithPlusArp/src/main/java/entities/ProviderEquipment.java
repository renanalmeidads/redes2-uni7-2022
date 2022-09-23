package entities;

import java.nio.file.ProviderNotFoundException;

public  interface ProviderEquipment {

    Ip getNextIp() throws ProviderNotFoundException;
}
