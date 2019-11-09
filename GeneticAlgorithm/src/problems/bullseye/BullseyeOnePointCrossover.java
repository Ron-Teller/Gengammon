package problems.bullseye;

import gene.Gene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import selection.Selection;
import crossover.Crossover;

public class BullseyeOnePointCrossover implements Crossover {

	private Random rand = new Random();
	
	@Override
	public List<Gene> breedOffpsring(Selection selector) {
		List<Gene> offspring = new ArrayList<Gene>();
		BullseyeGene p1 = (BullseyeGene) selector.selectGene();
		BullseyeGene p2 = (BullseyeGene) selector.selectGene();
		offspring.add(crossover(p1, p2));
		return offspring;
	}
	
	private BullseyeGene crossover(BullseyeGene p1, BullseyeGene p2) {
		int wordLength = p1.getWord().length();
		int splitIndex = rand.nextInt(wordLength);
		String word = p1.getWord().substring(0,splitIndex) +
					  p2.getWord().substring(splitIndex, wordLength);
		BullseyeGene child = new BullseyeGene(word);
		return child;
	}

	@Override
	public String toString() {
		return "Bullseye One Point Crossover";
	}

}
