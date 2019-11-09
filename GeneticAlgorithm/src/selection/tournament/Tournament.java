package selection.tournament;

import gene.Gene;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import selection.Selection;

public class Tournament implements Selection{

	private int k;
	private Random rand = new Random();
	private List<Gene> population;
	
	public Tournament(int k) {
		super();
		this.k = k;
	}

	@Override
	public Gene selectGene() {
		return chooseGene(selectRandomGenes());
	}
	
	@Override
	public void setGenes(List<Gene> genes) {
		this.population = genes;
	}

	private Gene chooseGene(List<Gene> genes) {
		Comparator<Gene> byIndex = (g1, g2) -> 
			Double.compare(population.indexOf(g1),
						   population.indexOf(g2));
		
		return genes.stream().sorted(byIndex).findFirst().get();
	}
	
	private List<Gene> selectRandomGenes() {
		List<Gene> selected = new ArrayList<Gene>();
		for (int i=0; i<k; i++) {
			selected.add(getRandomGene());
		}
		return selected;
	}
	
	private Gene getRandomGene() {
        int index = rand.nextInt(population.size());
        return population.get(index);
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	@Override
	public String toString() {
		return "Tournament [k=" + k + "]";
	}
	
}
