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
	
	public void status(){
		System.out.print(name + " ");
		int w = 0;
		for(Item i : contains){
			System.out.print(i.name + " ");
			w += i.size;
		}
		System.out.println();
		System.out.println("Number of items: " + contains.size());
		System.out.println("Total weight: " + w + "/" + size);
		System.out.println("Wasted capacity: " + (size-w));
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
