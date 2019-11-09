package problems.minimum;

import gene.Gene;

public class MinimumGene extends Gene{
	
	int x1Integer;
	int x2Integer;
	int x1Fraction;
	int x2Fraction;

	public MinimumGene(int x1Integer, int x2Integer, int x1Fraction,
			int x2Fraction) {
		super();
		this.x1Integer = x1Integer;
		this.x2Integer = x2Integer;
		this.x1Fraction = x1Fraction;
		this.x2Fraction = x2Fraction;
	}

	public int getX1Integer() {
		return x1Integer;
	}

	public double getX1() {
		return intsToDouble(x1Integer, x1Fraction);
	}
	
	public double getX2() {
		return intsToDouble(x2Integer, x2Fraction);
	}
	
	private double intsToDouble(int integer, int fraction) {
		return (double )integer+
				  (double) fraction/Integer.MAX_VALUE;
	}
	
	public void setX1Integer(int x1Integer) {
		this.x1Integer = x1Integer;
	}

	public int getX2Integer() {
		return x2Integer;
	}

	public void setX2Integer(int x2Integer) {
		this.x2Integer = x2Integer;
	}

	public int getX1Fraction() {
		return x1Fraction;
	}

	public void setX1Fraction(int x1Fraction) {
		this.x1Fraction = x1Fraction;
	}

	public int getX2Fraction() {
		return x2Fraction;
	}

	public void setX2Fraction(int x2Fraction) {
		this.x2Fraction = x2Fraction;
	}
}
