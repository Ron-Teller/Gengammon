package optima;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import algorithm.GeneticAlgorithm;

public abstract class LocalOptimaHandler {
	
	protected final GeneticAlgorithm algorithm;
	private int generation = 0;
	protected int checkForEveryNGenerations = 1;
	private int deviationQueueSize = 4;
	private CircularFifoQueue<Double> lastNDeviations = 
			new CircularFifoQueue<Double>(deviationQueueSize);
	protected double deviationThreshold = 20;	
	
	public LocalOptimaHandler(GeneticAlgorithm algorithm) {
		super();
		this.algorithm = algorithm;
	}

	public final void onNewGeneration() {
		newGeneration();
		generation = (generation+1)%checkForEveryNGenerations;
		if (shouldCheckForLocalOptima()) {
			if (isLocalOptima()) {
				takeAction();
			} else {
				onNoLocalOptima();
			}
		}
	}
	
	private final boolean shouldCheckForLocalOptima() {
		if (generation == checkForEveryNGenerations-1) {
			return true;
		}
		return false;
	}
	
	private final void newGeneration() {
		if (checkForEveryNGenerations - generation <= deviationQueueSize) {
			lastNDeviations.add(algorithm.getPopulation().getStandardDeviation());	
		}
	}
	
	private final boolean isLocalOptima() {
		double deviationAverage = lastNDeviations.stream()
				.mapToDouble(Double::doubleValue).average()
				.getAsDouble();
		if (deviationAverage < deviationThreshold) {
			return true;
		}
		return false;
	}
	
	protected abstract void onNoLocalOptima();
	protected abstract void takeAction();
}
