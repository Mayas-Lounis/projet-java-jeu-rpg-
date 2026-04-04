package lsg.consumables.repair;

import lsg.consumables.Consumable;
import lsg.exceptions.ConsumeEmptyException;
import lsg.weapons.Weapon;

public class RepairKit extends Consumable {

    public RepairKit(){
        super("Repair Kit", 10, Weapon.DURABILITY_STAT_STRING);
    }

    @Override
    public int use() throws ConsumeEmptyException {
        // Vérifie d'abord si le kit est vide
        if (getCapacity() == 0) {
            throw new ConsumeEmptyException(this);
        }
        
        // Réduit la capacité de 1 (comportement spécifique au RepairKit)
        int amount = getCapacity();
        setCapacity(amount - 1);
        return 1;
    }
}
