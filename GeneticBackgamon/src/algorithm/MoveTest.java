package algorithm;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import BG.Board;
import BG.Dice;
import BG.Glossary.Color;
import BG.RulesNew;

public class MoveTest implements Serializable {

	private final Board initial; //initial board state
	private final Dice dice; 	   // dice given
	private final Board expected;  // expected to have highest heuristic
								   // from all available moves from initial board
	private final Color player;
	private final List<Board> possibilities;
	
	public MoveTest(Board initial, Dice dice, Board expected, Color player) throws IOException {
		this.initial = initial;
		this.dice = dice;
		this.expected = expected;
		this.player = player;
		this.possibilities = RulesNew.getAllPossibleBoards(this.initial, this.player, this.dice);
		for (int i=0; i<this.possibilities.size(); i++) {
			if (this.possibilities.get(i).same(this.expected)) {
				this.possibilities.set(i, this.expected);
			}
		}
		boolean found = false;
		for (int i=0; i<this.possibilities.size(); i++) {
			if (this.possibilities.get(i).same(this.expected)) {
				found = true;
			}
		}
		if (this.possibilities.size() == 0) {
			found = true;
		}
		if (found == false) {
			   IOException e = new IOException("no board match found");
			   try {
				throw e;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
/*				System.out.println("Possibility size: "+this.possibilities.size());
				this.initial.print();
				System.out.println(this.dice+" "+this.player);
				this.expected.print();
				e1.printStackTrace();*/
				throw e1;
			}			
		}
	}

	public Board getInitial() {
		return initial;
	}

	public Dice getDice() {
		return dice;
	}

	public List<Board> getPossibilities() {
		return possibilities;
	}

	public Board getExpected() {
		return expected;
	}

	public Color getPlayer() {
		return player;
	}
	
	public boolean same(MoveTest other) {
		boolean same = (initial.same(other.initial) && 
				dice.same(other.dice) &&
				expected.same(other.expected));
		return same;
	}
	
	public boolean hasDifferentExpectation(MoveTest other) {
		boolean differentExpectation = false;
		if (initial.same(other.initial) && 
				dice.same(other.dice)) {
			if (! expected.same(other.expected)) {
				differentExpectation = true;
			}
		}
		return differentExpectation;
	}

	@Override
	public String toString() {
		return "MoveTest [initial=" + initial + ", dice=" + dice
				+ ", expected=" + expected + ", player=" + player + "]";
	}
	
	
	
}
