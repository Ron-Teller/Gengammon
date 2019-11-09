package problems.multiknapsack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import fitness.Fitness;
import gene.Gene;

public class MultiKnapsackFitness implements Fitness {

	private final List<Double> itemValues;
	private final List<List<Double>> sackItemWeights;
	private final List<Double> sackCapacities;
	private final int sacksAmount;
	private List<Double> accumulatedWeights;
	

	public MultiKnapsackFitness(List<Double> itemValues,
			List<List<Double>> sackItemWeights, List<Double> sackCapacities) {
		super();
		this.itemValues = itemValues;
		this.sackItemWeights = sackItemWeights;
		this.sackCapacities = sackCapacities;
		this.sacksAmount = sackCapacities.size();
	}

	@Override
	public void setFitness(Gene gene) {
		MultiKnapsackGene mGene = (MultiKnapsackGene) gene;
		accumulatedWeights = new ArrayList<Double>
					(Collections.nCopies(sacksAmount, 0.0));
		List<Integer> indexes = mGene.getSackItemIndexes();
		double accumulatedValue = 0;
		for (int i=0; i<itemValues.size(); i++) {
			if (isAnySackFullCapacityForFirstNItems(i, indexes)) {
				break;
			} else {
				accumulatedValue += itemValues.get(indexes.get(i));
			}
		}
		gene.setFitness(accumulatedValue);
	
	}
	
	private boolean isAnySackFullCapacityForFirstNItems(int n, 
			List<Integer> indexes) {
		boolean result = 
			IntStream.range(0, sackItemWeights.size())
			.anyMatch(i -> accumulatedWeights.get(i) +
					sackItemWeights.get(i).get(indexes.get(n)) > 
					sackCapacities.get(i));
		if (result == false) {
			IntStream.range(0, sackItemWeights.size())
			.forEach(i -> accumulatedWeights.set
				(i, accumulatedWeights.get(i) + 
						sackItemWeights.get(i).get(indexes.get(n))));
		}
		return result;
	}

}
