package cs4341hw4;

public class Item 
{
	String name;
	int size;
	
	void createItem(String n, int s)
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
