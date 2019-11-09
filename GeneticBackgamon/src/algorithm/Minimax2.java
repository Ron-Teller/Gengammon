package algorithm;

import java.util.List;

import BG.Board;
import BG.Dice;
import BG.GameMove;
import BG.Glossary.Color;
import BG.Glossary.GameMode;

public class Minimax2 extends Minimax {

	@Override
	protected double alphabeta(int depth, Board board, Color currentTurn,
			Double _alpha, Double _beta) {
		
		Double score = null;
		double branchScore;
		Board newBoard;
		List<GameMove> gmoves = getPossibleGameMovesForDepth2(board, currentTurn, depth);
		GameMove best = null;
		
		// If level is a node return static evaluation function or no moves left
		if (depth == maxDepth || gmoves.isEmpty()) {
			score = staticEvalutionFunction(board, currentTurn);
			if (gmoves != null) {
				best = calculateBestGameMoveByHeuristic(gmoves, board, currentTurn);
			}			
		}
		
		// If current level is calculating maximum move for player
		else if (currentTurn.equals(player)) {
			for (GameMove gm : gmoves) {
				newBoard = rules.moveBoard(board, gm.getMoves(), currentTurn);
				branchScore = alphabeta(depth+1, newBoard, 
						currentTurn.opposite(), null, null);
				// If this is the first score, it has to be maximum
				if (score == null) {
					score = branchScore;
					best = gm;
				}
				
				else {
					// Check if this is the new maximum score
					if (branchScore >= score) {
						score = branchScore;
						best = gm;
					}
				}
			}
		}
		
		// If current level is calculating minimum move for opponent
		else {
			for (GameMove gm : gmoves) {
				newBoard = rules.moveBoard(board, gm.getMoves(), currentTurn);
				// In this mode both players have the same dice bag, so
				// update both bags				
				branchScore = alphabeta(depth+1, newBoard, 
						currentTurn.opposite(), null, null);
				// If this is the first score, it has to be minimum
				if (score == null) {
					score = branchScore;
					best = gm;
				}
				// Check if this is the new maximum score
				else {
					if (branchScore < score) {
						score = branchScore;
						best = gm;
					}
				}
			}	
		}
		
		if (depth == 0) {
			gmove = best;
		}
				
		return score;
	}
	
}
