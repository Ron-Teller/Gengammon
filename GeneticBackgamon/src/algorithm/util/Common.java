package algorithm.util;

import BG.Board;
import BG.Glossary.Color;

public class Common {

	public static int[] convertBoardToNNInput(Board board, Color player) {
/*		// first half of array is for player board, second half for enemy board
		int[] input = new int[52];
		Color enemy = player.opposite();
		
		// player board
		for (int i=0; i<24; i++) {
			if (board.getPointColor(i+1).equals(player)) {
				input[i] = board.getPointAmount(i+1);
			}
		}
		input[24] = board.getBar(player);
		input[25] = board.getBearing(player);
		
		// enemy board
		for (int i=26; i<50; i++) {
			if (board.getPointColor(i-25).equals(enemy)) {
				input[i] = board.getPointAmount(i-25);
			}
		}
		input[50] = board.getBar(enemy);
		input[51] = board.getBearing(enemy);
		
//		return input;
*/		return convertBoardToNNInput2(board, player);
	}	
	
	public static int[] convertBoardToNNInput2(Board board, Color player) {
		int[] input = new int[52];
		Color enemy = player.opposite();
		
		if (board.getTopColor().equals(player)) {
			// player board
			input[0] =  board.getBearing(player);
			for (int i=1; i<25; i++) {
				if (board.getPointColor(i).equals(player)) {
					input[i] = board.getPointAmount(i);
				}
			}
			input[25] = 0;
			
			// enemy board
			input[26] = 0;
			for (int i=27; i<51; i++) {
				if (board.getPointColor(i-26).equals(enemy)) {
					input[i] = board.getPointAmount(i-26);
				}
			}
			input[51] = board.getBearing(enemy);
		}
		
		else {
			// player board
			input[0] = 0;
			for (int i=1; i<25; i++) {
				if (board.getPointColor(i).equals(player)) {
					input[i] = board.getPointAmount(i);
				}
			}
			input[25] = board.getBearing(player);
			
			// enemy board
			input[26] = board.getBar(enemy);
			for (int i=27; i<51; i++) {
				if (board.getPointColor(i-26).equals(enemy)) {
					input[i] = board.getPointAmount(i-26);
				}
			}
			input[51] = 0;
		}
		return input;
	}
	
}
