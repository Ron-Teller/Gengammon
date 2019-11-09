package BG.player;

import BG.Game;
import BG.Glossary.PlayerType;



public class AIPlayer implements PlayerInterface {

	PlayerType type = PlayerType.AI;
	
	@Override
	public PlayerType getPlayerType() {
		return type;
	}

	@Override
	public void playTurn(Game game) {

	}

}
