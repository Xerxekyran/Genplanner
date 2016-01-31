package genplanner.domain;

import genplanner.domain.customer.ICustomer;
import genplanner.domain.depot.IDepot;
import genplanner.domain.vehicle.IVehicle;
import genplanner.ga.gene.IRouteGene;

import java.util.LinkedList;

/**
 * A DataSet contains information about customers depots vehicles and the
 * IRouteGene class. It holds the actual data, that is used in other parts of
 * the application.
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class DataSet
{
	private LinkedList<ICustomer>	customers	= null;
	private LinkedList<IDepot>		depots		= null;
	private LinkedList<IVehicle>	vehicles	= null;
	private IRouteGene				routeType	= null;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * 
	 */
	public DataSet()
	{
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
	public LinkedList<ICustomer> getCustomers()
	{
		return customers;
	}

	public void setCustomers(LinkedList<ICustomer> customers)
	{
		this.customers = customers;
	}

	public LinkedList<IDepot> getDepots()
	{
		return depots;
	}

	public void setDepots(LinkedList<IDepot> depots)
	{
		this.depots = depots;
	}

	public IRouteGene getRouteType()
	{
		return routeType;
	}

	public void setRouteType(IRouteGene routeType)
	{
		this.routeType = routeType;
	}

	public LinkedList<IVehicle> getVehicles()
	{
		return vehicles;
	}

	public void setVehicles(LinkedList<IVehicle> vehicles)
	{
		this.vehicles = vehicles;
	}
}
