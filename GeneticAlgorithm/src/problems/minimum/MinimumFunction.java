package problems.minimum;

public class MinimumFunction implements BiDoubleFunction{

	@Override
	public double function(double x1, double x2) {
		return (20+Math.pow(x1, 2)+Math.pow(x2, 2)
				-10*(Math.cos(2*Math.PI*x1)+Math.cos(2*Math.PI*x2)));
	}

}
