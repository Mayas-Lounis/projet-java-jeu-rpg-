package lsg.bags;

import lsg.armor.DragonSlayerLeggings;
import lsg.armor.RingedKnightArmor;
import lsg.exceptions.BagFullException;
import lsg.weapons.ShotGun;

public class TransferTest {
    
    public static void main(String[] args) throws BagFullException {
        
        // Création des sacs
        Bag sac1 = new Bag(10);  // Capacité 10 kg
        Bag sac2 = new Bag(5);   // Capacité 5 kg (petit sac)
        
        // Création des items
        ShotGun shotgun = new ShotGun();           // 2 kg
        DragonSlayerLeggings leggings = new DragonSlayerLeggings();  // 3 kg
        RingedKnightArmor armor = new RingedKnightArmor();           // 4 kg
        
        // Remplissage du sac 1
        sac1.push(shotgun);      // +2 kg = 2/10
        sac1.push(leggings);     // +3 kg = 5/10
        sac1.push(armor);        // +4 kg = 9/10
        
        // Affichage avant transfert
        System.out.println("Sac 1 :");
        System.out.println(sac1);
        System.out.println();
        
        System.out.println("Sac 2 :");
        System.out.println(sac2);
        System.out.println();
        
        // Transfert
        System.out.println("Transfert de sac1 vers sac2...");
        Bag.transfer(sac1, sac2);
        System.out.println();
        
        // Affichage après transfert
        System.out.println("Sac 2 après transfert :");
        System.out.println(sac2);
        System.out.println();
        
        System.out.println("Sac 1 après transfert :");
        System.out.println(sac1);
        
    }
    
}