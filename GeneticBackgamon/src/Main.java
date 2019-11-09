

import java.io.IOException;

import BG.controller.StartGameController;

public class Main {

	
	public static void main(String[] args) throws IOException {
		 
/*		AlgorithmRunner runner = new AlgorithmRunner();
		runner.setOutput("resources/ga/123.txt");
		runner.setPopulation(20);
		runner.setTests(100);
		runner.run(0.15, 0.6, 30, 1000);*/
		
/*		AlgorithmRunner2 runner2 = new AlgorithmRunner2();
		runner2.setTests(10);
		runner2.setGenes("resources/ga/convolutionFeedSmall3.txt");
		runner2.setOutputFile("resources/ga/convolutionFeedSmall3.txt");
		runner2.run(1, null);*/
		
//		DataSerializer2 ser = new DataSerializer2();
//		List<BackgammonGene> genes = ser.loadPopulation("resources/ga/1234asd.txt");
//		System.out.println(genes.size());
		

		StartGameController controller = new StartGameController();
		controller.start();
		
/*		DataSerializer2 ser = new DataSerializer2();
		DataSerializer serializer = new DataSerializer();
		Dueler dueler = new Dueler();
		NeuralNetwork nn1 = serializer.loadPopulation("resources/ga/tryArrX.txt").get(0).getNn();
		NeuralNetwork nn2 = serializer.loadPopulation("resources/ga/tryArrX.txt").get(0).getNn();
//		NeuralNetwork nn2 = ser.loadPopulation("resources/ga/1234asd2.txt").get(0).getNn();
		dueler.duel(nn2, nn1, 1000);*/
	}
}
