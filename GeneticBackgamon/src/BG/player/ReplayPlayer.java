package BG.player;

import BG.Game;
import BG.Glossary.PlayerType;

public class ReplayPlayer implements PlayerInterface{

	private PlayerType type = PlayerType.REPLAY;
	
	@Override
	public PlayerType getPlayerType() {
		return type;
	}

	@Override
	public void playTurn(Game game) {
		// TODO Auto-generated method stub
		
	}

}
