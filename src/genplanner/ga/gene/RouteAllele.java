package genplanner.ga.gene;

import genplanner.ga.constraint.ConstraintViolation;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A RouteAllele represents the current Data for one Gene (list of visited
 * customer indices and the depotIndex)
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class RouteAllele
{
	private ArrayList<Integer>				servedCustomerIndices	= null;
	private Integer							startDepotIndex			= null;
	private LinkedList<ConstraintViolation>	violations				= null;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------

	/**
	 * Constructor with given init values
	 * 
	 * @param servedCustomerIndices
	 *            list of customer indices (in a DataSet)
	 * @param startDepotIndex
	 *            the startDepot index (in a DataSet)
	 */
	public RouteAllele(ArrayList<Integer> servedCustomerIndices, Integer startDepotIndex)
	{
		this.servedCustomerIndices = servedCustomerIndices;
		this.startDepotIndex = startDepotIndex;
	}

	/**
	 * Default ctor
	 */
	public RouteAllele()
	{
		this(new ArrayList<Integer>(), new Integer(-1));
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------
	@Override
	public String toString()
	{
		StringBuilder ret = new StringBuilder();
		for (Integer i : servedCustomerIndices)
			ret.append(i + " ");
		return ret.toString();
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------
	public ArrayList<Integer> getServedCustomerIndices()
	{
		return servedCustomerIndices;
	}

	public void setServedCustomerIndices(ArrayList<Integer> servedCustomerIndices)
	{
		this.servedCustomerIndices = servedCustomerIndices;
	}

	public Integer getStartDepotIndex()
	{
		return startDepotIndex;
	}

	public void setStartDepotIndex(Integer startDepotIndex)
	{
		this.startDepotIndex = startDepotIndex;
	}

	public LinkedList<ConstraintViolation> getViolations()
	{
		return violations;
	}

	public void setViolations(LinkedList<ConstraintViolation> violations)
	{
		this.violations = violations;
	}
}
