package BG;

import java.io.Serializable;

public class Dice implements Serializable{
	private int dice1;
	private int dice2;
	
	public Dice(int d1, int d2) {
		dice1 = d1;
		dice2 = d2;
	}
	
	public Dice(Dice d) {
		dice1 = d.getDice1();
		dice2 = d.getDice2();
	}
	
	public int getDice1() {
		return dice1;
	}
	
	public int getDice2() {
		return dice2;
	}
    public String toString() {
        String string = ("("+dice1+","+dice2+")");
        return string;
    }
    
    public boolean same(Dice other) {
    	return (dice1 == other.dice1 && dice2 == other.dice2); 
    }
    
    @Override
    public boolean equals(Object _obj) {
    	Dice obj = (Dice) _obj;
    	return (dice1 == obj.dice1 && dice2 == obj.dice2);
    }
}
