package problems.queens;

import org.apache.commons.lang3.StringUtils;

import gene.Gene;
import similarity.Similarity;

public class QueensSimilarity implements Similarity {

	@Override
	public double getSimilarity(Gene g1, Gene g2) {
		String s1 = geneToString((QueensGene) g1);
		String s2 = geneToString((QueensGene) g2);
		return 1-(double)(StringUtils.getLevenshteinDistance(s1, s2))/
				  s1.length();
	}
	
	private String geneToString(QueensGene gene) {
		StringBuilder sb = new StringBuilder();
		gene.getRows().stream().forEach(row -> sb.append(row));
		return sb.toString();
	}

}
