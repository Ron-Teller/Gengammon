package BG.controller;

import BG.Dice;
import BG.Glossary;
import BG.Glossary.Color;
import BG.Glossary.Side;

public class Converter {

	public Glossary.Side convertTo(Side side) {
		return (side.equals(Side.Top)) ? 
				Glossary.Side.Top : Glossary.Side.Bottom; 
	}
	
	public Glossary.Color convertTo(Color color) {
		return (color != null && color.equals(Color.White)) ? 
				Glossary.Color.White : Glossary.Color.Black;
	}
	
	public view.Dice convertTo(Dice dice) {
		return new view.Dice(dice.getDice1(), dice.getDice2());
	}
	
	public Color convertFrom(Glossary.Color color) {
		return (color.equals(Glossary.Color.White)) ? 
				Color.White : Color.Black;
	}
	
	public Side convertFrom(Glossary.Side side) {
		return (side.equals(Glossary.Side.Top)) ? 
				Side.Top :Side.Bottom; 	
	}
	
	public Dice convertFrom(view.Dice dice) {
		return new Dice(dice.getDice1(), dice.getDice2());
	}
}
