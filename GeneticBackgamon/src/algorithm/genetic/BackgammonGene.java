package algorithm.genetic;

import algorithm.NeuralNetwork;
import gene.Gene;

public class BackgammonGene extends Gene {

	NeuralNetwork nn;
	
	public BackgammonGene(NeuralNetwork nn) {
		super();
		this.nn = nn;
	}

	public BackgammonGene(BackgammonGene other) {
		this.nn = new NeuralNetwork(other.getNn().getL1_size(),
						other.getNn().getL2_size());
		copy(other);
	}	
	
	public void copy(BackgammonGene other) {
		this.setFitness(other.getFitness());
		this.nn.copy(other.nn);
	}

	public NeuralNetwork getNn() {
		return nn;
	}	
	
}
