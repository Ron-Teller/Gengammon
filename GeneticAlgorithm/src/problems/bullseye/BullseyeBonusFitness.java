package problems.bullseye;

public class BullseyeBonusFitness extends BullseyeFitness {

	private double bonusMultiplyer;
	
	public BullseyeBonusFitness(String target, double bonusMultiplyer) {
		super(target);
		this.bonusMultiplyer = bonusMultiplyer;
	}

	@Override
	protected int calculateGeneFitness(BullseyeGene gene) {
		return super.calculateGeneFitness(gene) -
					  getBullseyeBonus(gene);
	}
	
	private int getBullseyeBonus(BullseyeGene gene) {
		return (int) (bonusMultiplyer*
			 countCharactersIdenticalToTarget(gene.getWord()) -
			 target.length()*bonusMultiplyer);
	}
	
	private int countCharactersIdenticalToTarget(String word) {
		int count = 0;
		for (int i=0; i<target.length(); i++) {
			if (word.charAt(i) == target.charAt(i)) {
				count ++;
			}
		}
		return count;
	}
}
