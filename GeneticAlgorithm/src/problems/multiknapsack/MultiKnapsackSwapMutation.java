package problems.multiknapsack;

import java.util.Collections;
import java.util.Random;

import gene.Gene;
import mutation.Mutation;

public class MultiKnapsackSwapMutation implements Mutation{

	Random rand = new Random();
	
	@Override
	public void mutate(Gene gene) {
		MultiKnapsackGene mGene = (MultiKnapsackGene) gene;
		int sackSize = mGene.getSackItemIndexes().size();
		int firtsIndex = rand.nextInt(sackSize);
		int secondIndex = rand.nextInt(sackSize);
		if (firtsIndex == secondIndex) {
			secondIndex = (secondIndex+1)%sackSize;
		}		
		Collections.swap(mGene.getSackItemIndexes(), firtsIndex, secondIndex);
	}

	@Override
	public String toString() {
		return "MultiKnapsack Swap Mutation";
	}
	
}
