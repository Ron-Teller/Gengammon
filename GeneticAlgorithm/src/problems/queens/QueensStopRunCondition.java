package problems.queens;

import algorithm.GeneticAlgorithm;
import algorithm.StopRunCondition;

public class QueensStopRunCondition implements StopRunCondition {

	@Override
	public boolean stopRun(GeneticAlgorithm algorithm) {
		return (algorithm.getBest().getFitness() == 0);
	}

}
