package genplanner.ga.selectionOperator;

import org.jgap.Configuration;
import org.jgap.IChromosome;
import org.jgap.Population;
import org.jgap.RandomGenerator;

import util.Pair;

/**
 * Implementaiotn of an Selection operator. Randomly selects two chromosomes
 * from the given population
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class RandomSelection implements ISelectionOperator
{
	private Configuration	conf	= null;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * 
	 * @param conf
	 */
	public RandomSelection(Configuration conf)
	{
		this.conf = conf;
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------
	@Override
	public Pair<IChromosome, IChromosome> select(Population a_population)
	{
		RandomGenerator randomGenerator = conf.getRandomGenerator();

		int size = Math.min(conf.getPopulationSize(), a_population.size());

		IChromosome chrom1 = a_population.getChromosome(randomGenerator.nextInt(size));
		IChromosome chrom2 = a_population.getChromosome(randomGenerator.nextInt(size));

		return new Pair<IChromosome, IChromosome>(chrom1, chrom2);
	}
	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------

}
