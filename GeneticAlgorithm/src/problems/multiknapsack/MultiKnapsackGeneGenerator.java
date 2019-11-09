package problems.multiknapsack;

import gene.Gene;
import gene.GeneGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MultiKnapsackGeneGenerator implements GeneGenerator {

	private List<Integer> indexes;
	
	public MultiKnapsackGeneGenerator(int itemAmount) {
		this.indexes = IntStream.range(0, itemAmount)
				.boxed().collect(Collectors.toList());
	}

	@Override
	public Gene createGene() {
		List<Integer> shuffledIndexes = new ArrayList<Integer>(indexes);
		Collections.shuffle(shuffledIndexes);
		return new MultiKnapsackGene(shuffledIndexes);
	}

}
