package BG;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

import BG.ArtificialIntelligence.Algorithm;
import BG.Glossary.Color;
import BG.Glossary.GameMode;
import BG.Glossary.GameState;
import BG.Glossary.PlayerType;
import BG.Glossary.Side;
import BG.player.PlayerInterface;

public class Game {
	public static enum GameEvent {GameStart, PlayerStartTurn, PlayerMove, PlayerEndTurn, GameEnd};
	private List<Dice> res_top_dice;
	private List<Dice> res_bot_dice;
	private Color first;
	private Board board;		  // Game board
	private Color top;			  // Color occupying top base
	private PlayerInterface top_player; 
	private PlayerInterface bot_player;
	private GameState state;	  // Current game state
	private Color current_turn;
	private List<Move> current_moves;	  // Current players move choice
	private Dice current_dice;
	String output_path;
	private List<Dice> top_dice;
	private List<Dice> bot_dice;
	private List<GameMove> replay;
	private int replay_move;
	private RulesNew rules;
	GameMode mode;
	int turnCount = 0;
	ArtificialIntelligence ai;
	protected EnumMap<GameEvent, ArrayList<ActionListener>> listeners = 
			new EnumMap<GameEvent, ArrayList<ActionListener>>(GameEvent.class);
	
	public Game(Color _top, PlayerInterface t_type, PlayerInterface b_type, Color _first,
					GameMode _mode, ArtificialIntelligence _ai, String f_path) {
		top = _top;
		top_player = t_type;
		bot_player = b_type;
		state = GameState.Not_Started;
		first = _first;
		current_turn = _first;
		List<Move> current_moves = new ArrayList<Move>();
		top_dice  = new ArrayList<Dice>();
		bot_dice  = new ArrayList<Dice>();
		res_top_dice  = new ArrayList<Dice>();
		res_bot_dice  = new ArrayList<Dice>();		
		board =  new Board(top);
		mode = _mode;
		output_path = f_path;
		ai = _ai;
		init_players_dice();
		initGameFile();
	}
	
	public int getDepth() {
		return ai.getDepth();
	}

	public Algorithm getAlgorithm() {
		return ai.getAlgorithm();
	}
	
	public Side getCurrentPlayerSide() {
		return getColorSide(current_turn);
	}
	
	public void movePlayer(List<Move> moves, Dice dice) {
		// Advance the board for current player. This is the 
		// only function that advances game state
		if (state.equals(GameState.In_Progress)) {
			if ((rules.movesAreValid(getBoard(), getCurrentPlayerColor(), moves) &&
							rollIsValid(dice))) {
				current_moves = moves;
				current_dice = dice;			   // players dice roll for this turn
				removeCurrentPlayerRollOptions(dice);  // Player used up this dice option
				moveBoard(moves);		           // Move board according to moves
				
				if (hasEnded()) {          // Check if game has ended
					state = GameState.Ended;
					sendEvent(GameEvent.GameEnd);
				}
				
				else 
					switchTurn();				   // next players turn
			}
			turnCount ++;
		}
	}

	public Dice getCurrentPlayersDice() {
		return current_dice;
	}

	public Color getGameWinner() {
		Color winner = null;
		if (state.equals(GameState.Ended)) {
			winner = rules.getBoardLeader(getBoard()); }
		return winner;
	}

	public List<Dice> getDiceOptions(Color color) {
		List<Dice> options = (getColorSide(color).equals(Side.Top)) ? 
				top_dice : bot_dice;
		return options;
	}

	public void setReplay(List<GameMove> gm) {
		replay = gm;
		replay_move = 0;
	}
	
	public GameMove getNextReplayMove() {
		return replay.get(replay_move);
	}
	
	public void setMinimaxAlgorithm() {
		ai.setAlgorithm(Algorithm.Minimax);
	}

	public void setAlphabetaAlgorithm() {
		ai.setAlgorithm(Algorithm.Alphabeta);
	}

	public GameMove calculateNextGameMove() {
		return ai.getBestGameMove(board, current_turn, mode, 
				getDiceOptions(current_turn), getDiceOptions(current_turn.opposite()));
	}

	public PlayerType getPlayerType(Color color) {
		PlayerType type = (top.equals(color)) ? 
				top_player.getPlayerType() : bot_player.getPlayerType();
		return type;
	}

	public void setMinimaxDepth(int d) {
		ai.setDepth(d);
	}

	public void subscribe(GameEvent event, ActionListener listener)
	{
		if (!listeners.containsKey(event)) 
			listeners.put(event, new ArrayList<ActionListener>()); 
		
		listeners.get(event).add(listener);
	}

	public double getStaticEvaluationFunctionScore(Board board, Color self_col, Color next_turn,
			List<Dice> self_dice, List<Dice> opp_dice, Dice used) {
		return ai.staticEvalutionFunction(board, self_col, next_turn, self_dice, opp_dice, used);
	}

	public List<Move> getCurrentPlayerMove() {
		return current_moves;
	}

	public void newGame() {
		// Restarts game but generates new dice options 
		
		state = GameState.Not_Started;
		current_turn = first;
		List<Move> current_moves = new ArrayList<Move>();
		top_dice.clear();
		bot_dice.clear();
		init_players_dice();
		board =  new Board(top);
		turnCount = 0;
	}

	public void restart() {
		// Restarts game but keeps players dice
		
		state = GameState.Not_Started;
		current_turn = first;
		List<Move> current_moves = new ArrayList<Move>();
		top_dice.clear();
		bot_dice.clear();
		for (Dice d : res_top_dice) {
			top_dice.add(new Dice(d));
		}
		for (Dice d : res_bot_dice) {
			bot_dice.add(new Dice(d));
		}
		board =  new Board(top);
		turnCount = 0;
	}

	public GameMode getGameMode() {
		return mode;
	}

	public PlayerType getCurrentPlayerType() {
		PlayerType type = (getCurrentPlayerSide().equals(Side.Top)) ? 
							top_player.getPlayerType() : bot_player.getPlayerType();
		return type;
	}

	public Color getCurrentPlayerColor() {
		return current_turn;
	}

	public Board getBoard() {
		return new Board(board);
	}

	public void startGame() {
		// Run the game first time, this function must be invoked
		// to start playing the game
		if (! state.equals(GameState.Not_Started))
			throw new IllegalStateException("Can not start game in current state: " + state);
	
		else {
			sendEvent(GameEvent.GameStart);
			state = GameState.In_Progress;
			playTurn();
		}
	}

	private void initGameFile(){
		// if game file does not exist then create it
	
		if ((output_path != null)) {
			File game_file = new File(output_path);
			if (! game_file.exists()) {
				try {
					if (! game_file.createNewFile()) {	
						output_path = null;
					}
				
				} catch (IOException e) {
				      output_path = null;}
			}
		}
	}

	private void sendEvent(GameEvent event)
	{
		if (listeners.containsKey(event))
		{
			ArrayList<ActionListener> arr = listeners.get(event);
			for (ActionListener listener : arr)
			{
				listener.actionPerformed(null);
			}
		}	
	}

	private Side getColorSide(Color color) {
		Side side = (top.equals(color)) ? Side.Top : Side.Bottom;
		return side;
	}

	
	private void init_players_dice() {
		// Initialize dice for players
		Random random = new Random();
		int loop;
		
		if (mode.equals(GameMode.FIFTY)) {
			for (loop=0; loop<50; loop++) {
				top_dice.add(new Dice(random.nextInt(6)+1,random.nextInt(6)+1));
				res_top_dice.add(new Dice(top_dice.get(loop)));
				bot_dice.add(new Dice(random.nextInt(6)+1,random.nextInt(6)+1));
				res_bot_dice.add(new Dice(bot_dice.get(loop)));
			}
		}
		
		else if (mode.equals(GameMode.SEVENTY_TWO)){
			int loop2;
			for (loop=1; loop<=6; loop++) {
				for (loop2=1; loop2<=6; loop2++) {
					top_dice.add(new Dice(loop, loop2));
					top_dice.add(new Dice(loop, loop2));
					bot_dice.add(new Dice(loop, loop2));
					bot_dice.add(new Dice(loop, loop2));
					res_top_dice.add(new Dice(loop, loop2));
					res_top_dice.add(new Dice(loop, loop2));
					res_bot_dice.add(new Dice(loop, loop2));
					res_bot_dice.add(new Dice(loop, loop2));
				}
			}
		}
		
		else if (mode.equals(GameMode.NORMAL)) {
			
		}
		Collections.sort(top_dice, new DiceComparator());
		Collections.sort(bot_dice, new DiceComparator());
		Collections.sort(res_top_dice, new DiceComparator());
		Collections.sort(res_bot_dice, new DiceComparator());
	}
	
	private void switchTurn() {
		// switch between opponents turns
		if (mode.equals(GameMode.NORMAL)) {
			List<Dice> dice = getDiceOptions(getCurrentPlayerColor());
			dice.clear();
		}
		replay_move++;
		sendEvent(GameEvent.PlayerEndTurn);
		current_turn = current_turn.opposite();
		playTurn();
	}

	private void moveBoard(List<Move> moves) {
		// Advance board by given moves
		board = rules.moveBoard(getBoard(), moves, getCurrentPlayerColor());
		sendEvent(GameEvent.PlayerMove);
		writeMovesToFile(moves, current_dice);
	}
	
	private void writeMovesToFile(List<Move> moves, Dice dice) {
		// Write all moves to output file so external backgammon
		// program can read moves
		
		if (output_path != null && (! getCurrentPlayerType().equals(PlayerType.External)) ) {
			
			boolean done_writing = false;
			
			while (done_writing == false) {
				
					
					// Try write moves to file
					try {
						FileWriter fw = new FileWriter(output_path);
						 
						// Write player color
						fw.write(getCurrentPlayerColor().toString());
						fw.write(System.getProperty("line.separator"));
						
						// Write moves
						for (Move move : getCurrentPlayerMove()) {
							fw.write(Integer.toString(move.getSource()));
							fw.write(System.getProperty("line.separator"));
							fw.write(Integer.toString(move.getDest()));
							fw.write(System.getProperty("line.separator"));
						}
						
						// Write dice
						fw.write(Integer.toString(getCurrentPlayersDice().getDice1()));
						fw.write(System.getProperty("line.separator"));
						fw.write(Integer.toString(getCurrentPlayersDice().getDice2()));
					 
						fw.close();
						done_writing = true;
					}
					
					// Wait until file is available, try
					// try again later
					catch (Exception e) {
						 try {
							    Thread.sleep(500);             
						 } 
						 catch(InterruptedException ex) {
							    Thread.currentThread().interrupt();
						 }
					}
			}
	    }
	}
	
	public boolean hasEnded() {
		// Check if game has ended
		
		boolean ended = false;
		// Check if current player has beared off all checkers
		if (rules.playerWon(board, current_turn)) 
			ended = true;	
		// Check if both players have used up all dice options
		else if (! (mode.equals(GameMode.NORMAL) || mode.equals(GameMode.REPLAY))) {
			if (bot_dice.isEmpty() && top_dice.isEmpty())
				ended = true;
		}
		return ended;
	}
	
	private boolean rollIsValid(Dice dice) {
		// Check if player has this dice option
		
		if (mode.equals(GameMode.REPLAY)) {
			return true;
		}
		List<Dice> options = getCurrentPlayersDiceOptions();
		return options.contains(dice);
	}
	
	private void playTurn() {
		// The function  movePlayer MUST be invoked after this
		// for the game to move on. 
		PlayerInterface player = getCurrentPlayer();
		
		if (mode.equals(GameMode.NORMAL)) {
			List<Dice> dice = getDiceOptions(getCurrentPlayerColor());
			dice.clear();
			dice.add(generateRandomDice());
		} 
		if (mode.equals(GameMode.REPLAY)) {
			List<Dice> dice = getDiceOptions(getCurrentPlayerColor());
			dice.clear();
			if (replay_move < replay.size()) {
				dice.add(replay.get(replay_move).getDice());
			}
		}
		
		sendEvent(GameEvent.PlayerStartTurn);
		player.playTurn(this);
	}
	
	private PlayerInterface getCurrentPlayer() {
		
		PlayerInterface player = (getColorSide(current_turn).equals(Side.Top)) ? top_player : bot_player;
		return player;
	}

	private List<Dice> getCurrentPlayersDiceOptions() {
		List<Dice> options = (getCurrentPlayerSide().equals(Side.Top)) ?
								top_dice : bot_dice;
		return options;
	}
	
	private Dice generateRandomDice() {
		Random random = new Random();
		Dice dice = new Dice(random.nextInt(6)+1, random.nextInt(6)+1);
		return dice;
	}
	
	private void removeCurrentPlayerRollOptions(Dice dice) {
		// Remove one instance of this dice option. Play will have
		// one less dice option
		List<Dice> options = getCurrentPlayersDiceOptions();

		for (Dice option : options) {
			if (option.equals(dice)) {
				options.remove(option);
				break; }
		}
		
		// Remove from opponent as well since they share dice options
		if (mode.equals(GameMode.SEVENTY_TWO)) {
			options = getDiceOptions(current_turn.opposite());
			for (Dice option : options) {
				if (option.equals(dice)) {
					options.remove(option);
					break; }
			}	
		}
	}
	
}
