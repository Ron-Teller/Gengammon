package BG;

import java.util.List;

public class GameMove {
	private final List<Move> moves;
	private final Dice dice;
	
	public GameMove(List<Move> _moves, Dice _dice) {
		moves = _moves;
		dice = _dice;
	}
	
	public List<Move> getMoves() {
		return moves;
	}
	
	public Dice getDice() {
		return dice;
	}
}
