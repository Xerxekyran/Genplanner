package genplanner.domain.solution;

import genplanner.domain.depot.IDepot;
import genplanner.domain.vehicle.IVehicle;
import genplanner.ga.constraint.ConstraintViolation;

import java.util.LinkedList;

/**
 * A LogisticPlan represents one Plan for a certain vehicle. It holds a list of
 * customers that are visited within this plan, the used vehicle, the start
 * depot and informations about constraint violations
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class LogisticPlan
{
	private LinkedList<ConstraintViolation>	violations		= null;
	private LinkedList<CustomerEntry>		customer		= null;
	private IVehicle						vehicle			= null;
	private IDepot							depot			= null;
	private double							travelDistance	= 0.0;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * Constructor with value initialisation
	 * 
	 * @param vehicle
	 *            the vehicle this plan belongs to
	 * @param depot
	 *            the depot the vehicle of the plan belongs to
	 * @param customer
	 *            the customers that are visited with this plan (plus constraint
	 *            violation information if existing)
	 * @param violations
	 *            constraint violations belonging to the plan (like too many
	 *            working hours)
	 * @param traveledDistance
	 *            the distance the vehicle needs to travel with this plan
	 * 
	 */
	public LogisticPlan(IVehicle vehicle, IDepot depot, LinkedList<CustomerEntry> customer, LinkedList<ConstraintViolation> violations, double traveledDistance)
	{
		this.vehicle = vehicle;
		this.depot = depot;
		this.customer = customer;
		this.violations = violations;
		this.travelDistance = traveledDistance;
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
	public LinkedList<ConstraintViolation> getViolations()
	{
		return violations;
	}

	public void setViolations(LinkedList<ConstraintViolation> violations)
	{
		this.violations = violations;
	}

	public IVehicle getVehicle()
	{
		return vehicle;
	}

	public void setVehicle(IVehicle vehicle)
	{
		this.vehicle = vehicle;
	}

	public IDepot getDepot()
	{
		return depot;
	}

	public void setDepot(IDepot depot)
	{
		this.depot = depot;
	}

	public LinkedList<CustomerEntry> getCustomer()
	{
		return customer;
	}

	public void setCustomer(LinkedList<CustomerEntry> customer)
	{
		this.customer = customer;
	}

	public double getTravelDistance()
	{
		return travelDistance;
	}

	public void setTravelDistance(double travelDistance)
	{
		this.travelDistance = travelDistance;
	}

}
