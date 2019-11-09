package problems.knapsack;

import gene.Gene;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

import similarity.Similarity;

public class KnapsackSimilarity implements Similarity {

	@Override
	public double getSimilarity(Gene g1, Gene g2) {
		List<Item> s1 = ((KnapsackGene)g1).getSack();
		List<Item> s2 = ((KnapsackGene)g1).getSack();
		String str1 = IntStream.rangeClosed(1, s1.size()).boxed()
				.map(Object::toString).collect(Collectors.joining());
		String str2 = s2.stream().mapToInt(s1::indexOf).boxed()
				.map(Object::toString).collect(Collectors.joining());
		return 1-(double)(StringUtils.getLevenshteinDistance(str1, str2))/
						str1.length();
	}
}
