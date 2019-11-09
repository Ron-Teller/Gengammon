package fitness;

import similarity.Similarity;
import algorithm.GeneticAlgorithm;
import gene.Gene;

public class Niching implements Fitness {

	private double sharingRadius;
	private double alpha;
	private Fitness fitness;
	private Similarity similarity;
	private GeneticAlgorithm algorithm;

	public Niching(double sharingRadius, double alpha, Fitness fitness,
			Similarity similarity, GeneticAlgorithm algorithm) {
		super();
		this.sharingRadius = sharingRadius;
		this.alpha = alpha;
		this.fitness = fitness;
		this.similarity = similarity;
		this.algorithm = algorithm;
	}

	private double sharingFunction(double difference) {
		double result = 0;
		if (difference < sharingRadius) {
			result = 1-Math.pow(difference/sharingRadius, alpha);
		}
		return result;
	}
	
	private double getSharedFitness(Gene gene) {
		double result =  gene.getFitness()*getSharedFitnessSum(gene);
		return result;
	}	
	
	private double getSharedFitnessSum(Gene gene) {
		double sharingSum = algorithm.getPopulation().getGenes()
				.stream().mapToDouble(g -> 
				sharingFunction(1-similarity.getSimilarity(gene, g)))
				.sum();
		return sharingSum;
	}
	
	
	@Override
	public void setFitness(Gene gene) {
		fitness.setFitness(gene);
		gene.setFitness(getSharedFitness(gene));
	}

}
