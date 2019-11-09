package BG;

import java.util.Comparator;

public class DiceComparator implements Comparator<Dice> {
    @Override
    public int compare(Dice dice1, Dice dice2) {
        return (dice1.getDice1()*6+dice1.getDice2()) - 
        	   (dice2.getDice1()*6+dice2.getDice2());
    }
}