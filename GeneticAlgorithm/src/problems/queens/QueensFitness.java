package problems.queens;

import java.util.List;

import fitness.Fitness;
import gene.Gene;

public class QueensFitness implements Fitness {

	@Override
	public void setFitness(Gene gene) {
		QueensGene qGene = (QueensGene) gene;
		qGene.setFitness(countQueenConflicts(qGene.getRows()));
	}
	
	private int countQueenConflicts(List<Integer> rows) {
		int conflictingQueens=0;
		for (int row=0; row<rows.size(); row++) {
			if (containsQueenConflictingWithAdvancedRows(row, rows)) {
				conflictingQueens++;
			}
		}	
		return conflictingQueens;
	}
	
	private boolean containsQueenConflictingWithAdvancedRows(int row, 
			List<Integer> rows) {
		boolean conflicts = false;
		for (int advancedRow=row+1; advancedRow<rows.size();
											advancedRow++) {
			if (checkQueensOnSameDiagonal(row, advancedRow, rows)) {
				conflicts = true;
				break;
			}
		}
		return conflicts;
	}
	
	private boolean checkQueensOnSameDiagonal(int row1, int row2,
			List<Integer> rows) {
		return Math.abs(row1-row2) == 
				Math.abs(rows.get(row1)- rows.get(row2));
	}
}
