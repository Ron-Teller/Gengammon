package crossover;

import gene.Gene;

import java.util.List;

import selection.Selection;

public interface Crossover {
	public List<Gene> breedOffpsring(Selection selector);
}
