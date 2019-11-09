package gene;

import java.io.Serializable;

public abstract class Gene implements Serializable{
	
	private double fitness;
	private int age = 0;
	
	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
