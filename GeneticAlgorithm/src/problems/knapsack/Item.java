package problems.knapsack;

public class Item {

	private final int weight;
	private final int value;
	
	public Item(int weight, int value) {
		super();
		this.weight = weight;
		this.value = value;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "SackItem [weight=" + weight + ", value=" + value + "]";
	}
}
