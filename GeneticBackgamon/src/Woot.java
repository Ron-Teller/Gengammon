
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ChromosomePair;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.CrossoverPolicy;
import org.apache.commons.math3.genetics.MutationPolicy;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.SelectionPolicy;

public class Woot {

	public void foo() {
		double crossoverRate = 0.85;
		double mutationRate = 0.1;
		CrossoverPolicy crossoverPolicy = new CrossoverPolicy() {
			
			@Override
			public ChromosomePair crossover(Chromosome arg0, Chromosome arg1)
					throws MathIllegalArgumentException {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		SelectionPolicy selectionPolicy = new SelectionPolicy() {
			
			@Override
			public ChromosomePair select(Population arg0)
					throws MathIllegalArgumentException {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		MutationPolicy mutationPolicy = new MutationPolicy() {
			
			@Override
			public Chromosome mutate(Chromosome arg0)
					throws MathIllegalArgumentException {
				// TODO Auto-generated method stub
				return null;
			}
		};
		GeneticAlgorithm ga = new GeneticAlgorithm(crossoverPolicy, crossoverRate, mutationPolicy, mutationRate, selectionPolicy);
	}
}
