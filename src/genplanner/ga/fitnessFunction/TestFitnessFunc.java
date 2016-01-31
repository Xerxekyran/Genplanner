package genplanner.ga.fitnessFunction;

import genplanner.domain.DataSet;
import genplanner.domain.Position;
import genplanner.domain.customer.ICustomer;
import genplanner.domain.depot.IDepot;
import genplanner.domain.map.IDistanceCalculator;
import genplanner.domain.vehicle.IVehicle;
import genplanner.ga.constraint.ConstraintViolation;
import genplanner.ga.constraint.EConstraints;
import genplanner.ga.gene.IRouteGene;
import genplanner.ga.gene.RouteAllele;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.IChromosome;

import util.LogWriter;
import util.LogWriter.LogLevel;

/**
 * A first implementation of a fitness function. It uses penalty operators to
 * calculate how good a solution is. The penalty opertors are differentyl
 * weighted and can be configured in the config.xml file
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class TestFitnessFunc extends FitnessFunction
{
	private static final String	MODULE_NAME					= "TestFitnessFunc";
	private static final long	serialVersionUID			= 1L;
	private IDistanceCalculator	calculator					= null;
	private ICustomer[]			customerData				= null;
	private IDepot[]			depotData					= null;
	private IVehicle[]			vehicleData					= null;
	public static double		TravelCostWeight			= 0.5;
	private double				CustomerNotVisitedWeight	= 1000.0;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * A ctor with data initialisation
	 * 
	 * @param data
	 *            the dataSet the calculation is based on
	 * @param distanceCalculator
	 *            a distance calculation object for distance type calculations
	 */
	public TestFitnessFunc(DataSet data, IDistanceCalculator distanceCalculator)
	{
		super();
		try
		{
			customerData = new ICustomer[data.getCustomers().size()];
			depotData = new IDepot[data.getDepots().size()];
			vehicleData = new IVehicle[data.getVehicles().size()];
			this.calculator = distanceCalculator;

			data.getCustomers().toArray(customerData);
			data.getDepots().toArray(depotData);
			data.getVehicles().toArray(vehicleData);

		}
		catch (Exception e)
		{
			LogWriter.getInstance().logToFile(LogLevel.Error, MODULE_NAME, e.toString());
			System.out.println("Received wrong data!!" + e.toString());
		}
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	@Override
	protected double evaluate(IChromosome chrom)
	{
		// make a list of all available customers (to check if we visited them
		// all)
		Set<Integer> visitedCustomerIndices = new HashSet<Integer>(customerData.length);

		double totalSolutionLength = 0.0;
		ArrayList<ConstraintViolation> violations = new ArrayList<ConstraintViolation>();
		double[] currentDistanceCalculation;
		Gene[] routes = chrom.getGenes();

		// -------------------------------------------
		// for every route on the solution
		for (int routeIndex = 0; routeIndex < routes.length; routeIndex++)
		{
			double currentTime = 0.0;
			double load = 0.0;

			// read the gene data
			IRouteGene route = (IRouteGene) routes[routeIndex];
			ArrayList<Integer> servedCustomersIndices = ((RouteAllele) route.getAllele()).getServedCustomerIndices();
			Integer depotIndex = ((RouteAllele) route.getAllele()).getStartDepotIndex();
			visitedCustomerIndices.addAll(servedCustomersIndices);

			// create a new list of violations
			((RouteAllele) route.getAllele()).setViolations(new LinkedList<ConstraintViolation>());

			Position p1 = null;
			Position p2 = null;

			// are there any customers and a depot? -> add the distances from
			// depot to first
			if (servedCustomersIndices.size() > 0 && depotIndex > -1)
			{
				p1 = depotData[depotIndex].getPosition();
				p2 = customerData[servedCustomersIndices.get(0)].getPosition();

				// calculate distance and needed time
				currentDistanceCalculation = calculator.calculate(p1, p2, vehicleData[routeIndex]);

				// add the depot to first customer distance
				totalSolutionLength += currentDistanceCalculation[0];

				// calculate time until arival
				currentTime += currentDistanceCalculation[1];

				// check if we arrived to soon
				if (customerData[servedCustomersIndices.get(0)].getTimeWindow().getStartTime() > currentTime)
				{
					// Wait until the customer wants to be delivered
					currentTime = customerData[servedCustomersIndices.get(0)].getTimeWindow().getStartTime();
				}

				// check if we are too late (constraint violation)
				if (customerData[servedCustomersIndices.get(0)].getTimeWindow().getEndTime() < currentTime)
				{
					ConstraintViolation violation = new ConstraintViolation(EConstraints.TimeWindow, currentTime - customerData[servedCustomersIndices.get(0)].getTimeWindow().getEndTime(), servedCustomersIndices.get(0));
					violations.add(violation);
					((RouteAllele) route.getAllele()).getViolations().add(violation);
				}

				// add the service time (time needed to work at the customer)
				currentTime += customerData[servedCustomersIndices.get(0)].getServiceTime();

				// add the load that is needed at this customer
				load += customerData[servedCustomersIndices.get(0)].getDemandSize();
			}

			// for every customer on the route
			for (int customerIndex = 0; customerIndex < servedCustomersIndices.size() - 1; customerIndex++)
			{
				p1 = customerData[servedCustomersIndices.get(customerIndex)].getPosition();
				p2 = customerData[servedCustomersIndices.get(customerIndex + 1)].getPosition();

				// calculate distance and needed time
				currentDistanceCalculation = calculator.calculate(p1, p2, vehicleData[routeIndex]);

				// add the depot to first customer distance
				totalSolutionLength += currentDistanceCalculation[0];

				// calculate time until arival
				currentTime += currentDistanceCalculation[1];

				// check if we arrived to soon
				if (customerData[servedCustomersIndices.get(customerIndex + 1)].getTimeWindow().getStartTime() > currentTime)
				{
					// Wait until the customer wants to be delivered
					currentTime = customerData[servedCustomersIndices.get(customerIndex + 1)].getTimeWindow().getStartTime();
				}

				// check if we are too late (constraint violation)
				if (customerData[servedCustomersIndices.get(customerIndex + 1)].getTimeWindow().getEndTime() < currentTime)
				{
					ConstraintViolation violation = new ConstraintViolation(EConstraints.TimeWindow, currentTime - customerData[servedCustomersIndices.get(customerIndex + 1)].getTimeWindow().getEndTime(), servedCustomersIndices.get(customerIndex + 1));
					violations.add(violation);
					((RouteAllele) route.getAllele()).getViolations().add(violation);
				}

				// add the service time (time needed to work at the customer)
				currentTime += customerData[servedCustomersIndices.get(customerIndex + 1)].getServiceTime();

				// add the load that is needed at this customer
				load += customerData[servedCustomersIndices.get(customerIndex + 1)].getDemandSize();
			}

			// are there any customers and a depot? -> add the distances from
			// last cutstomer to depot
			if (servedCustomersIndices.size() > 0 && depotIndex > -1)
			{
				// the the last customer to depot distance
				p1 = customerData[servedCustomersIndices.get(servedCustomersIndices.size() - 1)].getPosition();
				p2 = depotData[depotIndex].getPosition();

				// calculate distance and needed time
				currentDistanceCalculation = calculator.calculate(p1, p2, vehicleData[routeIndex]);

				// add the depot to first customer distance
				totalSolutionLength += currentDistanceCalculation[0];

				// calculate time until arival
				currentTime += currentDistanceCalculation[1];
			}

			// -------------------------------------------
			// check if the vehicle has too much to carry
			if (load > vehicleData[routeIndex].getLoadVolume())
			{
				ConstraintViolation violation = new ConstraintViolation(EConstraints.VehicleLoad, vehicleData[routeIndex].getLoadVolume() - load, -1);
				violations.add(violation);
				((RouteAllele) route.getAllele()).getViolations().add(violation);
			}

			// -------------------------------------------
			// check for the working time of this vehicle (depends on the depot
			// it belongs to)
			if (currentTime > depotData[depotIndex].getTimeWindow().getEndTime())
			{
				ConstraintViolation violation = new ConstraintViolation(EConstraints.WorkTime, currentTime - depotData[depotIndex].getTimeWindow().getEndTime(), -1);
				violations.add(violation);
				((RouteAllele) route.getAllele()).getViolations().add(violation);
			}
		}

		// Weight the calculated route length
		totalSolutionLength *= TravelCostWeight;

		// did we visit all customers?
		if (visitedCustomerIndices.size() != customerData.length)
		{
			// just add a high penalty. we need to accept these solutions as
			// well, because some operations need fitness values for not fully
			// completed chromosomes
			totalSolutionLength += (customerData.length - visitedCustomerIndices.size()) * CustomerNotVisitedWeight;
		}

		// add the constraint weights to the solution length
		for (ConstraintViolation violation : violations)
		{
			totalSolutionLength += violation.getWeightedValue();
		}

		if (totalSolutionLength == 0)
		{
			return 0;
		}
		else
		{
			return (10000 / totalSolutionLength);
		}
	}

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------
	public IDistanceCalculator getDistanceCalculator()
	{
		return calculator;
	}
}
