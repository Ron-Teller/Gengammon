package BG;

import java.util.List;

import BG.Glossary.Color;
import BG.Glossary.GameMode;
import algorithm.Minimax;

public class MiniMaxArtificialIntelligence extends ArtificialIntelligence {

	private Minimax minimax;
	
	public MiniMaxArtificialIntelligence(Algorithm alg, int d, GameMode _mode,
			Minimax minimax) {
		super(alg, d, _mode);
		this.minimax = minimax;
	}

	@Override
	public GameMove getBestGameMove(Board board, Color player, GameMode _mode,
			List<Dice> self_dice, List<Dice> opp_dice) {
		// TODO Auto-generated method stub
		GameMove move = minimax.findNextBestMove(board, player, self_dice.get(0));
		return move;
	}
	
}
