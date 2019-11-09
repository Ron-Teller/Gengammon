package problems.bullseye;

import gene.Gene;

public class BullseyeGene extends Gene {
	
	String word;

	public BullseyeGene(String word) {
		super();
		this.word = word;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}
}
