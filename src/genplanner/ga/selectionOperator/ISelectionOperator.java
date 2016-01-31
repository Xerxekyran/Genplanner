package genplanner.ga.selectionOperator;

import org.jgap.IChromosome;
import org.jgap.Population;

import util.Pair;

/**
 * Interface for Selection operators. This operator is called to get the two
 * parents for the crossover operation.
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public interface ISelectionOperator
{
	/**
	 * Selects two chromosomes from the given population
	 * 
	 * @param a_population
	 *            the population this operator should work with
	 * @return a pair of two selected chromosomes
	 */
	public Pair<IChromosome, IChromosome> select(Population a_population);
}
