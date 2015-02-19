package cs4341hw4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Backtrack {
	int stateCount = 0;

	public boolean base(HashMap<Item, Bag> assigned){
		stateCount++;
		if(assigned.keySet().containsAll(Main.items)){
			return validConfig(assigned);
		}
		Item var = unassignedVar(assigned.keySet());
		for(Bag b : Main.bags){
			HashMap<Item, Bag> next = new HashMap<Item, Bag>(assigned);
			next.put(var, b);
			b.contains.add(var);
			if(base(next)){
				return true;
			}
			b.contains.remove(var);
		}
		return false;
	}
	
	public boolean withHeur(HashMap<Item, Bag> assigned){
		stateCount++;
		if(assigned.keySet().containsAll(Main.items)){
			if(validConfig(assigned)){
				for(Item i : assigned.keySet()){
					assigned.get(i).contains.add(i);
				}
			}
			return validConfig(assigned);
		}
		Item var = mrv(assigned);
		for(Bag b : lcv(var, assigned)){
			HashMap<Item, Bag> next = new HashMap<Item, Bag>(assigned);
			next.put(var, b);
			if(withHeur(next)){
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
	
	//Return the unassigned item that has the minimum remaining values
	private Item mrv(Map<Item, Bag> assignments){
		Set<Item> assigned = assignments.keySet();
		ArrayList<Item> unassigned = new ArrayList<Item>(Main.items);
		unassigned.removeAll(assigned);
		int minValue = 9999;
		Item minItem = null;
		int degree = 0;
		//For each item, check how many bags it can still be placed in.
		for(Item i : unassigned){
			int availableBags = availableBags(i, assignments);
			if(availableBags < minValue){
				minValue = availableBags;
				minItem = i;
				degree = degree(i);
			}
			else if(availableBags == minValue){
				//If this item has a higher degree, it breaks the tie.
				if(degree(i) > degree){
					minValue = availableBags;
					minItem = i;
					degree = degree(i);
				}
			}
		}
		return minItem;
	}
	
	//Count available bags for an item
	private int availableBags(Item i, Map<Item, Bag> assignments){
		Set<Item> assigned = assignments.keySet();
		//Create a set of all the bags and remove the ones that the item can't be placed in
		Collection<Bag> availableBags = new ArrayList<Bag>(Main.bags);
		Collection<Bag> toRemove = new ArrayList<Bag>();
		for(Bag b : availableBags){
			//Remove a bag based on item maximum
			if(Main.fitMax - b.itemCount() == 0){
				toRemove.add(b);
				continue;
			}
			//Remove bag based on capacity
			if(b.size - b.filled() < i.size){
				toRemove.add(b);
				continue;
			}
			//Remove bag based on unary constraint
			if(i.badBags.contains(b)){
				toRemove.add(b);
				continue;
			}
			//Remove bag based on binary constraint
			for(String key : Main.matrices.keySet()){
				if(key.contains(i.name)){
					if(assigned.contains(Main.getItem(key.replace(i.name, "")))){
						if(Main.matrices.get(key).get(b.name, assignments.get(Main.getItem(key.replace(i.name, ""))).name) == -1){
							toRemove.add(b);
							break;
						}
					}
				}
			}
		}
		availableBags.removeAll(toRemove);
		return availableBags.size();
	}
	
	//Count the number of binary constraints involving an item
	private int degree(Item i){
		int count = 0;
		for(String key : Main.matrices.keySet()){
			if(key.contains(i.name)){
				count++;
			}
		}
		return count;
	}
	
	//Return a list of bags ordered by least constraining value
	Collection<Bag> lcv(Item var, Map<Item, Bag> assignments){
		//Keep track of how constrained the problem is as a result of choosing each bag
		HashMap<Bag, Integer> constraints = new HashMap<Bag, Integer>();
		//The sorted list to return
		ArrayList<Bag> list = new ArrayList<Bag>();
		
		for(Bag b : Main.bags){
			constraints.put(b, 0);
			
			//Create a new map simulating choosing this bag for this item
			HashMap<Item, Bag> next = new HashMap<Item, Bag>(assignments);
			next.put(var, b);
			//Get the remaining unassigned items
			Set<Item> assigned = next.keySet();
			ArrayList<Item> unassigned = new ArrayList<Item>(Main.items);
			unassigned.removeAll(assigned);
			
			//Try adding this item and see how constrained each of the remaining items are
			b.contains.add(var);
			for(Item i : unassigned){
				constraints.put(b, constraints.get(b) + Main.bags.size() - availableBags(i, assignments));
			}
			b.contains.remove(var);
			
			//Sort list using insertion sort based on the constraint value we have generated
			int i = 0;
			while(i<list.size() && constraints.get(list.get(i)) < constraints.get(b)){
				i++;
			}
			list.add(i, b);
		}
		return list;
	}
}
