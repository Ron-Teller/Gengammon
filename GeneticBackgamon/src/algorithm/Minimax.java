package algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.Collections.reverseOrder;
import BG.Board;
import BG.Dice;
import BG.GameMove;
import BG.Glossary.Color;
import BG.Move;
import BG.RulesNew;
import algorithm.util.Common;

public class Minimax {

	protected RulesNew rules;
	protected int maxDepth;
	protected GameMove gmove = null;
	protected NeuralNetwork heuristicNeuralNetwork;
	protected int branchingFactor;
	protected int rootBranchingFactor;
	protected Color player;
	protected List<List<Dice>> diceUsedInDepth = new ArrayList<List<Dice>>();
	protected Dice playerDice;

	
	
	public GameMove findNextBestMove(Board board, Color currentTurn, Dice playerDice) {
		this.player = currentTurn;
		this.playerDice = playerDice;
		setDiceUsedInDepths();
		alphabeta(0, board, currentTurn, null, null);
		if (gmove == null) {
//			System.out.println("GOT ONTHING");
			gmove = new GameMove(new ArrayList<Move>(), playerDice);
		} 
//		System.out.println("Gmove:"+gmove);
		return gmove;		
	}
	
	protected void setDiceUsedInDepths() {
		List<Dice> depthDice;
		for (int depth=0; depth<maxDepth+1; depth++) {
			depthDice = new ArrayList<Dice>();
			for (int branch=0; branch<branchingFactor; branch++) {
				depthDice.add(generateRandomDice());
			}
			diceUsedInDepth.add(depthDice);
		}
	}
	
	protected double alphabeta(int depth, Board board, Color currentTurn,
			Double _alpha, Double _beta) {
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

		double childScore;
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
				childScore = alphabeta(depth+1, newBoard, 
						currentTurn.opposite(), alpha, beta);
				
				// Get max child score
				if (score == null) {
					score = childScore;
					best = gm;
				} else {
					if (childScore >= score) {
						score = childScore;
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
				newBoard = rules.moveBoard(board, gm.getMoves(), currentTurn);
				childScore = alphabeta(depth+1, newBoard, 
						currentTurn.opposite(), alpha, beta);
				
				// Get min child score
				if (score == null) {
					score = childScore;
					best = gm;
				}
				else {
					if (childScore < score) {
						score = childScore;
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
		
		if (depth == 0) {
			gmove = best;
		}
				
		return score;
	}
	
	protected List<GameMove> getPossibleGameMovesForDepth(Board board, Color currentTurn, int depth) {
		List<GameMove> possibleGameMoves = new ArrayList<GameMove>();
		if (depth == 0) {
//			System.out.println("Depth 0::");
			possibleGameMoves = getNBestGameMovesForDice(rootBranchingFactor, board, currentTurn, playerDice);
//			System.out.println("possibile 0: "+possibleGameMoves.size());
		} else {
			for (Dice dice : diceUsedInDepth.get(depth)) {
//				possibleGameMoves.add(getBestPossibleGameMoveForDice(board, currentTurn, dice));
				possibleGameMoves.addAll(getNBestGameMovesForDice(1, board, currentTurn, dice));
			}
		}
		return possibleGameMoves;
	}
	
	protected List<GameMove> getPossibleGameMovesForDepth2(Board board, Color currentTurn, int depth) {
		List<GameMove> possibleGameMoves = new ArrayList<GameMove>();
		if (depth == 0) {
			possibleGameMoves = getNBestGameMovesForDice(rootBranchingFactor, board, currentTurn, playerDice);
		} else {
			for (Dice dice : allPossibleDice()) {
				possibleGameMoves.addAll(getNBestGameMovesForDice(1, board, currentTurn, dice));
			}
			possibleGameMoves = getNBestGameMoves(branchingFactor, board, currentTurn, possibleGameMoves);
		}
		return possibleGameMoves;
	}
	
	protected List<Dice> allPossibleDice() {
		List<Dice> allDice = new ArrayList<Dice>();
		for (int i=1; i<=6; i++) {
			for (int j=i; j<=6; j++) {
				allDice.add(new Dice(i,j));
			}
		}
		return allDice;
	}
	
	protected List<GameMove> getNBestGameMovesForDice(int n, Board board, Color currentTurn, Dice dice) {
//		System.out.println("=========== DICE IS: "+dice);
		return rules.getPossibleGameMoves(currentTurn, board, dice)
				.stream()
				.collect(Collectors.toMap(c -> c, c->
				staticEvalutionFunction(rules.moveBoard(currentTurn, board, (GameMove)c), currentTurn) ))
				.entrySet().stream().sorted(reverseOrder(Map.Entry.comparingByValue()))
//				.peek(c -> {rules.moveBoard(currentTurn, board, (GameMove)c.getKey()).print();
//						System.out.println("SCORE ^^: "+c.getValue()+"  "+currentTurn);})
				.limit(n)
				.map(c -> c.getKey()).collect(Collectors.toList());
	}
	
	public List<GameMove> getNBestGameMoves(int n, Board board, Color currentTurn, List<GameMove> gameMoves) {
		return gameMoves
				.stream()
				.collect(Collectors.toMap(c -> c, c->
				staticEvalutionFunction(rules.moveBoard(currentTurn, board, (GameMove)c), currentTurn) ))
				.entrySet().stream().sorted(reverseOrder(Map.Entry.comparingByValue()))
//				.peek(c -> {rules.moveBoard(currentTurn, board, (GameMove)c.getKey()).print();
//						System.out.println("SCORE ^^: "+c.getValue()+"  "+currentTurn);})
				.limit(n)
				.map(c -> c.getKey()).collect(Collectors.toList());
	}
	
	protected GameMove getBestPossibleGameMoveForDice(Board board, Color currentTurn, Dice dice) {
		List<GameMove> possibleGameMoves = rules.getPossibleGameMoves(currentTurn, board, dice);
		return calculateBestGameMoveByHeuristic(possibleGameMoves, board, currentTurn);
	}
	
	protected GameMove calculateBestGameMoveByHeuristic(List<GameMove> gameMoves, Board board, Color currentTurn) {
		GameMove bestGameMove = gameMoves.stream().collect(Collectors.toMap(c -> c, c->
			staticEvalutionFunction(rules.moveBoard(currentTurn, board, (GameMove)c), currentTurn) ))
			.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
		return bestGameMove;
	}
	
	protected double staticEvalutionFunction(Board board, Color currentTurn) {
		Double playersScore = heuristicNeuralNetwork.feed(Common.convertBoardToNNInput(board, currentTurn));
//		Double oponentsScore = heuristicNeuralNetwork.feed(Common.convertBoardToNNInput(board, currentTurn.opposite()));
		return playersScore;
	}

	public final int getDepth() {
		return maxDepth;
	}

	public final void setDepth(int depth) {
		this.maxDepth = depth;
	}

	public final NeuralNetwork getHueristicNeuralNetwork() {
		return heuristicNeuralNetwork;
	}

	public final void setHueristicNeuralNetwork(NeuralNetwork hueristicNeuralNetwork) {
		this.heuristicNeuralNetwork = hueristicNeuralNetwork;
	}

	public final int getBranchingFactor() {
		return branchingFactor;
	}

	public final void setBranchingFactor(int branchingFactor) {
		this.branchingFactor = branchingFactor;
	}

	protected Dice generateRandomDice() {
		Random random = new Random();
		Dice dice = new Dice(random.nextInt(6)+1, random.nextInt(6)+1);
		return dice;
	}

	public final int getRootBranchingFactor() {
		return rootBranchingFactor;
	}

	public final void setRootBranchingFactor(int rootBranchingFactor) {
		this.rootBranchingFactor = rootBranchingFactor;
	}	
}
