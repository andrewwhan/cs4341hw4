package cs4341hw4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Backtrack {

	public boolean base(HashMap<Item, Bag> assigned){
		if(assigned.keySet().containsAll(Main.items)){
			if(validConfig(assigned)){
				for(Item i : assigned.keySet()){
					assigned.get(i).contains.add(i);
				}
			}
			return validConfig(assigned);
		}
		Item var = unassignedVar(assigned.keySet());
		for(Bag b : Main.bags){
			HashMap<Item, Bag> next = new HashMap<Item, Bag>(assigned);
			next.put(var, b);
			if(base(next)){
				return true;
			}
		}
		return false;
	}
	
	private boolean validConfig(HashMap<Item, Bag> assigned){
		//Keep counts of capacity and number of items in each bag with this configuration
		HashMap<Bag, Integer> capacity = new HashMap<Bag, Integer>();
		HashMap<Bag, Integer> itemCount = new HashMap<Bag, Integer>();
		for(Bag b : Main.bags){
			capacity.put(b, 0);
			itemCount.put(b, 0);
		}
		
		for(Item i : assigned.keySet()){
			capacity.put(assigned.get(i), capacity.get(assigned.get(i)) + i.size);
			itemCount.put(assigned.get(i), itemCount.get(assigned.get(i)) + 1);
			//If a unary constraint prevents i from being in the bag it has been assigned, this config is not valid.
			if(i.badBags.contains(assigned.get(i).name)){
				return false;
			}
		}
		
		for(Bag b : Main.bags){
			//If a bag is over/under filled by capacity or count, this config is not valid
			if(capacity.get(b) > b.size || capacity.get(b) < (b.size*9)/10 ||
					itemCount.get(b) < Main.fitMin || itemCount.get(b) > Main.fitMax){
				return false;
			}
		}
		
		for(String key : Main.matrices.keySet()){
			//From the key, get the two items involved. Check and make sure the bags the two items are in aren't restricted by the BinMatrix
			if(Main.matrices.get(key).get(assigned.get(Main.getItem(key.substring(0,1))).name, assigned.get(Main.getItem(key.substring(1,2))).name) == -1){
				return false;
			}
		}
		return true;
	}
	
	private Item unassignedVar(Set<Item> assigned){
		ArrayList<Item> unassigned = new ArrayList<Item>(Main.items);
		unassigned.removeAll(assigned);
		return unassigned.get(0);
	}
}
