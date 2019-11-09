package BG.controller;

import io.DataSerializer;
import io.DataSerializer2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import view.BackGammonView;
import view.StartFrame;
import BG.ArtificialIntelligence;
import BG.ArtificialIntelligence.Algorithm;
import BG.Game;
import BG.GeneticArtificialIntelligence;
import BG.Glossary.Color;
import BG.Glossary.GameMode;
import BG.Glossary.PlayerType;
import BG.MiniMaxArtificialIntelligence;
import BG.player.AIPlayer;
import BG.player.ExternalPlayer;
import BG.player.HumanPlayer;
import BG.player.PlayerInterface;
import algorithm.Gene;
import algorithm.Minimax2;
import algorithm.NeuralNetwork;
import algorithm.genetic.BackgammonGene;

public class StartGameController {

	private StartFrame frame;
	
	public StartGameController() {
		frame = new StartFrame();
		frame.subscribeStartEvent(new ActionListener() { @Override
			public void actionPerformed(ActionEvent arg) {
			onStartGame();	}});

	}
	
	public void start() {
		frame.setVisible(true);
	}
	
	private void onStartGame() {
		
		new Thread(new Runnable() {
		    public void run() {
				Color top_color;
				Color first_move;
				PlayerInterface top_player = null;
				PlayerInterface bot_player = null;
				Algorithm algorithm;
				GameMode mode = null;
				int depth;
				ArtificialIntelligence ai = null;
				String input_file = frame.getInputFile();
				String output_file = frame.getOutputFile();
				String neuralNetworkFile = frame.getNeuralNetworkFile();
				depth = frame.getDepth();
				
				top_color = (frame.getTopColorSelected().equals("white")) ?
						Color.White : Color.Black;

				first_move = (frame.getFirstColor().equals("white")) ? 
						Color.White : Color.Black;
				
				if (frame.getTopType().equals("ai")) 
					top_player = new AIPlayer();
				else if (frame.getTopType().equals("human"))
					top_player = new HumanPlayer();
				else if (frame.getTopType().equals("external"))
					top_player = new ExternalPlayer(input_file);
				
				if (frame.getBotType().equals("ai")) 
					bot_player = new AIPlayer();
				else if (frame.getBotType().equals("human"))
					bot_player = new HumanPlayer();
				else if (frame.getBotType().equals("external"))
					bot_player = new ExternalPlayer(input_file);
				
				if (top_player.getPlayerType().equals(PlayerType.External) &&
						bot_player.getPlayerType().equals(PlayerType.External)){
					throw new IllegalStateException("Both players can not be external.");
				}

				if (frame.getGameMode().equals("50")) {
					mode = GameMode.FIFTY;
				}
				else if (frame.getGameMode().equals("72")) {
					mode = GameMode.SEVENTY_TWO;
				}
				else if (frame.getGameMode().equals("normal")) { 
					mode = GameMode.NORMAL;
				}
				
				switch (frame.getAlgorithm()) {
		            case "minimax":  algorithm = Algorithm.Minimax;
		            				ai = new ArtificialIntelligence(algorithm, depth, mode);
		            				break;
		            case "alphabeta":  algorithm = Algorithm.Alphabeta;
		            					ai = new ArtificialIntelligence(algorithm, depth, mode);
		            					break;
		            case "genetic":  algorithm = Algorithm.Genetic;
						            DataSerializer2 serializer = new DataSerializer2();
									String popFile = neuralNetworkFile;
									List<BackgammonGene> population = serializer.loadPopulation(popFile);
									NeuralNetwork nn = population.get(0).getNn();
									ai = new GeneticArtificialIntelligence(algorithm, depth, mode, nn);
									break;
		            case "genetic-minimax": algorithm = Algorithm.Genetic_Minimax;
		            				DataSerializer2 serializer2 = new DataSerializer2();
									String popFile2 = neuralNetworkFile;
									List<BackgammonGene> population2 = serializer2.loadPopulation(popFile2);
									NeuralNetwork nn2 = population2.get(0).getNn();
									Minimax2 minimax = new Minimax2();
									minimax.setBranchingFactor(2);
									minimax.setDepth(7);
									minimax.setRootBranchingFactor(4);
									minimax.setHueristicNeuralNetwork(nn2);
									ai = new MiniMaxArtificialIntelligence(algorithm, depth, mode, minimax);
//									ai = new GeneticArtificialIntelligence(algorithm, depth, mode, nn2);
									break;
				 }
				 
			

				Game game = new Game(top_color, top_player, bot_player, 
						first_move, mode, 
						ai, output_file);
				
				BackGammonView view = new BackGammonView();
				ViewAdapter adapter = new ViewAdapter(view);
				Controller control = new Controller(game, adapter);
		    }
		}).start();
	}
	
}
