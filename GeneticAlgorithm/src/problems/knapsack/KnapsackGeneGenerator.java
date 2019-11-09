package problems.knapsack;

import gene.Gene;
import gene.GeneGenerator;

import java.util.Collections;
import java.util.List;

public class KnapsackGeneGenerator implements GeneGenerator {

	private List<Item> sack;
	
	public KnapsackGeneGenerator(List<Item> sack) {
		super();
		this.sack = sack;
	}

	@Override
	public Gene createGene() {
		List<Item> shuffledSack = sack;
		Collections.shuffle(shuffledSack);
		KnapsackGene gene = new KnapsackGene(shuffledSack);
		return gene;
	}
	
}
