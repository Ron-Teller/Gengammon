package BG;

import java.util.ArrayList;
import java.util.List;

import BG.Glossary.Color;
import BG.Glossary.GameMode;
import BG.Glossary.PlayerGameState;

public class ArtificialIntelligence {

	public static enum Algorithm {Minimax, Alphabeta, Genetic, Genetic_Minimax};
	
	private RulesNew rules;
	private int depth;
	private GameMove gmove = null;
	private Algorithm algorithm;
	private GameMode mode;
	
	public ArtificialIntelligence(Algorithm alg, int d, GameMode _mode) {
		algorithm = alg;
		depth = d;
		mode = _mode;
	}
	
	public void setDepth(int d) {
		depth = d;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public Algorithm getAlgorithm() {
		return algorithm;
	}
	
	public void setAlgorithm(Algorithm alg) {
		algorithm = alg;
	}
	
	public GameMove getBestGameMove(Board board, Color player, GameMode _mode, List<Dice> self_dice, List<Dice> opp_dice) {
		mode = _mode;
		if (algorithm.equals(Algorithm.Minimax)) {
			minimax(0, board, player, player, self_dice, opp_dice, null);
		}
		else {
			alphabeta(0, board, player, player, null, null, self_dice, opp_dice, null);
		}
		if (gmove == null) {
			gmove = new GameMove(new ArrayList<Move>(), self_dice.get(0));
		}
		return gmove;
	}
	
	private double minimax(int curr_depth, Board board, Color player, Color current, 
			List<Dice> self_dice, List<Dice> opp_dice, Dice used) {
		// Finds best game move and places it in object global variable
		Double score = null;
		double branch_score;
		Board new_board;
		List<Dice> new_dice;
		List<GameMove> gmoves = getPossibleGameMoves(board, current, self_dice);
		GameMove best = null;
		
		// If level is a node return static evaluation function or no moves left
		if (curr_depth == depth || gmoves.isEmpty()) {
			score = staticEvalutionFunction(board, player, current, self_dice, opp_dice, used);
		}
		
		// If current level is calculating maximum move for player
		else if (current.equals(player)) {
			for (GameMove gm : gmoves) {
				new_board = rules.moveBoard(board, gm.getMoves(), current);
				new_dice = removeDiceOption(self_dice, gm.getDice());
				// In this mode both players have the same dice bag, so
				// update both bags
				if (mode.equals(GameMode.SEVENTY_TWO)) {
					branch_score = minimax(curr_depth+1, new_board, player, 
							current.opposite(), new_dice, new_dice, gm.getDice());
				}
				// Update only moving players bag
				else {
					branch_score = minimax(curr_depth+1, new_board, player, 
							current.opposite(), new_dice, opp_dice, gm.getDice());
				}
				// If this is the first score, it has to be maximum
				if (score == null) {
					score = branch_score;
					best = gm;
				}
				
				else {
					// Check if this is the new maximum score
					if (branch_score >= score) {
						score = branch_score;
						best = gm;
					}
				}
			}
		}
		
		// If current level is calculating minimum move for opponent
		else {
			for (GameMove gm : gmoves) {
				new_board = rules.moveBoard(board, gm.getMoves(), current);
				new_dice = removeDiceOption(opp_dice, gm.getDice());
				// In this mode both players have the same dice bag, so
				// update both bags				
				if (mode.equals(GameMode.SEVENTY_TWO)) {
					branch_score = minimax(curr_depth+1, new_board, player, 
							current.opposite(), new_dice, new_dice, gm.getDice());
				}
				// Update only moving players bag
				else {
					branch_score = minimax(curr_depth+1, new_board, player, 
							current.opposite(), self_dice, new_dice, gm.getDice());
				}
				// If this is the first score, it has to be minimum
				if (score == null) {
					score = branch_score;
					best = gm;
				}
				// Check if this is the new maximum score
				else {
					if (branch_score < score) {
						score = branch_score;
						best = gm;
					}
				}
			}	
		}
		
		if (curr_depth == 0) {
			gmove = best;
		}
				
		return score;
	}
	
	private double alphabeta(int curr_depth, Board board, Color player, Color current,
			Double _alpha, Double _beta, List<Dice> self_dice, List<Dice> opp_dice, Dice used) {
		// Finds best game move and places it in object global variable
		Double score = null;
		Double alpha;
		Double beta;
		// Make new reference to alpha so recursive calls wont change
		// same variable
		if (_alpha == null) {
			alpha = null;}
		else {
			alpha = new Double(_alpha.doubleValue()); }
		
		if (_beta == null) {
			beta = null;}
		else {
			beta = new Double(_beta.doubleValue()); }

		double child_score;
		Board new_board;
		List<Dice> new_dice;
		List<GameMove> gmoves = getPossibleGameMoves(board, current, self_dice);
		GameMove best = null;
		
		// If level is a node return static evaluation function or no moves left
		if (curr_depth == depth || gmoves.isEmpty()) {
			score = staticEvalutionFunction(board, player, current, self_dice, opp_dice, used);
		}
		
		// If current level is calculating maximum move for player
		else if (current.equals(player)) {
			for (GameMove gm : gmoves) {
				new_board = rules.moveBoard(board, gm.getMoves(), current);
				new_dice = removeDiceOption(self_dice, gm.getDice());
				child_score = alphabeta(curr_depth+1, new_board, player, 
						current.opposite(), alpha, beta, new_dice, opp_dice, gm.getDice());
				
				// Get max child score
				if (score == null) {
					score = child_score;
					best = gm;
				} else {
					if (child_score >= score) {
						score = child_score;
						best = gm;
					}
				}
				
				// Alpha gets max between score and alpha
				if (alpha == null) {
					alpha = new Double(score.doubleValue());
				}
				else if (alpha.compareTo(score) < 0) {
					alpha = new Double(score.doubleValue());
				}
				
				// If beta <= alpha
				if (beta != null) {
					if (alpha.compareTo(beta) >= 0) {
						break;
					}
				}
			}
		}
		
		// If current level is calculating minimum move for opponent
		else {
			for (GameMove gm : gmoves) {
				new_board = rules.moveBoard(board, gm.getMoves(), current);
				new_dice = removeDiceOption(opp_dice, gm.getDice());
				child_score = alphabeta(curr_depth+1, new_board, player, 
						current.opposite(), alpha, beta, self_dice, new_dice, gm.getDice());
				
				// Get min child score
				if (score == null) {
					score = child_score;
					best = gm;
				}
				else {
					if (child_score < score) {
						score = child_score;
						best = gm;
					}
				}
				
				// Beta gets minimum between score and beta
				if (beta == null) {
					beta = new Double(score.doubleValue());
				}
				else if (beta.compareTo(score) > 0) {
					beta = new Double(score.doubleValue());
				}
				
				// If beta <= alpha
				if (alpha != null) {
					if (alpha.compareTo(beta) >= 0) {
						break;
					}
				}
			}	
		}
		
		if (curr_depth == 0) {
			gmove = best;
		}
				
		return score;
	}
	
	public double staticEvalutionFunction(Board board, Color self_col, Color next_turn,
						List<Dice> self_dice, List<Dice> opp_dice, Dice used) {
		// The bigger the difference between players score and opponents score
		// the better (only is player has higher score) 
		
		// Assign different magnitudes of importance for each 
		// static board evaluation type.
		double pip_base = 6;
		double blot_base_penalty = 6;
		double block_base = 3;
		
		double self_blot_mult;
		double opp_blot_mult;
		
		// If it is players turn next round, blots should
		// have a smaller penalty since he can block them
		// or move them
		if (next_turn.equals(self_col)) {
			opp_blot_mult = blot_base_penalty;
			self_blot_mult = opp_blot_mult*0.3;
		}
		else {
			self_blot_mult = blot_base_penalty;
			opp_blot_mult = self_blot_mult*0.3;
		}
		
		double self_pip_mult = pip_base;
		double opp_pip_mult = self_pip_mult;
		
		// Blocks should be more important in normal mode
		// because there is no choice of dice so it is harder
		// to advance checkers over opponents block
		if (mode.equals(GameMode.NORMAL)) {
			block_base = 3.7;
		}
		
		double self_block_mult = block_base;
		double opp_block_mult = block_base;
	
		double self_runner_mult = 0.2;
		double opp_runner_mult = self_runner_mult;
		
		
		double self_score = 0;
		
		double self_pip = getModifiedPipCount(board, self_col);
		double self_blot_penalty = getBlotsPenalty(board, self_col, self_dice, opp_dice);
		double self_block_score = getBlockadeScore(board, self_col);
		double self_runner_penalty = getRunnerPenalty(board, self_col);
		
		self_score = self_pip*self_pip_mult + self_blot_penalty*self_blot_mult +
				self_block_score*self_block_mult + self_runner_penalty*self_runner_mult;
		
		double opponent_score = 0;
		double opp_pip = getModifiedPipCount(board, self_col.opposite());
		double opp_blot_penalty = getBlotsPenalty(board, self_col.opposite(), self_dice, opp_dice);
		double opp_block_score = getBlockadeScore(board, self_col.opposite());
		double opp_runner_penalty = getRunnerPenalty(board, self_col.opposite());
		
		opponent_score = opp_pip*opp_pip_mult + opp_blot_penalty*opp_blot_mult +
				opp_block_score*opp_block_mult + opp_runner_penalty*opp_runner_mult;
		
		/*System.out.println("\n====================================");
		System.out.println("<SELF "+self_col.toString()+"> pip:"+self_pip+" blot: "+self_blot_penalty+" block: "+self_block_score+" runner: "+self_runner_penalty);
		System.out.println("<SELF MULT> pip: "+self_pip_mult+" blot: "+self_blot_mult+" block: "+self_block_mult+" runner:"+self_runner_mult+" SCORE: "+self_score);
		System.out.println("<OPP "+self_col.opposite().toString()+"> pip:"+opp_pip+" blot: "+opp_blot_penalty+" block: "+opp_block_score+" runner: "+opp_runner_penalty);
		System.out.println("<OPP MULT> pip: "+opp_pip_mult+" blot: "+opp_blot_mult+" block: "+opp_block_mult+" runner:"+opp_runner_mult+" SCORE: "+opponent_score);*/
		
		
		// In case both players have the same bad of dice, using up
		// a good roll should award more points
		if (mode.equals(GameMode.SEVENTY_TWO)) {
			double dice_mult = 0;
			double dice_bonus = getDiceRollBonus(self_dice, used);
			
			// If players turn then he should get bonus for using up
			// the dice
			if (next_turn.equals(self_col.opposite())) {
				self_score = self_score + dice_bonus*dice_mult;
			}
			
			// If opponents turn then he should get bonus for using up
			// the dice
			else {
				opponent_score = opponent_score + dice_bonus*dice_mult;
			}
		}
		
		double diff = Math.abs(self_score-opponent_score);
		return (self_score > opponent_score) ? diff : -diff;
	}

	
	private double getDiceRollBonus(List<Dice> options, Dice dice) {
		// Should return positive number, dice with bigger
		// rolls get bigger bonus
		double bonus = 0;
		List<Integer> rolls = rules.toRolls(dice);
		for (Integer roll : rolls) {
			bonus = bonus + roll.intValue();
		}
		bonus = bonus/2;
		return bonus;
	}
	
	private double getModifiedPipCount(Board board, Color color) {
		// Higher is better, this evaluates to players points by 
		// how advanced his checkers are
		
		double score = 0;
		int point;
		PlayerGameState state = rules.getPlayerGameState(board, color);
		
		// If player is top
		if (board.getTopColor().equals(color)) {
			for (point=1; point<=24; point++) {
				if (board.getPointColor(point).equals(color)) {
					// Home points get the same score, even if they are closer to bearing
					// This is to discourage movement of checkers already in home

					if (isHomePoint(board, color, point) &&
							(! bothPlayersHomeBound(board)) &&
							(! state.equals(PlayerGameState.Bearing))) {
						score = score + board.getPointAmount(point)*(19+(point-19)/1.5);
					}
					else {
						score = score + board.getPointAmount(point)*point;
					}
				}
			}
		}
		// If player is bot
		else {
			for (point=24; point>=1; point--) {
				if (board.getPointColor(point).equals(color)) {
					// Home points get the same score, even if they are closer to bearing
					// This is to discourage movement of checkers already in home
					if (isHomePoint(board, color, point) &&
							(! bothPlayersHomeBound(board)) &&
							(! state.equals(PlayerGameState.Bearing))) {
						score = score + board.getPointAmount(point)*(19+(25-point-19)/1.5);
					}
					else {
						score = score + board.getPointAmount(point)*(25-point);
					}
				}
			}
		}
		
		score = score + board.getBearing(color)*(25);
		
		// We want bearing to prefer taking out checkers instead
		// of advancing them, so we give a bonus for every checker.
		if (state.equals(PlayerGameState.Bearing)) { 
			double bearing_bonus = (double) board.getBearing(color)/15;
			score = score + bearing_bonus;
		}
		
		// We prefer to insert as many checkers home so we can get
		// to bearing stage faster
		if (! state.equals(PlayerGameState.Bearing)) {
			double home_bound_penalty = getHomeBoundPenalty(board, color);
			score = score + home_bound_penalty;
		}


		return score;
	}
	

	private double getBlotsPenalty(Board board, Color color, 
			List<Dice> self_dice, List<Dice> opp_dice) {
		// Should return non positive number, higher is better
		// Calculates penalty for blots on board, because they can
		// be hit and the overall score should drop
		double penalty = 0;
		List<Integer> blots = getBlotPoints(board, color);
		
		for (Integer blot  : blots) {
			penalty = penalty + getBlotPenalty(board, color, blot, 
					self_dice, opp_dice);
		}
		
		return penalty;
	}
	
	private double getBlockadeScore(Board board, Color color) {
		//should return positive score, higher is better
		// Evaluates how well we are blocking the opponent from
		// progressing his checkers
		
		double score = 0;
		List<Integer> blockades = getBlockades(board, color);
		
		for (Integer block : blockades) {
			score = score + getBlockScore(board, color, block);
		}
		
		return score;
	}
	
	private double getBlotPenalty(Board board, Color color, int blot, 
				List<Dice> self_dice, List<Dice> opp_dice) {
		// Should return non positive number. Evaluates the penalty
		// of a blot, because it can be hit by an opponent
		
		double penalty=0;
		
		// If blot can be hit by any dice roll then give it a penalty.
		// We do not know if the dice roll that will hit our blot will
		// be worth it for the opponent. 
		if (blotCanBeHit(board, color, blot, self_dice, opp_dice)) {
			// If player is top
			if (board.getTopColor().equals(color)) {
				// calculate penalty by distance lost from bar
				// if hit
				penalty = -(blot);
			}
			// If player is bot
			else {
				penalty = -(25-blot);
			}
		}
		
		return penalty;
	}
	
	private double getBlockScore(Board board, Color color, int block) {
		// Evaluate score of a specific block to block opponent checkers
		// from advancing
		double score=0;
		int point;
		int block_range; // effective block range
		double block_importance;
		
		// Sanity check
		if (! board.getPointColor(block).equals(color)) {
			throw new IllegalStateException("not color "+color.toString()+" block! "+block);
		}
		
		// If top player
		if (board.getTopColor().equals(color)) {
			
			// Award points only for blocks that block opponents checkers
			// that are in 6 point proximity, since that is that maximum 
			// a dice can roll. Currently, no points are awarded for 
			// blocks of bigger range
			block_range = block+6;
			if (block_range > 24) {
				block_range = 24;
			}
			
			// For all blocks that point opponent checkers on one of the points
			for (point=(block+1); point<=block_range; point++) {
				if (board.getPointColor(point).equals(color.opposite())) {
					// The farther away point is from home the better it is to block it
					block_importance = point/24;
					score = score + (point-block)*block_importance;
				}
			}
			
			// For home blocks that block opponent bar checkers
			if (isHomePoint(board, color, block)) {
				// if opponent has bar checkers
				if (board.getBar(color.opposite()) > 0) {
					// Home blocks get more importance than normal blocks 
					// if opponent has bar checkers
					block_importance = 1.5;
					score = score + (25-block)*block_importance*board.getBar(color.opposite());
				}
			}
		}
		// If bot player
		else {
			block_range = block-6;
			if (block_range < 1) {
				block_range = 1;
			}
			for (point=(block-1); point>=block_range; point--) {
				if (board.getPointColor(point).equals(color.opposite())) {
					// The farther away point is from home the better it is to block it
					block_importance = (25-point)/24;
					score = score + (block-point)*block_importance;
				}
			}
			
			// For home blocks that block opponent bar checkers
			if (isHomePoint(board, color, block)) {
				// if opponent has bar checkers
				if (board.getBar(color.opposite()) > 0) {
					// Home blocks get more importance than normal blocks 
					// if opponent has bar checkers
					block_importance = 1.5;
					score = score + (block)*block_importance*board.getBar(color.opposite());
				}
			}
		}
		
		return score;
	}
	
	private double getRunnerPenalty(Board board, Color color) {
		// Evaluate penalty for the farthest checker of player
		// Should return non positive number
		double penalty = 0;
		int point;
		
		if ( (! rules.getPlayerGameState(board, color).equals(PlayerGameState.Bearing))
				&& (!bothPlayersHomeBound(board)) ) {
			
			// If top player
			if (board.getTopColor().equals(color)) {
				for (point=1; point<=24; point++) {
					if (board.getPointColor(point).equals(color)) {
						penalty = -1*(25-point);
						break;
					}
				}
			}
			
			// If bot player
			else {
				for (point=24; point>=1; point--) {
					if (board.getPointColor(point).equals(color)) {
						penalty = -1*point;
						break;
					}
				}
			}
		}
		
		return penalty;
	}
	
	private List<Integer> getBlotPoints(Board board, Color color) {
		// Return all points that contain blots for player color
		List<Integer> blots = new ArrayList<Integer>();
		int point;
		
		for (point=1; point<=24; point++) {
			if (board.getPointColor(point).equals(color)) {
				if (board.getPointAmount(point) == 1) {
					blots.add(point);
				}
			}
		}
		
		return blots;
	}
	
	private List<Integer> getBlockades(Board board, Color color) {
		// Return all points that contain blockades for player color
		List<Integer> blockades = new ArrayList<Integer>();
		int point;
		
		for (point=1; point<=24; point++) {
			if (board.getPointColor(point).equals(color)) {
				if (board.getPointAmount(point) > 1) {
					blockades.add(point);
				}
			}
		}
		
		return blockades;
	}

	
	private boolean blotCanBeHit(Board board, Color _color, int blot,
			List<Dice> self_dice, List<Dice> opp_dice) {
		boolean hit = false;
		Color color = board.getPointColor(blot);
		// If player is top
		if (board.getTopColor().equals(color)) {
			if (! mode.equals(GameMode.NORMAL)) {
				if (blot >= 17 && board.getBar(color.opposite()) > 0) {
					hit = true;
				}			
			}
			for (int point=blot+1; point <=24; point ++) {
				if (! mode.equals(GameMode.NORMAL)) {
					if (board.getPointColor(point).equals(color.opposite()) &&
							(point-blot) <= 9) {
						hit = true;
					}
				}
				else {
					if (board.getPointColor(point).equals(color.opposite()) &&
							(point-blot) <= 9) {
						hit = true;
					}
				}
			}
		}
		
		// If player is bot
		else {
			if (! mode.equals(GameMode.NORMAL)) {
				if (blot <= 8 && board.getBar(color.opposite()) > 0) {
					hit = true;
				}
			}
			for (int point=(blot-1); point >=1; point --) {
				if (! mode.equals(GameMode.NORMAL)) {
					if (board.getPointColor(point).equals(color.opposite()) &&
							(blot-point) <= 9) {
						hit = true;
					}
				}
				else {
					if (board.getPointColor(point).equals(color.opposite()) &&
							(blot-point) <= 9) {
						hit = true;
					}	
				}
			}
		}
		
		// Check is opponent bar checker can hit home checker
		if (isHomePoint(board, color, blot)) {
			if (board.getBar(color.opposite()) > 0) {
				hit = true;
			}
		}
		return hit;
	}
	
	private List<Dice> removeDiceOption(List<Dice> dice, Dice die) {
		List<Dice> copy = new ArrayList<Dice>();
		
		for (Dice d : dice) {
			copy.add(new Dice(d));
		}
		
		copy.remove(die);
		return copy;
	}
	
	private boolean isHomePoint(Board board, Color _color, int point) {
		boolean home_point = false;
		Color color = board.getPointColor(point);
		// If player is top
		if (board.getTopColor().equals(color)) {
			if (point >=19) {
				home_point = true;
			}
		}
		
		// If player is bot
		else {
			if (point <= 6) {
				home_point = true;
			}
		}
		
		return home_point;
	}
	
	private List<GameMove> getPossibleGameMoves(Board board, Color player, List<Dice> dice) {
		List<GameMove> game_moves = new ArrayList<GameMove>();
		List<Dice> no_dup = new ArrayList<Dice>();
		Dice reverse_check;
		
		for (Dice d : dice) {
			// If dice is not already in list
			if (! no_dup.contains(d)) {
				// Do not add dice if its reverse already exists
				// example: 2,3 and 3,2 are the same, no need 
				// to compute both
				if (d.getDice1() > d.getDice2()) {
					reverse_check = new Dice(d.getDice2(), d.getDice1());
					if (! no_dup.contains(reverse_check)) {
						no_dup.add(d);
					}
				}
				else {
					no_dup.add(d);
				}
			}
		}

		// Find game moves for each dice
		for (Dice d : no_dup) {
			findGameMoves(game_moves, new ArrayList<Move>(), board, 
					player, rules.toRolls(d), d);
		}
		
		return game_moves;
	}
	
	private void findGameMoves(List<GameMove> game_moves, List<Move> moved, 
			Board board, Color player, List<Integer> _rolls, Dice dice) {
		// Adds possible game moves to referenced variable, recursive function
		
		List<Integer> single_roll = new ArrayList<Integer>();
		List<Integer> movable = new ArrayList<Integer>();
		List<Integer> reduced_rolls;
		List<Move> new_moved;
		Board new_board;
		List<Integer> point_moves = new ArrayList<Integer>();
		Move move;
		int i;
		int dest;
		List<Integer> rolls = new ArrayList<Integer>();
		for (i=0; i<_rolls.size(); i++) {
			rolls.add(new Integer(_rolls.get(i)));
		}
		
		
		// If no more moves available add game move
		if (rules.getMovablePoints(board, rolls, player).isEmpty()) {
			game_moves.add(new GameMove(moved, dice));
		}
		
		else if (dice.getDice1() != dice.getDice2()){
			for (Integer roll : rolls) {
				reduced_rolls = new ArrayList<Integer>();
				for (i=0; i<rolls.size(); i++) {
					reduced_rolls.add(new Integer(rolls.get(i)));
				}
				reduced_rolls.remove(roll);
				single_roll.clear();
				single_roll.add(roll);
				movable = rules.getMovablePoints(board, single_roll, player);
				for (Integer point : movable) {
					// This is done so we don't evaluate the same board state achieved
					// by the same moves ordered differently 
					/*if (! ((! moved.isEmpty()) &&
							(pointIsFartherAwayThan(board, player, point, moved.get(moved.size()-1).getSource())))) 
					{*/
						point_moves = rules.getAvailablePointMoves(board, point, single_roll, player);
						dest = point_moves.get(0);
						move = new Move(point, dest);
						new_moved = new ArrayList<Move>();
						for (Move m : moved) {
							new_moved.add(new Move(m));
						}
						new_moved.add(move);
						new_board = rules.moveGameBoard(board, move, player);
						findGameMoves(game_moves, new_moved, new_board, player, reduced_rolls, dice);
					
				}
			}
		}
		
		else {
			reduced_rolls = new ArrayList<Integer>();
			for (i=0; i<rolls.size(); i++) {
				reduced_rolls.add(new Integer(rolls.get(i)));
			}
			reduced_rolls.remove(rolls.get(0));
			single_roll.clear();
			single_roll.add(rolls.get(0));
			movable = rules.getMovablePoints(board, single_roll, player);
			
			for (Integer point : movable) {
				// This is done so we don't evaluate the same board state achieved
				// by the same moves ordered differently 
				if (! ((! moved.isEmpty()) &&
						(pointIsFartherAwayThan(board, player, point, moved.get(moved.size()-1).getSource())))) 
				{
					point_moves = rules.getAvailablePointMoves(board, point, single_roll, player);
					dest = point_moves.get(0);
					move = new Move(point, dest);
					new_moved = new ArrayList<Move>();
					for (Move m : moved) {
						new_moved.add(new Move(m));
					}
					new_moved.add(move);
					new_board = rules.moveGameBoard(board, move, player);
					findGameMoves(game_moves, new_moved, new_board, player, reduced_rolls, dice);	
				}
			}
		}
	}
	
	private boolean pointIsFartherAwayThan(Board board, Color player, int point1, int point2) {
		// Evaluates if a point1 is farther away from bearing than point2
		boolean farther = false;
		
		// If player is top
		if (board.getTopColor().equals(player)) {
			if (point1 < point2) {
				farther = true; }
		}
		
		// If player is bot
		else {
			if (point1 > point2) {
				farther = true;}
		}
		return farther;
	
	}
	
	private boolean bothPlayersHomeBound(Board board) {
		// True if both players checkers have passed each other
		Color top = board.getTopColor();
		boolean home_bound = false;
		int point;
		int white_count = 0;
		white_count = white_count + board.getBearing(top);
		
		if (board.getBar(top.opposite()) == 0) {
		
			for (point=24; point>=1; point--) {
				if (board.getPointColor(point).equals(top)) {
					white_count = white_count+ board.getPointAmount(point);
					if (white_count == 15) {
						home_bound = true;
						break;
					}
				}
				else if (board.getPointColor(point).equals(top.opposite())) {
					break;
				}
			}
		}
		
		return home_bound;
	}
	
	private double getHomeBoundPenalty(Board board, Color color) {
		// returns negative number 0 and -1
		double penalty = 0;
		
		// If player is top
		if (board.getTopColor().equals(color)) {
			// penalty for every point that is out of players home
			// the farther away he is the bigger the penalty
			for (int point=1; point<=18; point++) {
				if (board.getPointColor(point).equals(color)) {
					penalty = penalty -(19-point)*board.getPointAmount(point);
				}
			}
		}
		
		// If player is bot
		else {
			for (int point=7; point<=24; point++) {
				if (board.getPointColor(point).equals(color)) {
					penalty = penalty -(point-6)*board.getPointAmount(point);
				}
			}
		}
		penalty = penalty -(25)*board.getBar(color);
		double max_penalty = 25*15;
		// We want penalty to be a value between 0 and -1
		penalty = penalty/max_penalty;
		return penalty;
	}
}







