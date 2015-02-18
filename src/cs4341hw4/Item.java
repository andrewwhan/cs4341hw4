package cs4341hw4;

import java.util.ArrayList;
import java.util.Collection;

public class Item 
{
	String name;
	int size;
	Collection<String> badBags = new ArrayList<String>();
	
	Item(String n, int s)
	{
		name = n;
		size = s;
	}
	
	public String itemName()
	{
		return name;
	}
	
	public int itemSize()
	{
		return size;
	}
}
