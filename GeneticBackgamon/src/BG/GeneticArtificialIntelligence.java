package BG;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import BG.Glossary.Color;
import BG.Glossary.GameMode;
import algorithm.GeneticAlgorithmDep;
import algorithm.NeuralNetwork;
import algorithm.util.Common;

public class GeneticArtificialIntelligence extends ArtificialIntelligence {

	private NeuralNetwork neuralNetwork;
	private RulesNew rules;
	
	public GeneticArtificialIntelligence(Algorithm alg, int d, GameMode _mode,
			NeuralNetwork neuralNetwork) {
		super(alg, d, _mode);
		this.neuralNetwork = neuralNetwork;
	}

	@Override
	public GameMove getBestGameMove(Board board, Color player, GameMode _mode,
			List<Dice> self_dice, List<Dice> opp_dice) {
		
		if (! rules.isHomebound(board)) {
			Comparator<GameMove> byBoardHeuristicScore = 
				(gm1, gm2) -> Double.compare(
						  neuralNetwork.feed(GeneticAlgorithmDep.
							convertBoardToNNInput(rules.moveBoard(player, board, gm2), player)),
						  neuralNetwork.feed(GeneticAlgorithmDep.
							convertBoardToNNInput(rules.moveBoard(player, board, gm1), player)));
				rules.getPossibleGameMoves(player, board, self_dice.get(0)).stream()
				.sorted(byBoardHeuristicScore)
				.peek(c -> {rules.moveBoard(player, board, c).print();  System.out.println("SCORE: "+
				neuralNetwork.feed(Common.convertBoardToNNInput(rules.moveBoard(player, board, c), player))+"  "+player);})
				.collect(Collectors.toList());
				System.out.println("=============");
			GameMove best = rules.getPossibleGameMoves(player, board, self_dice.get(0)).stream()
				.sorted(byBoardHeuristicScore)
				.peek(c -> {rules.moveBoard(player, board, c).print();  System.out.println("SCORE: "+
				neuralNetwork.feed(Common.convertBoardToNNInput(rules.moveBoard(player, board, c), player))+"  "+player);})
				.findFirst().get();
			return best;
		}
		else {
			return super.getBestGameMove(board, player, _mode, self_dice, opp_dice);
		}
	}
	
	
	
}
