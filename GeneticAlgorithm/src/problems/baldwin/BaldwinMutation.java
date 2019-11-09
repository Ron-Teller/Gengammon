package problems.baldwin;

import gene.Gene;

import java.util.Random;

import mutation.Mutation;

public class BaldwinMutation implements Mutation{

	Random rand = new Random();
	
	@Override
	public void mutate(Gene gene) {
		BaldwinGene bGene = (BaldwinGene) gene;
		StringBuilder connections = new StringBuilder(bGene.getConnections());
		int mutateIndex = rand.nextInt(connections.length());
		connections.setCharAt(mutateIndex, getRandomChar());
		bGene.setConnections(connections.toString());
	}
	
	private char getRandomChar() {
		int random = rand.nextInt(3);
		char c;
		if (random == 0) {
			c = '0';
		} else if (random == 1) {
			c = '1';
		} else {
			c = '?';
		}
		return c;
	}

	@Override
	public String toString() {
		return "Baldwin Mutation";
	}
	
}
