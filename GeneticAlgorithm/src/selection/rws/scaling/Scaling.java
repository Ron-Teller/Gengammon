package selection.rws.scaling;

import gene.Gene;

import java.util.List;

public interface Scaling {

	public double scaleFitness(Gene gene, List<Gene> genes);
}
