package genplanner.domain.map;

import genplanner.domain.Position;
import genplanner.domain.vehicle.IVehicle;

/**
 * An Interface for distance calculators. These classes need to calculate the
 * distance between two positions and the time that is needed to travel between
 * them with the given vehicle type
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public interface IDistanceCalculator
{
	/**
	 * Calculates the travel distance between the two given points and the time
	 * that is needed using the given vehicle
	 * 
	 * @param from
	 *            startposition of the calculation
	 * @param to
	 *            endposition of the calculation
	 * @param withVehicle
	 *            the vehicle the distance should be traveled with (needed for
	 *            the speed evaluation)
	 * @return a two dimensional array with the travelled distance and needed
	 *         time for the segment: [distance][needTime]
	 */
	public double[] calculate(Position from, Position to, IVehicle withVehicle);
}
