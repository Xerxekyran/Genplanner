package genplanner.ga.crossoverOperator;

import genplanner.ga.gene.RouteAllele;
import genplanner.ga.gene.RouteGene;
import genplanner.ga.selectionOperator.ISelectionOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.IGeneticOperatorConstraint;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import org.jgap.RandomGenerator;
import org.jgap.impl.CrossoverOperator;

import util.Pair;

/**
 * BCRC = Best cost route crossover
 * 
 * This is a problem specific crossover operator. It generate two new children.
 * The implementation is base on the method described in: "Multi-objective
 * Genetic Algorithms for Vehicle Routing Problem with Time Windows" (January
 * 2004) by B. Ombuki, B. J. Ross, and F. Hanshar
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class BCRCOperator extends CrossoverOperator
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
	public BCRCOperator(Configuration a_configuration, double ratePercentage, ISelectionOperator selectionOperator) throws InvalidConfigurationException
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
			// IChromosome firstMate = (IChromosome) chrom1.clone();
			// IChromosome secondMate = (IChromosome) chrom2.clone();
			// Cross over the chromosomes.
			// ---------------------------
			doCrossover(selectedParents.firstValue, selectedParents.secondValue, candidateChromosomes, getConfiguration().getRandomGenerator());
		}
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	@Override
	protected void doCrossover(IChromosome firstMate, IChromosome secondMate, List a_candidateChromosomes, RandomGenerator generator)
	{
		// get random route indices
		int firstRandomRouteIndex = Math.abs(generator.nextInt(firstMate.getGenes().length));
		int secondRandomRouteIndex = Math.abs(generator.nextInt(secondMate.getGenes().length));

		// ------------------------------------------------------------
		// deleting the indices that should be changed (they will not be cloned)

		ArrayList<Integer> firstRandomCustomerIndices = ((RouteAllele) firstMate.getGenes()[firstRandomRouteIndex].getAllele()).getServedCustomerIndices();
		ArrayList<Integer> secondRandomCustomerIndices = ((RouteAllele) secondMate.getGenes()[secondRandomRouteIndex].getAllele()).getServedCustomerIndices();

		IChromosome firstResultChrom = getClone(firstMate, secondRandomCustomerIndices);
		IChromosome secondResultChrom = getClone(secondMate, firstRandomCustomerIndices);

		// ------------------------------------------------------------
		// put the missing customerIndices in the new place

		for (int i = 0; i < secondRandomCustomerIndices.size(); i++)
		{
			putCustomerInBestPlace(firstResultChrom, secondRandomCustomerIndices.get(i), generator);
		}

		for (int i = 0; i < firstRandomCustomerIndices.size(); i++)
		{
			putCustomerInBestPlace(secondResultChrom, firstRandomCustomerIndices.get(i), generator);
		}

		// System.out.println("--------------------------------------------------------------");
		// System.out.println("OLD {{ " + firstMate + " }} NEW {{ " +
		// firstResultChrom + " }}");
		// System.out.println("OLD {{ " + secondMate + " }} NEW {{ " +
		// secondResultChrom + " }}");
		// System.out.println();

		// ------------------------------------------------------------
		// mark these chromosome as candidates for the evolutionary process

		a_candidateChromosomes.add(firstResultChrom);
		a_candidateChromosomes.add(secondResultChrom);
	}

	/**
	 * Puts the the customer index in a best found place on the given chromosome
	 * 
	 * @param chromosome
	 *            the chromosome the customer should be placed
	 * @param customerIndex
	 *            the customer that should be placed on the given chromosome
	 */
	private void putCustomerInBestPlace(IChromosome chromosome, int customerIndex, RandomGenerator generator)
	{

		FitnessFunction func = getConfiguration().getFitnessFunction();

		double bestFit = 0.0;
		double tmpFit = 0.0;
		int genePosition = -1;
		int allelePosition = -1;

		for (int n = 0; n < chromosome.getGenes().length; n++)
			if (((RouteAllele) chromosome.getGenes()[n].getAllele()).getServedCustomerIndices().size() <= 1)
			{
				((RouteAllele) chromosome.getGenes()[n].getAllele()).getServedCustomerIndices().add(customerIndex);
				return;
			}

		// try to find the best place for the given customerIndex
		// try all genes (vehicles)
		for (int i = 0; i < chromosome.getGenes().length; i++)
		{
			ArrayList<Integer> custIndices = ((RouteAllele) chromosome.getGenes()[i].getAllele()).getServedCustomerIndices();

			// try every position on the route
			for (int j = 0; j < custIndices.size(); j++)
			{
				custIndices.add(j, customerIndex);
				tmpFit = func.getFitnessValue(chromosome);
				if (tmpFit > bestFit || bestFit == 0.0)
				{
					bestFit = tmpFit;
					genePosition = i;
					allelePosition = j;
				}

				custIndices.remove(j);
			}
		}

		if (genePosition == -1)
		{
			genePosition = 0;
			allelePosition = 0;
		}

		((RouteAllele) chromosome.getGenes()[genePosition].getAllele()).getServedCustomerIndices().add(allelePosition, customerIndex);

		// int firstRandomRouteIndex =
		// Math.abs(generator.nextInt(chromosome.getGenes().length));
		// for (int n = 0; n < chromosome.getGenes().length; n++)
		// if (((RouteAllele)
		// chromosome.getGenes()[n].getAllele()).getServedCustomerIndices().size()
		// <= 1)
		// firstRandomRouteIndex = n;
		//
		// ((RouteAllele)
		// chromosome.getGenes()[firstRandomRouteIndex].getAllele()).getServedCustomerIndices().add(customerIndex);
	}

	/**
	 * Creates a deep copy of the given chromosome and returns it. The integer
	 * values in the given arrayList will not be cloned as customerIndices
	 * 
	 * @param toClone
	 *            the Chromosome that should be cloned
	 * @param exceptTheseCustomer
	 *            a list of customer indices that should not be cloned
	 * @return the cloned version of the given chromosome
	 */
	private synchronized IChromosome getClone(IChromosome toClone, ArrayList<Integer> exceptTheseCustomer)
	{
		IChromosome ret = null;

		try
		{
			Gene[] genes = toClone.getGenes();

			Gene[] newGenes = new RouteGene[genes.length];
			for (int i = 0; i < newGenes.length; i++)
			{
				newGenes[i] = (RouteGene) ((RouteGene) genes[i]).cloneExceptCertainCustomer(exceptTheseCustomer);
			}

			ret = new Chromosome(toClone.getConfiguration(), newGenes);
		}
		catch (InvalidConfigurationException e)
		{
			e.printStackTrace();
		}

		return ret;
	}
	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------

}
