package algorithm;

import java.io.Serializable;
import java.util.Random;

public class NeuralNetwork implements Serializable{

	private int l1Size;
	private int l2Size;
	public int[][] l2Weights;
	
	public NeuralNetwork(int l1_size, int l2_size) {
		this.l1Size = l1_size;
		this.l2Size = l2_size;
		l2Weights = new int[l2_size][l1_size];
	}
	
	public void copy(NeuralNetwork other) {
		this.l1Size = other.l1Size;
		this.l2Size = other.l2Size;
		
		for (int i=0; i<l2Size; i++) {
			System.arraycopy(other.l2Weights[i], 0, this.l2Weights[i], 0, l1Size);
		}
	}
	
	public double feed(int[] l1) {
		// returns output of neural network
//		return uniformFeed(l1);
		return convolutionFeed(l1);
	}
	
	public double uniformFeed(int[] l1) {
		// returns output of neural network
		double l2_sum = 0;
		double nueron_sum;
		for (int i=0; i<l2Size; i++) {
			nueron_sum = 0;
			for (int k=0; k<l1Size; k++) {
				nueron_sum += l2Weights[i][k]*l1[k];
			}
			l2_sum += nueron_sum;
		}
		return l2_sum;
	}	

	public double convolutionFeed(int[] l1) {
		// returns output of neural network
		int convolutionSize = 8;
		double l2_sum = 0;
		double nueron_sum;
		for (int i=0; i<l2Size; i++) {
			nueron_sum = 0;
			if (i+convolutionSize<l2Size) {
				for (int k=0; k<convolutionSize; k++) {
					nueron_sum += l2Weights[i][k]*l1[k+i];
					nueron_sum += l2Weights[i][k+convolutionSize]*l1[k+26+i];
				}
			} else {
				for (int k=0; k<l1Size; k++) {
					nueron_sum += l2Weights[i][k]*l1[k];
				}
			}
			l2_sum += nueron_sum;
		}
		return l2_sum;
	}
	
	public int getL1_size() {
		return l1Size;
	}

	public int getL2_size() {
		return l2Size;
	}

	public int[][] getL2_weights() {
		return l2Weights;
	}
	
	public void randomize() {
		Random rand = new Random();
		for (int i=0; i<l2Size; i++) {
			for (int k=0; k<l1Size; k++) {
				l2Weights[i][k] = rand.nextInt();
			}
		}
	}
}
