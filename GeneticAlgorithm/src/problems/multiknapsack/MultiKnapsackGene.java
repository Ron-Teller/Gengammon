package problems.multiknapsack;

import gene.Gene;

import java.util.List;

public class MultiKnapsackGene extends Gene {

	private List<Integer> sackItemIndexes;

	public MultiKnapsackGene(List<Integer> sackItemIndexes) {
		super();
		this.sackItemIndexes = sackItemIndexes;
	}

	public List<Integer> getSackItemIndexes() {
		return sackItemIndexes;
	}

	public void setSackItemIndexes(List<Integer> sackItemIndexes) {
		this.sackItemIndexes = sackItemIndexes;
	}
	
}
