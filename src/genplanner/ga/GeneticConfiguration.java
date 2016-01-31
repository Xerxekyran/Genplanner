package genplanner.ga;

import org.jgap.Configuration;
import org.jgap.DefaultFitnessEvaluator;
import org.jgap.InvalidConfigurationException;
import org.jgap.event.EventManager;
import org.jgap.impl.ChromosomePool;
import org.jgap.impl.StockRandomGenerator;

/**
 * A Genetic Configuration is a jgap configuration implementation. Only
 * mandatory values are set, no genetic operations or natural selector.
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class GeneticConfiguration extends Configuration
{

	private static final long	serialVersionUID	= 1L;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * Default constructor that sets the needed values for the configuration
	 */
	public GeneticConfiguration()
	{
		super();
		Configuration.reset();
		try
		{
			setRandomGenerator(new StockRandomGenerator());
			setMinimumPopSizePercent(0);
			setEventManager(new EventManager());

			// default implementation: a higher fitness value is seen as fitter
			setFitnessEvaluator(new DefaultFitnessEvaluator());

			setChromosomePool(new ChromosomePool());
		}
		catch (InvalidConfigurationException e)
		{
			throw new RuntimeException("Fatal error: DefaultConfiguration class could not use its " + "own stock configuration values. This should never happen. " + "Please report this as a bug to the JGAP team.");
		}
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------
}
