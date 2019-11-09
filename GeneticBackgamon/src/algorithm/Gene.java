package algorithm;

import java.io.Serializable;


public class Gene implements Serializable {

	NeuralNetwork nn;
	double fitness;
	
	public Gene(NeuralNetwork nn) {
		this.nn = nn;
	}
	
	public Gene(Gene other) {
		this.nn = new NeuralNetwork(other.getNn().getL1_size(),
						other.getNn().getL2_size());
		copy(other);
	}	
	
	public void copy(Gene other) {
		this.fitness = other.fitness;
		this.nn.copy(other.nn);
	}

	public NeuralNetwork getNn() {
		return nn;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public double getFitness() {
		return fitness;
	}
}
