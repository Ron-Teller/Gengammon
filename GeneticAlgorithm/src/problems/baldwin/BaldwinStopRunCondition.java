package problems.baldwin;

import algorithm.GeneticAlgorithm;
import algorithm.StopRunCondition;

public class BaldwinStopRunCondition implements StopRunCondition{

	private String target;

	public BaldwinStopRunCondition(String target) {
		super();
		this.target = target;
	}

	@Override
	public boolean stopRun(GeneticAlgorithm algorithm) {
		BaldwinGene gene = (BaldwinGene) algorithm.getBest();
		return target.equals(gene.getConnections());
	}
	
}
