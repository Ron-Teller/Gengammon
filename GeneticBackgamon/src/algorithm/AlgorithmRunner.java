package algorithm;

import io.DataSerializer;
import io.GameFileConverter;
import io.GameFileParser;
import io.MoveTestFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AlgorithmRunner {

	private List<MoveTest> tests;
	private String output;
	private GameFileConverter converter;
	private MoveTestFilter filter;
	private DataSerializer serializer;
	private List<Gene> population;
	
	public AlgorithmRunner() {
		super();
		GameFileParser parser = new GameFileParser();
		this.converter = new GameFileConverter(parser);
		this.filter = new MoveTestFilter();
		this.serializer = new DataSerializer();
	}

	public void setTests(int maxFiles) {
		String dirPath = "resources/training/matches/";
		File directory = new File(dirPath);
		tests = filter.filter
				(converter.convertFilesToTests(directory, maxFiles));
	}
	
	public void setOutput(String output) {
		this.output = output;
	}
	
	public void run(double eliteRate, double mutationRate, int generations,
			int iterations) {
		GeneticAlgorithmDep ga = new GeneticAlgorithmDep(eliteRate, mutationRate, generations, tests, population);
		for (int i=0; i<iterations; i++) {
			ga.run();
			serializer.savePopulation(population, output);
		}
	}
	
	public void setPopulation(String input) {
		this.population = serializer.loadPopulation(input);
		System.out.println(this.population.size());
	}
	
	public void setPopulation(int populationSize) {
		NeuralNetwork nn;
		int popsize = populationSize;
		int l1Size = 52;
		int l2Size = 26;
		population = new ArrayList<Gene>();
		for (int i=0; i<popsize; i++) {
			nn = new NeuralNetwork(l1Size, l2Size);
			nn.randomize();
			population.add(new Gene(nn));
		}
	}

	public List<Gene> getPopulation() {
		return population;
	}

	public List<MoveTest> getTests() {
		return tests;
	}
	
}
