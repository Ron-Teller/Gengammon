package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import algorithm.util.Common;
import BG.Board;
import BG.Glossary.Color;

public class GeneticAlgorithmDep {

	private int popsize;
	private double eliteRate;
	private double mutationRate;
	private int populaitonSize;
	private List<Gene> population;
	private List<Gene> nextPopulation;
	int generations;
	int generationCount;
	private final static int l1Size = 52;
	private final int l2Size = 26;
	int[] input;
	int k=4;
	Random rand = new Random();
	List<MoveTest> tests;
	
	public GeneticAlgorithmDep(double elite_r, double mute_r, int gen, List<MoveTest> tests
			, List<Gene> population) {
		popsize = population.size();
		eliteRate = elite_r;
		mutationRate = mute_r;
		generations = gen;
		input = new int[l1Size];
		for (int i=0; i<l1Size; i++) {
			input[i] = 1;
		}
		this.tests = tests;
		this.population = population;
		this.populaitonSize = population.size();
		this.generationCount = 0;
		init_population();
	}
	
	private void init_population() {
		NeuralNetwork nn;
		nextPopulation = new ArrayList<Gene>();
		for (int i=0; i<popsize; i++) {
			nn = new NeuralNetwork(l1Size, l2Size);
			nextPopulation.add(new Gene(nn));
		}
	}
	
	public void run() {
		for (int i=0; i<generations; i++) {
			System.out.println("<"+generationCount+">Generation ===============");
			System.out.println(tests.size());
			System.out.println("Percentage: "+(double)population.get(0).getFitness()/tests.size());
			calculateFitness();
			sortPopulationByFitness();
			breedNewPopulation();
			printBest();
			printAverage();
			generationCount ++;
		}
	}
	
	public List<Gene> getPopulation() {
		return population;
	}
	
	private void printBest() {
		System.out.println("Best: "+population.get(0).getFitness());
	}
	
	private void printAverage() {
		System.out.println("Average: "+fitnessAverage());
	}
	
	private void calculateFitness() {
		population
			.parallelStream()
			.forEach(gene -> gene.fitness = calculateGeneFitness(gene));
	}
	
	public double calculateGeneFitness(Gene gene) {
		double fitness =  tests.parallelStream()
			.mapToDouble(test -> calculateMoveTestFitness(gene, test))
			.sum();
		return fitness;
	}
	
	Comparator<Map.Entry<Board, Double>> byScore = (b1, b2) -> b1.getValue().compareTo(b2.getValue());
	public double calculateMoveTestFitness(Gene gene, MoveTest test) {
		double fitness;
		Map<Board, Double> board_score = new LinkedHashMap<Board, Double>();
		for (Board board : test.getPossibilities()) {
			board_score.put(board, gene.getNn()
					.feed(convertBoardToNNInput(board, test.getPlayer())));
		}
		ArrayList<Board> keys = new ArrayList<>();
		board_score = board_score.entrySet().stream().sorted(byScore)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, 
						(e1,e2)->e2, LinkedHashMap::new));
		board_score.entrySet().stream().forEach(a -> keys.add(a.getKey()) );
//		board_score.entrySet().stream().sorted(byScore).forEach(a -> keys.add(a.getKey()));
		int index = keys.indexOf(test.getExpected());
		if (keys.size() == 0 || index == (keys.size()-1) ) {
			fitness = 1;
		} 
		else {
/*			fitness = (double) (index+1)/keys.size();
			if (index == keys.size()-1) {
				fitness += 0.5;
			} */
			fitness = (double)(index+1)/keys.size();
			ArrayList<Double> scores = new ArrayList<>(board_score.values());
			double bestScore = scores.get(scores.size()-1);
			double worstScore = scores.get(0);
			double indexScore = scores.get(index);
			double nextIndexScore = scores.get(index+1);
//			fitness += (Math.abs(indexScore-worstScore)/Math.abs(bestScore-worstScore))/keys.size();
			fitness += ((Double.MAX_VALUE-Math.abs(bestScore-indexScore))/Double.MAX_VALUE)/keys.size();
//			fitness += ((Double.MAX_VALUE-Math.abs(nextIndexScore-indexScore))/Double.MAX_VALUE)/keys.size();
/*			ArrayList<Double> scores = new ArrayList<>(board_score.values());
			double bestScore = scores.get(scores.size()-1);
			double worstScore = scores.get(0);
			double indexScore = scores.get(index);
			fitness = Math.abs(indexScore-worstScore)/Math.abs(bestScore-worstScore);*/
			if (fitness > 1) {
				System.out.println("BIGGER  "+fitness);
				System.out.println("BIGGER  "+fitness);
				System.out.println("BIGGER  "+fitness);
				System.out.println("BIGGER  "+fitness);
				System.out.println("BIGGER  "+fitness);
				System.out.println("BIGGER  "+fitness);
				System.out.println("BIGGER  "+fitness);
				System.out.println("BIGGER  "+fitness);
				System.out.println("BIGGER  "+fitness);
				System.out.println("BIGGER  "+fitness);
			}
		}
		return fitness;
	}
	
	public static int[] convertBoardToNNInput(Board board, Color player) {
		// first half of array is for player board, second half for enemy board
		return Common.convertBoardToNNInput(board, player);
	}
	
	private double fitnessAverage() {
		return population.stream().mapToDouble(a -> a.getFitness())
				.average().getAsDouble();
	}
	
	Comparator<Gene> byFitness = (g1, g2) -> Double.compare(g2.getFitness(), g1.getFitness());
	private void sortPopulationByFitness() {
		Collections.sort(population, byFitness);
	}
	
	private void breedElite() {
		int elite_index = 0;
		for (Gene gene : nextPopulation.subList(0, getEliteSize())) {
			gene.copy(population.get(elite_index));
			elite_index++;
		}
	}
	
	private int getEliteSize() {
		return (int)(eliteRate*popsize);
	}
	
	private int getBreedSize() {
		return popsize - getEliteSize();
	}
	
	private void breedGeneration() {
		int breed_size = getBreedSize();
		int elite_size = getEliteSize();
		Gene child1, child2, p1, p2;
		
		// avoid out of bound index because two point crossover
		// returns 2 children
		if (breed_size%2 == 1) {
			breed_size--;
			p1 = tournamentSelect();
			p2 = tournamentSelect();
			child1 = nextPopulation.get(nextPopulation.size()-1);
			crossover(p1, p2, child1, child1);
			mutate(child1);
		}
		
		for (int i=elite_size; i<elite_size+breed_size; i+=2) {
			p1 = tournamentSelect();
			p2 = tournamentSelect();
			child1 = nextPopulation.get(i);
			child2 = nextPopulation.get(i+1);
			crossover(p1, p2, child1, child2);
			mutate(child1);
			mutate(child2);
		}
	}
	
	private void breedNewPopulation() {
		breedElite();
		breedGeneration();
		swapPopulations();
	}
	
	private void swapPopulations() {
		List<Gene> temp = new ArrayList<>();
		for (int i=0; i<popsize; i++) {
			temp.add(population.get(i));
		}
		for (int i=0; i<popsize; i++) {
			population.set(i, nextPopulation.get(i));
			nextPopulation.set(i, temp.get(i));
		}
	}
	
	private boolean shouldMutate() {
		return (rand.nextDouble() <= mutationRate);
	}
	
	public void mutateFlip(Gene gene) {
		int flipBit = rand.nextInt(Integer.SIZE);
		int randL2 = rand.nextInt(l2Size);
		int randL1 = rand.nextInt(l1Size);
		gene.getNn().l2Weights[randL2][randL1] ^= (1 << flipBit);
	}
	
	private void mutate(Gene gene) {
		if (shouldMutate()) {
			mutateSwap(gene);
		}
	}
	
	public void mutateSwap(Gene gene) {
		int tmp;
		int randL2First, randL1First;
		int randL2Second, randL1Second;
		randL2First = rand.nextInt(l2Size);
		randL1First = rand.nextInt(l1Size);
		randL2Second = rand.nextInt(l2Size);
		randL1Second = rand.nextInt(l1Size);
		tmp = gene.getNn().l2Weights[randL2First][randL1First];
		gene.getNn().l2Weights[randL2First][randL1First] = 
				gene.getNn().l2Weights[randL2Second][randL1Second];
		gene.getNn().l2Weights[randL2Second][randL1Second] = tmp;
	}
	
	private void twoPointCrossover(Gene p1, Gene p2
			, Gene child1, Gene child2) {
		
		int and_bits = randIntBitsTwoPointCrossover();
		for (int i=0; i<l2Size; i++) {
			for (int k=0; k<l1Size; k++) {
				child1.getNn().l2Weights[i][k] = 
						(p1.getNn().l2Weights[i][k]&and_bits) + 
						(p2.getNn().l2Weights[i][k]&(~and_bits));
				
				child2.getNn().l2Weights[i][k] = 
						(p1.getNn().l2Weights[i][k]&(~and_bits)) + 
						(p2.getNn().l2Weights[i][k]&and_bits);				
			}
		}
	}
	
	private void crossover(Gene p1, Gene p2
			, Gene child1, Gene child2) {
		uniformCrossover2(p1, p2, child1, child2);
	}
	
	private void uniformCrossover(Gene p1, Gene p2
			, Gene child1, Gene child2) {
		
		int and_bits = rand.nextInt();
		for (int i=0; i<l2Size; i++) {
			for (int k=0; k<l1Size; k++) {
				child1.getNn().l2Weights[i][k] = 
						(p1.getNn().l2Weights[i][k]&and_bits) + 
						(p2.getNn().l2Weights[i][k]&(~and_bits));
				
				child2.getNn().l2Weights[i][k] = 
						(p1.getNn().l2Weights[i][k]&(~and_bits)) + 
						(p2.getNn().l2Weights[i][k]&and_bits);				
			}
		}
	}
	
	private void uniformCrossover2(Gene p1, Gene p2
			, Gene child1, Gene child2) {
		for (int i=0; i<l2Size; i++) {
			for (int k=0; k<l1Size; k++) {
				if (rand.nextBoolean() == true) {
					child1.getNn().l2Weights[i][k] = 
							p1.getNn().l2Weights[i][k];
					child2.getNn().l2Weights[i][k] = 
							p2.getNn().l2Weights[i][k];						
				} else {
					child1.getNn().l2Weights[i][k] = 
							p2.getNn().l2Weights[i][k];
					child2.getNn().l2Weights[i][k] = 
							p1.getNn().l2Weights[i][k];				
				}
			}
		}
	}
	
	private int randIntBitsTwoPointCrossover() {
		int first_point = rand.nextInt(Integer.SIZE);
		int second_point = rand.nextInt(Integer.SIZE-first_point)+first_point;
		int and_bits = (int)(Math.pow(2, first_point)-1) +
					   ~((int)(Math.pow(2, second_point)-1));
		return and_bits;
	}

	private Gene tournamentSelect() {
		return population.get(IntStream.range(0, k)
				.map(i -> rand.nextInt(populaitonSize)).min().getAsInt());
	}
	
}
