package problems.queens;

import java.util.List;
import java.util.Random;

import gene.Gene;
import mutation.Mutation;

public class QueensSwapMutation implements Mutation {

	Random rand = new Random();
	
	@Override
	public void mutate(Gene gene) {
		QueensGene qGene = (QueensGene) gene;
		swapTwoQueens(qGene.getRows());
	}
	
	private void swapTwoQueens(List<Integer> rows) {
		int firtsIndex = rand.nextInt(rows.size());
		int secondIndex = rand.nextInt(rows.size());
		if (firtsIndex == secondIndex) {
			secondIndex = (secondIndex+1)%rows.size();
		}
		Integer tmp = rows.get(firtsIndex);
		rows.set(firtsIndex, rows.get(secondIndex));
		rows.set(secondIndex, tmp);
	}

	@Override
	public String toString() {
		return "Queens Swap Mutation";
	}
	
}
