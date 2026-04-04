package lsg.buffs;

import java.util.Locale;

import lsg.bags.Collectible;

public abstract class BuffItem implements Collectible{

	public static final int WEIGHT=1;
	
	private String name ; 
	
	public BuffItem(String name) {
		this.name = name ;
	}

	@Override
	public int getWeight(){
		return WEIGHT;
	}

	public abstract float computeBuffValue() ;
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return String.format(Locale.US, "[%s, %.2f]", getName(), computeBuffValue()) ;
	}
	
}
