package algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import mutation.Mutation;
import optima.LocalOptimaHandler;
import population.Population;
import selection.Selection;
import crossover.Crossover;
import fitness.Fitness;
import gene.Gene;


public class GeneticAlgorithm {
	
	private double eliteRate;
	private double mutationRate;
	private Population population;
	private Selection selector;
	private Crossover crosser;
	private Mutation mutator;
	private Fitness fitness;
	private Comparator<Gene> fitnessComparator;
	private LocalOptimaHandler optimaHandler;
	Random rand = new Random();

	public void advanceGeneration() {
		selector.setGenes(population.getGenes());
		List<Gene> nextGeneration = new ArrayList<Gene>();
		addPopulationElite(nextGeneration);
		addBreededOffspring(nextGeneration);
		mutate(nextGeneration);
		population.setNewGeneration(nextGeneration);
		population.setFitness(fitness);
		population.sort(fitnessComparator);
//		optimaHandler.onNewGeneration();
	}
	
	public void initializePopulation() {
		population.initialize();
		population.setFitness(fitness);
		population.sort(fitnessComparator);
	}
	
	public Gene getBest() {
		return population.getGenes().get(0);
	}
	
	private void addPopulationElite(List<Gene> genes) {
		genes.addAll(population.getGenes()
				.subList(0, getEliteCount()));
	}
	
	private void addBreededOffspring(List<Gene> genes) {
		List<Gene> offspring = new ArrayList<Gene>();
		while (offspring.size() < getOffspringCount()) {
			offspring.addAll(crosser.breedOffpsring(selector));
		}
		offspring = offspring.subList(0, getOffspringCount());
		genes.addAll(offspring);
	}
	
	private void mutate(List<Gene> genes) {
		genes.subList(getEliteCount(), genes.size()).stream()
		.forEach(gene -> mutate(gene));
	}
	
	private void mutate(Gene gene) {
		if (shouldMutate()) {
			mutator.mutate(gene);
		}
	}
	
	private boolean shouldMutate() {
		return rand.nextInt(Integer.MAX_VALUE) < 
					Integer.MAX_VALUE*mutationRate;
	}
	
	private int getEliteCount() {
		return (int) (eliteRate*population.getSize());
	}
	
	private int getOffspringCount() {
		return population.getSize() - getEliteCount();
	}

	public double getEliteRate() {
		return eliteRate;
	}

	public void setEliteRate(double eliteRate) {
		this.eliteRate = eliteRate;
	}

	public double getMutationRate() {
		return mutationRate;
	}

	public void setMutationRate(double mutationRate) {
		this.mutationRate = mutationRate;
	}

	public Population getPopulation() {
		return population;
	}

	public void setPopulation(Population population) {
		this.population = population;
		this.population.setFitness(fitness);
		this.population.sort(fitnessComparator);
	}

	public void setSelector(Selection selector) {
		this.selector = selector;
	}

	public void setCrosser(Crossover crosser) {
		this.crosser = crosser;
	}

	public void setMutator(Mutation mutator) {
		this.mutator = mutator;
	}

	public void setFitness(Fitness fitness) {
		this.fitness = fitness;
	}

	public void setFitnessComparator(Comparator<Gene> fitnessComparator) {
		this.fitnessComparator = fitnessComparator;
	}

	public void setOptimaHandler(LocalOptimaHandler optimaHandler) {
		this.optimaHandler = optimaHandler;
	}

	public Fitness getFitness() {
		return fitness;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Selection: "+selector+"\n");
		sb.append("Crossover: "+crosser+"\n");
		sb.append("Mutator: "+mutator+"\n");
		sb.append("Elite Rate: "+eliteRate+"\n");
		sb.append("Mutation Rate: "+mutationRate+"\n");
		sb.append("Population Size: "+population.getSize());
		return sb.toString();
	}
	
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<>();
		map.put("selection",selector.toString());
		map.put("crossover",crosser.toString());
		map.put("mutator",mutator.toString());
		map.put("elite",Double.toString(eliteRate));
		map.put("mutation",Double.toString(mutationRate));
		map.put("population",Integer.toString(population.getSize()));
		return map;
	}
	
}
