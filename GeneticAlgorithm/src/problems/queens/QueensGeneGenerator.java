package problems.queens;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import gene.Gene;
import gene.GeneGenerator;

public class QueensGeneGenerator implements GeneGenerator {

	private final int queenCount;
	
	public QueensGeneGenerator(int queens) {
		super();
		this.queenCount = queens;
	}

	@Override
	public Gene createGene() {
		return new QueensGene(createQueenPermutation());
	}

	private List<Integer> createQueenPermutation() {
		List<Integer> permutation = IntStream
				.range(0, queenCount)
				.boxed().collect(Collectors.toList());
		Collections.shuffle(permutation);
		return permutation;
	}
}
