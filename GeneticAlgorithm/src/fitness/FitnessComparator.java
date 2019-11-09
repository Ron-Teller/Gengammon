package fitness;

import gene.Gene;

import java.util.Comparator;

public class FitnessComparator implements Comparator<Gene> {
	
	@Override
	public int compare(Gene g1, Gene g2) {
		return Double.compare(g2.getFitness(), g1.getFitness());
	}
}
