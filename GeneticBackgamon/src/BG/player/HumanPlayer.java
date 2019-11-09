package BG.player;

import BG.Game;
import BG.Glossary.PlayerType;

public class HumanPlayer implements PlayerInterface {
	
	private PlayerType type = PlayerType.Human;
	
	public HumanPlayer() {
		
	}
	
	@Override
	public PlayerType getPlayerType() {
		return type;
	}

	@Override
	public void playTurn(Game game) {
		//Do nothing

	}

}
