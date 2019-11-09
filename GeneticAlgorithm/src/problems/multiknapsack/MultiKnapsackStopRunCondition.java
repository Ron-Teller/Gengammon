package problems.multiknapsack;

import algorithm.GeneticAlgorithm;
import algorithm.StopRunCondition;

public class MultiKnapsackStopRunCondition implements StopRunCondition{

	private double optimalValue;
	private double threshold;
	

	public MultiKnapsackStopRunCondition(double optimalValue, double threshold) {
		super();
		this.optimalValue = optimalValue;
		this.threshold = threshold;
	}

	@Override
	public boolean stopRun(GeneticAlgorithm algorithm) {
		return (algorithm.getBest().getFitness() >= optimalValue*threshold);
	}

}
