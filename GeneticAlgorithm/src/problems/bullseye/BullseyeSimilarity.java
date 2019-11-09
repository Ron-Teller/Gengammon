package problems.bullseye;

import org.apache.commons.lang3.StringUtils;

import gene.Gene;
import similarity.Similarity;

public class BullseyeSimilarity implements Similarity {

	@Override
	public double getSimilarity(Gene g1, Gene g2) {
		String s1 = ((BullseyeGene) g1).getWord();
		String s2 = ((BullseyeGene) g2).getWord();
		return 1-(double)(StringUtils.getLevenshteinDistance(s1, s2))/
						s1.length();
	}
}
