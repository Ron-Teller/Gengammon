package problems.baldwin;

import gene.Gene;

public class BaldwinGene extends Gene {

	private String connections;

	public BaldwinGene(String connections) {
		super();
		this.connections = connections;
	}

	public String getConnections() {
		return connections;
	}

	public void setConnections(String connections) {
		this.connections = connections;
	}
	
}
