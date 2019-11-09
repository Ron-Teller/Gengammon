package selection.sus;

import gene.Gene;

import java.util.Collections;
import java.util.List;

import selection.Selection;

public class SUS implements Selection{

	private double worstFitness;
	private double invertedFitnessSum;
	private double accumulatedFitness;
	private int currentGeneIndex;
	private List<Gene> population;
	private int sliceIndex;
	private double sliceSize;
	
	@Override
	public Gene selectGene() {
		return spinWheel();
	}
	
	@Override
	public void setGenes(List<Gene> genes) {
		accumulatedFitness = 0;
		currentGeneIndex = 0;
		sliceIndex = 0;
		this.population = genes;
		worstFitness = genes.get(genes.size()-1).getFitness();
		invertedFitnessSum = genes.stream()
			.mapToDouble(gene -> worstFitness-gene.getFitness()).sum();
		sliceSize = invertedFitnessSum/population.size();
	}

	private Gene spinWheel() {
		Gene selected = null;
		Gene current = getCurrentGene();
		while (selected == null) {
			if (accumulatedFitness + current.getFitness() > 
					sliceSize*sliceIndex) {
				selected = current;
			} else {
				accumulatedFitness += current.getFitness();
				current = getNextGene();
			}
		}
		sliceIndex ++;
		return selected;
	}
	
	private Gene getNextGene() {
		currentGeneIndex  = (currentGeneIndex+1)%population.size();
		return population.get(currentGeneIndex);
	}
	
	private Gene getCurrentGene() {
		return population.get(currentGeneIndex);
	}

	@Override
	public String toString() {
		return "SUS";
	}

}
