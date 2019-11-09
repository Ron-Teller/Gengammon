package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import BG.Dice;
import BG.GameMove;
import BG.Move;

public class GameFileParser {

	private File gameFile;
	
	private List<StringBuilder> retrieveAllGameTexts() throws IOException {
		//  returns text relevant to each game
		BufferedReader br = new BufferedReader(new FileReader(gameFile));
		List<StringBuilder> games = new ArrayList<StringBuilder>();
		String line;
		StringBuilder game_sb;
		
		while ((line = br.readLine()) != null) {
			if (startOfGameText(line)) {
				game_sb = new StringBuilder();
				while (! endOfGameText(line)) {
					if (containsMove(line)) {
						game_sb.append(line);
					}
					line = br.readLine();
				}
				games.add(game_sb);
			}
		}
		br.close();
		return games;
	}
	
	public List<List<GameMove>> getAllGameMoves() throws IOException {
		List<List<GameMove>> all_moves = new ArrayList<>();
		List<StringBuilder> texts = retrieveAllGameTexts();
		List<GameMove> moves;
		for (StringBuilder text : texts) {
			moves = getGameMoves(text);
			if (! moves.isEmpty()) {
				all_moves.add(moves);
			}
		}
		return all_moves;
	}
	
	private List<GameMove> getGameMoves(StringBuilder text) {
		List<GameMove> gmoves = new ArrayList<GameMove>();
		Pattern pattern = Pattern.compile("[1-6]{2}[:]\\s[[0-9],/,*]*\\s?[[0-9],/,*]*\\s?[[0-9],/,*]*\\s?[[0-9],/,*]*");
		Matcher matcher = pattern.matcher(text);
		while (matcher.find() != false) {
			gmoves.add(strToGameMove(matcher.group()));
		}
		gmoves = normalizeGameMoves(gmoves);
		return gmoves;
	}
	
	private GameMove strToGameMove(String str) {
		// Parses string that represents single game move
		GameMove gmove;
		int dice1 = Integer.parseInt(str.substring(0, 1));
		int dice2 = Integer.parseInt(str.substring(1, 2));
		String[] str_moves = str.substring(4).replaceAll("\\*", "").split("\\s");
		List<Move> moves = new ArrayList<Move>();
		Move move;
		String[] str_move;
		for (String s : str_moves) {
			if (s.length() > 1) {
				str_move = s.split("/");
				move = new Move(convertToBoardPosition(Integer.parseInt(str_move[0])),
							convertToBoardPosition(Integer.parseInt(str_move[1])));
				moves.add(move);
			}
		}
		gmove = new GameMove(moves, new Dice(dice1, dice2));
		return gmove;
	}
	
	private List<GameMove> normalizeGameMoves(List<GameMove> gmoves) {
		// Converts file move locations to GameMove standards
		// Both players home base is located at points 24-19
		// according to file, normalize so one player has 
		// homebase 24-10, other 6-1
		
		List<GameMove> normalized = new ArrayList<GameMove>();
		boolean flip = true;
		for (GameMove gmove : gmoves) {
			if (flip) {
				normalized.add(flipMovePositions(gmove));
			} else {
				normalized.add(gmove);
			}
			flip = !flip;
		}
		return normalized;
	}
	
	private GameMove flipMovePositions(GameMove gmove) {
		GameMove flipped;
		List<Move> moves = new ArrayList<Move>();
		for (Move move : gmove.getMoves()) {
			moves.add(flipMove(move));
		}
		flipped = new GameMove(moves, gmove.getDice());
		return flipped;
	}
	
	private Move flipMove(Move move) {
		Move flipped = new Move(oppositePoint(move.getSource()),
						oppositePoint(move.getDest()));
		return flipped;
	}
	
	private int oppositePoint(int point) {
		int opposite = point;
		if (point<=24 && point>=1) {
			if (point<=12) {
				opposite = 13+(12-point);
			} else {
				opposite = 12-(point-13);
			}
		} 
		return opposite;
	}
	
	private int convertToBoardPosition(int point) {
		int position;
		if (point<=24 && point>=1) {
			position = point;
		} else if (point == 25) {
			position = Move.Bar;
		} else {
			position = Move.Bearing;
		}
		return position;
	}
	
	private boolean containsMove(String line) {
		return true;
	}
	
	private boolean startOfGameText(String line) {
		return line.contains("Game");
	}

	private boolean endOfGameText(String line) {
		return line.contains("Wins");
	}

	public void setGameFile(File gameFile) {
		this.gameFile = gameFile;
	}
}
