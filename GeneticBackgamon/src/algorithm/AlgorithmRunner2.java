package algorithm;

import fitness.Fitness;
import fitness.FitnessComparator;
import fitness.Niching;
import gene.Gene;
import gene.GeneGenerator;
import io.DataSerializer;
import io.DataSerializer2;
import io.GameFileConverter;
import io.GameFileParser;
import io.MoveTestFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
import selection.Selection;
import selection.ranking.RankSelection;
import selection.rws.RouletteWheelInvertedFitness;
import selection.rws.scaling.ExponentialScaling;
import selection.rws.scaling.NormalScaling;
import selection.sus.SUS;
import selection.tournament.Tournament;
import similarity.Similarity;
import sun.swing.BakedArrayList;
import algorithm.genetic.BackgammonFitness;
import algorithm.genetic.BackgammonGene;
import algorithm.genetic.BackgammonGeneGenerator;
import algorithm.genetic.BackgammonSerializedGeneGenerator;
import algorithm.genetic.BackgammonSwapMutation;
import algorithm.genetic.BackgammonTwoPointCrossover;
import algorithm.genetic.BackgammonUniformWeightCrossover;
import crossover.Crossover;

public class AlgorithmRunner2 {

	GameFileParser parser = new GameFileParser();
	private GameFileConverter converter = new GameFileConverter(parser);
	private MoveTestFilter filter = new MoveTestFilter();
	private DataSerializer2 serializer = new DataSerializer2();
	private List<MoveTest> tests = new ArrayList<MoveTest>();
	private List<Gene> genes;
	int l1Size = 52;
	int l2Size = 26;
	private String inputFile;
	private String outputFile;

	public void run(int iterations, Double timeout) {
		
		GeneticAlgorithm ga = createGeneticAlgorithm();
		StopRunCondition condition = new DefaultStopRunCondition();
		GeneticAlgorithmTimer timer = new GeneticAlgorithmTimer(ga,
				timeout,condition);
		timer.addGenerationListener(new ActionListener() {
			
			int count = 0;
			int saveEvery = 30;
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Percentage: "+ga.getBest().getFitness()/tests.size());
				count ++;
				if (count >= saveEvery) {
					count = 0;
					List<BackgammonGene> bgenes = new ArrayList<BackgammonGene>();
					for (Gene gene : ga.getPopulation().getGenes()) {
						bgenes.add((BackgammonGene) gene);
					}
					serializer.savePopulation(bgenes, outputFile);
				}
				
			}
		});
		
		List<Selection> selectors = getSelectors();
		List<Double> mutationRates = getMutationRates();
		List<Double> eliteRates = getEliteRates();
		List<Integer> populationSizes = getPopulationSizes();
		
		if (iterations == 1 && mutationRates.size() == 1 &&
				eliteRates.size()==1 && populationSizes.size() == 1) {
			timer.setVerbose(true);
		}
		timer.setVerbose(true);
		Map<String, String> resultOutput = new HashMap<>();
		for (Integer popSize : populationSizes) {
			for (Double eRate : eliteRates) {
				for (Double mRate : mutationRates) {
					ga.getPopulation().setSize(popSize);
					ga.getPopulation().setNewGeneration(this.genes);
					ga.setEliteRate(eRate);
					ga.setMutationRate(mRate);
					timer.run();
				}
			}
		}
	}
	
	private GeneticAlgorithm createGeneticAlgorithm() {
		GeneticAlgorithm ga = new GeneticAlgorithm();
		GeneGenerator geneGenerator = new BackgammonGeneGenerator();
		Comparator<Gene> fitnessComparator = new FitnessComparator();
		Population population = new Population(geneGenerator);
		Selection selector = new Tournament(4);
		Crossover crosser = new BackgammonUniformWeightCrossover();
//		Crossover crosser = new BackgammonTwoPointCrossover();
		Mutation mutator = new BackgammonSwapMutation();
		Similarity similarity = null;
		Fitness fitness = new BackgammonFitness(tests);
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
	
	private List<Integer> getPopulationSizes() {
//		List<Integer> populationSizes = Arrays.asList(300, 1000);
		List<Integer> populationSizes = Arrays.asList(20);
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
	
	public void setTests(int maxFiles) {
		String dirPath = "resources/training/matches/";
		File directory = new File(dirPath);
		tests = filter.filter
				(converter.convertFilesToTests(directory, maxFiles));
	}

	public void setGenes(String input) {
		this.genes = new ArrayList<Gene>();
		List<BackgammonGene> bgenes = serializer.loadPopulation(input);
		this.genes.addAll(bgenes);
	}
	
	public void setGenes(int populationSize) {
		NeuralNetwork nn;
		int popsize = populationSize;
		this.genes = new ArrayList<Gene>();
		for (int i=0; i<popsize; i++) {
			nn = new NeuralNetwork(l1Size, l2Size);
			nn.randomize();
			this.genes.add(new BackgammonGene(nn));
		}
	}

	public final String getInputFile() {
		return inputFile;
	}

	public final void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public final String getOutputFile() {
		return outputFile;
	}

	public final void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

}
