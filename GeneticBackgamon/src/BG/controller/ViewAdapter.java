package BG.controller;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import BG.Dice;
import view.Glossary;
import BG.Glossary.Color;
import BG.Glossary.Side;
import view.BackGammonView;


public class ViewAdapter implements ViewInterface {

	private BackGammonView view;
	
	public ViewAdapter(BackGammonView _view) {
		view = _view;
	}
	
	private Glossary.Side convertTo(Side side) {
		if (side != null) {
			return (side.equals(Side.Top)) ? 
					Glossary.Side.Top : Glossary.Side.Bottom; 
		}
		else
			return null;
	}
	
	
	private Glossary.Color convertTo(Color color) {
		if (color != null) {
			return (color.equals(Color.White)) ? 
					Glossary.Color.White : Glossary.Color.Black;
		}
		else
			return null;
	}
	
	private view.Dice convertTo(Dice dice) {
		if (dice != null) {
			return new view.Dice(dice.getDice1(), dice.getDice2());
		}
		else
			return null;
	}
	
	private Side convertFrom(Glossary.Side side) {
		if (side != null) {
			return (side.equals(Glossary.Side.Top)) ? 
					Side.Top :Side.Bottom; 	
		} else
			return null;
	}
	
	private Dice convertFrom(view.Dice dice) {
		return new Dice(dice.getDice1(), dice.getDice2());
	}
	
	@Override
	public void start() {
		view.start();
	}
	
	@Override
    public void selectMinimax() {
    	view.selectMinimax();
    }
	
    @Override
    public void selectAlphabeta() {
    	view.selectAlphabeta();
    }
    
    @Override
    public void clearLog() {
    	view.clearLog();
    }
	
    @Override
    public void selectFirstRoll(Color color) {
    	view.selectFirstRoll(convertTo(color));
    }
    
	@Override
	public void setDepth(int depth) {
		view.setDepth(depth);
	}
	
	@Override
	public void subscribeMoveForMeEvent(ActionListener listener) {
		view.subscribeMoveForMeEvent(listener);
	}
	
	@Override
	public void disableMoveForeMe() {
		view.disableMoveForeMe();
	}
	
	@Override
	public void enableMoveForeMe() {
		view.enableMoveForeMe();
	}
	
	@Override
	public void subscribeDepthChange(ActionListener listener) {
		view.subscribeDepthChange(listener);
	}
	
	@Override
	public void subscribeMinimaxEvent(ActionListener listener) {
		view.subscribeMinimaxEvent(listener);
	}
	
	@Override
	public void subscribeAlphabetaEvent(ActionListener listener) {
		view.subscribeAlphaBetaEvent(listener);
	}
	
	@Override
	public void subscribeNewGameEvent(ActionListener listener) {
		view.subscribeNewGameEvent(listener);
	}
	
	@Override
	public void subscribeRestartEvent(ActionListener listener) {
		view.subscribeRestartEvent(listener);
	}
	
	@Override
	public void notifyPlayerWin(Color color) {
		view.notifyPlayerWin(convertTo(color));
	}
	
	@Override
	public int getDepth() {
		return view.getDepth();
	}
	
	@Override
	public void disableDepth() {
		view.disableDepth();
	}
	
	@Override
	public void enableDepth() {
		view.enableDepth();
	}
	
	@Override
	public void setBearing(Side side, Color color, int amount) {
		view.setBearing(convertTo(side), convertTo(color), amount);
	}
    
	@Override
	public Dice getSelectedDice(Color color) {
		return convertFrom(view.getSelectedDice(convertTo(color)));
	}
    
	@Override
	public void setDiceOptions(List<Dice> dice, Color color) {
		List<view.Dice> list = new ArrayList<view.Dice>();
		for (Dice die : dice) {
			list.add(convertTo(die));
		}
		view.setDiceOptions(list, convertTo(color));
	}
	
	@Override
	public void enableRoll(Color color) {
		view.enableRoll(convertTo(color));
	}
	
	@Override
	public void enableUndo() {
		view.enableUndo();
	}
	
	@Override
	public void enableMove() {
		view.enableMove();
	}
	
	@Override
	public void subscribePointEvent(ActionListener listener) {
		view.subscribePointEvent(listener);
	}
	
	@Override
	public void subscribeBearingEvent(ActionListener listener) {
		view.subscribeBearingEvent(listener);
	}

	@Override
	public void subscribeExitEvent(ActionListener listener) {
		view.subscribeExitEvent(listener);
	}

	@Override
	public void subscribeRollEvent(ActionListener listener) {
		view.subscribeRollEvent(listener);
	}

	@Override
	public void subscribeRightClickEvent(ActionListener listener) {
		view.subscribeRightClickEvent(listener);
	}

	@Override
	public void subscribeUndoMoveEvent(ActionListener listener) {
		view.subscribeUndoMoveEvent(listener);
	}

	@Override
	public void subscribeMoveButton(ActionListener listener) {
		view.subscribeMoveButton(listener);
	}

	@Override
	public void subscribeBarEvent(ActionListener listener) {
		view.subscribeBarEvent(listener);
	}

	@Override
	public void setStatusBar(String status) {
		view.setStatusBar(status);
	}

	@Override
	public void addLogInfo(String info) {
		view.addLogInfo(info);
	}

	@Override
	public void disableRoll(Color color) {
		view.disableRoll(convertTo(color));
	}

	@Override
	public void disableUndo() {
		view.disableUndo();
	}

	@Override
	public void disableMove() {
		view.disableMove();
	}

	@Override
	public void setPointCheckers(int point, Color color, int amount) {
		view.setPointCheckers(point, convertTo(color), amount);
	}

	@Override
	public void setBar(Side side, Color color, int amount) {
		view.setBar(convertTo(side), convertTo(color), amount);
	}

	@Override
	public void setBotBearing(Color color, int amount) {
		view.setBotBearing(convertTo(color), amount);
	}

	@Override
	public void setTopBearing(Color color, int amount) {
		view.setTopBearing(convertTo(color), amount);
	}

	@Override
	public void markPoint(int point) {
		view.markPoint(point);
	}

	@Override
	public void removeAllMarks() {
		view.removeAllMarks();
	}

	@Override
	public void hightlightLastPointChecker(int point, Color color) {
		view.hightlightLastPointChecker(point, convertTo(color));
	}

	@Override
	public void removeHighlightFromPoint(int point, Color color) {
		view.removeHighlightFromPoint(point, convertTo(color));
	}

	@Override
	public void setDice(int d1, int d2) {
		view.setDice(d1, d2);
	}

	@Override
	public void removeDice() {
		view.removeDice();
	}
	
	@Override
	public int getLastPointClicked() {
		return view.getLastPointClicked();
	}

	@Override
	public Side getLastBarClicked() {
		return convertFrom(view.getLastBarClicked());
	}

	@Override
    public void hightlightLastBarChecker(Side side, Color color) {
		view.hightlightLastBarChecker(convertTo(side), convertTo(color));
	}
	
	@Override
	public void removeHighlightFromBar(Side side, Color color) {
		view.removeHighlightFromBar(convertTo(side), convertTo(color));
	}
	
}


