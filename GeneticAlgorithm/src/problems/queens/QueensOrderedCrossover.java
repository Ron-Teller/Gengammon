package problems.queens;

import gene.Gene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import selection.Selection;
import crossover.Crossover;

public class QueensOrderedCrossover implements Crossover {

	@Override
	public List<Gene> breedOffpsring(Selection selector) {
		QueensGene p1 = (QueensGene) selector.selectGene();
		QueensGene p2 = (QueensGene) selector.selectGene();
		return crossover(p1, p2);
	}
	
	private List<Gene> crossover(QueensGene p1, QueensGene p2) {
		List<Gene> offspring = new ArrayList<Gene>();
		offspring.add(new QueensGene(
				crossoverRows(p1.getRows(), p2.getRows())));
		return offspring;
	}

	private List<Integer> crossoverRows(List<Integer> rows1, List<Integer> rows2) {
		int rowsSize = rows1.size();
		List<Integer> crossoveredRows = new ArrayList<Integer>
						(Collections.nCopies(rowsSize, null));		
		
		generateNUniqueRandomIndexes(rowsSize-1, rowsSize/2).stream()
			.forEach(index -> crossoveredRows.set(index, rows1.get(index)));
		
		IntStream.range(0, rowsSize)
			.filter(index -> crossoveredRows.get(index) == null)
			.forEach(index -> crossoveredRows.set(index, 
					findFirstNonContainedItemFromRow(crossoveredRows,rows2)));
		
		return crossoveredRows;
	}
	
	private List<Integer> generateNUniqueRandomIndexes(int lastIndex, int n) {
		List<Integer> indexes = IntStream.rangeClosed(0, lastIndex)
				.boxed().collect(Collectors.toList());
		Collections.shuffle(indexes);
		return indexes.subList(0, n);
	}
	
	private Integer findFirstNonContainedItemFromRow(List<Integer> missing, 
										List<Integer> from) {
		return from.stream().filter(row -> ! missing.contains(row))
				.findFirst().get();
	}

	@Override
	public String toString() {
		return "Queens Ordered Crossover";
	}
	
}
