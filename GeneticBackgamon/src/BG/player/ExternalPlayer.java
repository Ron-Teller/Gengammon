package BG.player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import BG.Dice;
import BG.Game;
import BG.GameMove;
import BG.Glossary.Color;
import BG.Glossary.PlayerType;
import BG.Move;


public class ExternalPlayer implements PlayerInterface {

	PlayerType type = PlayerType.External;
	private String file;
	Game game;
	
	public ExternalPlayer(String _file) {
		file = _file;
	}
	
	@Override
	public PlayerType getPlayerType() {
		return type;
	}

	@Override
	public void playTurn(Game _game) {
		// Wait and read move from file
		game = _game;
		new Thread(new Runnable() {
		    public void run() {		
				GameMove gmove = readGameMoveFromFile(game.getCurrentPlayerColor());
				game.movePlayer(gmove.getMoves(), gmove.getDice());
		    }
		}).start();		
	}
	
	private GameMove readGameMoveFromFile(Color player) {
		// All lines except last should indicate move source and destination
		// Two last lines should be dice picked
		// File should empty file after reading game move
		GameMove gmove = null;
		List<Move> moves = new ArrayList<Move>();
		List<String> game_move = new ArrayList<String>();
		boolean got_move = false;
		
		// Get moves from file
		while (got_move == false) {
			// Try to read file. If its not empty then read move
			 try
			 {
			   BufferedReader reader = new BufferedReader(new FileReader(file));
			   String line;
			   while ((line = reader.readLine()) != null)
			   {
				  game_move.add(line);
			   }
			   reader.close();
			   
			   // If file is not empty then check if players moves are written
			   // since both players moves are written to same file
			   if (! game_move.isEmpty()) {
				   // If players move is written and not previous
				   if (player.toString().equals(game_move.get(0))) {
					   // erase file content
					   PrintWriter writer = new PrintWriter(file);
					   writer.print("");
					   writer.close();
					   
					   game_move.remove(0);
					   got_move = true;
				   }
				   else {
					   waitForMoveWrite();	
				   }
			   }
			   
			   // File is empty move has not yet been written
			   else {
				   waitForMoveWrite();	
			   }
			   
			 }
			 catch (Exception e)
			 {
			     // Move is being written to file
				 // Try reading later
				 waitForMoveWrite();	
			 }
	    }
		
		int src;
		int dest;
		Move move;
		// Get player moves
		for (int loop=0; loop<(game_move.size()-2)/2; loop++) {
			src = Integer.parseInt(game_move.get(loop*2));
			dest = Integer.parseInt(game_move.get(loop*2+1));
			move = new Move(src, dest);
			moves.add(move);
		}
		
		// Get player dice
		int die1 = Integer.parseInt(game_move.get(game_move.size()-2));
		int die2 = Integer.parseInt(game_move.get(game_move.size()-1));
		Dice dice = new Dice(die1, die2);
		gmove = new GameMove(moves, dice);
		
		return gmove;
	}

	private void waitForMoveWrite() {
		 try {
			    Thread.sleep(500);             
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
	}	
	
}


