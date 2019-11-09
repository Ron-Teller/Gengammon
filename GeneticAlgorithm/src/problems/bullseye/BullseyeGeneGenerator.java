package problems.bullseye;

import gene.Gene;
import gene.GeneGenerator;

import java.util.Random;

public class BullseyeGeneGenerator implements GeneGenerator {

	private String target;
	private Random rand = new Random();
	
	public BullseyeGeneGenerator(String target) {
		super();
		this.target = target;
	}

	@Override
	public Gene createGene() {
		String word = generateTargetLengthedRandomWord();
		return new BullseyeGene(word);
	}
	
	private String generateTargetLengthedRandomWord() {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<target.length(); i++) {
				sb.append((char)(rand.nextInt(90)+32));
		}
		return sb.toString();
	}

}
