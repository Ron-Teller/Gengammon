package selection.rws;

import gene.Gene;

import java.util.List;
import java.util.Random;

import selection.Selection;
import selection.rws.scaling.Scaling;

public class RouletteWheelInvertedFitness implements Selection {
	// Use when better fitness is lower value

	Random rand = new Random();
	Scaling scaling;
	private double worstFitness;
	private double invertedFitnessSum;
	private List<Gene> population;
	
	public RouletteWheelInvertedFitness(Scaling scaling) {
		super();
		this.scaling = scaling;
	}

	@Override
	public Gene selectGene() {
		
		double randomFitness = rand.nextInt((int)invertedFitnessSum+1);
		
		double invertedFitness;
		double accumulatedFitness = 0;
		Gene selectedGene = null;
		for (Gene gene  : population) {
			invertedFitness = worstFitness - scaling.scaleFitness(gene, population);
			if (accumulatedFitness + invertedFitness >= randomFitness) {
				selectedGene = gene;
				break;
			} else {
				accumulatedFitness += invertedFitness;
			}
		}
		return selectedGene;
	}

	@Override
	public void setGenes(List<Gene> genes) {
		this.population = genes;
		worstFitness = scaling.scaleFitness(genes.
				get(genes.size()-1), genes);
		invertedFitnessSum = genes.stream()
		.mapToDouble(gene -> worstFitness-
					scaling.scaleFitness(gene, genes)).sum();
	}

	@Override
	public String toString() {
		return "Roulette Wheel Selection [scaling=" + scaling + "]";
	}
	
	
}
