package problems.minimum;

import fitness.Fitness;
import gene.Gene;

public class MinimumFitness implements Fitness{

	private BiDoubleFunction function;
	
	public MinimumFitness(BiDoubleFunction function) {
		super();
		this.function = function;
	}

	@Override
	public void setFitness(Gene gene) {
		MinimumGene mGene = (MinimumGene) gene;
		mGene.setFitness(function.function(mGene.getX1(), mGene.getX2()));
	}

}
