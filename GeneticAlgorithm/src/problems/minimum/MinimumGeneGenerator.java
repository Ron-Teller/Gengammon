package problems.minimum;

import java.util.Random;

import gene.Gene;
import gene.GeneGenerator;

public class MinimumGeneGenerator implements GeneGenerator{

	private Random rand = new Random();
	
	@Override
	public Gene createGene() {
		int x1Integer = rand.nextInt();
		int x2Integer = rand.nextInt();
		int x1Fraction = rand.nextInt();
		int x2Fraction = rand.nextInt();
		return new MinimumGene(x1Integer, x2Integer, x1Fraction, x2Fraction);
	}
}
