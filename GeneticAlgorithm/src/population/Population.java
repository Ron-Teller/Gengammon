package population;

import fitness.Fitness;
import gene.Gene;
import gene.GeneGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public class Population {

	private int size;
	protected List<Gene> genes;
	private int generation;
	private GeneGenerator geneGenerator;
	
	public Population(GeneGenerator geneGenerator) {
		super();
		this.size = size;
		this.geneGenerator = geneGenerator;
		genes = new ArrayList<Gene>();
	}

	public void sort(Comparator<Gene> comparator) {
		Collections.sort(genes, comparator);
	}
	
	public void setFitness(Fitness fitness) {
		genes.parallelStream().forEach(gene -> 
						fitness.setFitness(gene));
	}
	
	public void initialize() {
		generation = 0;
		genes.clear();
		for (int i=0; i<size; i++) {
			genes.add(geneGenerator.createGene()); 
		}
	}
	
	public double getStandardDeviation() {
		StandardDeviation sd = new StandardDeviation();
		return sd.evaluate(genes.stream()
				.mapToDouble(Gene::getFitness).toArray());
	}
	
	public double getFitnessAverage() {
		return genes.stream().mapToDouble(Gene::getFitness)
				.average().getAsDouble();
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<Gene> getGenes() {
		return genes;
	}

	public void setNewGeneration(List<Gene> population) {
		generation ++;
		this.genes = population;
		for (Gene gene : this.genes) {
			gene.setAge(gene.getAge()+1);
		}
	}
	
	public void setCurrentGeneration(List<Gene> population) {
		this.genes = population;
	}

	public int getGeneration() {
		return generation;
	}

	public GeneGenerator getGeneGenerator() {
		return geneGenerator;
	}
}
