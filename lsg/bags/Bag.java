package lsg.bags;

import java.util.HashSet;

import lsg.exceptions.BagFullException;

public class Bag {
    
    private int capacity;
    private int weight;
    private HashSet<Collectible> items;
    
    public Bag(int capacity){
        this.capacity=capacity;
        this.weight=0;
        this.items=new HashSet<>();
    }

    public int getCapacity(){
        return capacity;
    }

    public int getWeight(){
        return weight;
    }

    public void push(Collectible item) throws BagFullException{
        if(item==null) return;

        int itemWeight=item.getWeight();

        if(weight+itemWeight>capacity){
            throw new BagFullException(this);
        }
        items.add(item);
        weight+=itemWeight;
    }

    public Collectible pop(Collectible item){
        if(item==null) return null;

        if(items.contains(item)){
            items.remove(item);
            weight-=item.getWeight();
            return item;
        }
        return null;
    }

    public boolean contains(Collectible item){
        return items.contains(item);
    }

    public Collectible[] getItems(){
        return items.toArray(new Collectible[0]);
    }

    public static void transfer(Bag from, Bag into) {
    if (from == null || into == null) {
        return;
    }

    Collectible[] itemsToTransfer = from.getItems();

    for (Collectible item : itemsToTransfer) {
        try {
            into.push(item);  // Lance BagFullException si plein
            from.pop(item);   // Retire seulement si ajout réussi
        } catch (BagFullException e) {
            // Sac plein, on passe à l'item suivant (peut-être plus petit)
            continue;
        }
    }
}

    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();

        sb.append((getClass().getSimpleName()));
        sb.append(" [ ");
        sb.append(items.size());
        sb.append(" items | ");
        sb.append(weight);
        sb.append("/");
        sb.append(capacity);
        sb.append(" kg ]");
        sb.append("\n");

        if(items.isEmpty()){
            sb.append("• empty");
        }else{
            for(Collectible item:items){
                sb.append("• ");
                sb.append(item.toString());
                sb.append(" [");
                sb.append(item.getWeight());
                sb.append(" kg]");
                sb.append("\n");
            }
        }

        return sb.toString().trim();
    }
}
