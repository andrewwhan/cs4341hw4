package cs4341hw4;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class BinMatrix {
	//Matrix representation of binary constraints. 0 means no constraint, -1 means not allowed.
	int[][] matrix;
	HashMap<String, Integer> names = new HashMap<String, Integer>();
	
	BinMatrix(Collection<Bag> bags){
		matrix = new int[bags.size()][bags.size()];
		int i = 0;
		Iterator<Bag> b = bags.iterator();
		while(b.hasNext()){
			names.put(b.next().bagName(), i);
			i++;
		}
	}
	
	public int get(String row, String column){
		return matrix[names.get(row)][names.get(column)];
	}
	
	public void put(String row, String column, int value){
		matrix[names.get(row)][names.get(column)] = value;
	}
}
