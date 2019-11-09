package algorithm.genetic;

import gene.Gene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import algorithm.NeuralNetwork;
import selection.Selection;
import crossover.Crossover;

public class BackgammonUniformWeightCrossover implements Crossover {

	Random rand = new Random();
	int l2Size = 26;
	int l1Size = 52;
	
	@Override
	public List<Gene> breedOffpsring(Selection selector) {
		
		List<Gene> offspring = new ArrayList<Gene>();
		BackgammonGene p1 = (BackgammonGene) selector.selectGene();
		BackgammonGene p2 = (BackgammonGene) selector.selectGene();
		NeuralNetwork nn1 = new NeuralNetwork(l1Size, l2Size);
		NeuralNetwork nn2 = new NeuralNetwork(l1Size, l2Size);
		
		for (int i=0; i<l2Size; i++) {
			for (int k=0; k<l1Size; k++) {
				if (rand.nextBoolean() == true) {
					nn1.l2Weights[i][k] = 
							p1.getNn().l2Weights[i][k];
					nn2.l2Weights[i][k] = 
							p2.getNn().l2Weights[i][k];						
				} else {
					nn1.l2Weights[i][k] = 
							p2.getNn().l2Weights[i][k];
					nn2.l2Weights[i][k] = 
							p1.getNn().l2Weights[i][k];				
				}
			}
		}
		
		offspring.add(new BackgammonGene(nn1));
		offspring.add(new BackgammonGene(nn2));
		return offspring;			
	}

}
