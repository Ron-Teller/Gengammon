package algorithm.genetic;

import gene.Gene;
import gene.GeneGenerator;
import algorithm.NeuralNetwork;

public class BackgammonGeneGenerator implements GeneGenerator {

	@Override
	public Gene createGene() {
		NeuralNetwork nn;
		nn = new NeuralNetwork(52, 26);
		nn.randomize();
		return new BackgammonGene(nn);
	}
}

