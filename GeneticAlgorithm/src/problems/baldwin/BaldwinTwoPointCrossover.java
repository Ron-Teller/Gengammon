package problems.baldwin;

import gene.Gene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import selection.Selection;
import crossover.Crossover;

public class BaldwinTwoPointCrossover implements Crossover{

	private Random rand = new Random();
	
	@Override
	public List<Gene> breedOffpsring(Selection selector) {
		BaldwinGene p1 = (BaldwinGene) selector.selectGene();
		BaldwinGene p2 = (BaldwinGene) selector.selectGene();
		return crossover(p1, p2);
	}
	
	private List<Gene> crossover(BaldwinGene p1, BaldwinGene p2) {
		List<Gene> offspring = new ArrayList<Gene>();
		int wordLength = p1.getConnections().length();
		int firstPoint = rand.nextInt(wordLength);
		int secondPoint = rand.nextInt(wordLength-firstPoint)+firstPoint;
		String word1 = p1.getConnections().substring(0,firstPoint) +
					   p2.getConnections().substring(firstPoint,secondPoint) +
					   p1.getConnections().substring(secondPoint,wordLength);
		
		String word2 = p2.getConnections().substring(0,firstPoint) +
				   p1.getConnections().substring(firstPoint,secondPoint) +
				   p2.getConnections().substring(secondPoint,wordLength);
		
		offspring.add(new BaldwinGene(word1));
		offspring.add(new BaldwinGene(word2));
		return offspring;
	}

	@Override
	public String toString() {
		return "Baldwin Two Point Crossover";
	}
}
