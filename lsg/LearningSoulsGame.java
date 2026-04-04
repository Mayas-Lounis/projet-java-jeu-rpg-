package lsg;

import lsg.armor.ArmorItem;
import lsg.armor.BlackWitchVeil;
import lsg.armor.DragonSlayerLeggings;
import lsg.armor.RingedKnightArmor;
import lsg.bags.Bag;
import lsg.bags.Collectible;
import lsg.bags.MediumBag;
import lsg.buffs.rings.DragonSlayerRing;
import lsg.buffs.rings.RingOfDeath;
import lsg.buffs.talismans.MoonStone;
import lsg.characters.Character;
import lsg.characters.Hero;
import lsg.characters.Lycanthrope;
import lsg.characters.Monster;
import lsg.consumables.Consumable;
import lsg.consumables.MenuBestOfV4;
import lsg.consumables.drinks.Coffee;
import lsg.consumables.drinks.Whisky;
import lsg.consumables.drinks.Wine;
import lsg.consumables.food.Hamburger;
import lsg.consumables.repair.RepairKit;
import lsg.exceptions.BagFullException;
import lsg.exceptions.ConsumeEmptyException;
import lsg.exceptions.ConsumeNullException;
import lsg.exceptions.ConsumeRepairNullWeaponException;
import lsg.exceptions.NoBagException;
import lsg.exceptions.StaminaEmptyException;
import lsg.exceptions.WeaponBrokenException;
import lsg.exceptions.WeaponNullException;
import lsg.weapons.ShotGun;
import lsg.weapons.Sword;
import lsg.weapons.Weapon;

import java.util.Scanner;

public class LearningSoulsGame {

	public static final String BULLET_POINT = "\u2219 " ;

	Scanner scanner = new Scanner(System.in) ;
	
	Hero hero ;
	Monster monster ;

	

	

	private void init(){
		hero = new Hero() ;
		hero.setWeapon(new Sword());
		hero.setArmorItem(new DragonSlayerLeggings(), 1);
		hero.setRing(new RingOfDeath(), 1);
		hero.setRing(new DragonSlayerRing(), 2);

		hero.setConsumable(new Hamburger());

		monster = new Lycanthrope() ; // plus besoin de donner la skin et l'arme !
		monster.setTalisman(new MoonStone());
	}

	private void play(){
		init();
		fight1v1();
	}
	
	private void fight1v1(){

		refresh();
        
        Character agressor = hero;
        Character target = monster;
        int action;
        int attack, hit;
        Character tmp;

        while (hero.isAlive() && monster.isAlive()) {
            action = 1;
            System.out.println();

            if (agressor == hero) {
                do {
                    System.out.print("Hero's action for next move : (1) attack | (2) consume > ");
                    action = scanner.nextInt();
                } while (action < 1 || action > 2);
                System.out.println();
            }

            if (action == 2) {
                try{
					hero.consume();
				}catch(ConsumeNullException e){
					System.out.println("IMPOSSIBLE ACTION : no consumable has been equiped !");
				}
				catch(ConsumeEmptyException e){
					System.out.println("ACTION HAS NO EFFECT: " + e.getConsumable().getName() + " is empty !");
				}catch(ConsumeRepairNullWeaponException e){
					System.out.println("IMPOSSIBLE ACTION : " + e.getMessage());
				}
                System.out.println();
            } else {
                try {
                    attack = agressor.attack();
                } catch (WeaponNullException e) {
                    // Attaque sans arme : 0 dégâts, message d'avertissement
                    attack = 0;
                    System.out.println("WARNING : no weapon has been equiped !!!");
                }catch(WeaponBrokenException e){
					attack=0;
					System.out.println("WARNING : "+e.getMessage());
				}
				catch(StaminaEmptyException e){
					attack=0;
					System.out.println("ACTION HAS NO EFFECT: no more stamina !!!");
				}
				hit = target.getHitWith(attack);
                    
                        
                    System.out.printf("%s attacks %s with %s (ATTACK:%d | DMG : %d)", 
                        agressor.getName(), target.getName(), agressor.getWeapon(), attack, hit);
                    System.out.println();
                    System.out.println();
            }

            refresh();
            
            tmp = agressor;
            agressor = target;
            target = tmp;
        }
        
        Character winner = (hero.isAlive()) ? hero : monster;
        System.out.println();
        System.out.println("--- " + winner.getName() + " WINS !!! ---");
		
	}
	
	private void refresh(){
		hero.printStats();
		
		hero.printArmor();
		hero.printRings();
		hero.printConsumable();
		hero.printWeapon();
		
		try {
        hero.printBag();
    	} catch (NoBagException e) {
        	System.out.println("BAG : null");
    	}
		

		

		System.out.println();

		monster.printStats();

		if(monster.getWeapon()!=null){
			System.out.println("WEAPON : "+monster.getWeapon());
		}
	}

	private void title(){
		System.out.println();
		System.out.println("###############################");
		System.out.println("#   THE LEARNING SOULS GAME   #");
		System.out.println("###############################");
		System.out.println();
	}

	private void testExceptions(){
		System.out.println("=== TEST EXCEPTIONS ===");
        System.out.println();
        
        // 1. Test WeaponNullException (attaque sans arme)
        System.out.println("--- 1. WeaponNullException ---");
        init();
        hero.setWeapon(null);
        try {
            hero.attack();
        } catch (WeaponNullException e) {
            System.out.println("OK : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERREUR : " + e.getClass().getSimpleName());
        }
        System.out.println();
        
        // 2. Test WeaponBrokenException (attaque avec arme cassée)
        System.out.println("--- 2. WeaponBrokenException ---");
        init();
        // Casse l'arme en l'utilisant jusqu'à la limite
        Weapon weapon = hero.getWeapon();
        while (!weapon.isBroken()) {
            weapon.use();
        }
        try {
            hero.attack();
        } catch (WeaponBrokenException e) {
            System.out.println("OK : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERREUR : " + e.getClass().getSimpleName());
        }
        System.out.println();
        
        // 3. Test StaminaEmptyException (attaque sans stamina)
        System.out.println("--- 3. StaminaEmptyException ---");
        init();
        //hero.setStamina(0);
        try {
            hero.attack();
        } catch (StaminaEmptyException e) {
            System.out.println("OK : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERREUR : " + e.getClass().getSimpleName());
        }
        System.out.println();
        
        // 4. Test NoBagException (opération sans sac)
        System.out.println("--- 4. NoBagException ---");
        init();
        hero.setBag(null);
        try {
            hero.pickUp(new Hamburger());
        } catch (NoBagException e) {
            System.out.println("OK : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERREUR : " + e.getClass().getSimpleName());
        }
        System.out.println();
        
        // 5. Test BagFullException (sac plein)
        System.out.println("--- 5. BagFullException ---");
        init();
        // Remplit le sac (SmallBag = 10kg max)
        try {
            hero.pickUp(new RingedKnightArmor());  // 4kg
            hero.pickUp(new RingedKnightArmor());  // 4kg
            hero.pickUp(new DragonSlayerLeggings()); // 3kg = 11kg > 10kg
        } catch (BagFullException e) {
            System.out.println("OK : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERREUR : " + e.getClass().getSimpleName());
        }
        System.out.println();
        
        // 6. Test ConsumeNullException (consommable null)
        System.out.println("--- 6. ConsumeNullException ---");
        init();
        try {
            hero.consume();  // consumable = null par défaut dans init()
        } catch (ConsumeNullException e) {
            System.out.println("OK : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERREUR : " + e.getClass().getSimpleName());
        }
        System.out.println();
        
        // 7. Test ConsumeEmptyException (consommable vide)
        System.out.println("--- 7. ConsumeEmptyException ---");
        init();
        Hamburger emptyBurger = new Hamburger();
        try {
            emptyBurger.use();  // Vide le burger
            hero.setConsumable(emptyBurger);
            hero.consume();
        } catch (ConsumeEmptyException e) {
            System.out.println("OK : " + e.getMessage() + " (" + e.getConsumable().getName() + ")");
        } catch (Exception e) {
            System.out.println("ERREUR : " + e.getClass().getSimpleName());
        }
        System.out.println();
        
        // 8. Test ConsumeRepairNullWeaponException (réparer sans arme)
        System.out.println("--- 8. ConsumeRepairNullWeaponException ---");
        init();
        hero.setWeapon(null);
        hero.setConsumable(new RepairKit());
        try {
            hero.consume();
        } catch (ConsumeRepairNullWeaponException e) {
            System.out.println("OK : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERREUR : " + e.getClass().getSimpleName());
        }
        System.out.println();
        
        System.out.println("=== FIN DES TESTS ===");
		
	}

	/*private void createExhaustedHero(){
		Hero hero = new Hero();

		hero.getHitWith(99);

		hero.setWeapon(new Weapon("Grosse Arme", 0, 0, 1000, 100));

		try{
			hero.attack();
		}catch(WeaponNullException e){
			e.printStackTrace();
		}

		System.out.println(hero);
	
	}*/


	public static void main(String[] args) {
		LearningSoulsGame lsg = new LearningSoulsGame() ;
		lsg.title();
		
		lsg.testExceptions();
	}

}
