package cs4341hw4;

import java.util.ArrayList;
import java.util.Collection;

public class Bag 
{
	String name;
	int size;
	Collection<Item> contains = new ArrayList<Item>();
	
	Bag(String n, int s)
	{
		name = n;
		size = s;
	}
	
	public String bagName()
	{
		return name;
	}
	
	public int bagSize()
	{
		return size;
	}
	
	public int itemCount(){
		return contains.size();
	}
	
	public int filled(){
		int w = 0;
		for(Item i : contains){
			w += i.size;
		}
		return w;
	}
	
	public void status(){
		System.out.print(name + " ");
		for(Item i : contains){
			System.out.print(i.name + " ");
		}
		System.out.println();
		System.out.println("Number of items: " + contains.size());
		System.out.println("Total weight: " + filled() + "/" + size);
		System.out.println("Wasted capacity: " + (size-filled()));
	}
	
	public int currentSize()
	{
		int w = 0;
		for(Item i : contains)
		{
			w += i.size;
		}
		return w;
	}
}
