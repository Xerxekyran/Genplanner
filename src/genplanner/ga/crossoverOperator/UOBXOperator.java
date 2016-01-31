package genplanner.ga.crossoverOperator;

import genplanner.ga.gene.RouteAllele;
import genplanner.ga.gene.RouteGene;
import genplanner.ga.selectionOperator.ISelectionOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.IGeneticOperatorConstraint;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import org.jgap.RandomGenerator;
import org.jgap.impl.CrossoverOperator;

import util.Pair;

/**
 * A UOBX crossover as described in "Genetische Algorithmen und
 * Evolutionsstrategien" by Eberhard Schöneburg, Frank Heinzmann and Sven
 * Feddersen. It generates two children from two parents.
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class UOBXOperator extends CrossoverOperator
{
	private ISelectionOperator	selectionOperator		= null;
	private double				crossoverRatePercent	= 0.0;
	private static final long	serialVersionUID		= 1L;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * 
	 * @param a_configuration
	 *            the jgap configuration object
	 * @param ratePercentage
	 *            the desired rate of crossover in percentage of the population
	 * @param selectionOperator
	 *            the selection operator (selecting the mates for the crossover)
	 * @throws InvalidConfigurationException
	 *             if the configuration object contains any errors
	 */
	public UOBXOperator(Configuration a_configuration, double ratePercentage, ISelectionOperator selectionOperator) throws InvalidConfigurationException
	{
		super(a_configuration, ratePercentage);
		this.crossoverRatePercent = ratePercentage;
		this.selectionOperator = selectionOperator;
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	@Override
	public void operate(Population a_population, List candidateChromosomes)
	{
		// Work out the number of crossovers that should be performed.
		// -----------------------------------------------------------
		int size = Math.min(getConfiguration().getPopulationSize(), a_population.size());
		int numCrossovers = 0;
		numCrossovers = (int) (size * crossoverRatePercent);

		IGeneticOperatorConstraint constraint = getConfiguration().getJGAPFactory().getGeneticOperatorConstraint();
		// For each crossover, grab two chromosomes, pick a random
		// locus (gene location), and then swap that gene and all genes
		// to the "right" (those with greater loci) of that gene between
		// the two chromosomes.
		// --------------------------------------------------------------
		for (int i = 0; i < numCrossovers; i++)
		{
			// select the two parents with the selectionOperator
			Pair<IChromosome, IChromosome> selectedParents = selectionOperator.select(a_population);
			// Verify that crossover is allowed.
			// ---------------------------------
			if (!isXoverNewAge() && selectedParents.firstValue.getAge() < 1 && selectedParents.secondValue.getAge() < 1)
			{
				// Crossing over two newly created chromosomes is not seen as
				// helpful here.
				// ------------------------------------------------------------------
				continue;
			}
			if (constraint != null)
			{
				List v = new Vector();
				v.add(selectedParents.firstValue);
				v.add(selectedParents.secondValue);
				if (!constraint.isValid(a_population, v, this))
				{
					// Constraint forbids crossing over.
					// ---------------------------------
					continue;
				}
			}
			// Clone the chromosomes.
			// ----------------------
			IChromosome firstMate = getClone(selectedParents.firstValue);
			IChromosome secondMate = getClone(selectedParents.secondValue);

			// Cross over the chromosomes.
			// ---------------------------
			doCrossover(firstMate, secondMate, candidateChromosomes, getConfiguration().getRandomGenerator());
		}
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	@Override
	protected void doCrossover(IChromosome firstMate, IChromosome secondMate, List a_candidateChromosomes, RandomGenerator generator)
	{
		// how many customers are we currently handling?
		int numCustomers = 0;
		for (Gene g : firstMate.getGenes())
		{
			numCustomers += ((RouteAllele) g.getAllele()).getServedCustomerIndices().size();
		}

		int[] firstIndices = new int[numCustomers];
		int[] secondIndices = new int[numCustomers];
		ArrayList<Integer> firstGapFiller = new ArrayList<Integer>();
		ArrayList<Integer> secondGapFiller = new ArrayList<Integer>();

		// ------------------------------------------------------------
		// read the current order of customer indices
		int i = 0;
		for (Gene g : firstMate.getGenes())
		{
			for (Integer value : ((RouteAllele) g.getAllele()).getServedCustomerIndices())
			{
				firstIndices[i] = value.intValue();
				secondGapFiller.add(firstIndices[i]);
				i++;
			}
		}
		i = 0;
		for (Gene g : secondMate.getGenes())
		{
			for (Integer value : ((RouteAllele) g.getAllele()).getServedCustomerIndices())
			{
				secondIndices[i] = value.intValue();
				firstGapFiller.add(secondIndices[i]);
				i++;
			}
		}

		// ------------------------------------------------------------
		// doing the actual crossover
		ArrayList<Integer> firstResultIndices = new ArrayList<Integer>(numCustomers);
		ArrayList<Integer> secondResultIndices = new ArrayList<Integer>(numCustomers);

		// write the values due to a random boolean into the result arrays (true
		// = first to firstResult, false = second to seconResult)
		for (int j = 0; j < numCustomers; j++)
		{
			if (generator.nextBoolean())
			{
				firstResultIndices.add(firstIndices[j]);
				secondResultIndices.add(-1);
			}
			else
			{
				firstResultIndices.add(-1);
				secondResultIndices.add(secondIndices[j]);
			}
		}

		// delete the already used indices from the gap fillers
		firstGapFiller.removeAll(firstResultIndices);
		secondGapFiller.removeAll(secondResultIndices);

		// now fill the gaps with the missing values in the order of the other
		// cromosome
		for (int k = 0; k < numCustomers; k++)
		{
			// do we have a gap?
			if (firstResultIndices.get(k).equals(-1))
			{
				firstResultIndices.set(k, firstGapFiller.remove(0).intValue());
			}
		}

		for (int k = 0; k < numCustomers; k++)
		{
			// do we have a gap?
			if (secondResultIndices.get(k).equals(-1))
			{
				secondResultIndices.set(k, secondGapFiller.remove(0).intValue());
			}
		}

		// ------------------------------------------------------------
		// write the new data back to the genes
		i = 0;
		for (int geneIndex = 0; geneIndex < firstMate.getGenes().length; geneIndex++)
		{
			Gene g = firstMate.getGene(geneIndex);
			ArrayList<Integer> alleleValues = ((RouteAllele) g.getAllele()).getServedCustomerIndices();

			for (int alleleIndex = 0; alleleIndex < alleleValues.size(); alleleIndex++)
			{
				alleleValues.set(alleleIndex, firstResultIndices.get(i++));
			}
		}
		i = 0;
		for (int geneIndex = 0; geneIndex < secondMate.getGenes().length; geneIndex++)
		{
			Gene g = secondMate.getGene(geneIndex);
			ArrayList<Integer> alleleValues = ((RouteAllele) g.getAllele()).getServedCustomerIndices();

			for (int alleleIndex = 0; alleleIndex < alleleValues.size(); alleleIndex++)
			{
				alleleValues.set(alleleIndex, secondResultIndices.get(i++));
			}
		}

		// ------------------------------------------------------------
		// mark these chromosome as candidates for the evolutionary process
		a_candidateChromosomes.add(firstMate);
		a_candidateChromosomes.add(secondMate);
	}

	/**
	 * Creates a deep copy of the given chromosome and returns it.
	 * 
	 * @param toClone
	 *            the Chromosome that should be cloned
	 * 
	 * @return the cloned version of the given chromosome
	 */
	private synchronized IChromosome getClone(IChromosome toClone)
	{
		IChromosome ret = null;

		try
		{
			Gene[] genes = toClone.getGenes();

			Gene[] newGenes = new RouteGene[genes.length];
			for (int i = 0; i < newGenes.length; i++)
			{
				newGenes[i] = (RouteGene) ((RouteGene) genes[i]).cloneExceptCertainCustomer(null);
			}

			ret = new Chromosome(toClone.getConfiguration(), newGenes);
		}
		catch (InvalidConfigurationException e)
		{
			e.printStackTrace();
		}

		return ret;
	}

}
