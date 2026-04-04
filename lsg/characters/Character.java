package lsg.characters;

import lsg.bags.Bag;
import lsg.bags.Collectible;
import lsg.bags.SmallBag;
import lsg.consumables.Consumable;
import lsg.consumables.drinks.Drink;
import lsg.consumables.food.Food;
import lsg.consumables.repair.RepairKit;
import lsg.exceptions.BagFullException;
import lsg.exceptions.ConsumeEmptyException;
import lsg.exceptions.ConsumeNullException;
import lsg.exceptions.ConsumeRepairNullWeaponException;
import lsg.exceptions.NoBagException;
import lsg.exceptions.StaminaEmptyException;
import lsg.exceptions.WeaponBrokenException;
import lsg.exceptions.WeaponNullException;
import lsg.helpers.Dice;
import lsg.weapons.Weapon;

import java.util.Locale;

public abstract class Character {

	public static final String LIFE_STAT_STRING = "life" ;
	public static final String STAM_STAT_STRING = "stamina" ;
	public static final String PROTECTION_STAT_STRING = "protection" ;
	public static final String BUFF_STAT_STRING = "buff" ;

	private static String MSG_ALIVE = "(ALIVE)" ;
	private static String MSG_DEAD = "(DEAD)" ;
	
	private String name ; // Nom du personnage
	
	private int maxLife, life ; 		// Nombre de points de vie restants
	private int maxStamina, stamina ;	// Nombre de points d'action restants
	
	private Weapon weapon ;

	private Consumable consumable ;


	private Dice dice101 = new Dice(101) ;

	protected Bag bag;
	
	public Character(String name) {
		this.name = name ;
		this.bag=new SmallBag();
	}

	public Bag getBag(){
		return bag;
	}

	public Consumable getConsumable() {
		return consumable;
	}

	/**
	 * Methode qui utilise le consommable equipé par le personnage
	 */
	public void consume() throws ConsumeNullException,ConsumeEmptyException,ConsumeRepairNullWeaponException{
		use(consumable);
	}

	public void setConsumable(Consumable consumable) {
		this.consumable = consumable;
	}

	private void eat(Food food)throws ConsumeNullException,ConsumeEmptyException{
		if(food == null){
			throw new ConsumeNullException();
		}
		System.out.println(getName() + " eats " + food);
		int newLife = getLife() + food.use() ;
		newLife = (newLife < maxLife) ? newLife : maxLife ;
		setLife(newLife);
	}

	private void drink(Drink drink) throws ConsumeNullException,ConsumeEmptyException{

		if(drink==null){
			throw new ConsumeNullException();
		}
		
		System.out.println(getName() + " drinks " + drink);
		int newStam = getStamina() + drink.use() ;
		newStam = (newStam < maxStamina) ? newStam : maxStamina ;
		setStamina(newStam);
	}

	private void repairWeaponWith(RepairKit kit)throws WeaponNullException, ConsumeNullException, ConsumeEmptyException{
		if(weapon ==null){
			throw new WeaponNullException();
		}

		System.out.println(getName() + " repairs " + weapon + " with " + kit);
		weapon.repairWith(kit);
	}

	public void use(Consumable consumable) throws ConsumeNullException,ConsumeEmptyException, ConsumeRepairNullWeaponException{

		if(consumable==null){
			throw new ConsumeNullException();
		}
		if(consumable instanceof Drink){
			drink((Drink)consumable);
		}else if(consumable instanceof Food){
			eat((Food)consumable) ;
		}else if(consumable instanceof RepairKit){
			try{
				repairWeaponWith((RepairKit)consumable);
			}catch(WeaponNullException e){
				throw new ConsumeRepairNullWeaponException(consumable);
			}
		}
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getMaxLife() {
		return maxLife;
	}
	
	protected void setMaxLife(int maxLife) {
		this.maxLife = maxLife;
	}
	
	public int getLife() {
		return life;
	}
	
	protected void setLife(int life) {
		this.life = life;
	}
	
	public int getMaxStamina() {
		return maxStamina;
	}
	
	protected void setMaxStamina(int maxStamina) {
		this.maxStamina = maxStamina;
	}
	
	public int getStamina() {
		return stamina;
	}
	
	protected void setStamina(int stamina) {
		this.stamina = stamina;
	}
	
	public boolean isAlive(){
		return life > 0 ;
	}
	
	public void printStats(){
		System.out.println(this);
	}

	@Override
	public String toString() {
		
		String classe = getClass().getSimpleName() ;
		String life = String.format("%5d", getLife()) ; 
		String stam = String.format("%5d", getStamina()) ; 
		String protection = String.format(Locale.US, "%6.2f", computeProtection()) ;
		String buff = String.format(Locale.US, "%6.2f", computeBuff()) ;
		
		String msg = String.format("%-20s %-20s %s:%-10s %s:%-10s %s:%-10s %s:%-10s", "[ " + classe + " ]",
				getName(),
				LIFE_STAT_STRING.toUpperCase(),
				life,
				STAM_STAT_STRING.toUpperCase(),
				stam,
				PROTECTION_STAT_STRING.toUpperCase(),
				protection,
				BUFF_STAT_STRING.toUpperCase(),
				buff) ;
		
		String status ;
		if(isAlive()){
			status = MSG_ALIVE ;
		}else{
			status = MSG_DEAD ;
		}
		
		return msg + status ;
	}
	
	public int attack() throws WeaponNullException, WeaponBrokenException, StaminaEmptyException{
		return attackWith(this.getWeapon()) ;
	}
	
	/**
	 * Calcule une attaque en fonction d'une arme.
	 * Le calcul dépend des statistiques de l'arme, de la stamina (restante) du personnage et des buffs eventuels
	 * 
	 * @param weapon : l'arme utilisée.
	 * @return la valeur de l'attaque eventuellement buffée ; 0 si l'arme est cassée.
	 */
	protected int attackWith(Weapon weapon) throws WeaponNullException, WeaponBrokenException, StaminaEmptyException{

		if(weapon==null){
			throw new WeaponNullException();
		}

		if(weapon.isBroken()){
			throw new WeaponBrokenException(weapon);
		}

		if(getStamina()<=0){
			throw new StaminaEmptyException();
		}

		int min = weapon.getMinDamage() ;
		int max = weapon.getMaxDamage() ;
		int cost = weapon.getStamCost() ;
		
		int attack = 0 ;
		
		
		attack = min + Math.round((max-min) * dice101.roll() / 100.f) ;
		int stam = getStamina() ;
		if(cost <= stam){ // il y a assez de stam pour lancer l'attaque
			setStamina(getStamina()-cost);
		}else{
			attack = Math.round(attack * ((float)stam / cost)) ;
			setStamina(0);
		}
			
		weapon.use();
		
		
		return attack + Math.round(attack*computeBuff()/100);
	}
	
	public Weapon getWeapon() {
		return weapon;
	}
	
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}
	
	/**
	 * @return le nombre de points de buff du personnage
	 */
	protected abstract float computeBuff() ; 
	
	/**
	 * @return le nombre de points de protection du personnage
	 */
	protected abstract float computeProtection() ;
	
	/**
	 * Calcule le nombre de PV retires en tenant compte de la protection
	 * @param value : le montant des degats reçus
	 * @return le nombre de PV effectivement retires (value reduite par la protecion si assez de vie ; le reste de la vie sinon)
	 */
	public int getHitWith(int value){
		
		int life = getLife() ;
		int dmg ;
		float protection = computeProtection() ;
		if(protection > 100) protection = 100 ; // si la protection depasse 100, elle absorbera 100% de l'attaque
		value = Math.round(value - (value * protection / 100)) ;
		dmg = (life > value) ? value : life ; 
		setLife(life-dmg);
		return dmg ;
	}

	public void pickUp(Collectible item) throws NoBagException,BagFullException{
		if(bag==null){
			throw new NoBagException();
		}
		if(item==null)return;

		bag.push(item);

		System.out.println(getName()+" picks up "+item);
	}

	public Collectible pullOut(Collectible item)throws NoBagException{
		if(bag==null){
			throw new NoBagException();
		}
		if(item==null) return null;

		Collectible pulled=bag.pop(item);
		if(pulled!=null){
			System.out.println(getName()+" pulls out "+pulled);
		}

		return pulled;
	}

	public void printBag() throws NoBagException{
		if(bag==null){
			throw new NoBagException();
		}
		System.out.println("BAG : "+bag.toString());
		
	}

	public int getBagCapacity() throws NoBagException{
		if(bag==null){
			throw new NoBagException();
		}
		return bag.getCapacity();
	}

	public int getBagWeight() throws NoBagException{
		if(bag==null){
			throw new NoBagException();
		}
		return bag.getWeight();
	}

	public Collectible[] getBagItems() throws NoBagException{
		if(bag==null){
			throw new NoBagException();
		}
		return bag.getItems();
	}

	public Bag setBag(Bag bag){
		
		System.out.println(getName()+" changes "+this.bag.getClass().getSimpleName()+" for "+(bag !=null ?bag.getClass().getSimpleName():"null"));

		if(bag == null){
			Bag oldbag=this.bag;
			this.bag=null;
			return oldbag;
		}

		Bag oldBag=this.bag;
		this.bag=bag;

		Bag.transfer(oldBag, this.bag);

		return oldBag;

	}

	public void equip(Weapon weapon) throws NoBagException{
		if(bag==null){
			throw new NoBagException();
		}
		if(weapon==null)return;

		if(bag.contains(weapon)){
			bag.pop(weapon);
			setWeapon(weapon);
			System.out.println(getName()+" pulls out "+weapon+" and equips it!");
		}
	}

	public void equip(Consumable consumable) throws NoBagException{
		if(bag==null){
			throw new NoBagException();
		}
		if(consumable==null)return;

		if(bag.contains(consumable)){
			bag.pop(consumable);
			setConsumable(consumable);
			System.out.println(getName()+" pulls out "+consumable+" and equips it!");
		}
	}

	private <T extends Consumable> T fastUseFirst(Class<T> type) throws ConsumeNullException,ConsumeEmptyException,ConsumeRepairNullWeaponException, NoBagException{
		if(bag==null){
			throw new NoBagException();
		}
		for(Collectible item:bag.getItems()){
			if(type.isInstance(item)){
				T consumable=type.cast(item);

				use(consumable);
				if(consumable.getCapacity()==0){
					bag.pop(consumable);
				}
				return consumable;
			}
		}
		return null;
	}

	public Drink fastDrink() throws ConsumeNullException,ConsumeEmptyException,NoBagException{
		System.out.println(getName()+" drinks FAST :");
		try{
			return fastUseFirst(Drink.class);
		}catch(ConsumeRepairNullWeaponException e){
			return null;
		}
	}

	public Food fastEat() throws ConsumeNullException, ConsumeEmptyException,NoBagException{
		System.out.println(getName()+" eats FAST :");
		try{
			return fastUseFirst(Food.class);
		}catch(ConsumeRepairNullWeaponException e){
			return null;
		}
	}

	public RepairKit fastRepair() throws ConsumeNullException, ConsumeEmptyException, ConsumeRepairNullWeaponException,NoBagException{
		System.out.println(getName()+" repairs FAST :");
		RepairKit kit=fastUseFirst(RepairKit.class);
		return kit;
	}


	public void printWeapon() {
        System.out.println("WEAPON : " + (weapon != null ? weapon : "empty"));
    }

	public void printConsumable(){
		System.out.println("CONSUMABLE : "+(consumable !=null ? consumable:"empty"));
	}
	
}
