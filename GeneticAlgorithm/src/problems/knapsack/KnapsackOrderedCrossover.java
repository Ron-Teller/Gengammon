package problems.knapsack;

import gene.Gene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import selection.Selection;
import crossover.Crossover;

public class KnapsackOrderedCrossover implements Crossover {

	@Override
	public List<Gene> breedOffpsring(Selection selector) {
		KnapsackGene p1 = (KnapsackGene) selector.selectGene();
		KnapsackGene p2 = (KnapsackGene) selector.selectGene();
		return crossover(p1, p2);
	}
	
	private List<Gene> crossover(KnapsackGene p1, KnapsackGene p2) {
		List<Gene> offspring = new ArrayList<Gene>();
		offspring.add(new KnapsackGene(
				crossoverSack(p1.getSack(), p2.getSack())));
		return offspring;
	}
	
	private List<Item> crossoverSack(List<Item> sack1,
								List<Item> sack2) {
		int sackSize = sack1.size();
		List<Item> crossoveredSack = new ArrayList<Item>
						(Collections.nCopies(sackSize, null));		
		
		generateNUniqueRandomIndexes(sackSize-1, sackSize/2).stream()
			.forEach(index -> crossoveredSack.set(index, sack1.get(index)));
		
		IntStream.range(0, sackSize)
			.filter(index -> crossoveredSack.get(index) == null)
			.forEach(index -> crossoveredSack.set(index, 
					findFirstNonContainedItemFromSack(crossoveredSack,sack2)));
		
		return crossoveredSack;
	}
	
	private List<Integer> generateNUniqueRandomIndexes(int lastIndex, int n) {
		List<Integer> indexes = IntStream.rangeClosed(0, lastIndex)
				.boxed().collect(Collectors.toList());
		Collections.shuffle(indexes);
		return indexes.subList(0, n);
	}
	
	private Item findFirstNonContainedItemFromSack(List<Item> missing, 
										List<Item> from) {
		return from.stream().filter(item -> ! missing.contains(item))
				.findFirst().get();
	}

	@Override
	public String toString() {
		return "Knapsack Ordered Crossover";
	}
	
}
