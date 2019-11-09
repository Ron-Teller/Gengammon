package algorithm;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.time.StopWatch;

public class GeneticAlgorithmTimer {

	private GeneticAlgorithm geneticAlgorithm;
	private Double maxRunTime;
	private StopRunCondition stopRunCondition;
	private StopWatch timer;
	private boolean verbose = false;
	private List<ActionListener> generationListeners = new ArrayList<>();
	
	public GeneticAlgorithmTimer(GeneticAlgorithm geneticAlgorithm,
			Double maxRunTime, StopRunCondition stopRunCondition) {
		super();
		this.geneticAlgorithm = geneticAlgorithm;
		this.maxRunTime = maxRunTime;
		this.stopRunCondition = stopRunCondition;
		this.timer = new StopWatch();
	}

	public void run() {
		timer.reset();
		timer.start();
		timer.suspend();
//		geneticAlgorithm.initializePopulation();
		while (! shouldStopRun()) {
			timer.resume();
			geneticAlgorithm.advanceGeneration();
			timer.suspend();
			if (verbose) {
				printGenerationInfo();
			}
			for (ActionListener listener : generationListeners) {
				listener.actionPerformed(null);
			}
		}
		timer.stop();
	}
	
	private void printGenerationInfo() {
		System.out.print("Generation: "+geneticAlgorithm.getPopulation().getGeneration()+" ");
		System.out.print("Avg: "+geneticAlgorithm.getPopulation().getFitnessAverage()+" ");
		System.out.print("Best:"+geneticAlgorithm.getBest().getFitness()+" ");
		System.out.print("Dev:"+geneticAlgorithm.getPopulation().getStandardDeviation()+" ");
		System.out.print("Time Elapsed: "+(double)getRunTime()/1000000000);
		System.out.println();
	}
	
	public boolean hasTimedOut() {
		if (maxRunTime == null) {
			return false;
		} else {
			return timer.getNanoTime()/1000000000 > maxRunTime ;
		}
	}
	
	public long getRunTime() {
		return timer.getNanoTime();
	}
	
	private boolean shouldStopRun() {
		return (hasTimedOut() ||
				stopRunCondition.stopRun(geneticAlgorithm));
	}

	public GeneticAlgorithm getGeneticAlgorithm() {
		return geneticAlgorithm;
	}
	
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public double getMaxRunTime() {
		return maxRunTime;
	}
	
	public void addGenerationListener(ActionListener listener) {
		generationListeners.add(listener);
	}
	
	public void removeGenerationListener(ActionListener listener) {
		generationListeners.remove(listener);
	}	
	
}
