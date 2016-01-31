package genplanner.ga;

import genplanner.domain.DataSet;
import genplanner.domain.customer.ICustomer;
import genplanner.domain.map.IDistanceCalculator;
import genplanner.ga.gene.RouteAllele;
import genplanner.ga.gene.RouteGene;

import java.util.ArrayList;
import java.util.Random;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;

/**
 * This class offers static methods to initialize a chromosome. different ways
 * to this are presented
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class ChromosomeInitialisation
{
	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------
	/**
	 * Initialises a chromosome. tries to add only the nearest customers to a
	 * route
	 * 
	 * @param conf
	 *            the configuration needed to set up the chromosome
	 * @param data
	 *            the data the chromosome should be based on
	 * @return a feasable chromosome that is initialised with routes that have
	 *         customers neaby
	 * @throws InvalidConfigurationException
	 */
	public static IChromosome getFeasableChromosomeWithNearestFirstInit(Configuration conf, DataSet data, IDistanceCalculator distanceCalculator) throws InvalidConfigurationException
	{
		int numVehicles = data.getVehicles().size();
		int numCustomer = data.getCustomers().size();
		System.out.println("Vehicles: " + numVehicles + "  Customer: " + numCustomer);

		Gene[] initGenes = new Gene[numVehicles];
		Random randomizer = new Random();

		// write alle available indices for customers to a temp var
		ArrayList<Integer> availableCustomers = new ArrayList<Integer>();
		for (int i = 0; i < numCustomer; i++)
		{
			availableCustomers.add(i);
		}
		int customerPerGene = numCustomer / numVehicles;
		// for every gene on the chromosome (try to pass customers to each gene)
		// each gene represents one vehicle
		for (int i = 0; i < initGenes.length; i++)
		{
			RouteAllele rAllele = new RouteAllele();

			// try to find the depot this vehicle belongs to (init with 0 if we
			// do not find any depot)
			rAllele.setStartDepotIndex(0);
			String depotName = data.getVehicles().get(i).getBelongingDepotName();
			for (int depotIndex = 0; depotIndex < data.getDepots().size(); depotIndex++)
			{
				if (data.getDepots().get(depotIndex).getName().equalsIgnoreCase(depotName))
				{
					// found the right depot
					rAllele.setStartDepotIndex(depotIndex);
					break;
				}
			}

			// for every customer this gene should be assigned to
			for (int y = 0; y < customerPerGene; y++)
			{
				int customerIndex = randomizer.nextInt(availableCustomers.size());

				// if we are adding a customer to an already started route, look
				// for the nearest one
				if (y > 0)
				{
					// the last added customer
					ICustomer lastCustomer = data.getCustomers().get(rAllele.getServedCustomerIndices().get(rAllele.getServedCustomerIndices().size() - 1));
					ICustomer tmpCustomer;
					double distance = Double.MAX_VALUE;
					double tmpDistance;

					// calculate the distance from the last customer to all
					// others (available)
					for (int x = 0; x < availableCustomers.size(); x++)
					{
						tmpCustomer = data.getCustomers().get(availableCustomers.get(x));
						tmpDistance = distanceCalculator.calculate(lastCustomer.getPosition(), tmpCustomer.getPosition(), data.getVehicles().get(i))[0];

						// is this distance shorter?
						if (tmpDistance < distance)
						{
							distance = tmpDistance;
							customerIndex = x;
						}
					}
				}
				rAllele.getServedCustomerIndices().add(availableCustomers.remove(customerIndex));
			}

			initGenes[i] = new RouteGene(conf, rAllele);
		}

		// check for customers not yet passed to any gene
		while (availableCustomers.size() > 0)
		{
			int geneIndex = randomizer.nextInt(numVehicles);
			int customerIndex = randomizer.nextInt(availableCustomers.size());
			((RouteAllele) initGenes[geneIndex].getAllele()).getServedCustomerIndices().add(availableCustomers.remove(customerIndex));
		}

		for (Gene g : initGenes)
			System.out.println(g);

		return new Chromosome(conf, initGenes);
	}

	/**
	 * Generates a randomly but feasable chromosome
	 * 
	 * @param conf
	 *            the configuration needed to set up the chromosome
	 * @param data
	 *            the data the chromosome should be based on
	 * @return a feasable and randomly created chromosome
	 * @throws InvalidConfigurationException
	 */
	public static IChromosome getFeasableRandomChromosome(Configuration conf, DataSet data) throws InvalidConfigurationException
	{
		int numVehicles = data.getVehicles().size();
		int numCustomer = data.getCustomers().size();
		System.out.println("Vehicles: " + numVehicles + "  Customer: " + numCustomer);

		Gene[] initGenes = new Gene[numVehicles];
		Random randomizer = new Random();

		// write alle available indices for customers to a temp var
		ArrayList<Integer> availableCustomers = new ArrayList<Integer>();
		for (int i = 0; i < numCustomer; i++)
		{
			availableCustomers.add(i);
		}
		int customerPerGene = numCustomer / numVehicles;
		// for every gene on the chromosome (try to pass customers to each gene)
		// each gene represents one vehicle
		for (int i = 0; i < initGenes.length; i++)
		{
			RouteAllele rAllele = new RouteAllele();

			// try to find the depot this vehicle belongs to (init with 0 if we
			// do not find any depot)
			rAllele.setStartDepotIndex(0);
			String depotName = data.getVehicles().get(i).getBelongingDepotName();
			for (int depotIndex = 0; depotIndex < data.getDepots().size(); depotIndex++)
			{
				if (data.getDepots().get(depotIndex).getName().equalsIgnoreCase(depotName))
				{
					// found the right depot
					rAllele.setStartDepotIndex(depotIndex);
					break;
				}
			}

			// for every customer this gene should be assigned to
			for (int y = 0; y < customerPerGene; y++)
			{
				int customerIndex = randomizer.nextInt(availableCustomers.size());
				rAllele.getServedCustomerIndices().add(availableCustomers.remove(customerIndex));
			}

			initGenes[i] = new RouteGene(conf, rAllele);
		}

		// check for customers not yet passed to any gene
		while (availableCustomers.size() > 0)
		{
			int geneIndex = randomizer.nextInt(numVehicles);
			int customerIndex = randomizer.nextInt(availableCustomers.size());
			((RouteAllele) initGenes[geneIndex].getAllele()).getServedCustomerIndices().add(availableCustomers.remove(customerIndex));
		}

		for (Gene g : initGenes)
			System.out.println(g);

		return new Chromosome(conf, initGenes);
	}	
	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------
}
