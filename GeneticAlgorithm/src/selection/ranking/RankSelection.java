package selection.ranking;

import gene.Gene;

import java.util.List;
import java.util.Random;

import selection.Selection;

public class RankSelection implements Selection {

	private List<Gene> population;
	private long rankFitnessSum;
	Random rand = new Random();
	
	@Override
	public Gene selectGene() {
		long randomFitness = (long)(rand.nextDouble()*rankFitnessSum)+1;
		return population.get(sumToSequenceNumber(randomFitness));
	}

	@Override
	public void setGenes(List<Gene> genes) {
		this.population = genes;
		this.rankFitnessSum = arithmeticProgressionSum();
	}
	
	private int sumToSequenceNumber(long sum) {
		return(int)Math.floor((-1+Math.sqrt(8*sum)/2));
	}
	
	private long arithmeticProgressionSum() {
		int first = 1;
		int last = population.size();
		int size = population.size();
		long sum = size*(first+last)/2;
		return sum;
	}

	@Override
	public String toString() {
		return "Rank Selection";
	}
	
	
}
