package cs4341hw4;

public class Bag 
{
	String name;
	int size;
	
	void createBag(String n, int s)
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
}
