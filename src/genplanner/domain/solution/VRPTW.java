package genplanner.domain.solution;

import genplanner.domain.vehicle.IVehicle;
import genplanner.ga.constraint.ConstraintViolation;
import genplanner.language.LanguageManager;
import genplanner.language.messages.GenplannerMessageBundles;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * A solution class for a VRPTW (Vehicle Routing Problem with Time Windows). It
 * holds the whole information that is calculated by the algorithm
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class VRPTW implements ISolution
{
	private static final long						serialVersionUID	= 1L;

	protected LinkedList<LinkedList<LogisticPlan>>	plans				= null;
	protected LinkedList<Double>					fitnessValues		= null;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * Constructor with value init
	 * 
	 * @param plans
	 *            a list of logistic plans that belong to this solution
	 * 
	 * @param fitnessValues
	 *            the fitness values of the logistic plans
	 */
	public VRPTW(LinkedList<LinkedList<LogisticPlan>> plans, LinkedList<Double> fitnessValues)
	{
		this.plans = plans;
		this.fitnessValues = fitnessValues;
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------
	@Override
	public String getTourDetailsAsString(int solutionIndex, int routeIndex)
	{
		// get localized text
		ResourceBundle text = LanguageManager.getInstance().getMessageBundle(GenplannerMessageBundles.INTERNALFRAME_SOLUTIONBROWSER_MESSAGES);

		DecimalFormat df = new DecimalFormat("#.00");

		StringBuilder ret = new StringBuilder();
		if (plans != null && solutionIndex < plans.size())
		{
			if (plans.get(solutionIndex) != null && plans.get(solutionIndex).size() > routeIndex)
			{
				LogisticPlan plan = plans.get(solutionIndex).get(routeIndex);

				ret.append(text.getString("depot") + ": \n");
				ret.append(plan.getDepot().toString() + "\n");
				ret.append("\n" + text.getString("customers") + ":\n");
				for (CustomerEntry custEntr : plan.getCustomer())
				{
					ret.append(custEntr.getCustomer().toString());

					ret.append(" \t" + text.getString("arriving") + ": " + df.format(custEntr.getArrivalTime()));

					// for every violation
					if (custEntr.getViolations() != null)
					{
						for (ConstraintViolation viol : custEntr.getViolations())
						{
							ret.append(" [" + viol.getType().toString() + "]");
						}
					}
					ret.append("\n");
				}

				ret.append("\n");
				ret.append(text.getString("problems") + ": \n");
				for (ConstraintViolation viol : plan.getViolations())
				{
					ret.append("- " + viol.getType().toString() + "\n");
				}
			}
		}
		return ret.toString();
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------
	@Override
	public LinkedList<LinkedList<LogisticPlan>> getPlans()
	{
		return plans;
	}

	@Override
	public IVehicle getVehicle(int vehicleIndex)
	{
		return plans.get(0).get(vehicleIndex).getVehicle();
	}

	@Override
	public Double getFitnessValue(int solutionIndex)
	{
		return fitnessValues.get(solutionIndex);
	}
}
