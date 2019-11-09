package algorithm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneticAlgorithmIterationRunner {

	private GeneticAlgorithmTimer algorithmTimer;
	private int iterations;
	private List<ActionListener> iterationListeners = new ArrayList<>();
	
	public GeneticAlgorithmIterationRunner(
			GeneticAlgorithmTimer algorithmTimer, int iterations) {
		super();
		this.algorithmTimer = algorithmTimer;
		this.iterations = iterations;
	}

	public Map<String, String> run() {
		double timeElapsed = 0;
		int algorithmTimedOutCount = 0;
		long generationSum = 0;
		double fitnessSum = 0;
		int generations;
		double deviation;
		double deviationSum = 0;
		double devSum = 0;
		double threshold = 0;
		int devIter = 0;
		for (int i=0; i<iterations; i++) {
			algorithmTimer.run();
			if (algorithmTimer.hasTimedOut()) {
				algorithmTimedOutCount ++;
			} else {
				timeElapsed += (double)algorithmTimer.getRunTime()/1000000000;
				generations = algorithmTimer.getGeneticAlgorithm()
						.getPopulation().getGeneration();
				generationSum += generations;
				deviation = algorithmTimer.getGeneticAlgorithm()
						.getPopulation().getStandardDeviation();
				if (deviation < threshold) {
					devIter ++;
					devSum += deviation;
					
				}
				deviationSum += deviation;
			}
			fitnessSum += algorithmTimer.getGeneticAlgorithm().getBest().getFitness();
			for (ActionListener listener : iterationListeners) {
				listener.actionPerformed(null);
			}
		}
		Map<String, String> result = new HashMap<String, String>();
		System.out.println();
		printResult(timeElapsed, algorithmTimedOutCount, generationSum, deviationSum, fitnessSum);
		System.out.println("Threshold: 100");
		System.out.println("Threshold deviation: "+(double)devSum/devIter);
		result.put("threshold", Double.toString(threshold));
		result.put("threshold-deviation", Double.toString(devSum/devIter));
		result.put("average-generations", Double.toString((double)generationSum/iterations));
		result.put("timed-out", Integer.toString(algorithmTimedOutCount));
		result.put("average-run-time", Double.toString(timeElapsed/iterations));
		result.put("average-fitness", Double.toString(fitnessSum/iterations));
		result.put("iterations", Long.toString(iterations));
		result.put("deviation", Double.toString(deviationSum/iterations));
		return result;
	}
	
	public void printResult(double timeElapsed, int timedOut, long generationSum,
			double deviationSum, double fitnessSum) {
		System.out.println("Iterations: "+iterations);
		System.out.println("Max Run Time: "+algorithmTimer.getMaxRunTime());
		System.out.println("Timed out: "+timedOut);
		System.out.println("Average Time: "+timeElapsed/(iterations));
		System.out.println("Average Fitness: "+fitnessSum/iterations);
		System.out.println("Average generations: "+(double)generationSum/iterations);
		System.out.println("Standard deviation: "+(double)deviationSum/iterations);
	}
	
	public void addIterationListener(ActionListener listener) {
		iterationListeners.add(listener);
	}
	
	public void removeIterationListener(ActionListener listener) {
		iterationListeners.remove(listener);
	}
}
