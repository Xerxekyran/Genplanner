package genplanner.domain.solution;

import genplanner.domain.vehicle.IVehicle;

import java.util.LinkedList;

/**
 * This interface is for wrapper objects of solution data. These objects hold a
 * list (not only the best) of possible solutions for the given logistic plan.
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public interface ISolution
{
	/**
	 * Getter of a string representation of a certain tour in thesolution
	 * object.
	 * 
	 * @param solutionIndex
	 *            The index of the solution that should be analyzed
	 * @param routeIndex
	 *            The route in the selected solution that should be analyzed
	 * @return The string representation of a tour
	 */
	public String getTourDetailsAsString(int solutionIndex, int routeIndex);

	/**
	 * Getter for the logistic plans
	 * 
	 * @return A list of all plans in the solution object (a list of lists,
	 *         because the first list holds all solutions, the second one is a
	 *         list of plans for one whole plans)
	 */
	public LinkedList<LinkedList<LogisticPlan>> getPlans();

	/**
	 * Getter for a certain vehicle
	 * 
	 * @param vehicleIndex
	 *            The index of the vehicle
	 * @return A certain vehicle in the solution
	 */
	public IVehicle getVehicle(int vehicleIndex);

	/**
	 * Getter for the fitness value of vertain solution
	 * 
	 * @param solutionIndex
	 *            the solution the index value should be retrieved from
	 * @return the fitness value of a certain solution (calculated from the
	 *         algorithm)
	 */
	public Double getFitnessValue(int solutionIndex);
}
