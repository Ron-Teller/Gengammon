package io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import BG.ArtificialIntelligence;
import BG.Board;
import BG.Dice;
import BG.Game;
import BG.GameMove;
import BG.ArtificialIntelligence.Algorithm;
import BG.Glossary.Color;
import BG.Glossary.GameMode;
import BG.player.ReplayPlayer;
import algorithm.MoveTest;


public class GameFileConverter {

	private GameFileParser parser;

	public GameFileConverter(GameFileParser parser) {
		super();
		this.parser = parser;
	}
	
	public List<MoveTest> convertFilesToTests(File directory, int maxFiles) {
		List<List<GameMove>> games = getGamesFromDirectory(directory, maxFiles);
		return gamesToTests(games);
		
	}
	
	private List<MoveTest> gamesToTests(List<List<GameMove>> games) {
		
		List<MoveTest> tests = new ArrayList<MoveTest>();
		Color topColor = Color.White;
		Board board = new Board(topColor);
		int firstSrc;
		Color firstMove;
		Game game;
		ReplayPlayer top_player = new ReplayPlayer();
		ReplayPlayer bot_player = new ReplayPlayer();
		GameMode mode = GameMode.REPLAY;
		ArtificialIntelligence ai = new ArtificialIntelligence(Algorithm.Alphabeta, 1, GameMode.NORMAL);
		String output_file = null;
		Board initial;
		Color player;
		Dice dice;
		Board expected;
		MoveTest mv;		
		
		for (List<GameMove> list : games) {
			System.out.println(list.size());
			if (list.size() > 0 && 
					list.get(0).getMoves().size() > 0)
				try {
					{
				firstSrc = list.get(0).getMoves().get(0).getSource();
				firstMove = board.getPointColor(firstSrc);				
				game = new Game(topColor, top_player, bot_player, 
						firstMove, mode, 
						ai, output_file);
				game.setReplay(list);
				game.startGame();
				try {
					for (GameMove gm : list) {
						initial = new Board(game.getBoard());
						player = game.getCurrentPlayerColor();
						dice = new Dice(gm.getDice());
						game.movePlayer(gm.getMoves(), gm.getDice());
						expected = new Board(game.getBoard());
						try {
							mv = new MoveTest(initial, dice, expected, player);
							tests.add(mv);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							break;
						}
					}
				} catch (Exception e) {
					System.out.println("Failed parsing game move");
					e.printStackTrace();
				}
			}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Failed parsing game");
					e.printStackTrace();
				}
		}		
		return tests;
	}
	
	private List<List<GameMove>> getGamesFromDirectory(File directory, int maxFiles) {
		List<List<GameMove>> games = new ArrayList<List<GameMove>>();
		File[] directoryListing = directory.listFiles();
		int fileCount = 0;
		
		if (directoryListing != null) {
			for (File file : directoryListing) {
				if (isValid(file)) {
					parser.setGameFile(file);
					games.addAll(fileToGames(file));
					fileCount ++;
				}
				if (fileCount >= maxFiles) {
					break;
				} 
			}
		}
		return games;
	}
	
	private boolean isValid(File file) {
		return (! file.toString().endsWith(".txt"));
	}
	
	private List<List<GameMove>> fileToGames(File file) {
		try {
			return parser.getAllGameMoves();
		} catch (Exception e) {
			System.out.print("Failed retrieving games from file: "+file.toString());
			e.printStackTrace();
		}
		return new ArrayList<List<GameMove>>();
	}
	
}
