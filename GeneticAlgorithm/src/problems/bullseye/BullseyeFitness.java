package problems.bullseye;

import fitness.Fitness;
import gene.Gene;

public class BullseyeFitness implements Fitness{

	protected String target;
	
	public BullseyeFitness(String target) {
		super();
		this.target = target;
	}

	@Override
	public void setFitness(Gene gene) {
		BullseyeGene bGene = (BullseyeGene) gene;
		gene.setFitness(calculateGeneFitness(bGene));
	}
	
	protected int calculateGeneFitness(BullseyeGene gene) {
		return getWordDistanceFromTarget(gene.getWord());
	}
	
	protected int getWordDistanceFromTarget(String word) {
		int fitness = 0;
		for (int i=0; i<target.length(); i++) {
			fitness += Math.abs((int)word.charAt(i)-
								   (int) target.charAt(i));
		}
		return fitness;
	}
}
