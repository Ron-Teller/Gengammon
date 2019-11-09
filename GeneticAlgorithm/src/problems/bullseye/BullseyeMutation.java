package problems.bullseye;

import java.util.Random;

import gene.Gene;
import mutation.Mutation;

public class BullseyeMutation implements Mutation {
	
	Random rand = new Random();
	
	@Override
	public void mutate(Gene gene) {
		mutateGene((BullseyeGene) gene);
	}
	
	private void mutateGene(BullseyeGene gene) {
		gene.setWord(mutateString(gene.getWord()));
	}
	
	private String mutateString(String str) {
		int mutateIndex = rand.nextInt(str.length());
		StringBuilder mutated = new StringBuilder(str);
		mutated.setCharAt(mutateIndex, mutateChar(str.charAt(mutateIndex)));
		return mutated.toString();
	}
	
	private char mutateChar(char c) {
		int delta = (rand.nextInt()%90)+32;
		return (char) ((c+delta)%122);		
	}

	@Override
	public String toString() {
		return "Bullseye Mutation";
	}
}
