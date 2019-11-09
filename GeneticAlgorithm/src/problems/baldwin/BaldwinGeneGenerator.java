package problems.baldwin;

import gene.Gene;
import gene.GeneGenerator;

import java.util.Random;

public class BaldwinGeneGenerator implements GeneGenerator {

	private String target;
	private double correctProbability;
	private double incorrectProbability;
	Random rand = new Random();



	public BaldwinGeneGenerator(String target, double correctProbability,
			double incorrectProbability) {
		super();
		this.target = target;
		this.correctProbability = correctProbability;
		this.incorrectProbability = incorrectProbability;
	}

	@Override
	public Gene createGene() {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<target.length(); i++) {
			sb.append(createCharFor(i));
		}
		return new BaldwinGene(sb.toString());
	}
	
	private char createCharFor(int index) {
		double random = rand.nextDouble();
		char c;
		if (correctProbability > random) {
			c = target.charAt(index);
		} else if (incorrectProbability+correctProbability > random){
			c = (target.charAt(index) == '1') ? '0' : '1';
		} else {
			c = '?';
		}
		return c;
	}

}
