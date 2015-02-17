package cs4341hw4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ProjectInterface 
{
	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	public void input() throws IOException
	{
		Scanner s = new Scanner(in.readLine());
		
		//waits for a line that begins with #####
		while(!s.hasNext("#####"))
		{
			s.next();
		}
		//skips the rest of the line 
		s.next();
		
		//reads the item declarations
		while(!s.hasNext("#####"))
		{
			int size;
			String name;
			name = s.next();
			size = s.nextInt();
			Item item = new Item();
			item.createItem(name, size);
		}
		s.next();
		
		//reads the bag declarations
		while(!s.hasNext("#####"))
		{
			int size;
			String name;
			name = s.next();
			size = s.nextInt();
			Bag bag = new Bag();
			bag.createBag(name, size);
		}
		s.next();
		
		//reads the unary constraints
		while(!s.hasNext("#####"))
		{
			//Add unary constraints here, I'm not sure how to implement this
		}
		s.next();
		
		//reads the binary constraints
		while(!s.hasNext("#####"))
		{
			//Add binary constraints here, I'm not sure how to implement this
		}
		s.next();
		
		s.close();
	}
	
}
