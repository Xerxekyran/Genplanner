package genplanner.ga.selectionOperator;

import java.util.Arrays;
import java.util.Comparator;

import org.jgap.Configuration;
import org.jgap.IChromosome;
import org.jgap.Population;
import org.jgap.RandomGenerator;

import util.Pair;

/**
 * A tournament selction operator. Selects n chromosomes and then compares their
 * fittnes. the fittest one comes into the next round. after k rounds the two
 * best survived chromosomes will be returned
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class TournamentSelection implements ISelectionOperator
{
	public static int				tournamentSize		= 4;
	public static int				tournamentRounds	= 2;
	private ChromosomeComparator	comparator			= new ChromosomeComparator();

	private Configuration			conf				= null;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * default ctor
	 * @param conf
	 *            the needed configuration object (needed for the jgap
	 *            chromosome)
	 */
	public TournamentSelection(Configuration conf)
	{
		this.conf = conf;
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------
	@Override
	public Pair<IChromosome, IChromosome> select(Population a_population)
	{
		RandomGenerator random = conf.getRandomGenerator();
		int size = Math.min(conf.getPopulationSize(), a_population.size());
		IChromosome[] tournamentChromosomes = new IChromosome[tournamentSize];

		// we start with two randomly selected chromosomes
		Pair<IChromosome, IChromosome> ret = new Pair<IChromosome, IChromosome>(a_population.getChromosome(random.nextInt(size)), a_population.getChromosome(random.nextInt(size)));

		// repeat the tournament
		for (int k = 0; k < tournamentRounds; k++)
		{
			// select the tournament chromosomes
			tournamentChromosomes[0] = ret.firstValue;
			tournamentChromosomes[1] = ret.secondValue;
			for (int n = 2; n < tournamentChromosomes.length; n++)
			{
				tournamentChromosomes[n] = a_population.getChromosome(random.nextInt(size));
			}

			// now get the two fittest
			Arrays.sort(tournamentChromosomes, comparator);
			ret.firstValue = tournamentChromosomes[0];
			ret.secondValue = tournamentChromosomes[1];
		}

		return ret;
	}
	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------
}

/**
 * the comparator to sort the array of selected chromosomes
 * 
 * @author Lars George
 * 
 */
class ChromosomeComparator implements Comparator<IChromosome>
{

	@Override
	public int compare(IChromosome arg0, IChromosome arg1)
	{
		if (arg1.getFitnessValueDirectly() < arg0.getFitnessValueDirectly())
		{
			return -1;
		}
		else
			return 1;
	}

}
