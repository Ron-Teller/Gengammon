package algorithm;

public class DefaultStopRunCondition implements StopRunCondition {

	@Override
	public boolean stopRun(GeneticAlgorithm algorithm) {
		return false;
	}

}
