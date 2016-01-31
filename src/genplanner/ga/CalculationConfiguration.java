package genplanner.ga;

/**
 * This class represents the configurations for a genetic algorithm. The genetic
 * operations and parameters can be set.
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class CalculationConfiguration
{
	public enum CrossoverOperator
	{
		BCRC, UOBX
	}

	public enum SelectionOperator
	{
		Random, Tournament
	}

	public enum ConstraintChecker
	{
		FixedRoute
	}

	public enum FitnessFunction
	{
		Default
	}

	public enum NaturalSelector
	{
		WeightedRoulette, BestChromSelector
	}

	public enum Initialisation
	{
		Random, NearestFirst
	}

	private CrossoverOperator	crossoverOperator;
	private FitnessFunction		fitnessFunction;
	private NaturalSelector		naturalSelector;
	private Initialisation		initialisation;
	private SelectionOperator	selectionOperator;

	private double				crossoverRate	= 0.8;
	private int					populationSize	= 50;
	private int					maxGenerations	= 300;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------

	/**
	 * Constructor with attribute initialisation
	 * 
	 * @param fitnessFunction
	 *            the fitness function for the GA
	 * @param naturalSelector
	 *            the natural selector (decides which chromosomes comes to the
	 *            next generation) for the GA
	 * @param crossoverOperator
	 *            the crossover operator for the GA
	 * @param initialisation
	 *            the initialisation method
	 * @param selectionOperator
	 *            the selection operator for the GA (chooses the chromosomes for
	 *            the crossover)
	 */
	public CalculationConfiguration(FitnessFunction fitnessFunction, NaturalSelector naturalSelector, CrossoverOperator crossoverOperator, Initialisation initialisation, SelectionOperator selectionOperator)
	{
		this.crossoverOperator = crossoverOperator;
		this.naturalSelector = naturalSelector;
		this.fitnessFunction = fitnessFunction;
		this.initialisation = initialisation;
		this.selectionOperator = selectionOperator;
	}

	/**
	 * Constructor with default initialisation
	 */
	public CalculationConfiguration()
	{
		this.crossoverOperator = CrossoverOperator.BCRC;
		this.naturalSelector = NaturalSelector.BestChromSelector;
		this.fitnessFunction = FitnessFunction.Default;
		this.initialisation = Initialisation.NearestFirst;
		this.selectionOperator = SelectionOperator.Random;
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

	public CrossoverOperator getCrossoverOperator()
	{
		return crossoverOperator;
	}

	public void setCrossoverOperator(CrossoverOperator crossoverOperator)
	{
		this.crossoverOperator = crossoverOperator;
	}

	public FitnessFunction getFitnessFunction()
	{
		return fitnessFunction;
	}

	public void setFitnessFunction(FitnessFunction fitnessFunction)
	{
		this.fitnessFunction = fitnessFunction;
	}

	public NaturalSelector getNaturalSelector()
	{
		return naturalSelector;
	}

	public void setNaturalSelector(NaturalSelector naturalSelector)
	{
		this.naturalSelector = naturalSelector;
	}

	public int getPopulationSize()
	{
		return populationSize;
	}

	public void setPopulationSize(int populationSize)
	{
		this.populationSize = populationSize;
	}

	public int getMaxGenerations()
	{
		return maxGenerations;
	}

	public void setMaxGenerations(int maxGenerations)
	{
		this.maxGenerations = maxGenerations;
	}

	public Initialisation getInitialisation()
	{
		return initialisation;
	}

	public void setInitialisation(Initialisation initialisation)
	{
		this.initialisation = initialisation;
	}

	public SelectionOperator getSelectionOperator()
	{
		return selectionOperator;
	}

	public void setSelectionOperator(SelectionOperator selectionOperator)
	{
		this.selectionOperator = selectionOperator;
	}

	public double getCrossoverRate()
	{
		return crossoverRate;
	}

	public void setCrossoverRate(double crossoverRate)
	{
		this.crossoverRate = crossoverRate;
	}
}
