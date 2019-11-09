package problems.knapsack;

import gene.Gene;

import java.util.List;
import java.util.Random;

import mutation.Mutation;

public class KnapsackSwapMutation implements Mutation {

	Random rand = new Random();
	
	@Override
	public void mutate(Gene gene) {
		KnapsackGene kGene = (KnapsackGene) gene;
		swapTwoSackItems(kGene.getSack());
	}
	
	private void swapTwoSackItems(List<Item> sack) {
		int firtsIndex = rand.nextInt(sack.size());
		int secondIndex = rand.nextInt(sack.size());
		if (firtsIndex == secondIndex) {
			secondIndex = (secondIndex+1)%sack.size();
		}
		Item tmp = sack.get(firtsIndex);
		sack.set(firtsIndex, sack.get(secondIndex));
		sack.set(secondIndex, tmp);
	}

	@Override
	public String toString() {
		return "Knapsack Swap Mutation";
	}
	
}
