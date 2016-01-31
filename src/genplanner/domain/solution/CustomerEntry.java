package genplanner.domain.solution;

import genplanner.domain.customer.ICustomer;
import genplanner.ga.constraint.ConstraintViolation;

import java.util.LinkedList;

/**
 * This class represents a customer entry in a logistic plan. it holds an
 * customer and additional information like the constraint violations belonging
 * to him
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class CustomerEntry
{
	private ICustomer						customer	= null;
	private double							arrivalTime	= 0.0;
	private LinkedList<ConstraintViolation>	violations	= null;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * constructor with data initialisiation
	 * 
	 * @param customer
	 *            the customer for this entry
	 * @param violations
	 *            the violations belonging to the given customer
	 * @param arrivalTime
	 *            the arrival time of the vehicle at the given customer
	 */
	public CustomerEntry(ICustomer customer, LinkedList<ConstraintViolation> violations, double arrivalTime)
	{
		this.customer = customer;
		this.violations = violations;
		this.arrivalTime = arrivalTime;
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
	public ICustomer getCustomer()
	{
		return customer;
	}

	public void setCustomer(ICustomer customer)
	{
		this.customer = customer;
	}

	public LinkedList<ConstraintViolation> getViolations()
	{
		return violations;
	}

	public void setViolations(LinkedList<ConstraintViolation> violations)
	{
		this.violations = violations;
	}

	public double getArrivalTime()
	{
		return arrivalTime;
	}

	public void setArrivalTime(double arrivalTime)
	{
		this.arrivalTime = arrivalTime;
	}
}
