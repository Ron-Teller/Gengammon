package problems.multiknapsack;

import gene.Gene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import selection.Selection;
import crossover.Crossover;

public class MultiKnapsackOrderedCrossover implements Crossover {

	@Override
	public List<Gene> breedOffpsring(Selection selector) {
		MultiKnapsackGene p1 = (MultiKnapsackGene) selector.selectGene();
		MultiKnapsackGene p2 = (MultiKnapsackGene) selector.selectGene();
		return crossover(p1, p2);
	}
	
	private List<Gene> crossover(MultiKnapsackGene p1, MultiKnapsackGene p2) {
		List<Gene> offspring = new ArrayList<Gene>();
		offspring.add(new MultiKnapsackGene(
				crossoverIndexes(p1.getSackItemIndexes(), p2.getSackItemIndexes())));
		return offspring;
	}
	
	private List<Integer> crossoverIndexes(List<Integer> indexes1,
			List<Integer> indexes2) {
		int sackSize = indexes1.size();
		List<Integer> crossoveredIndexes = new ArrayList<Integer>
						(Collections.nCopies(sackSize, null));		
		
		generateNUniqueRandomIndexes(sackSize-1, sackSize/2).stream()
			.forEach(index -> crossoveredIndexes.set(index, indexes1.get(index)));
		
		IntStream.range(0, sackSize)
			.filter(index -> crossoveredIndexes.get(index) == null)
			.forEach(index -> crossoveredIndexes.set(index, 
					findFirstNonContainedIndexFromSackIndexes(crossoveredIndexes,indexes2)));
		
		return crossoveredIndexes;
	}
	
	private List<Integer> generateNUniqueRandomIndexes(int lastIndex, int n) {
		List<Integer> indexes = IntStream.rangeClosed(0, lastIndex)
				.boxed().collect(Collectors.toList());
		Collections.shuffle(indexes);
		return indexes.subList(0, n);
	}
	
	private Integer findFirstNonContainedIndexFromSackIndexes(List<Integer> missing, 
										List<Integer> from) {
		return from.stream().filter(item -> ! missing.contains(item))
				.findFirst().get();
	}

	@Override
	public String toString() {
		return "Multi Knapsack Ordered Crossover";
	}

}
