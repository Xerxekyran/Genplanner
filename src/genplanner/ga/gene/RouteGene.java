package genplanner.ga.gene;

import java.util.ArrayList;

import org.jgap.BaseGene;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.IGeneConstraintChecker;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;
import org.jgap.UnsupportedRepresentationException;

import util.LogWriter;
import util.LogWriter.LogLevel;

/**
 * A RouteGene is a JGAP BaseGene with the special gene structure of tours
 * (RouteAllele)
 * 
 * @author Lars George
 * @version 1.0
 */
public class RouteGene extends BaseGene implements IRouteGene
{
	private static final String	MODULE_NAME			= "RouteGene";
	private static final long	serialVersionUID	= 1L;
	protected RouteAllele		allele				= null;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * 
	 * @param a_configuration
	 * @throws InvalidConfigurationException
	 */
	public RouteGene(Configuration a_configuration) throws InvalidConfigurationException
	{
		this(a_configuration, new RouteAllele());
	}

	/**
	 * 
	 * @param a_configuration
	 * @param allele
	 * @throws InvalidConfigurationException
	 */
	public RouteGene(Configuration a_configuration, RouteAllele allele) throws InvalidConfigurationException
	{
		super(a_configuration);
		this.allele = allele;
	}
	

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------
	@Override
	protected Gene newGeneInternal()
	{
		try
		{
			return new RouteGene(getConfiguration());
		}
		catch (InvalidConfigurationException ex)
		{
			throw new IllegalStateException(ex.getMessage());
		}
	}

	@Override
	public void applyMutation(int index, double percentage)
	{
		RandomGenerator rand = getConfiguration().getRandomGenerator();
		ArrayList<Integer> customerIndices = allele.getServedCustomerIndices();

		// choose a random index of a customer (can only be between 0 and
		// size-2, so we can swap the element to the right)
		int swapIndex = Math.abs(rand.nextInt(customerIndices.size() - 1));
		// wrong index? do nothing
		if (swapIndex < 0 || swapIndex > customerIndices.size() - 2)
		{
			LogWriter.getInstance().logToFile(LogLevel.Warn, MODULE_NAME, "Mutation operator generate invalid index (out of bounds): " + swapIndex);
			return;
		}

		// do the swapping
		Integer tmpVal = customerIndices.get(swapIndex);
		customerIndices.set(swapIndex, customerIndices.get(swapIndex + 1));
		customerIndices.set(swapIndex + 1, tmpVal);
	}

	/**
	 * Compares this Gene with the specified object for order. Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object. The given object
	 * must be a QuarterGene.
	 * 
	 * @param otherQuarterGene
	 *            the Object to be compared.
	 * @return a negative integer, zero, or a positive integer as this object is
	 *         less than, equal to, or greater than the specified object.
	 * 
	 * @throws ClassCastException
	 *             if the specified object's type prevents it from being
	 *             compared to this Object.
	 */
	@Override
	public int compareTo(Object otherQuarterGene)
	{
		// if there is no other Gene, we are bigger
		if (otherQuarterGene == null)
		{
			return 1;
		}

		// is our allele null?
		if (allele == null)
		{
			// then check if the other one is as well, then we are equal
			if (((RouteGene) otherQuarterGene).getAllele() == null)
			{
				return 0;
			}
			else
			// we are null, the other not, we are less
			{
				return -1;
			}
		}

		return allele.getServedCustomerIndices().size() - ((RouteAllele) ((RouteGene) otherQuarterGene).getAllele()).getServedCustomerIndices().size();
	}

	/**
	 * Creates a new copy of this object
	 */
	public Object cloneExceptCertainCustomer(ArrayList<Integer> exceptTheseCustomer)
	{
		RouteGene ret = null;
		try
		{
			ret = new RouteGene(getConfiguration());
		}
		catch (InvalidConfigurationException e)
		{
			e.printStackTrace();
		}
		ArrayList<Integer> servedCustomer = new ArrayList<Integer>();
		boolean found = false;
		for (int i : allele.getServedCustomerIndices())
		{
			found = false;

			if (exceptTheseCustomer != null)
			{
				for (int j = 0; j < exceptTheseCustomer.size(); j++)
				{
					if (exceptTheseCustomer.get(j).equals(i))
					{
						found = true;
						break;
					}
				}
			}
			if (!found)
				servedCustomer.add(new Integer(i));
		}

		ret.allele = new RouteAllele(servedCustomer, new Integer(allele.getStartDepotIndex()));

		return ret;
	}

	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		str.append("ROADROUTE::Customerindices:");
		for (Integer i : this.allele.getServedCustomerIndices())
			str.append(i + " ");

		return str.toString();
	}
	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------
	@Override
	public void setAllele(Object arg0)
	{
		// if we have a constraint checker
		IGeneConstraintChecker checker = getConstraintChecker();
		if (checker != null)
		{
			// is this a valide change request?
			if (checker.verify(this, arg0, null, -1))
			{
				// then change it
				this.allele = (RouteAllele) arg0;
			}
		}
		else
		{
			// no constraint checker, we can simply change the allele value
			this.allele = (RouteAllele) arg0;
		}
	}

	@Override
	public Object getAllele()
	{
		return this.allele;
	}

	@Override
	public void setToRandomValue(RandomGenerator numberGenerator)
	{
		allele = new RouteAllele();
		ArrayList<Integer> customers = allele.getServedCustomerIndices();
		for (Integer customerIndex : customers)
		{
			String a = "Before: " + customerIndex;
			customerIndex = (numberGenerator.nextInt());
			a.concat(" After: " + customerIndex);

		}
		allele.setServedCustomerIndices(customers);
	}

	@Override
	public void setValueFromPersistentRepresentation(String arg0) throws UnsupportedOperationException, UnsupportedRepresentationException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getPersistentRepresentation() throws UnsupportedOperationException
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		// return null;
	}
	
	@Override
	protected Object getInternalValue()
	{
		return allele;
	}
	
}
