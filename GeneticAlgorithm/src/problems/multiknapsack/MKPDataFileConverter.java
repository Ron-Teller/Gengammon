package problems.multiknapsack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MKPDataFileConverter {

	private List<Double> itemValues = new ArrayList<Double>();
	private List<List<Double>> sackItemWeights = new ArrayList<List<Double>>();
	private List<Double> sackCapacities = new ArrayList<Double>();
	private int sacksAmount;
	private int itemAmount;
	private Consumer<String> lineHandler;
	private double optimumValue;
	
	public void convert(File dataFile) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		String line;
		lineHandler = this::firstLineHandler;
		
		while ((line = br.readLine()) != null) {
			lineHandler.accept(line);
		}
		br.close();
	}
	
	private void firstLineHandler(String line) {
		String[] words = line.split("\\s+");
		sacksAmount = Integer.parseInt(words[0]);
		itemAmount = Integer.parseInt(words[1]);
		lineHandler = this::itemValueLineHandler;
	}
	
	private void itemValueLineHandler(String line) {
		List<Double> values = getWords(line).stream()
				.mapToDouble(Double::parseDouble)
				.boxed()
				.collect(Collectors.toList());
		itemValues.addAll(values);
		if (itemValues.size() == itemAmount) {
			lineHandler = this::sackCapacitiesLineHandler;
		}
	}
	
	private void sackCapacitiesLineHandler(String line) {
		List<Double> values = getWords(line).stream()
				.mapToDouble(Double::parseDouble)
				.boxed()
				.collect(Collectors.toList());
		sackCapacities.addAll(values);
		if (sackCapacities.size() == sacksAmount) {
			lineHandler = this::sackConstraintsLineHandler;
		}
	}
	
	private void sackConstraintsLineHandler(String line) {
		List<Double> values = getWords(line).stream()
				.mapToDouble(Double::parseDouble)
				.boxed()
				.collect(Collectors.toList());
		if (values.isEmpty()) {
			lineHandler = this::optimumValueLineHandler;
		} else {
			addConstraintsToLastSack(values);
		}
	}
	
	private void optimumValueLineHandler(String line) {
		if (! line.isEmpty()) {
			optimumValue = Double.parseDouble(line.split("\\s+")[0]);
			lineHandler = this::doNothingLineHandler;
		}
	}
	
	private void addConstraintsToLastSack(List<Double> constraints) {
		int lastIndex = sackItemWeights.size()-1;
		if (sackItemWeights.isEmpty() ||
				sackItemWeights.get(lastIndex).size() == itemAmount) {
			sackItemWeights.add(new ArrayList<Double>(constraints));
		} else {
			sackItemWeights.get(lastIndex).addAll(constraints);
		}
	}
	
	private List<String> getWords(String line) {
		List<String> words = Arrays.asList(line.split("\\W+"))
				.stream().filter(word -> ! word.isEmpty())
				.collect(Collectors.toList());
		return words;
	}
	
	private void doNothingLineHandler(String line) {
	}
	
	public List<Double> getItemValues() {
		return itemValues;
	}
	public List<List<Double>> getSackItemWeights() {
		return sackItemWeights;
	}
	public List<Double> getSackCapacities() {
		return sackCapacities;
	}
	public int getSacksAmount() {
		return sacksAmount;
	}
	public int getItemAmount() {
		return itemAmount;
	}

	public double getOptimumValue() {
		return optimumValue;
	}
}
