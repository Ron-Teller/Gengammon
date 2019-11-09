package algorithm.genetic;

import java.util.Random;

import gene.Gene;
import mutation.Mutation;

public class BackgammonSwapMutation implements Mutation {

	Random rand = new Random();
	
	@Override
	public void mutate(Gene _gene) {
		BackgammonGene gene = (BackgammonGene) _gene;
		int tmp;
		int randL2First, randL1First;
		int randL2Second, randL1Second;
		randL2First = rand.nextInt(gene.getNn().getL2_size());
		randL1First = rand.nextInt(gene.getNn().getL1_size());
		randL2Second = rand.nextInt(gene.getNn().getL2_size());
		randL1Second = rand.nextInt(gene.getNn().getL1_size());
		tmp = gene.getNn().l2Weights[randL2First][randL1First];
		gene.getNn().l2Weights[randL2First][randL1First] = 
				gene.getNn().l2Weights[randL2Second][randL1Second];
		gene.getNn().l2Weights[randL2Second][randL1Second] = tmp;
	}

}
