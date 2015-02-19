package cs4341hw4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class Main{

	public static Collection<Item> items = new ArrayList<Item>();
	public static Collection<Bag> bags = new ArrayList<Bag>();
	public static int fitMin = -1;
	public static int fitMax = 9999;
	public static HashMap<String, BinMatrix> matrices = new HashMap<String, BinMatrix>();

	public static void main(String args[]) throws IOException{
		if(args.length > 0){
			input(args[0]);
		}
		Backtrack backtracker = new Backtrack();
		if(backtracker.withFC(new HashMap<Item, Bag>())){
			for(Bag b : bags){
				b.status();
				System.out.println();
			}
			//System.out.println(backtracker.stateCount);
		}
		else{
			System.out.println("No solution possible");
			//System.out.println(backtracker.stateCount);
		}
	}

	private static void input(String inFile) throws IOException{
		BufferedReader in;
		in = new BufferedReader(new FileReader(new File(inFile)));
		String line;

		line = in.readLine();
		//Wait for first section header
		while(!line.contains("#####")){
			line = in.readLine();
		}

		//Read the first line after the header
		line = in.readLine();
		//As long as there are still items in the section, process them.
		while(!line.contains("#####")){
			Scanner s = new Scanner(line);
			if(s.hasNext()){
				items.add(new Item(s.next(), s.nextInt()));
			}
			s.close();
			line = in.readLine();
		}

		//First line after the next header
		line = in.readLine();
		//Process bags
		while(!line.contains("#####")){
			Scanner s = new Scanner(line);
			if(s.hasNext()){
				bags.add(new Bag(s.next(), s.nextInt()));
			}
			s.close();
			line = in.readLine();
		}

		//Fit limit
		line = in.readLine();
		while(!line.contains("#####")){
			Scanner s = new Scanner(line);
			if(s.hasNext()){
				fitMin = s.nextInt();
				fitMax = s.nextInt();
			}
			s.close();
			line = in.readLine();
		}

		//Inclusive unary restraints
		line = in.readLine();
		while(!line.contains("#####")){
			Scanner s = new Scanner(line);
			if(s.hasNext()){
				Item item = getItem(s.next());
				//Generate a list of bags this item can be placed in
				Collection<String> included = new ArrayList<String>();
				while(s.hasNext()){
					included.add(s.next());
				}
				//For all bags, if it's not one of the bags this item can be placed in, add it to the list of bad bags
				Iterator<Bag> b = bags.iterator();
				while(b.hasNext()){
					Bag bag = b.next();
					if(!included.contains(bag.bagName())){
						item.badBags.add(bag.bagName());
					}
				}
			}
			s.close();
			line = in.readLine();
		}

		//Exclusive unary restraints
		line = in.readLine();
		while(!line.contains("#####")){
			Scanner s = new Scanner(line);
			if(s.hasNext()){
				Item item = getItem(s.next());
				//Add each bag that this item can't be placed in to the list of bad bags
				while(s.hasNext()){
					item.badBags.add(s.next());
				}
			}
			s.close();
			line = in.readLine();
		}

		//Binary equals
		line = in.readLine();
		while(!line.contains("#####")){
			Scanner s = new Scanner(line);
			if(s.hasNext()){
				String key = s.next() + s.next();
				//If we don't have a binary constraint matrix for this combination of items yet, make a new one.
				if(!matrices.containsKey(key)){
					matrices.put(key, new BinMatrix(bags));
				}
				for(Bag bag1 : bags){
					for(Bag bag2 : bags){
						if(!bag1.equals(bag2)){
							matrices.get(key).put(bag1.bagName(), bag2.bagName(), -1);
						}
					}
				}
			}
			s.close();
			line = in.readLine();
		}

		//Binary not equals
		line = in.readLine();
		while(!line.contains("#####")){
			Scanner s = new Scanner(line);
			if(s.hasNext()){
				String key = s.next() + s.next();
				//If we don't have a binary constraint matrix for this combination of items yet, make a new one.
				if(!matrices.containsKey(key)){
					matrices.put(key, new BinMatrix(bags));
				}
				for(Bag bag : bags){
					matrices.get(key).put(bag.bagName(), bag.bagName(), -1);
				}
			}
			s.close();
			line = in.readLine();
		}

		//Binary mutual exclusive
		line = in.readLine();
		while(line != null && !line.contains("#####")){
			Scanner s = new Scanner(line);
			if(s.hasNext()){
				String key = s.next() + s.next();
				//If we don't have a binary constraint matrix for this combination of items yet, make a new one.
				if(!matrices.containsKey(key)){
					matrices.put(key, new BinMatrix(bags));
				}
				String bagName1 = s.next();
				String bagName2 = s.next();
				matrices.get(key).put(bagName1, bagName2, -1);
				matrices.get(key).put(bagName2, bagName1, -1);
			}
			s.close();
			line = in.readLine();
		}
		in.close();
	}

	public static Item getItem(String s){
		Iterator<Item> i = items.iterator();
		while(i.hasNext()){
			Item item = i.next();
			if(item.itemName().equals(s)){
				return item;
			}
		}
		return null;
	}

	public static Bag getBag(String s){
		Iterator<Bag> i = bags.iterator();
		while(i.hasNext()){
			Bag bag = i.next();
			if(bag.bagName().equals(s)){
				return bag;
			}
		}
		return null;
	}
	
	public boolean minConflicts(int steps)
	{
		int conflicts = 0;
		
		//assign each item to the bag with the most available space
		for(Item item: items)
		{
			Bag activeBag;
			Iterator<Bag> bagIterator = bags.iterator();
			activeBag = bagIterator.next();
			int smallestSize = activeBag.currentSize();
			for(Bag b: bags)
			{
				if(b.currentSize() < smallestSize)
				{
					smallestSize = b.currentSize();
					activeBag = b;
				}
			
			activeBag.contains.add(item);	
			}
		}
		
		//loops the process the provided number of times
		for(int j = 0; j < steps; j++)
		{
			Iterator<Item> itemIterator = items.iterator();
			Item activeItem = itemIterator.next();
			
			//count the number of conflicts for a given item and return true if there are no conflicts
			for(Bag b:bags)
			{
				//check the size of the bag
				if(b.currentSize() > b.bagSize()) {conflicts++;}
				if(b.currentSize() < (0.9 * b.bagSize())) {conflicts++;}
				
				//check for unary conflicts
				for(Item i:items)
				{
					if(b.contains.contains(i))
					{
						if(i.badBags.contains(b)){conflicts++;}
					}
				}
				//check for binary conflicts
				
			}
			
			if(conflicts == 0)
			{
				//I'm not sure how to provide list of bags that was correct
				return true;
			}
			
			//move item to the bag with the least amount of conflicts
			Bag incorectBag = null;
			
			//find the bag with the activeItem and remove it from it
			for(Bag b:bags)
			{
				if(b.contains.contains(activeItem))
				{
					b.contains.remove(activeItem);
					incorectBag = b;
				}
			}
			
			//place the activeItem in every bag that is not the previous bag and find the bag with the fewest conflicts
			for(Bag b:bags)
			{
				int newConflicts = 0;
				Bag bestBag = null;
				if(b != incorectBag)
				{
					b.contains.add(activeItem);
					for(Bag b2:bags)
					{
						//check the size of the bag
						if(b2.currentSize() > b2.bagSize()) {newConflicts++;}
						if(b2.currentSize() < (0.9 * b2.bagSize())) {newConflicts++;}
						
						//check for unary conflicts
						for(Item i:items)
						{
							if(b2.contains.contains(i))
							{
								if(i.badBags.contains(b2)){newConflicts++;}
							}
						}
						//ToDo check for binary conflicts
						if(newConflicts <=conflicts)
						{
							//this is a better solution
							bestBag = b2;
							conflicts = newConflicts;
						}
						else 
						{
							newConflicts = 0;
						}
					}
					b.contains.remove(activeItem);
				}
				
				//assign the item to the bag with the least number of conflicts
				bestBag.contains.add(activeItem);
			}
		}
		//the solution could not be found in the given number of steps
		return false;
	}
}