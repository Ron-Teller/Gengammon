package selection.rws.scaling;

import gene.Gene;

import java.util.List;

public class NormalScaling implements Scaling {

	@Override
	public double scaleFitness(Gene gene, List<Gene> genes) {
		return gene.getFitness();
	}

	@Override
	public String toString() {
		return "Normal Scaling";
	}
	
}
