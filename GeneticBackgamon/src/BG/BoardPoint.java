package BG;

import java.io.Serializable;

import BG.Glossary.Color;

public class BoardPoint implements Serializable {
	// Individual board point on backgammon board. Holds checkers of certain color and amount
	
	private Color color;  // color of stone in this point
	private int amount;	        // amount of stones in this point
	
	public BoardPoint (Color _color, int _amount) {
		color = _color;
		amount = _amount;
	}
	
	public BoardPoint (BoardPoint copy) {
		this.color = copy.getColor();
		this.amount = copy.getAmount();
	}
	
	public int getAmount() {
		return amount;
	}
	
	public Color getColor() {
		return color;
	}
	
	
	public void setPoint(Color _color, int _amount) {
		// Sets the points type and amount of stone occupying it.
		amount = _amount;
		if (amount == 0)
			color = Color.Empty;
		else
			color = _color;
	}
}
