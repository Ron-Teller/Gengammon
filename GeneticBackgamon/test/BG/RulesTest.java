import static org.junit.Assert.assertEquals;

import org.junit.Test;

import BG.Board;
import BG.Dice;
import BG.Glossary.Color;
import BG.RulesNew;


public class RulesTest {

	Color top = Color.Black;
	Color bot = top.opposite();
	
	private Board arrayToBoard(int[] white, int[] black) {
		Board board = new Board(top);
		board.clear();

		for (int i=1; i<=24; i++) {
			if (white[i-1] > 0) {
				board.setPoint(i, Color.White, white[i-1]);
			} else if (black[i-1] > 0) {
				board.setPoint(i, Color.Black, black[i-1]);
			}
		}
		board.setBlackBar(black[24]);
		board.setBlackBearing(black[25]);
		board.setWhiteBar(white[24]);
		board.setWhiteBearing(white[25]);
		return board;
	}
	
	private boolean isPossibleBoardState(Board initial, Board moved, Dice dice) {
		for (Board board : RulesNew.getAllPossibleBoards(initial, bot, dice)) {
			if (moved.same(board)) {
				return true;
			}
		}
		return false;
	}
	
	@Test
	public void board1() throws Exception {
		int[] whiteInit = {0,0,0,0,0,5, 0,3,1,0,0,0, 3,0,0,0,0,0, 0,0,0,0,0,2, 1,0};
		int[] blackInit = {1,0,0,0,0,0, 1,0,0,0,0,4, 0,0,1,0,3,0, 5,0,0,0,0,0, 0,0};
		
		int[] whiteEx = {0,0,0,0,0,5, 0,3,1,0,0,0, 3,0,1,0,0,0, 0,0,0,1,0,1, 0,0};
		int[] blackEx = {1,0,0,0,0,0, 1,0,0,0,0,4, 0,0,0,0,3,0, 5,0,0,0,0,0, 1,0};	
		
		Dice dice = new Dice(3,3);
		Board initial = arrayToBoard(whiteInit, blackInit);
		Board moved = arrayToBoard(whiteEx, blackEx);
		assertEquals(isPossibleBoardState(initial, moved, dice), true);
	}
	
	@Test
	public void board2() throws Exception {
		int[] whiteInit = {0,0,0,2,0,5, 0,2,0,0,0,0, 4,0,0,1,0,0, 0,0,0,0,0,0, 1,0};
		int[] blackInit = {0,1,0,0,0,0, 2,0,0,0,0,2, 0,0,0,0,3,0, 5,0,0,0,0,2, 0,0};
		
		int[] whiteEx = {0,0,0,3,0,4, 0,2,0,0,2,0, 2,0,0,1,0,0, 0,0,0,0,1,0, 0,0};
		int[] blackEx = {0,1,0,0,0,0, 2,0,0,0,0,2, 0,0,0,0,3,0, 5,0,0,0,0,2, 0,0};		
		
		Dice dice = new Dice(2,2);
		Board initial = arrayToBoard(whiteInit, blackInit);
		Board moved = arrayToBoard(whiteEx, blackEx);
		assertEquals(isPossibleBoardState(initial, moved, dice), true);
	}
	
	@Test
	public void board3() throws Exception {
		int[] whiteInit = {0,0,0,3,0,4, 0,2,0,0,2,0, 2,0,0,0,0,0, 0,0,0,0,1,0, 1,0};
		int[] blackInit = {0,1,0,0,0,0, 1,0,0,0,0,2, 0,0,0,1,3,0, 5,0,0,0,0,2, 0,0};
		
		int[] whiteEx = {0,1,0,2,0,4, 1,2,0,0,1,0, 2,0,0,0,0,0, 0,0,0,0,2,0, 0,0};
		int[] blackEx = {0,0,0,0,0,0, 0,0,0,0,0,2, 0,0,0,1,3,0, 5,0,0,0,0,2, 2,0};		
		Dice dice = new Dice(2,2);
		Board initial = arrayToBoard(whiteInit, blackInit);
		Board moved = arrayToBoard(whiteEx, blackEx);
		assertEquals(isPossibleBoardState(initial, moved, dice), true);	
	}
	
	@Test
	public void board4() throws Exception {
		int[] whiteInit = {0,0,0,2,0,3, 0,2,0,0,0,0, 2,0,0,0,0,0, 0,2,0,0,2,0, 2,0};
		int[] blackInit = {1,0,0,0,0,0, 0,0,0,0,1,3, 0,0,0,0,2,0, 2,0,2,2,0,2, 0,0};
		
		int[] whiteEx = {0,0,0,2,0,3, 0,2,0,0,2,0, 0,0,0,0,0,0, 0,2,0,0,4,0, 0,0};
		int[] blackEx = {1,0,0,0,0,0, 0,0,0,0,0,3, 0,0,0,0,2,0, 2,0,2,2,0,2, 1,0};		
		Dice dice = new Dice(2,2);
		Board initial = arrayToBoard(whiteInit, blackInit);
		Board moved = arrayToBoard(whiteEx, blackEx);
		assertEquals(isPossibleBoardState(initial, moved, dice), true);	
	}
	
	@Test
	public void board5() throws Exception {
		int[] whiteInit = {0,0,0,2,1,3, 0,2,0,0,1,0, 0,0,0,0,0,0, 0,2,0,0,3,0, 1,0};
		int[] blackInit = {2,0,0,0,0,0, 0,0,0,0,0,2, 0,0,0,0,2,1, 2,0,2,2,0,2, 0,0};
		
		int[] whiteEx = {0,0,0,2,1,3, 0,2,0,0,1,0, 0,0,0,0,0,3, 0,3,0,0,0,0, 0,0};
		int[] blackEx = {2,0,0,0,0,0, 0,0,0,0,0,2, 0,0,0,0,2,0, 2,0,2,2,0,2, 1,0};		
		Dice dice = new Dice(5,5);
		Board initial = arrayToBoard(whiteInit, blackInit);
		Board moved = arrayToBoard(whiteEx, blackEx);
		assertEquals(isPossibleBoardState(initial, moved, dice), true);
	}
	
	@Test
	public void board6() throws Exception {
		int[] whiteInit = {0,0,0,0,0,4, 2,3,0,0,0,0, 2,0,0,1,0,0, 0,1,0,0,0,1, 1,0};
		int[] blackInit = {0,0,0,0,2,0, 0,0,1,0,0,0, 0,2,0,0,4,0, 4,0,2,0,0,0, 0,0};
		
		int[] whiteEx = {1,0,0,0,0,4, 2,3,0,0,0,0, 2,0,0,0,0,0, 0,2,0,0,0,1, 0,0};
		int[] blackEx = {0,0,0,0,2,0, 0,0,1,0,0,0, 0,2,0,0,4,0, 4,0,2,0,0,1, 0,0};		
		Dice dice = new Dice(5,5);
		Board initial = arrayToBoard(whiteInit, blackInit);
		Board moved = arrayToBoard(whiteEx, blackEx);
		assertEquals(isPossibleBoardState(initial, moved, dice), true);	
	}
	
	@Test
	public void board7() throws Exception {
		int[] whiteInit = {6,3,2,2,0,0, 0,0,0,0,0,0, 0,0,0,0,0,0, 0,0,0,0,1,0, 1,0};
		int[] blackInit = {0,0,0,0,0,0, 0,0,0,0,0,0, 0,0,0,0,0,0, 0,0,0,2,0,3, 0,10};
		
		int[] whiteEx = {6,3,2,2,0,0, 0,0,1,0,0,0, 0,0,0,0,0,0, 0,0,0,0,1,0, 0,0};
		int[] blackEx = {0,0,0,0,0,0, 0,0,0,0,0,0, 0,0,0,0,0,0, 0,0,0,2,0,3, 0,10};		
		Dice dice = new Dice(4,4);
		Board initial = arrayToBoard(whiteInit, blackInit);
		Board moved = arrayToBoard(whiteEx, blackEx);
		assertEquals(isPossibleBoardState(initial, moved, dice), true);
	}
}
