package problems.bullseye;

import algorithm.GeneticAlgorithm;
import algorithm.StopRunCondition;

public class BullseyeStopRunCondition implements StopRunCondition {

	@Override
	public boolean stopRun(GeneticAlgorithm algorithm) {
		return (algorithm.getBest().getFitness() == 0);
	}
}
