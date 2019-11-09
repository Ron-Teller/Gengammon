package problems.multiknapsack;

import gene.Gene;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import similarity.Similarity;

public class MultiKnapsackSimilarity implements Similarity {

	@Override
	public double getSimilarity(Gene g1, Gene g2) {
		List<Integer> s1 = ((MultiKnapsackGene)g1).getSackItemIndexes();
		List<Integer> s2 = ((MultiKnapsackGene)g2).getSackItemIndexes();
		String str1 = s1.stream()
				.map(Object::toString).collect(Collectors.joining());
		String str2 = s2.stream()
				.map(Object::toString).collect(Collectors.joining());
		return 1-(double)(StringUtils.getLevenshteinDistance(str1, str2))/
						str1.length();
	}

}
