package algorithm;

public class IterationResult {

	public double threshold;
	public double underThresholdDeviation;
	public long iterations;
	public int timedOut;
	public double averageRunTime;
	public double averageBestFitness;
	public double averageGenerations;
	public double standardDeviation;
	
	public IterationResult(double threshold, double underThresholdDeviation,
			long iterations, int timedOut, double averageRunTime,
			double averageBestFitness, double averageGenerations,
			double standardDeviation) {
		super();
		this.threshold = threshold;
		this.underThresholdDeviation = underThresholdDeviation;
		this.iterations = iterations;
		this.timedOut = timedOut;
		this.averageRunTime = averageRunTime;
		this.averageBestFitness = averageBestFitness;
		this.averageGenerations = averageGenerations;
		this.standardDeviation = standardDeviation;
	}
}
