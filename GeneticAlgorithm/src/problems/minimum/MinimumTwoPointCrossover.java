package problems.minimum;

import gene.Gene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import selection.Selection;
import crossover.Crossover;

public class MinimumTwoPointCrossover implements Crossover{

	Random rand = new Random();
	
	@Override
	public List<Gene> breedOffpsring(Selection selector) {
		List<Gene> offspring = new ArrayList<Gene>();
		MinimumGene p1 = (MinimumGene) selector.selectGene();
		MinimumGene p2 = (MinimumGene) selector.selectGene();
		int andBits = randIntBitsTwoPointCrossover();
		//System.out.println(Integer.toBinaryString(andBits));
		offspring.add(createCrossoverOffspring(p1, p2, andBits));
		offspring.add(createCrossoverOffspring(p1, p2, ~andBits));
		return offspring;
	}

	private MinimumGene createCrossoverOffspring(MinimumGene p1,
								MinimumGene p2, int andBits) {
		int x1Integer = crossover(p1.getX1Integer()
								 ,p2.getX1Integer(), andBits);
		int x2Integer = crossover(p1.getX2Integer()
								 ,p2.getX2Integer(), andBits);
		int x1Fraction = crossover(p1.getX1Fraction()
				 				  ,p2.getX1Fraction(), andBits);
		int x2Fraction = crossover(p1.getX2Fraction()
								  ,p2.getX2Fraction(), andBits);
		return new MinimumGene(x1Integer, x2Integer,
				x1Fraction, x2Fraction);
	}	
	
	private int crossover(int num1, int num2, int andBits) {
		int result = (num1&andBits) +
				     (num2&(~andBits));
		return result;
	}

	private int randIntBitsTwoPointCrossover() {
		int firstPoint = rand.nextInt(Integer.SIZE);
		int secondPoint = rand.nextInt(Integer.SIZE-firstPoint)+firstPoint;
		int andBits = (int)(Math.pow(2, firstPoint)-1) +
					   ~((int)(Math.pow(2, secondPoint)-1));
		return andBits;
	}

	@Override
	public String toString() {
		return "Minimum Two Point Crossover";
	}	
	
}
