package algorithm;

import BG.ArtificialIntelligence;
import BG.ArtificialIntelligence.Algorithm;
import BG.Board;
import BG.Game;
import BG.GameMove;
import BG.GeneticArtificialIntelligence;
import BG.Glossary.Color;
import BG.Glossary.GameMode;
import BG.player.HumanPlayer;

public class Dueler {

	public void duel(NeuralNetwork nn1, NeuralNetwork nn2, int iterations) {
		Color topPlayerColor = Color.White;
		HumanPlayer topPlayer = new HumanPlayer();
		HumanPlayer botPlayer = new HumanPlayer();
		Color firstMove = Color.White;
		GameMode mode = GameMode.NORMAL;
		ArtificialIntelligence ai = null;
		String output = null;
		GeneticArtificialIntelligence gai1 = new GeneticArtificialIntelligence(Algorithm.Genetic, 1, GameMode.NORMAL, nn1);
		GeneticArtificialIntelligence gai2 = new GeneticArtificialIntelligence(Algorithm.Genetic, 1, GameMode.NORMAL, nn2);		
		Game game = new Game(topPlayerColor, topPlayer, botPlayer, 
				firstMove, mode, ai, output);	
		int moveCount;
		Board board;
		Color player;
		GameMove gameMove;
		int firstWinCount = 0;
		int secondWinCount = 0;
		for (int i = 0; i < iterations; i++) {
			moveCount = i;
			game.newGame();
			game.startGame();
			while (!game.hasEnded()) {
				board = game.getBoard();
				player = game.getCurrentPlayerColor();
				moveCount++;
				if (moveCount % 2 == 0) {
					gameMove = gai1.getBestGameMove(board, player, mode,
							game.getDiceOptions(player),
							game.getDiceOptions(player.opposite()));
				} else {
					gameMove = gai2.getBestGameMove(board, player, mode,
							game.getDiceOptions(player),
							game.getDiceOptions(player.opposite()));

				}
				game.movePlayer(gameMove.getMoves(), gameMove.getDice());
			}
			if (moveCount%2 == 0) {
				firstWinCount ++;
			} else {
				secondWinCount ++;
			}
			System.out.println("Winner is: "
					+ ((moveCount % 2 == 0) ? "first" : "second"));
		}
		System.out.println();
		System.out.println("Wins: "+firstWinCount+"-"+secondWinCount);
	}
	
	public void duel(NeuralNetwork nn1, int iterations) {
		Color topPlayerColor = Color.White;
		HumanPlayer topPlayer = new HumanPlayer();
		HumanPlayer botPlayer = new HumanPlayer();
		Color firstMove = Color.White;
		GameMode mode = GameMode.NORMAL;
		ArtificialIntelligence ai = new ArtificialIntelligence(Algorithm.Minimax, 1, mode);
		String output = null;
		GeneticArtificialIntelligence gai1 = new GeneticArtificialIntelligence(Algorithm.Genetic, 1, GameMode.NORMAL, nn1);
		Game game = new Game(topPlayerColor, topPlayer, botPlayer, 
				firstMove, mode, ai, output);	
		int moveCount;
		Board board;
		Color player;
		GameMove gameMove;
		int firstWinCount = 0;
		int secondWinCount = 0;
		for (int i = 0; i < iterations; i++) {
			moveCount = i;
			game.newGame();
			game.startGame();
			while (!game.hasEnded()) {
				board = game.getBoard();
				player = game.getCurrentPlayerColor();
				moveCount++;
				if (moveCount % 2 == 0) {
					gameMove = gai1.getBestGameMove(board, player, mode,
							game.getDiceOptions(player),
							game.getDiceOptions(player.opposite()));
				} else {
					gameMove = game.calculateNextGameMove();

				}
				game.movePlayer(gameMove.getMoves(), gameMove.getDice());
			}
			if (moveCount%2 == 0) {
				firstWinCount ++;
			} else {
				secondWinCount ++;
			}
			System.out.println("Winner is: "
					+ ((moveCount % 2 == 0) ? "genetic" : "alphabeta"));
		}
		System.out.println();
		System.out.println("Wins: <gen:ab>"+firstWinCount+"-"+secondWinCount);
	}
}
