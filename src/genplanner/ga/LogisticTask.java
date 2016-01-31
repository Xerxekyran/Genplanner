package genplanner.ga;

import genplanner.domain.DataSet;
import genplanner.domain.map.IDistanceCalculator;
import genplanner.domain.map.SimpleCalculator;
import genplanner.domain.solution.CustomerEntry;
import genplanner.domain.solution.ISolution;
import genplanner.domain.solution.LogisticPlan;
import genplanner.domain.solution.VRPTW;
import genplanner.ga.constraint.ConstraintViolation;
import genplanner.ga.constraint.FixedRouteChecker;
import genplanner.ga.constraint.GeneConstraintCheckerList;
import genplanner.ga.crossoverOperator.BCRCOperator;
import genplanner.ga.crossoverOperator.UOBXOperator;
import genplanner.ga.fitnessFunction.TestFitnessFunc;
import genplanner.ga.gene.RouteAllele;
import genplanner.ga.selectionOperator.ISelectionOperator;
import genplanner.ga.selectionOperator.RandomSelection;
import genplanner.ga.selectionOperator.TournamentSelection;

import java.util.Iterator;
import java.util.LinkedList;

import org.jgap.Chromosome;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import org.jgap.impl.BestChromosomesSelector;
import org.jgap.impl.CrossoverOperator;
import org.jgap.impl.MutationOperator;
import org.jgap.impl.WeightedRouletteSelector;

import util.LogWriter;
import util.Pair;
import util.XMLManager;
import util.LogWriter.LogLevel;

/**
 * class that manages a logistic calculation task. it creates the genetic
 * algorithm object and periodically tells the population to evolve. Requests
 * for the current solution have to whait unitl one evolution process has
 * finished (one generation).
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class LogisticTask implements Runnable
{
	private static final String					MODULE_NAME				= "LogisticTask";
	private int									populationSize			= 300;
	private int									maxGenerations			= 350;

	private int 								generationCounter 		= 0;
	private ELogisticTaskStatus					status					= ELogisticTaskStatus.Paused;
	private DataSet								data					= null;
	private Genotype							population				= null;
	private boolean								working					= false;
	private boolean								somethingChanged		= false;
	private ISolution							lastCalculatedSolution	= null;
	private LinkedList<Pair<Double, Double>>	fitnessHistory			= new LinkedList<Pair<Double, Double>>();
	private LinkedList<Pair<Double, Double>>	avgFitnessHistory		= new LinkedList<Pair<Double, Double>>();

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * Constructor with data to process the GA algorithm, using default values
	 * for the population size and the maximum of generations
	 * 
	 * @param data
	 *            the data this LogisticTask should be based on
	 * @param conf
	 *            The configuration settings for the genetic algorithm. if this
	 *            param is null, default settings are used
	 */
	public LogisticTask(DataSet data, CalculationConfiguration conf)
	{
		this.data = data;
		try
		{
			init(conf);
			status = ELogisticTaskStatus.Paused;
		}
		catch (InvalidConfigurationException e)
		{
			LogWriter.getInstance().logToFile(LogLevel.Error, MODULE_NAME, e.toString());
		}

		try
		{
			// try to read the constraint weight values from the config file
			ConstraintViolation.VehicleLoadContraintWeight = Double.parseDouble(XMLManager.getInstance().getXMLLoader(XMLManager.APP_CONFIG_FILE).getString("VehicleLoadContraintWeight"));
			ConstraintViolation.TimeWindowConstraintWeight = Double.parseDouble(XMLManager.getInstance().getXMLLoader(XMLManager.APP_CONFIG_FILE).getString("TimeWindowConstraintWeight"));
			ConstraintViolation.WorkTimeConstraintWeight = Double.parseDouble(XMLManager.getInstance().getXMLLoader(XMLManager.APP_CONFIG_FILE).getString("WorkTimeConstraintWeight"));
			TestFitnessFunc.TravelCostWeight = Double.parseDouble(XMLManager.getInstance().getXMLLoader(XMLManager.APP_CONFIG_FILE).getString("TravelCostWeight"));

			// try to read the tournament selection settings
			TournamentSelection.tournamentRounds = Integer.parseInt(XMLManager.getInstance().getXMLLoader(XMLManager.APP_CONFIG_FILE).getString("TournamentSelctionTournamentRounds"));
			TournamentSelection.tournamentSize = Integer.parseInt(XMLManager.getInstance().getXMLLoader(XMLManager.APP_CONFIG_FILE).getString("TournamentSelctionTournamentSize"));
		}
		catch (Exception e)
		{
			LogWriter.getInstance().logToFile(LogLevel.Warn, MODULE_NAME, "Could not read the genetic algorithm configuration values: " + e.toString());
		}
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	@Override
	public synchronized void run()
	{
		generationCounter = 0;

		// repeat as long as we are paused or running and have not already
		// reached the maximum generations
		while (status == ELogisticTaskStatus.Paused || status == ELogisticTaskStatus.Running)
		{
			try
			{

				if (status == ELogisticTaskStatus.Paused)
				{
					Thread.sleep(1000);
				}
				else
				{
					working = true;
					generationCounter++;

					// evolve the population one generation
					population.evolve();

					// mark that this task changed the solution
					somethingChanged = true;

					// -----------------------------------
					// read fitness values for the history

					// current best fitness
					IChromosome chrom = population.getFittestChromosome();

					synchronized (fitnessHistory)
					{
						fitnessHistory.add(new Pair<Double, Double>(generationCounter + 0.0, chrom.getFitnessValue()));
					}

					// calculate average fitness
					Iterator<IChromosome> it = (Iterator<IChromosome>) population.getPopulation().getChromosomes().iterator();
					Double avgFitness = 0.0;
					int numCalculatedFitnesses = 0;
					double currentFitness = 0.0;
					while (it.hasNext())
					{
						// only get already calculated values
						currentFitness = it.next().getFitnessValueDirectly();

						// if a fitness value was calculated for this chrom
						if (currentFitness != FitnessFunction.NO_FITNESS_VALUE)
						{
							avgFitness += currentFitness;
							numCalculatedFitnesses++;
						}
					}
					if (numCalculatedFitnesses == 0)
						numCalculatedFitnesses = 1;

					avgFitness /= numCalculatedFitnesses;
					synchronized (avgFitnessHistory)
					{
						avgFitnessHistory.add(new Pair<Double, Double>(generationCounter + 0.0, avgFitness));
					}

					System.out.println("Generation [" + generationCounter + "], Best Fitness [" + chrom.getFitnessValueDirectly() + "], Avg Fitness [" + avgFitness + "]");
					// String out =
					// Double.toString(chrom.getFitnessValueDirectly()).replace('.',
					// ',');
					// System.out.println(out);

					// if we are at the last generation, set this task to pause
					if (generationCounter >= maxGenerations)
					{
						setStatus(ELogisticTaskStatus.Paused);
					}
				}
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
				LogWriter.getInstance().logToFile(LogLevel.Error, MODULE_NAME, "run(): " + e.toString());
			}
			finally
			{
				working = false;
			}
		}
	}

	/**
	 * Getter for the current colution data
	 * 
	 * @return the current solution data
	 */
	@SuppressWarnings("unchecked")
	public ISolution getCurrentSolution()
	{
		// only recalculate if its the first time or something changed
		if (lastCalculatedSolution == null || somethingChanged)
		{

			// tell the worker thread to pause
			ELogisticTaskStatus oldStatus = getStatus();
			setStatus(ELogisticTaskStatus.Paused);

			// wait until the thread is paused (and we have a correctly evolved
			// population)
			while (working)
			{
				try
				{
					Thread.sleep(200);
				}
				catch (InterruptedException e)
				{
					System.out.println(e.toString());
					LogWriter.getInstance().logToFile(LogLevel.Error, MODULE_NAME, e.toString());
				}
			}

			// ---------------------------------------
			// no we can read the current solution data
			LinkedList<LinkedList<LogisticPlan>> allPlans = new LinkedList<LinkedList<LogisticPlan>>();
			LinkedList<Double> fitnessValues = new LinkedList<Double>();

			LinkedList<CustomerEntry> customers = null;
			LinkedList<ConstraintViolation> violations = null;

			IDistanceCalculator calculator = null;

			// try to get the distance calculator of the configuration
			try
			{
				calculator = ((TestFitnessFunc) population.getConfiguration().getFitnessFunction()).getDistanceCalculator();
			}
			catch (Exception e)
			{
				// it is something differen
				System.out.println("Using default distance calculator");
				calculator = new SimpleCalculator();
			}

			// for every chromosome (representing one solution)
			Iterator<IChromosome> it = population.getFittestChromosomes(populationSize).iterator();
			while (it.hasNext())
			{
				int vehicleIndex = 0;
				LinkedList<LogisticPlan> onePlan = new LinkedList<LogisticPlan>();
				IChromosome chrom = it.next();

				// for every gene (representing one vehicle plan)
				for (Gene g : chrom.getGenes())
				{
					double travelDistance = 0.0;
					double currenTime = 0.0;
					int lastCustomerIndex = -1;

					customers = new LinkedList<CustomerEntry>();

					RouteAllele ra = ((RouteAllele) g.getAllele());

					// for every customer
					for (Integer custIndex : ra.getServedCustomerIndices())
					{
						// check for constraint violations belonging to this
						// customer
						LinkedList<ConstraintViolation> customerViolations = new LinkedList<ConstraintViolation>();

						if (ra.getViolations() != null)
						{
							for (ConstraintViolation viol : ra.getViolations())
							{
								if (custIndex.equals(viol.getCustomerIndex()))
								{
									customerViolations.add(new ConstraintViolation(viol.getType(), viol.getValue(), viol.getCustomerIndex()));
								}
							}
						}

						// calculate the needed time until this customer
						if (lastCustomerIndex == -1)
						{
							// special case, cause we are coming from the depot
							// not
							// from another customer
							lastCustomerIndex = custIndex;
							double[] calculation = calculator.calculate(data.getDepots().get(ra.getStartDepotIndex()).getPosition(), data.getCustomers().get(custIndex).getPosition(), data.getVehicles().get(vehicleIndex));
							travelDistance += calculation[0];
							currenTime += calculation[1];
						}
						else
						{
							double[] calculation = calculator.calculate(data.getCustomers().get(lastCustomerIndex).getPosition(), data.getCustomers().get(custIndex).getPosition(), data.getVehicles().get(vehicleIndex));
							lastCustomerIndex = custIndex;
							travelDistance += calculation[0];
							currenTime += calculation[1];
						}

						// new customer entry
						customers.add(new CustomerEntry(data.getCustomers().get(custIndex), customerViolations, currenTime));

						// if we are too soon at the customer, wait until his
						// time window starts
						if (currenTime < data.getCustomers().get(custIndex).getTimeWindow().getStartTime())
						{
							currenTime = data.getCustomers().get(custIndex).getTimeWindow().getStartTime();
						}

						// add the service time
						currenTime += data.getCustomers().get(custIndex).getServiceTime();
					}

					// add the distance of last customer to depot
					double[] calculation = calculator.calculate(data.getCustomers().get(ra.getServedCustomerIndices().get(ra.getServedCustomerIndices().size() - 1)).getPosition(), data.getDepots().get(ra.getStartDepotIndex()).getPosition(), data.getVehicles().get(vehicleIndex));
					travelDistance += calculation[0];

					// check for plan violations
					if (ra.getViolations() != null)
					{
						violations = new LinkedList<ConstraintViolation>();

						for (ConstraintViolation viol : ra.getViolations())
						{
							if (viol.getCustomerIndex() == -1)
							{
								violations.add(new ConstraintViolation(viol.getType(), viol.getValue(), -1));
							}
						}
					}

					onePlan.add(new LogisticPlan(data.getVehicles().get(vehicleIndex), data.getDepots().get(ra.getStartDepotIndex()), customers, violations, travelDistance));

					vehicleIndex++;
				}
				allPlans.add(onePlan);
				fitnessValues.add(chrom.getFitnessValueDirectly());
			}

			lastCalculatedSolution = new VRPTW(allPlans, fitnessValues);
			setStatus(oldStatus);
		}

		somethingChanged = false;
		return lastCalculatedSolution;
	}
	
	/**
	 * Increases the generationCounter if the maximum number of generations had been reached
	 */
	public void increaseGenerationcounterIfAtMax()
	{
		if(generationCounter >= maxGenerations)
		{
			maxGenerations = 2 * generationCounter;
		}
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------
	/**
	 * @throws InvalidConfigurationException
	 * 
	 */
	private void init(CalculationConfiguration calcConf) throws InvalidConfigurationException
	{
		LogWriter.getInstance().logToFile(LogLevel.Info, MODULE_NAME, "************************************");
		LogWriter.getInstance().logToFile(LogLevel.Info, MODULE_NAME, "Starting new LogisticTask");
		GeneticConfiguration conf = new GeneticConfiguration();
		// if no configuration was given, use a default one
		if (calcConf == null)
		{
			calcConf = new CalculationConfiguration();
		}
		populationSize = calcConf.getPopulationSize();
		maxGenerations = calcConf.getMaxGenerations();
		LogWriter.getInstance().logToFile(LogLevel.Info, MODULE_NAME, "Population  size: " + populationSize);
		LogWriter.getInstance().logToFile(LogLevel.Info, MODULE_NAME, "Maximum number of generations: " + maxGenerations);

		conf = new GeneticConfiguration();
		conf.setPreservFittestIndividual(true);
		conf.setKeepPopulationSizeConstant(true);

		// -------------------------------------------------
		// Fitness Function
		IDistanceCalculator distCalc = new SimpleCalculator();
		switch (calcConf.getFitnessFunction())
		{
		case Default:
			TestFitnessFunc fitness = new TestFitnessFunc(data, distCalc);
			conf.setFitnessFunction(fitness);
			break;
		}
		LogWriter.getInstance().logToFile(LogLevel.Info, MODULE_NAME, "Using fitness function: " + calcConf.getFitnessFunction().toString());

		// -------------------------------------------------
		// Selection operator
		ISelectionOperator selectionOperator = null;
		switch (calcConf.getSelectionOperator())
		{
		case Random:
			selectionOperator = new RandomSelection(conf);
			break;
		case Tournament:
			selectionOperator = new TournamentSelection(conf);
			break;
		}
		LogWriter.getInstance().logToFile(LogLevel.Info, MODULE_NAME, "Using selection operator: " + calcConf.getSelectionOperator().toString());

		// -------------------------------------------------
		// Crossover operator
		CrossoverOperator cross = null;
		switch (calcConf.getCrossoverOperator())
		{
		case BCRC:
			cross = new BCRCOperator(conf, calcConf.getCrossoverRate(), selectionOperator);
			break;
		case UOBX:
			cross = new UOBXOperator(conf, calcConf.getCrossoverRate(), selectionOperator);
			break;
		}
		conf.addGeneticOperator(cross);
		LogWriter.getInstance().logToFile(LogLevel.Info, MODULE_NAME, "Using crossover operator: " + calcConf.getCrossoverOperator().toString());

		// -------------------------------------------------
		// Natural selection
		switch (calcConf.getNaturalSelector())
		{
		case BestChromSelector:
			// elitism
			BestChromosomesSelector bestChromsSelector = new BestChromosomesSelector(conf);
			bestChromsSelector.setDoubletteChromosomesAllowed(true);
			// bestChromsSelector.setOriginalRate(0.8);
			conf.addNaturalSelector(bestChromsSelector, false);
			break;
		case WeightedRoulette:
			WeightedRouletteSelector rouletteSelector = new WeightedRouletteSelector(conf);
			conf.addNaturalSelector(rouletteSelector, false);
			break;
		}
		LogWriter.getInstance().logToFile(LogLevel.Info, MODULE_NAME, "Using natural selector: " + calcConf.getNaturalSelector().toString());

		// -------------------------------------------------
		// Use standard Mutation operator (actual mutation will be done by the
		// gene itself)
		MutationOperator mut = new MutationOperator(conf);
		mut.setMutationRate(100);
		conf.addGeneticOperator(mut);

		// -------------------------------------------------
		// the constraint checker list object
		GeneConstraintCheckerList checkerList = new GeneConstraintCheckerList();
		checkerList.addConstraintChecker(new FixedRouteChecker());

		// -------------------------------------------------
		// create own first population to guarentee feasability
		IChromosome[] initialChromosomes = new Chromosome[populationSize];
		for (int i = 0; i < initialChromosomes.length; i++)
		{
			// which method to initialise a chromosome?
			switch (calcConf.getInitialisation())
			{
			case Random:
				initialChromosomes[i] = ChromosomeInitialisation.getFeasableRandomChromosome(conf, data);
				break;

			case NearestFirst:
				initialChromosomes[i] = ChromosomeInitialisation.getFeasableChromosomeWithNearestFirstInit(conf, data, distCalc);
				break;
			}
			initialChromosomes[i].setConstraintChecker(checkerList);
		}
		LogWriter.getInstance().logToFile(LogLevel.Info, MODULE_NAME, "Using initialisiation: " + calcConf.getInitialisation().toString());

		conf.setSampleChromosome(initialChromosomes[0]);
		conf.setPopulationSize(populationSize);
		Population pop = new Population(conf, initialChromosomes);
		population = new Genotype(conf, pop);
		LogWriter.getInstance().logToFile(LogLevel.Info, MODULE_NAME, "************************************");
	}

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------

	public ELogisticTaskStatus getStatus()
	{
		return status;
	}

	public void setStatus(ELogisticTaskStatus status)
	{
		this.status = status;
	}

	public LinkedList<Pair<Double, Double>> getFitnessHistory()
	{
		return fitnessHistory;
	}

	public LinkedList<Pair<Double, Double>> getAvgFitnessHistory()
	{
		return avgFitnessHistory;
	}
}
