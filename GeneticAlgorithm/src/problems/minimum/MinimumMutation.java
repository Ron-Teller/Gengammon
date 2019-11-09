package problems.minimum;

import gene.Gene;

import java.util.Random;

import mutation.Mutation;

public class MinimumMutation implements Mutation{

	Random rand = new Random();
	
	@Override
	public void mutate(Gene gene) {
		MinimumGene mGene = (MinimumGene) gene;
		mGene.setX1Integer(flipRandomBit(mGene.getX1Integer()));
		mGene.setX2Integer(flipRandomBit(mGene.getX2Integer()));
		mGene.setX1Fraction(flipRandomBit(mGene.getX1Fraction()));
		mGene.setX2Fraction(flipRandomBit(mGene.getX2Fraction()));
	}
	
	private int flipRandomBit(int num) {
		int bit = rand.nextInt(Integer.SIZE);
		return num ^= (1 << bit);
	}

	@Override
	public String toString() {
		return "MinimumMutation";
	}
}
