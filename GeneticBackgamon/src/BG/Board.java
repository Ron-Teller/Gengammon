package BG;

import java.io.Serializable;

import BG.Glossary.Color;
import BG.Glossary.Side;


public class Board implements Serializable{
	

	private BoardPoint[] points;
	private Color top;	// Color of opponents pieces occupying top board
	private Color bot;  // Color of opponents pieces occupying bottom board
	private int white_bar;		// Number of white pieces captured by black player
	private int black_bar;		// Number of black pieces captured by white player
	private int white_bearing;  // Number of white checkers beared off
	private int black_bearing;  // Number of black checkers beared off
	
	public Board(Color _top) {
		points = new BoardPoint[24];
		top = _top;
		bot = _top.opposite();
		white_bar = 0;
		black_bar = 0;
		white_bearing = 0;
		black_bearing = 0;
		initializeBoard(top, bot);
	}
	
	public Board(Board copy) {
		this.top = copy.getTopColor();
		this.bot = copy.getBotColor();
		this.white_bar = copy.getWhiteBar();
		this.black_bar = copy.getBlackBar();
		this.white_bearing = copy.getWhiteBearing();
		this.black_bearing = copy.getBlackBearing();
		this.points = copy.getPointsCopy();
	}
	
	public void clear() {
		for (int i=1; i<=24; i++) {
			setPoint(i, Color.Empty, 0);
		}
		white_bar = 0;
		white_bearing = 0;
		black_bearing = 0;
		black_bar = 0;
	}
	
	private void initializeBoard(Color top, Color bot) {
		// Setup starting board positions.
		int index;
		for (index=0; index<24; index++) {
			points[index] = new BoardPoint(Color.Empty, 0);
		}
		
		// Setup top pieces
		points[18].setPoint(top, 5); 
		points[16].setPoint(top, 3); 
		points[11].setPoint(top, 5); 
		points[0].setPoint(top, 2); 
		
		// Setup bot pieces
		points[5].setPoint(bot, 5); 
		points[7].setPoint(bot, 3); 
		points[12].setPoint(bot, 5); 
		points[23].setPoint(bot, 2); 
	}
	
	public BoardPoint[] getPointsCopy() {
		// make new deep copy
		int index = 0;
		BoardPoint[] copy = new BoardPoint[24];
		for (index=0; index<24; index++) {
			copy[index] = new BoardPoint(this.points[index]);
		}
		return copy;
	}
	
	
	public Color getTopColor() {
		return top;
	}
	
	public Color getBotColor() {
		return bot;
	}
	
	public int getBar(Color color) {
		int bar = (color.equals(Color.White)) ? getWhiteBar() : getBlackBar();
		return bar;
	}
	
	public int getWhiteBar() {
		return white_bar;
	}
	
	public int getBlackBar() {
		return black_bar;
	}
	
	public int getBearing(Color color) {
		int bearing = (color.equals(Color.White)) ? getWhiteBearing() : getBlackBearing();
		return bearing;
	}
	
	public int getWhiteBearing() {
		return white_bearing;
	}
	
	public int getBlackBearing() {
		return black_bearing;
	}
	
	public void increaseBearing(Color color) {
		// increments bearing of chosen color by 1
		if (color.equals(Color.Black)) 
			black_bearing = black_bearing + 1;
		else
			white_bearing = white_bearing + 1;	
	}
	
	public void increaseBar(Color color) {
		// increments bar of chosen color by 1
		if (color.equals(Color.Black)) 
			black_bar = black_bar + 1;
		else
			white_bar = white_bar + 1;	
	}
	
	public void decreaseBar(Color color) {
		// decreases bar of chosen color by 1
		if (color.equals(Color.Black)) 
			black_bar = black_bar - 1;
		else
			white_bar = white_bar - 1;
	}
	
	public void setPoint(int point, Color color, int amount) {
		points[point-1].setPoint(color, amount);
	}
	
	public int getPointAmount(int point) {
		return points[point-1].getAmount();
	}
	
	public Color getPointColor(int point) {
		return points[point-1].getColor();
	}
	
	public void print() {
		String string = "";
		for (int i=13; i<=24; i++) {
			string += getPointAmount(i)+getPointColor(i).toString()+" ";
			if (i == 18)
				string += "|| ";
		}
		System.out.println(string);
		string = "";
		for (int i=12; i>=1; i--) {
			string += getPointAmount(i)+getPointColor(i).toString()+" ";
			if (i == 7)
				string += "|| ";
		}
		string += "\n";
		string += "White bar: "+getBar(Color.White) +
				" Black bar: "+getBar(Color.Black) +
				" White bearing: "+getBearing(Color.White) + 
				" Black bearing: "+getBearing(Color.Black);		
		System.out.println(string);
	}
	
	public boolean same(Board other) {
		boolean same = true;
		
		for (int i=1; i<=24; i++) {
			if (getPointAmount(i) != other.getPointAmount(i) ||
					getPointColor(i) != other.getPointColor(i)) {
				return false;
			}
		}
		
		if (getBar(Color.White) != other.getBar(Color.White)) {
			same = false;
		} else if (getBar(Color.Black) != other.getBar(Color.Black)) {
			same = false;
		} else if (getBearing(Color.White) != other.getBearing(Color.White)) {
			same = false;
		} else if (getBearing(Color.Black) != other.getBearing(Color.Black)) {
			same = false;
		}
		
		return same;
		
	}
	
	public void setTopBar(int bar) {
		if (getTopColor().equals(Color.White)) {
			white_bar = bar;
		} else {
			black_bar = bar;
		}
	}
	
	public void setBotBar(int bar) {
		if (getBotColor().equals(Color.White)) {
			white_bar = bar;
		} else {
			black_bar = bar;
		}
	}
	
	public void setTopBearing(int bearing) {
		if (getTopColor().equals(Color.White)) {
			white_bearing = bearing;
		} else {
			black_bearing = bearing;
		}
	}
	
	public void setBotBearing(int bearing) {
		if (getBotColor().equals(Color.White)) {
			white_bearing = bearing;
		} else {
			black_bearing = bearing;
		}
	}	
	
	public void setWhiteBar(int bar) {
		white_bar = bar;
	}
	
	public void setBlackBar(int bar) {
		black_bar = bar;
	}
	
	public void setWhiteBearing(int bearing) {
		white_bearing = bearing;
	}
	
	public void setBlackBearing(int bearing) {
		black_bearing = bearing;
	}	
	
	public Side getColorSide(Color player) {
		return (top.equals(player)) ? Side.Top : Side.Bottom;
	}
	
}
