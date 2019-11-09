package algorithm;

import java.util.List;

public class HillClimbNeuralNetwork {

	GeneticAlgorithmDep ga;
	Gene bestGene;


	public HillClimbNeuralNetwork(GeneticAlgorithmDep ga, Gene bestGene) {
		super();
		this.ga = ga;
		this.bestGene = bestGene;
	}

	private void climb(int iterations) {
		for (int i=0; i<iterations; i++) {
			swapRandomWeights();
		}
	}
	
}
