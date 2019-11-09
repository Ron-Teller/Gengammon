package problems.knapsack;

import fitness.Fitness;
import gene.Gene;

public class KnapsackFitness implements Fitness{

	private final int maxSackWeight;
	
	public KnapsackFitness(int maxWeight) {
		super();
		this.maxSackWeight = maxWeight;
	}

	@Override
	public void setFitness(Gene gene) {
		KnapsackGene kGene = (KnapsackGene) gene;
		int accumulatedWeight = 0;
		int accumulatedValue = 0;

		for (Item item : kGene.getSack()) {
			if (accumulatedWeight + item.getWeight() <=
				maxSackWeight) {
				accumulatedValue += item.getValue();
				accumulatedWeight += item.getWeight();
			} else {
				break;
			}
		}
		kGene.setFitness(accumulatedValue);
	}
}
