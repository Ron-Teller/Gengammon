package problems.minimum;

import algorithm.GeneticAlgorithm;
import algorithm.StopRunCondition;

public class MinimumStopRunCondition implements StopRunCondition {

	@Override
	public boolean stopRun(GeneticAlgorithm algorithm) {
		MinimumGene gene = (MinimumGene) algorithm.getBest();
//		return (gene.getX1() == 0 && gene.getX2() == 0);
		return (gene.getFitness() == 0);
	}
}
