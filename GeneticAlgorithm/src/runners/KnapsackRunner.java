package runners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import problems.knapsack.Item;

public class KnapsackRunner implements Runner {

	@Override
	public void run(int iterations, double timeout) {
		// TODO Auto-generated method stub

	}

	private  List<Item> createSack(int maxItemWeight, 
			int maxItemValue, int sackSize) {
	Random rand = new Random(1);
	List<Item> sack = new ArrayList<Item>();
	for (int i=0; i<sackSize; i++) {
		sack.add(new Item(rand.nextInt(maxItemWeight)+1,
					rand.nextInt(maxItemValue)+1));
	}
	return sack;
}	
	
	
}
