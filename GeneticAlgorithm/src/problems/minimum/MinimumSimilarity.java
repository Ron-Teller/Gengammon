package problems.minimum;

import gene.Gene;
import similarity.Similarity;

public class MinimumSimilarity implements Similarity {

	@Override
	public double getSimilarity(Gene g1, Gene g2) {
		MinimumGene gene1 = (MinimumGene) g1;
		MinimumGene gene2 = (MinimumGene) g2;
		return 1-(double)(Math.abs(gene1.getX1()-gene2.getX1()) +
						Math.abs(gene1.getX2()-gene2.getX2())) /
						2*Integer.MAX_VALUE;
	}

}
