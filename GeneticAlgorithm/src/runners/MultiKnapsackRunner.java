package runners;

import fitness.Fitness;
import fitness.FitnessComparator;
import fitness.Niching;
import gene.Gene;
import gene.GeneGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mutation.Mutation;
import optima.LocalOptimaHandler;
import optima.RandomImigrants;
import population.Population;
import problems.multiknapsack.MKPDataFileConverter;
import problems.multiknapsack.MultiKnapsackFitness;
import problems.multiknapsack.MultiKnapsackGeneGenerator;
import problems.multiknapsack.MultiKnapsackOrderedCrossover;
import problems.multiknapsack.MultiKnapsackStopRunCondition;
import problems.multiknapsack.MultiKnapsackSwapMutation;
import selection.Selection;
import selection.ranking.RankSelection;
import selection.rws.RouletteWheelInvertedFitness;
import selection.rws.scaling.ExponentialScaling;
import selection.rws.scaling.NormalScaling;
import selection.sus.SUS;
import selection.tournament.Tournament;
import similarity.Similarity;
import algorithm.GeneticAlgorithm;
import algorithm.GeneticAlgorithmIterationRunner;
import algorithm.GeneticAlgorithmTimer;
import algorithm.StopRunCondition;
import crossover.Crossover;

public class MultiKnapsackRunner implements Runner{

	private MKPDataFileConverter converter = new MKPDataFileConverter();
	private List<Double> itemValues = getItemValues();
	private List<List<Double>> sackItemWeights = getSackItemWeights();
	private List<Double> sackCapacities = getSackCapacities();
	private int itemAmount;
	private String file;
	
	public MultiKnapsackRunner() {
		this.converter = new MKPDataFileConverter();
		file = "PB6.DAT";
		File dataFile = new File(Paths.get("data/", file).toString());
		try {
			converter.convert(dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		itemAmount = converter.getItemAmount();		
		this.itemValues = getItemValues();
		this.sackItemWeights = getSackItemWeights();
		this.sackCapacities = getSackCapacities();
	}

	@Override
	public void run(int iterations, double timeout) {
		
		GeneticAlgorithm ga = createGeneticAlgorithm();
		double threshold = 1;
		StopRunCondition condition = new MultiKnapsackStopRunCondition(
				converter.getOptimumValue(), threshold);
		GeneticAlgorithmTimer timer = new GeneticAlgorithmTimer(ga,
				timeout,condition);
		
		GeneticAlgorithmIterationRunner iter = 
				new GeneticAlgorithmIterationRunner(timer, iterations);
		
		List<Selection> selectors = getSelectors();
		List<Double> mutationRates = getMutationRates();
		List<Double> eliteRates = getEliteRates();
		List<Integer> populationSizes = getPopulationSizes();
		
		if (iterations == 1 && mutationRates.size() == 1 &&
				eliteRates.size()==1 && populationSizes.size() == 1) {
			timer.setVerbose(true);
		}
		Map<String, String> resultOutput = new HashMap<>();
		for (Integer popSize : populationSizes) {
			for (Double eRate : eliteRates) {
				for (Double mRate : mutationRates) {
					ga.getPopulation().setSize(popSize);
					ga.setEliteRate(eRate);
					ga.setMutationRate(mRate);
					resultOutput.putAll(iter.run());
					resultOutput.putAll(ga.toMap());
					resultOutput.put("optimal", Double.toString(converter.getOptimumValue()));
					resultOutput.put("approximation", Double.toString(threshold));
					resultOutput.put("problem", "multiknapsack");
					System.out.println(ga);
					System.out.println("Optimal result: "+converter.getOptimumValue());
					System.out.println("Approximation: "+threshold);
					System.out.println("File: "+file);
				}
			}
		}
	}
	
	private GeneticAlgorithm createGeneticAlgorithm() {
		GeneticAlgorithm ga = new GeneticAlgorithm();
		GeneGenerator geneGenerator = new MultiKnapsackGeneGenerator(itemAmount);
		Comparator<Gene> fitnessComparator = new FitnessComparator();
		Population population = new Population(geneGenerator);
		Selection selector = new Tournament(2);
		Crossover crosser = new MultiKnapsackOrderedCrossover();
		Mutation mutator = new MultiKnapsackSwapMutation();
		Similarity similarity = null;
		Fitness fitness = new MultiKnapsackFitness(itemValues, sackItemWeights, sackCapacities);
		Fitness niching = new Niching(0.1, 1, fitness, similarity, ga);
		LocalOptimaHandler optimaHandler = new RandomImigrants(ga, 0.2);
		
		ga.setSelector(selector);
		ga.setCrosser(crosser);
		ga.setMutator(mutator);
		ga.setFitness(fitness);
		ga.setFitnessComparator(fitnessComparator);
		ga.setPopulation(population);
		ga.setOptimaHandler(optimaHandler);
		return ga;
	}
	
	
	private List<List<Double>> getSackItemWeights() {
		return converter.getSackItemWeights();
	}
	
	private List<Double> getItemValues() {
		return converter.getItemValues();
	}
	
	private List<Double> getSackCapacities() {
		return converter.getSackCapacities();
	}
	
	private List<Integer> getPopulationSizes() {
//		List<Integer> populationSizes = Arrays.asList(300, 1000);
		List<Integer> populationSizes = Arrays.asList(300);
		return populationSizes;
	}
	
	private List<Double> getEliteRates() {
		List<Double> eliteRates = Arrays.asList(0.15);
		return eliteRates;
	}
	
	private List<Double> getMutationRates() {
//		List<Double> mutationRates = Arrays.asList(0.1, 0.25, 0.4, 0.6, 0.75, 0.9);
		List<Double> mutationRates = Arrays.asList(0.6);
		return mutationRates;
	}
	
	private List<Selection> getSelectors() {
		List<Selection> selectors = new ArrayList<Selection>();
		selectors.add(new RouletteWheelInvertedFitness(new NormalScaling()));
		selectors.add(new RouletteWheelInvertedFitness(new ExponentialScaling()));
		selectors.add(new Tournament(2));
		selectors.add(new RankSelection());
		selectors.add(new SUS());
		return selectors;
	}

}
