package problems.bullseye;

import gene.Gene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import selection.Selection;
import crossover.Crossover;

public class BullseyeTwoPointCrossover implements Crossover{

	private Random rand = new Random();
	
	@Override
	public List<Gene> breedOffpsring(Selection selector) {
		BullseyeGene p1 = (BullseyeGene) selector.selectGene();
		BullseyeGene p2 = (BullseyeGene) selector.selectGene();
		return crossover(p1, p2);
	}
	
	private List<Gene> crossover(BullseyeGene p1, BullseyeGene p2) {
		List<Gene> offspring = new ArrayList<Gene>();
		int wordLength = p1.getWord().length();
		int firstPoint = rand.nextInt(wordLength);
		int secondPoint = rand.nextInt(wordLength-firstPoint)+firstPoint;
		String word1 = p1.getWord().substring(0,firstPoint) +
					   p2.getWord().substring(firstPoint,secondPoint) +
					   p1.getWord().substring(secondPoint,wordLength);
		
		String word2 = p2.getWord().substring(0,firstPoint) +
				   p1.getWord().substring(firstPoint,secondPoint) +
				   p2.getWord().substring(secondPoint,wordLength);
		
		offspring.add(new BullseyeGene(word1));
		offspring.add(new BullseyeGene(word2));
		return offspring;
	}

	@Override
	public String toString() {
		return "Bullseye Two Point Crossover";
	}

}
