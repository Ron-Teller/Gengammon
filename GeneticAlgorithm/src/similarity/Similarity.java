package similarity;

import gene.Gene;

public interface Similarity {
	public double getSimilarity(Gene g1, Gene g2);
}
