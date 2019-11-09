package algorithm.genetic;

import gene.Gene;
import gene.GeneGenerator;
import io.DataSerializer2;

import java.util.List;

public class BackgammonSerializedGeneGenerator implements GeneGenerator {

	private DataSerializer2 serializer = new DataSerializer2();
	private String filePath;
	private List<BackgammonGene> genes;
	private int index;
	
	public BackgammonSerializedGeneGenerator(String filePath) {
		super();
		this.filePath = filePath;
		genes = serializer.loadPopulation(this.filePath);
		index = 0;
	}

	@Override
	public Gene createGene() {
		return genes.get(index);
	}

}
