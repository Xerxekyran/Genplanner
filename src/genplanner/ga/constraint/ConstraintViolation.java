package genplanner.ga.constraint;

/**
 * A ConstraintViolation represents a not optimal part in a route. It has a type
 * showing the kind of the violation (time window, vehicle load...), a value for
 * the scale of the violation (higher values mean, that the violation is
 * stronger -> even more overloaded truck ) and a customerIndex if this
 * violation belongs to a certain customer
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class ConstraintViolation
{
	public static double	VehicleLoadContraintWeight	= 50;
	public static double	TimeWindowConstraintWeight	= 25;
	public static double	WorkTimeConstraintWeight	= 0.05;
	private EConstraints	type;
	private double			value						= 0.0;
	private int				customerIndex				= 0;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * Ctor for a constraint violation
	 * 
	 * @param type
	 *            the type of the violation
	 * @param value
	 *            the representing value (eg. the amount of time a time window
	 *            violation was done)
	 * @param belongsToCustomerIndex
	 *            if this violation belongs to a certain customer, his index
	 *            should be given here, otherwise -1
	 */
	public ConstraintViolation(EConstraints type, double value, int belongsToCustomerIndex)
	{
		this.type = type;
		this.value = Math.abs(value);
		this.customerIndex = belongsToCustomerIndex;
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
	public EConstraints getType()
	{
		return type;
	}

	public double getValue()
	{
		return value;
	}

	public double getWeightedValue()
	{
		double ret = value;
		switch (type)
		{
		case TimeWindow:
			ret *= TimeWindowConstraintWeight;
			break;
		case VehicleLoad:
			ret *= VehicleLoadContraintWeight;
			break;
		case WorkTime:
			ret *= WorkTimeConstraintWeight;
			break;
		}

		return ret;
	}

	public int getCustomerIndex()
	{
		return customerIndex;
	}

}
