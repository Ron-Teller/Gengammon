package problems.baldwin;

import java.util.Random;

import fitness.Fitness;
import gene.Gene;

public class BaldwinFitness implements Fitness {

	private int trialsAmount;
	private String target;
	Random rand = new Random();
	
	public BaldwinFitness(int trialsAmount, String target) {
		super();
		this.trialsAmount = trialsAmount;
		this.target = target;
	}

	@Override
	public void setFitness(Gene gene) {
		BaldwinGene bGene = (BaldwinGene) gene;
		double fitness;
		int trialsRemaining = trialsAmount;
		for (int trial=0; trial<trialsAmount; trial++) {
			if (passTrial(bGene)) {
				break;
			} else {
				trialsRemaining--;
			}
		}
		fitness = 1+((double)((bGene.getConnections().length()-1)
						*trialsRemaining)/trialsAmount);
		gene.setFitness(fitness);
	}
	
	private boolean passTrial(BaldwinGene bGene) {
		String filledConnections = fillRandomChars(bGene.getConnections());
		boolean passed = target.equals(filledConnections);
//		if (passed == true) {
//			System.out.println("PASSED! "+filledConnections);
//			throw new IllegalStateException();
//		}
		return passed;
	}
	
	private String fillRandomChars(String connections) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<connections.length(); i++) {
			if (connections.charAt(i) == '?'){
				sb.append(randomChooseZeroOrOne());
			} else {
				sb.append(connections.charAt(i));
			}
		}
		return sb.toString();
	}
	
	private int randomChooseZeroOrOne() {
		return (rand.nextBoolean() == true) ? 1 : 0;
	}

}
