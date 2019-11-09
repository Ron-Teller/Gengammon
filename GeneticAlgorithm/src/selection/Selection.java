package selection;

import gene.Gene;

import java.util.List;

public interface Selection {
	public Gene selectGene();
	public void setGenes(List<Gene> genes);
}
