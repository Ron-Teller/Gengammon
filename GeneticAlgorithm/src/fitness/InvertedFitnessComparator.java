package fitness;

import gene.Gene;

import java.util.Comparator;

public class InvertedFitnessComparator implements Comparator<Gene>{

	@Override
	public int compare(Gene g1, Gene g2) {
		return Double.compare(g1.getFitness(), g2.getFitness());
	}
}
