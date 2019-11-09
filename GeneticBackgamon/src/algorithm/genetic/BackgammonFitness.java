package algorithm.genetic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import BG.Board;
import algorithm.MoveTest;
import algorithm.util.Common;
import fitness.Fitness;
import gene.Gene;


public class BackgammonFitness implements Fitness, Serializable {

	List<MoveTest> tests;
	
	public BackgammonFitness(List<MoveTest> tests) {
		super();
		this.tests = tests;
	}

	@Override
	public void setFitness(Gene gene) {
		gene.setFitness(tests.parallelStream()
				.mapToDouble(test -> calculateMoveTestFitness(gene, test))
				.sum());
	}
	
	Comparator<Map.Entry<Board, Double>> byScore = (b1, b2) -> b1.getValue().compareTo(b2.getValue());
	public double calculateMoveTestFitness(Gene _gene, MoveTest test) {
		double fitness;
		BackgammonGene gene = (BackgammonGene) _gene;
		Map<Board, Double> board_score = new LinkedHashMap<Board, Double>();
		for (Board board : test.getPossibilities()) {
			board_score.put(board, gene.getNn()
					.feed(Common.convertBoardToNNInput(board, test.getPlayer())));
		}
		ArrayList<Board> keys = new ArrayList<>();
		board_score = board_score.entrySet().stream().sorted(byScore)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, 
						(e1,e2)->e2, LinkedHashMap::new));
		board_score.entrySet().stream().forEach(a -> keys.add(a.getKey()) );
//		board_score.entrySet().stream().sorted(byScore).forEach(a -> keys.add(a.getKey()));
		int index = keys.indexOf(test.getExpected());
		if (keys.size() == 0 || index == (keys.size()-1) ) {
			fitness = 1;
		} 
		else {
/*			fitness = (double) (index+1)/keys.size();
			if (index == keys.size()-1) {
				fitness += 0.5;
			} */
			fitness = (double)(index+1)/keys.size();
			ArrayList<Double> scores = new ArrayList<>(board_score.values());
			double bestScore = scores.get(scores.size()-1);
			double worstScore = scores.get(0);
			double indexScore = scores.get(index);
			double nextIndexScore = scores.get(index+1);
//			fitness += (Math.abs(indexScore-worstScore)/Math.abs(bestScore-worstScore))/keys.size();
			fitness += ((Double.MAX_VALUE-Math.abs(bestScore-indexScore))/Double.MAX_VALUE)/keys.size();
//			fitness += ((Double.MAX_VALUE-Math.abs(nextIndexScore-indexScore))/Double.MAX_VALUE)/keys.size();
/*			ArrayList<Double> scores = new ArrayList<>(board_score.values());
			double bestScore = scores.get(scores.size()-1);
			double worstScore = scores.get(0);
			double indexScore = scores.get(index);
			fitness = Math.abs(indexScore-worstScore)/Math.abs(bestScore-worstScore);*/
		}
		return fitness;
	}	
	
}
