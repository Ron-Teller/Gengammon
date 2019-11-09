package selection.rws.scaling;

import gene.Gene;

import java.util.List;

public class ExponentialScaling implements Scaling {

	@Override
	public double scaleFitness(Gene gene, List<Gene> genes) {
		return Math.sqrt(gene.getFitness());
	}

	@Override
	public String toString() {
		return "Exponential Scaling";
	}
}
