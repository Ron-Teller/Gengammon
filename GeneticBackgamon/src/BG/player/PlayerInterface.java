package BG.player;

import BG.Game;
import BG.Glossary.PlayerType;

public interface PlayerInterface {
	
	public PlayerType getPlayerType();
	public void playTurn(Game game);
}
