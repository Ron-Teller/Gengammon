package problems.knapsack;

import gene.Gene;

import java.util.List;

public class KnapsackGene extends Gene {

	private List<Item> sack;

	public KnapsackGene(List<Item> sack) {
		super();
		this.sack = sack;
	}

	public List<Item> getSack() {
		return sack;
	}

	public void setSack(List<Item> sack) {
		this.sack = sack;
	}
}
