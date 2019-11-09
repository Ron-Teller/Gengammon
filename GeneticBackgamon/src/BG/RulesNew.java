package BG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import BG.Glossary.Color;
import BG.Glossary.PlayerGameState;
import BG.Glossary.Side;

public class RulesNew {
	public static boolean moveIsValid(Move move, Board board, List<Integer> rolls, Color p_col) {
		// Checks if move is valid and no rules broken
		List<Integer> available = getAvailablePointMoves(board, move.getSource(), rolls, p_col);
		return available.contains(move.getDest());
	}
	
	public static PlayerGameState getPlayerGameState(Board board, Color color) {
		// Evaluates if player has checkers on bar, advancing normally or
		// bearing his checkers off
		PlayerGameState state;
		
		if (board.getBar(color) > 0)
			state = PlayerGameState.Bar;
		else if (allCheckersHome(board, color))
			state = PlayerGameState.Bearing;
		else 
			state = PlayerGameState.Advancing;
		
		return state;
	}
	
	public static Side getColorSide(Board board, Color color) {
		// Get the side of playing color on board
		Side side = (board.getTopColor().equals(color)) ? Side.Top : Side.Bottom;
		return side;
	}
	
	public static boolean allCheckersHome(Board board, Color color) {
		// Checks if no checkers are outside of players home
		Side side = getColorSide(board, color);
		boolean all_home = false;
		int index;
		int count=0;
		
		if  (side.equals(Side.Top)) {
			for (index=19; index<=24; index++) {
				if (board.getPointColor(index).equals(color)) {
					count += board.getPointAmount(index); 
				}
			}
			count += board.getBearing(color);
			if (count == 15) {
				all_home = true; }
		}
		
		else if  (side.equals(Side.Bottom)) {
			for (index=1; index<=6; index++) {
				if (board.getPointColor(index).equals(color)) {
					count += board.getPointAmount(index); 
				}
			}
			count += board.getBearing(color);
			if (count == 15) {
				all_home = true; }
		}
		
		return all_home;
	}
	
	public static boolean occupiedByPlayer(Board board, int point, Color p_col) {
		// Checks if point is occupied by player
		boolean occupied = false;
		if (point == Move.Bar) {
			if (board.getBar(p_col) > 0) {
				occupied = true;
			}
		}
		else {
			if (board.getPointColor(point).equals(p_col)) {
				occupied = true;
			}
		}
		return occupied;
	}
	
	public static Board moveGameBoard(Board _board, Move move, Color p_col) {
		// Moves the board a single move
		int dest = move.getDest();
		int src = move.getSource();
		Board board = new Board(_board); // new copy so passed parameter wont be changed
		
		// If Moving checker from bar
		if (src == Move.Bar) {
			// If landing on point occupied by opponent
			if (board.getPointColor(dest).equals(p_col.opposite())) {
				// If opponents point is a block (more than 1 checker)
				if (board.getPointAmount(dest) > 1) {
					throw new IllegalStateException("Can not move from point "
							+src+" of color "+p_col.toString()+
							" to occupied opponent block in point "+dest+".");
				}
				// If opponent has only 1 checker its a hit
				else {
					board.increaseBar(p_col.opposite());
					board.setPoint(dest, p_col, 1);
					board.decreaseBar(p_col);
				}
			}
			// if landing point is empty or already occupied by player
			else {
				board.setPoint(dest, p_col, board.getPointAmount(dest)+1);
				board.decreaseBar(p_col);
			}
		}
		
		// Check if point to move belongs to moving player
		else if (occupiedByPlayer(board, move.getSource(), p_col)) {
			
			// If destination is one of the 24 points
			if (dest >= 1 && dest <=24) {
				// If landing on point occupied by opponent
				if (board.getPointColor(dest).equals(p_col.opposite())) {
					// If opponents point is a block (more than 1 checker)
					if (board.getPointAmount(dest) > 1) {
						throw new IllegalStateException("Can not move from point "
								+src+" of color "+p_col.toString()+
								" to occupied opponent block in point "+dest+".");
					}
					// If opponent has only 1 checker its a hit
					else {
						board.increaseBar(p_col.opposite());
						board.setPoint(dest, p_col, 1);
						board.setPoint(src, p_col, board.getPointAmount(src)-1);
					}
				}
				// if landing point is empty or already occupied by player
				else {
					board.setPoint(dest, p_col, board.getPointAmount(dest)+1);
					board.setPoint(src, p_col, board.getPointAmount(src)-1);
				}
			}
			// If bearing off a checker
			else if (dest == Move.Bearing) {
				board.setPoint(src, p_col, board.getPointAmount(src)-1);
				board.increaseBearing(p_col);
			}
		}
		// Point to move does not belong to player
		else {
			System.out.println("src: "+src+" dest: "+dest);
			throw new IllegalStateException("The point "+src+" is not occupied by color "+p_col+".");
		}
		
		return board;
	}
	
	public static boolean hasPrisoners(Board board) {
		return (board.getBar(board.getTopColor()) > 0 ||
				board.getBar(board.getBotColor()) > 0);
	}
	
	public static boolean canBearOffPoint(Board board, int point, List<Integer> rolls, Color p_col) {
		// Checks if checker on point can be beared off
		int farthest;
		int index;
		boolean can_bear = false;
		
		// point is occupied by player
		if (board.getPointColor(point).equals(p_col)) {
			// If player is in a bearing off state
			if (getPlayerGameState(board, p_col).equals(PlayerGameState.Bearing)) {
				// If player is top
				if (board.getTopColor().equals(p_col)) {
					// If point can be beared off directly
					if (rolls.contains((Integer)(25-point))) {
						can_bear = true;
					}
					
					// If point is farthest away point from bearing
					// and a roll is big enough to bear it off
					else {
						for (index=19; index<=24; index++) {
							if (board.getPointColor(index).equals(p_col)) {
								farthest = index;
								// If point is the farthest point in players
								// home from bearing
								if (point == farthest) {
									// Dice roll is big enought to bear off this point
									if ((25-point) <= (int) Collections.max(rolls)) {
										can_bear = true;
									}
								}
								break;
							}
						}
					}
				}
				
				// If player is bot
				else {
					// If point can be beared off directly
					if (rolls.contains((Integer)(point))) {
						can_bear = true;
					}
					
					// If point is farthest away point from bearing
					// and a roll is big enough to bear it off
					else {
						for (index=6; index>=1; index--) {
							if (board.getPointColor(index).equals(p_col)) {
								farthest = index;
								// If point is the farthest point in players
								// home from bearing
								if (point == farthest) {
									// Dice roll is big enough to bear off this point
									if (point <= (int) Collections.max(rolls)) {
										can_bear = true;
									}
								}
								break;
							}
						}
					}
				}
			}
		}
		return can_bear;
	}
	
	public static boolean pointCanMove(Board board, int point, List<Integer> rolls, Color turn) {
		// Checks if player can move checkers on point given dice rolls
		return (getAvailablePointMoves(board, point, rolls, turn).size() > 0);
	}
	
	public static boolean isHomebound(Board board) {
		return ( (getFarthestPoint(board, board.getTopColor()) -
				getFarthestPoint(board, board.getBotColor())) > 0);
	}
	
	private static int getFarthestPoint(Board board, Color player) {
		// return a pointToInt conversion
//		if (board.getBar(player) > 0) {
//			return pointToInt(Move.Bar, board.getColorSide(player));
//		}
		if (board.getBar(player) > 0) {
			return pointToInt(Move.Bar, board.getColorSide(player));
		}
		return IntStream.rangeClosed(1, 24)
				.filter(i -> board.getPointColor(i) == player)
				.map(i -> pointToInt(i, board.getColorSide(player)))
				.max().getAsInt();
		
	}
	
	public static List<Integer> getAvailablePointMoves(Board board, int point, List<Integer> rolls, Color p_col) {
		// Get available destination points that given point can move to
		List<Integer> moves = new ArrayList<Integer>();
		List<Integer> roll_options = new ArrayList<Integer>();
		for (Integer roll : rolls) {
			if (! roll_options.contains(roll)) {
				roll_options.add(roll);
			}
		}
		int dest;

		// If point is bar
		if (point == Move.Bar) {
			// Check is player has checkers on bar to move
			if (board.getBar(p_col) > 0) {
				// Check if player can place checker in opponent base
				for (Integer roll : roll_options) {
					// If player is top
					if (board.getTopColor().equals(p_col)) {
						// roll will land top player on the same point number
						dest = roll;
						// If point occupied by opponent
						if (board.getPointColor(dest).equals(p_col.opposite())) {
							// If it is a blot then player can land on it
							if (board.getPointAmount(dest) == 1) {
								moves.add(dest);
							}
						}
						else {
							moves.add(dest);
						}
					}
					// If player is bot
					else  {
						// roll will land player on top right points
						dest = 25 - roll;
						// If point occupied by opponent
						if (board.getPointColor(dest).equals(p_col.opposite())) {
							// If it is a blot then player can land on it
							if (board.getPointAmount(dest) == 1) {
								moves.add(dest);
							}
						}
						else {
							moves.add(dest);
						}
					}
				}
			}
		}
		

		// Check if point is occupied by player
		else if (board.getPointColor(point).equals(p_col)) {
			// Check if player does not have checker on bar he has to move first
			if (board.getBar(p_col) == 0) {
				// Check possible moves for every roll
				for (Integer roll : roll_options) {
					
					// If player is top
					if (p_col.equals(board.getTopColor())) {
						dest = point + roll.intValue();
						// Check if roll can bear off checker
						if (dest > 24) {
							if (canBearOffPoint(board, point, rolls, p_col)) {
								moves.add(Move.Bearing);
							}
						}
						
						// Check if roll can advance checker
						else {
							// If point to move to is occupied by opponent
							if (board.getPointColor(dest).equals(p_col.opposite())) {
								// Then check if opponent does not have a block
								if (board.getPointAmount(dest) == 1) {
									moves.add(dest);
								}
							}
							// If point is empty or already occupied by player
							else {
								moves.add(dest);
							}
						}
				    }
					
					// If player is bot
					else if (p_col.equals(board.getBotColor())) {
						dest = point - roll.intValue();
						// Check if roll can bear off checker
						if (dest < 1) {
							if (canBearOffPoint(board, point, rolls, p_col)) {
								moves.add(Move.Bearing);
							}
						}
						
						// Check if roll can advance checker
						else {
							// If point to move to is occupied by opponent
							if (board.getPointColor(dest).equals(p_col.opposite())) {
								// Then check if opponent does not have a block
								if (board.getPointAmount(dest) == 1) {
									moves.add(dest);
								}
							}
							// If point is empty or already occupied by player
							else {
								moves.add(dest);
							}
						}
				    }
				}
			}
		}
		
		List<Integer> no_duplicate = new ArrayList<Integer>();
		for (Integer move : moves) {
			if (! no_duplicate.contains(move)) {
				no_duplicate.add(move);
			}
		}
		return no_duplicate;
	}
	
	public static List<Integer> getMovablePoints(Board board, List<Integer> rolls, Color p_col) {
		// Finds all points that are movable for any roll in given rolls
		
		int point;
		List<Integer> movable = new ArrayList<Integer>();
		
		for (point=1; point<=24; point++) {
			if (pointCanMove(board, point, rolls, p_col)) {
				movable.add(point);
			}		
		}
		
		if (pointCanMove(board, Move.Bar, rolls, p_col)) {
			movable.add(Move.Bar); 
		}
		
		return movable;
	}
	
	public static boolean canMove(Board board, Color color, List<Integer> rolls) {
		// Checks if player can move any point
		return (getMovablePoints(board, rolls, color).size() > 0);
	}
	
	public static Integer getRollUsed(Board board, Move move, List<Integer> rolls, Color p_col) {
		// Given roll that will be used given board state and move
		List<Integer> option = new ArrayList<Integer>();
		List<Integer> available = new ArrayList<Integer>();
		Integer roll_used = null;
		
		for (Integer roll : rolls) {
			// make list of only this option to roll
			option.clear();
			option.add(roll);
			
			available = getAvailablePointMoves(board, move.getSource(), option, p_col);
			if (available.size() > 0 && available.get(0).equals(move.getDest())) {
				roll_used = roll;
				break;
			}
		}
		return roll_used;
	}

	
	public static Board moveBoard(Board _board, List<Move> moves, Color p_col) {
		// Move board several moves forward
		Board board = new Board(_board);

		for (Move move : moves) {
			board = moveGameBoard(board, move, p_col);
		}
		return board;
	}
	
	public static boolean movesAreValid(Board board, Color player, List<Move> moves) {
		//TODO maybe not for now, assume input is correct
		return true;
	}
	
	public static List<Integer> toRolls(Dice dice) {
		// Find rolls of given dice.
		// For example: (2,2) => 2,2,2,2
		// 				(2,3) => 2,3
		List<Integer> rolls = new ArrayList<Integer>();
		
		if (dice.getDice1() == dice.getDice2()) {
			for (int loop=0; loop<4; loop++) {
				rolls.add(dice.getDice1());
			}
		}
		
		else {
			rolls.add(dice.getDice1());
			rolls.add(dice.getDice2());
		}
		
		return rolls;
	}
	
	public static boolean playerWon(Board board, Color color) {
		return (board.getBearing(color) == 15);
	}
	
	public static Color getBoardLeader(Board board) {
		// Get leading player, by winning or point leading
		Color leader = null;
		int top_score = 0;
		int bot_score = 0;
		Color top_col = board.getTopColor();
		Color bot_col = board.getBotColor();
		int point;
		
		for (point=1; point<=24; point++) {
			if (board.getPointColor(point).equals(top_col)) {
				top_score = top_score + board.getPointAmount(point)*point;
			}
			else if (board.getPointColor(point).equals(bot_col)) {
				bot_score = bot_score + board.getPointAmount(point)*(25-point);
			}
		}
		top_score = top_score + board.getBearing(top_col)*25;
		bot_score = bot_score + board.getBearing(bot_col)*25;
		
		// Decide leader by pip count
		if (top_score != bot_score) {
			leader = (top_score > bot_score) ? top_col : bot_col;
		}
		
		// Decide leader by bearing
		else if (board.getBearing(top_col) != board.getBearing(bot_col)){
			leader = (board.getBearing(top_col) > board.getBearing(bot_col)) ? top_col : bot_col;
		}
		
		// no winner
		else {
			leader = null;
		}
		
		return leader;
	}
	
	public static List<Board> getAllPossibleBoards(Board board, Color player, Dice dice) {
		// get all boards states that can be moved to from given board and dice
		return getPossibleGameMoves(player, board, dice).stream()
			.map(gameMove -> moveBoard(player, board, gameMove))
			.collect(Collectors.toList());
	}
	
	public static Board moveBoard(Color player, Board board, GameMove gameMove) {
		return moveBoard(board, gameMove.getMoves(), player);
	}
	
	public static List<GameMove> getPossibleGameMoves(Color player, Board board, Dice dice) {
		List<List<Move>> moves = new ArrayList<List<Move>>();
		findAllGameMovesRecursively(moves, new ArrayList<Move>(), board,
				player, toRolls(dice));
		return moves.stream().map(m -> new GameMove(m, dice)).collect(Collectors.toList());
	}
	
	private static Integer getAvailablePointDestination(Board board, int point, int roll, Color p_col) {
		List<Integer> rolls = new ArrayList<Integer>();
		rolls.add(roll);
		return getAvailablePointMoves(board, point, rolls, p_col).get(0);
	}
	
	private static void findAllGameMovesRecursively(List<List<Move>> moves, List<Move> moved, 
			Board board, Color player, List<Integer> rollsLeft)
	{
		// Should not move points behind previous moved points
		// Should not use roll on point smaller than the points previous roll
		
		if (getMovablePoints(board, rollsLeft, player).isEmpty()) {
			moves.add(moved);
		}
		
		List<Move> movedAfter;
		Integer moveTo;
		Move move;
		List<Integer> rollsLeftAfter;
		List<Integer> distinct = getDistinct(rollsLeft);
		for (Integer roll : distinct) {
			for (Integer point : getMovablePoints(board, roll, player)) {
				if (! isTrailing(point, moved, board.getColorSide(player)) &&
						rollBiggerEqualThanPrevious(point, roll, moved, board.getColorSide(player))) {
					movedAfter = new ArrayList<Move>(moved);
					moveTo = getAvailablePointDestination(board,point,roll,player);
					move = new Move(point, moveTo);
					movedAfter.add(move);
					rollsLeftAfter = new ArrayList<Integer>(rollsLeft);
					rollsLeftAfter.remove(roll);
					findAllGameMovesRecursively(moves, movedAfter, 
							moveGameBoard(board, move, player), player, rollsLeftAfter);
				}
			}
		}
	}
	
	private static List<Integer> getDistinct(List<Integer> rolls) {
		return rolls.stream().distinct().collect(Collectors.toList());
	}
	
	public static List<Integer> getMovablePoints(Board board, Integer roll, Color player) {
		return getMovablePoints(board, Arrays.asList(roll), player);
	}
	
	private static boolean isTrailing(int point, List<Move> moved, Side side) {
		boolean trailing = false;
		if (! moved.isEmpty()) {
			int lastMovedFrom = moved.get(moved.size()-1).getSource();
			
			if (lastMovedFrom != Move.Bar) {
				if (point == Move.Bar) {
					trailing = true;
				}
				else if (side.equals(Side.Top)) {
					if (point < lastMovedFrom) {
						trailing = true;
					}
				} 
				else if (side.equals(Side.Bottom)) {
					if (point > lastMovedFrom) {
						trailing = true;
					}
				}
			}
		}
		return trailing;
	}
	
	private static boolean rollBiggerEqualThanPrevious(int point, Integer roll, List<Move> moved, Side side) {
		boolean biggerEqual = true;
		if (! moved.isEmpty()) {
			Move lastMove = moved.get(moved.size()-1);
			if (point == lastMove.getSource()) {
				biggerEqual = (roll >= getMoveDistance(moved.get(moved.size()-1), side));
			}
		}
		return biggerEqual;
	}
	
	private static int getMoveDistance(Move move, Side side) {
		return Math.abs(pointToInt(move.getSource(),side) - 
						pointToInt(move.getDest(),side));
	}
	
	private static int pointToInt(int point, Side side) {
		int toInt;
		if (point == Move.Bar) {
			if (side.equals(Side.Top)) {
				toInt = 0;
			} else {
				toInt = 25;
			}
		}
		else if (point == Move.Bearing) {
			if (side.equals(Side.Top)) {
				toInt = 25;
			} else {
				toInt = 0;
			}
		}
		else {
			toInt = point;
		}
		return toInt;
	}
		
}
