package lsg.bags;

import lsg.armor.BlackWitchVeil;
import lsg.armor.DragonSlayerLeggings;
import lsg.consumables.food.Hamburger;
import lsg.exceptions.BagFullException;
import lsg.weapons.Sword;

public class BagTest {
    
    public static void main(String[] args) throws BagFullException {
        
        // Création d'un SmallBag
        SmallBag bag = new SmallBag();
        
        // Création des items
        BlackWitchVeil veil = new BlackWitchVeil();
        Hamburger burger = new Hamburger();
        Sword sword = new Sword();
        DragonSlayerLeggings leggings = new DragonSlayerLeggings();
        
        // Ajout des items
        System.out.println("=== Ajout des items ===");
        bag.push(veil);
        bag.push(burger);
        bag.push(sword);
        bag.push(leggings);
        
        // Affichage du sac
        System.out.println(bag);
        System.out.println();
        
        // Test de pop sur DragonSlayerLeggings
        System.out.println("Pop sur " + leggings);
        Collectible popped = bag.pop(leggings);
        System.out.println();
        
        // Affichage après retrait
        System.out.println(bag);
        System.out.println();
        
        // Vérification que l'item retiré est bien celui qu'on pense
        System.out.println("Item retiré : " + popped);
        
        // Test contains
        System.out.println("Contains veil ? " + bag.contains(veil));
        System.out.println("Contains leggings ? " + bag.contains(leggings));
        
        // Test MediumBag
        System.out.println("\n=== Test MediumBag ===");
        MediumBag bigBag = new MediumBag();
        System.out.println("Capacité MediumBag : " + bigBag.getCapacity() + " kg");
        System.out.println("Poids initial : " + bigBag.getWeight() + " kg");
        System.out.println(bigBag);
    }
    
}