package selection.rws;

import gene.Gene;

import java.util.List;
import java.util.Random;

import selection.Selection;

public class RouletteWheel implements Selection {

	Random rand = new Random();
	private List<Gene> population;
	
	@Override
	public Gene selectGene() {
		
		double fitnessSum = population.stream()
				.mapToDouble(gene -> gene.getFitness()).sum();
		double randomFitness = rand.nextInt((int)fitnessSum);
		
		double accumulatedFitness = 0;
		Gene selectedGene = null;
		for (Gene gene  : population) {
			if (accumulatedFitness + gene.getFitness() >= randomFitness) {
				selectedGene = gene;
				break;
			} else {
				accumulatedFitness += gene.getFitness();
			}
		}
		return selectedGene;
	}

	@Override
	public void setGenes(List<Gene> genes) {
		this.population = genes;
	}

	@Override
	public String toString() {
		return "Roulette Wheel Selection";
	}
	
	
}
