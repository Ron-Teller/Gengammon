package optima;

import algorithm.GeneticAlgorithm;

public class HyperMutations extends LocalOptimaHandler {
	
	private double originalMutationRate;
	private double mutationMultiplyer = 0.4;
	private boolean changedRate = false;
	
	public HyperMutations(GeneticAlgorithm algorithm) {
		super(algorithm);
	}

	@Override
	protected void takeAction() {
		if (changedRate == false) {
			originalMutationRate = algorithm.getMutationRate();
			double hyperMutationRate = originalMutationRate*mutationMultiplyer;
			if (hyperMutationRate > 0.9) {
				hyperMutationRate = 0.9;
			}
			algorithm.setMutationRate(hyperMutationRate);
			changedRate = true;
		}
	}

	@Override
	protected void onNoLocalOptima() {
		if (changedRate == true) {
			algorithm.setMutationRate(originalMutationRate);
		}
	}
	
}
