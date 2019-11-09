package problems.queens;

import java.util.List;
import java.util.stream.IntStream;

import gene.Gene;

public class QueensGene extends Gene {

	private List<Integer> rows;

	public QueensGene(List<Integer> rows) {
		super();
		this.rows = rows;
	}

	public List<Integer> getRows() {
		return rows;
	}

	public void setRows(List<Integer> rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" ");
		IntStream.range(0, rows.size())
			.forEach(index -> sb.append(" "+index));
		sb.append(System.lineSeparator());
		
		for (int row=0; row<rows.size(); row++) {
			sb.append((row+1)+"|");
			for (int col=0; col<rows.get(row); col++) {
				sb.append(" |");
			}
			sb.append("x|");
			for (int col=rows.get(row)+1; col<rows.size(); col++) {
				sb.append(" |");
			}
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
}
