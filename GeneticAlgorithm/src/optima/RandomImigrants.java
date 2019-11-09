package optima;

import gene.Gene;
import gene.GeneGenerator;

import java.util.ArrayList;
import java.util.List;

import algorithm.GeneticAlgorithm;

public class RandomImigrants extends LocalOptimaHandler {

	private double immigrantRate;
	
	public RandomImigrants(GeneticAlgorithm algorithm, double immigrantRate) {
		super(algorithm);
		this.immigrantRate = immigrantRate;
	}

	@Override
	protected void takeAction() {
		List<Gene> genes = algorithm.getPopulation().getGenes();
		List<Gene> immigrants = new ArrayList<Gene>();
		int immigrationSize = (int)(genes.size()*immigrantRate);
		GeneGenerator generator = algorithm.getPopulation().getGeneGenerator();
		
		for (int i=0; i<immigrationSize; i++) {
			immigrants.add(generator.createGene());
		}
		
		genes = genes.subList(0, immigrationSize);
		genes.addAll(immigrants);
		algorithm.getPopulation().setCurrentGeneration(genes);
	}

	@Override
	protected void onNoLocalOptima() {
	}
	
}
