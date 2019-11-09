package algorithm.genetic;

import gene.Gene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import selection.Selection;
import algorithm.NeuralNetwork;
import crossover.Crossover;

public class BackgammonTwoPointCrossover implements Crossover {

	Random rand = new Random();
	int l2Size = 26;
	int l1Size = 52;
	
	@Override
	public List<Gene> breedOffpsring(Selection selector) {
		
		List<Gene> offspring = new ArrayList<Gene>();
		BackgammonGene p1 = (BackgammonGene) selector.selectGene();
		BackgammonGene p2 = (BackgammonGene) selector.selectGene();
		int andBits = randIntBitsTwoPointCrossover();
		NeuralNetwork nn1 = new NeuralNetwork(l1Size, l2Size);
		NeuralNetwork nn2 = new NeuralNetwork(l1Size, l2Size);
		
		for (int i=0; i<l2Size; i++) {
			for (int k=0; k<l1Size; k++) {
				nn1.l2Weights[i][k] = 
						(p1.getNn().l2Weights[i][k]&andBits) + 
						(p2.getNn().l2Weights[i][k]&(~andBits));
				
				nn2.l2Weights[i][k] = 
						(p1.getNn().l2Weights[i][k]&(~andBits)) + 
						(p2.getNn().l2Weights[i][k]&andBits);				
			}
		}
		
		offspring.add(new BackgammonGene(nn1));
		offspring.add(new BackgammonGene(nn2));
		return offspring;		
	}
	
	private int randIntBitsTwoPointCrossover() {
		int first_point = rand.nextInt(Integer.SIZE);
		int second_point = rand.nextInt(Integer.SIZE-first_point)+first_point;
		int and_bits = (int)(Math.pow(2, first_point)-1) +
					   ~((int)(Math.pow(2, second_point)-1));
		return and_bits;
	}	

}
