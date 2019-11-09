package BG.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import BG.ArtificialIntelligence.Algorithm;
import BG.Board;
import BG.Dice;
import BG.Game;
import BG.GameMove;
import BG.Glossary.Color;
import BG.Glossary.GameMode;
import BG.Glossary.PlayerGameState;
import BG.Glossary.PlayerType;
import BG.Glossary.Side;
import BG.Move;
import BG.RulesNew;

public class Controller {
	
	Board previousBoard;
	private List<Move> moveHistory;
	private List<Board> moveStates;
	private int selectedPoint;  // point selected by user that is able to move
								 // with the current available rolls left in turn.
								 //  no point selected should be value of -1
	private List<Integer> rollsLeft;
	private List<Integer> rollsUsed;
	private boolean boardEnabled;
	private Game game; 
	private ViewInterface view;
	private RulesNew rules;
	
	public Controller(Game _game, ViewInterface _adapter) {
		game = _game;
		view = _adapter;
		boardEnabled = false;
		rollsLeft = new ArrayList<Integer>();
		rollsUsed = new ArrayList<Integer>();
		selectedPoint = -1;
		moveHistory = new ArrayList<Move>();
		moveStates = new ArrayList<Board>();
		subscribeGameEvents();
		subscribeViewEvents();
		view.start();
		view.setDepth(game.getDepth());
		view.disableMove();
		view.disableMoveForeMe();
		view.disableRoll(Color.White);
		view.disableRoll(Color.Black);
		view.disableUndo();
		setViewAlgorithm();
		setViewBoard(game.getBoard());
		
	}

	// Subscribe functions ======================================================================
	private void subscribeGameEvents() {
		
		game.subscribe(Game.GameEvent.GameStart, new ActionListener(){ @Override
			public void actionPerformed(ActionEvent e){
				onGameStart();	}});
		
		game.subscribe(Game.GameEvent.PlayerStartTurn, new ActionListener(){ @Override
			public void actionPerformed(ActionEvent e){
				onPlayerStartTurn();	}});
		
		game.subscribe(Game.GameEvent.PlayerMove, new ActionListener(){ @Override
			public void actionPerformed(ActionEvent e){
				onPlayerMove();	}});
		
		game.subscribe(Game.GameEvent.PlayerEndTurn, new ActionListener(){ @Override
			public void actionPerformed(ActionEvent e){
				onPlayerEndTurn();	}});
		
		game.subscribe(Game.GameEvent.GameEnd, new ActionListener(){ @Override
			public void actionPerformed(ActionEvent e){
				onGameEnd();	}});
		
	}
	
	private void subscribeViewEvents() {
		
		view.subscribeBearingEvent(new ActionListener() { @Override
			public void actionPerformed(ActionEvent arg) {
				OnBearingSelected(); }});
		
		view.subscribeMoveButton(new ActionListener() { @Override
			public void actionPerformed(ActionEvent arg) {
				onMoveEvent();	}});
		
		view.subscribePointEvent(new ActionListener() { @Override
			public void actionPerformed(ActionEvent arg) {
			onPointSelected();	}});
		
		view.subscribeRollEvent(new ActionListener() { @Override
			public void actionPerformed(ActionEvent arg) {
				onRollEvent();	}});
		
		view.subscribeUndoMoveEvent(new ActionListener() { @Override
			public void actionPerformed(ActionEvent arg) {
				onUndoEvent();	}});
		
		view.subscribeRightClickEvent(new ActionListener() { @Override
			public void actionPerformed(ActionEvent arg) {
				onRightClick();	}});
		
		view.subscribeDepthChange(new ActionListener() { @Override
			public void actionPerformed(ActionEvent arg) {
				onDepthChanged(); }});
		
		view.subscribeMinimaxEvent(new ActionListener() { @Override
			public void actionPerformed(ActionEvent arg) {
				onMinimaxEvent(); }});
		
		view.subscribeAlphabetaEvent(new ActionListener() { @Override
			public void actionPerformed(ActionEvent arg) {
				onAlphabetaEvent(); }});
		
		view.subscribeMoveForMeEvent(new ActionListener() { @Override
			public void actionPerformed(ActionEvent arg) {
				onMoveForMeEvent(); }});		
		
		view.subscribeRestartEvent(new ActionListener() { @Override
			public void actionPerformed(ActionEvent arg) {
				onRestartEvent(); }});
		
		view.subscribeNewGameEvent(new ActionListener() { @Override
			public void actionPerformed(ActionEvent arg) {
				onNewGameEvent(); }});		
	}
	
	
	// Game Events ==============================================================================
	
	private void onPlayerStartTurn() {
		previousBoard = getCurrentBoard();
		moveStates.clear(); 
		moveStates.add(getCurrentBoard());
		view.setDiceOptions(game.getDiceOptions(getCurrentPlayerColor()), getCurrentPlayerColor());
		view.addLogInfo("~~~ "+getCurrentPlayerColor().toString()+" turn ~~~");
		setPlayerStatus(getCurrentBoard());
		
		if (getCurrentPlayerType().equals(PlayerType.External)) {
			view.disableMove();
			view.disableMoveForeMe();
			view.disableRoll(getCurrentPlayerColor());
			view.disableUndo();
			boardEnabled = false;
		}
		
		else if (getCurrentPlayerType().equals(PlayerType.REPLAY)) {
			view.disableRoll(getCurrentPlayerColor());
			view.disableUndo();
			view.disableMoveForeMe();
			boardEnabled = false;
			view.enableMove();	
		}
		
		else if (getCurrentPlayerType().equals(PlayerType.AI)) {
			view.disableRoll(getCurrentPlayerColor());
			view.disableUndo();
			view.disableMoveForeMe();
			boardEnabled = false;
			// Get opponent player type
			if (game.getPlayerType(getCurrentPlayerColor().opposite()).equals(PlayerType.AI)) {
				view.enableMove();
			}
			else {
				view.disableMove();
				onMoveEvent();
			}
		}
		
		// If current player is human
		else if (getCurrentPlayerType().equals(PlayerType.Human)){
			view.disableMove();
			view.enableMoveForeMe();
			view.disableUndo();
			view.enableRoll(getCurrentPlayerColor());
			boardEnabled = false;
			moveHistory.clear();
			selectedPoint = -1;
			rollsUsed.clear();
			rollsLeft.clear();
			if (game.getGameMode().equals(GameMode.NORMAL)) {
				view.selectFirstRoll(getCurrentPlayerColor());
			}
		}
	}
	
	private void onPlayerEndTurn() {
		view.removeAllMarks();
		view.setDiceOptions(game.getDiceOptions(Color.White), Color.White);
		view.setDiceOptions(game.getDiceOptions(Color.Black), Color.Black);
		view.disableMoveForeMe();
		view.disableRoll(getCurrentPlayerColor());
		view.disableRoll(getCurrentPlayerColor().opposite());
		//setViewBoard(getCurrentBoard());
	}
	
	private void onPlayerMove() {
		Dice dice = game.getCurrentPlayersDice();
		view.addLogInfo(getCurrentPlayerColor().toString()+" rolled <"
				+dice.getDice1()+","+dice.getDice2()+">");
		for (Move move : game.getCurrentPlayerMove()) {
			view.addLogInfo(getCurrentPlayerColor().toString()+move.toString());
		}
		setViewBoard(getCurrentBoard());
		view.setDice(dice.getDice1(), dice.getDice2());
		view.disableMove();
		view.disableMoveForeMe();
		view.disableRoll(getCurrentPlayerColor());
		view.disableRoll(getCurrentPlayerColor().opposite());
		view.disableUndo();		
	
	}
	
	private void onGameStart() {
		view.addLogInfo("");
		view.setDiceOptions(game.getDiceOptions(Color.White), Color.White);
		view.setDiceOptions(game.getDiceOptions(Color.Black), Color.Black);
		view.disableRoll(getCurrentPlayerColor().opposite());
		setViewBoard(getCurrentBoard());
	}
	
	private void onGameEnd() {
		view.disableMove();
		view.disableMoveForeMe();
		view.disableRoll(Color.White);
		view.disableRoll(Color.Black);
		view.disableUndo();
		view.addLogInfo(game.getGameWinner().toString()+" wins!");
		view.setStatusBar("Match ended. "+game.getGameWinner()+" won");
		view.notifyPlayerWin(game.getGameWinner());
	}
	

	// View Events ==============================================================================
	private void onDepthChanged() {
		game.setMinimaxDepth(view.getDepth());
	}
	
	private void onPointSelected() {
		int point;
		Move move;
		List<Integer> point_moves;
		
		if (boardEnabled == true) {
			
			if (getCurrentPlayerGameState().equals(PlayerGameState.Bar)) {
				point = view.getLastPointClicked();
				move = new Move(Move.Bar, point);
				if (moveIsValid(move)) {
					addMoveBoardState(move);
				}
				
				else {
					view.addLogInfo(getCurrentPlayerColor().toString()+
							": "+"Can not move to point "+point+".");
				}
			}
			
			else  {
				if (selectedPoint == -1) {
					point = view.getLastPointClicked();
					if (pointCanMove(point)) {
						point_moves = rules.getAvailablePointMoves(getLastMoveState(), point, rollsLeft, getCurrentPlayerColor());
						if (point_moves.size() == 1) {
							move = new Move(point, point_moves.get(0));
							addMoveBoardState(move);
						}
						else {
							selectedPoint = point;
							view.hightlightLastPointChecker(selectedPoint, getCurrentPlayerColor());
							markAvailablePointMoves(selectedPoint);
						}
					}
					else {
						//view.addLogInfo(getCurrentPlayerColor().toString()+": "+"Point "+point+" is unmovable.");
					}
						
				}
				else {
					point = view.getLastPointClicked();
					move = new Move(selectedPoint, point);
					if (moveIsValid(move)) {
						addMoveBoardState(move);
					}
					
					else {
						//view.addLogInfo(getCurrentPlayerColor().toString()+
						//		": "+"Can not move from point "+selected_point+" to point "+point+".");
					}
				}
			}
		}
	}

	
	private void onRightClick() {
		if (selectedPoint != -1) {
			view.removeHighlightFromPoint(selectedPoint, getCurrentPlayerColor());
			view.removeAllMarks();
			selectedPoint = -1;
		}
	}
	
	private void onMoveForMeEvent() {
		view.disableMove();
		view.disableMoveForeMe();
		view.disableRoll(getCurrentPlayerColor());
		view.disableUndo();	
		GameMove gmove = game.calculateNextGameMove();
		game.movePlayer(gmove.getMoves(), gmove.getDice());
	}
	
	private void onMoveEvent() {
		view.disableMove();
		view.disableMoveForeMe();
		view.disableRoll(getCurrentPlayerColor());
		view.disableUndo();
		if (game.getCurrentPlayerType().equals(PlayerType.Human)) {
			game.movePlayer(moveHistory, view.getSelectedDice(getCurrentPlayerColor()));
		}
		else if (game.getCurrentPlayerType().equals(PlayerType.AI)) {
			GameMove gmove = game.calculateNextGameMove();
			game.movePlayer(gmove.getMoves(), gmove.getDice());
		} 
		else if (game.getCurrentPlayerType().equals(PlayerType.REPLAY)) {
			GameMove gm = game.getNextReplayMove();
			game.movePlayer(gm.getMoves(), gm.getDice());
		}
	}
	
	private void onUndoEvent() {
		rollsLeft.add(rollsUsed.get(rollsUsed.size()-1));
		rollsUsed.remove(rollsUsed.size()-1);
		view.disableMove();
		boardEnabled = true;
		
		if (rollsUsed.isEmpty()) {
			view.disableUndo(); }
		
		moveStates.remove(moveStates.size()-1);
		moveHistory.remove(moveHistory.size()-1);
		unselectPoint();
		setViewBoard(getLastMoveState());
		setPlayerStatus(getLastMoveState());
		

		if (getCurrentPlayerGameState().equals(PlayerGameState.Bar)) {
			markAvailablePointMoves(Move.Bar);
			view.hightlightLastBarChecker(getCurrentPlayerSide(), getCurrentPlayerColor());
		}
		else {
			markAvailablePointsToMove();
		}
	}
	
	private void onRollEvent() {
		
		Dice dice = view.getSelectedDice(getCurrentPlayerColor());
		view.disableUndo();
		view.disableMove();
		unselectPoint();
		moveStates.clear();
		moveHistory.clear();
		view.removeAllMarks();
		moveStates.add(getCurrentBoard());
		rollsUsed.clear();
		rollsLeft = rules.toRolls(dice);
		view.setDice(dice.getDice1(), dice.getDice2());
		
		if (hasMovesLeft()) {
			if (getCurrentPlayerGameState().equals(PlayerGameState.Bar)) {
				markAvailablePointMoves(Move.Bar);
				view.hightlightLastBarChecker(getCurrentPlayerSide(), getCurrentPlayerColor());
			}
			else {
				markAvailablePointsToMove();
			}
			boardEnabled = true;
		}
		else {
			view.enableMove();
			boardEnabled = false;
		}
	}
	
	private void OnBearingSelected() {
		Move move;
		
		if (boardEnabled == true) {
			
			if (getCurrentPlayerGameState().equals(PlayerGameState.Bearing)) {
				if (selectedPoint != -1) {
					move = new Move(selectedPoint, Move.Bearing);
					if (moveIsValid(move)) {
						addMoveBoardState(move);
					}
				
					else {
						view.addLogInfo(getCurrentPlayerColor().toString()+
								": Point "+selectedPoint+" can not be beared off.");
					}
				}
			}	
		}
	}	
	
	private void onMinimaxEvent() {
		game.setMinimaxAlgorithm();
	}

	private void onAlphabetaEvent() {
		game.setAlphabetaAlgorithm();
	}
	
	private void onRestartEvent() {
		game.restart();
		setViewBoard(game.getBoard());
		game.startGame();
	}

	private void onNewGameEvent() {
		game.newGame();
		setViewBoard(game.getBoard());
		game.startGame();
	}
	
	// game functions ================================================================
	
	private Integer getRollOptionUsed(Board board, Move move, List<Integer> rolls) {
		return rules.getRollUsed(board, move, rolls, getCurrentPlayerColor());
	}

	
	private boolean hasMovesLeft() {
		return rules.canMove(getLastMoveState(), getCurrentPlayerColor(), rollsLeft);
	}
	
	private Board getLastMoveState() {
		return moveStates.get(moveStates.size()-1);
	}
	
	private Board getCurrentBoard() {
		return game.getBoard();
	}
	
	private void unselectPoint() {
		if (selectedPoint != -1) {
			view.removeHighlightFromPoint(selectedPoint, getCurrentPlayerColor());
		}
		selectedPoint = -1;
	}
	
	private Side getCurrentPlayerSide() {
		return game.getCurrentPlayerSide();
	}
	
	private PlayerType getCurrentPlayerType() {
		return game.getCurrentPlayerType();
	}
	
	private Color getCurrentPlayerColor() {
		return game.getCurrentPlayerColor();
	}	
	
	
	private boolean pointCanMove(int point) {
		return rules.pointCanMove(getLastMoveState(), point, rollsLeft, getCurrentPlayerColor());
	}
	
	private void addMoveBoardState(Move move) {
		Integer roll_used = getRollOptionUsed(getLastMoveState(), move, rollsLeft);
		Board next_board;
		moveHistory.add(move);
		unselectPoint();
		next_board = moveBoard(getLastMoveState(), move);
		moveStates.add(next_board);
		view.removeAllMarks();
		
		rollsUsed.add(roll_used);
		rollsLeft.remove(roll_used);
		setViewBoard(next_board);
		setPlayerStatus(getLastMoveState());
		view.enableUndo();
		
		if (hasMovesLeft()) {
			if (getCurrentPlayerGameState().equals(PlayerGameState.Bar)) {
				markAvailablePointMoves(Move.Bar);
				view.hightlightLastBarChecker(getCurrentPlayerSide(), getCurrentPlayerColor());
			}
			else {
				markAvailablePointsToMove();
			}
		}
		
		else {
			view.enableMove();
			boardEnabled = false;
		}
			
	}
	
	private Board moveBoard(Board board, Move move) {
		return rules.moveGameBoard(board, move, getCurrentPlayerColor());
	}
	
	private PlayerGameState getCurrentPlayerGameState() {
		return rules.getPlayerGameState(getLastMoveState(), getCurrentPlayerColor());
	}
	
	private List<Integer> getRollsFromDice(Dice dice) {
		return rules.toRolls(dice);
	}
	
	private boolean moveIsValid(Move move) {
		return rules.moveIsValid(move, getLastMoveState(), rollsLeft, getCurrentPlayerColor());
	}
	
	// gui functions
	private void markAvailablePointMoves(int point) {
		view.removeAllMarks();
		List<Integer> points = rules.getAvailablePointMoves(getLastMoveState(),
				point, rollsLeft, getCurrentPlayerColor());
		for (Integer p : points) {
			if (p.intValue()>=1 && p.intValue()<=24)
				view.markPoint(p);
		}
		
	}
	
	private void setViewAlgorithm() {
		Algorithm alg = game.getAlgorithm();
		if (alg.equals(Algorithm.Minimax)) {
			view.selectMinimax();
		}
		else {
			view.selectAlphabeta();
		}
	}
	
	private void setViewBoard(Board board) {
		Color top = board.getTopColor();
		Color bot = board.getBotColor();
		view.setTopBearing(top, board.getBearing(top));
		view.setBotBearing(bot, board.getBearing(bot));
		view.setBar(Side.Top, top, board.getBar(top));
		view.setBar(Side.Bottom, bot, board.getBar(bot));
		
		int point;
		for (point=1; point<=24; point++) {
			view.setPointCheckers(point, 
					board.getPointColor(point), board.getPointAmount(point));
		}
	
	}	

	
	private void setPlayerStatus(Board board) {
		Color player = getCurrentPlayerColor();
		view.setStatusBar(getCurrentPlayerColor()+" turn <"+
				game.getCurrentPlayerType().toString()+">       |         Static Evaluation: "
				+game.getStaticEvaluationFunctionScore(board, player,
						player.opposite(), game.getDiceOptions(player), 
						game.getDiceOptions(player.opposite()), null ));		
	}
	
	private void markAvailablePointsToMove() {
		view.removeAllMarks();
		List<Integer> points = rules.getMovablePoints(getLastMoveState(), rollsLeft, getCurrentPlayerColor());
		
		for (Integer p : points) {
			view.markPoint(p);
		}
	}
	

	
}
