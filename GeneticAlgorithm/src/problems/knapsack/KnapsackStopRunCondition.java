package problems.knapsack;

import algorithm.GeneticAlgorithm;
import algorithm.StopRunCondition;

public class KnapsackStopRunCondition implements StopRunCondition {

	private double optimalValue;

	public KnapsackStopRunCondition(double optimalValue) {
		super();
		this.optimalValue = optimalValue;
	}

	@Override
	public boolean stopRun(GeneticAlgorithm algorithm) {
		return (algorithm.getBest().getFitness() == optimalValue);
	}

}
