package genplanner.domain.map;

import genplanner.domain.Position;
import genplanner.domain.vehicle.IVehicle;

/**
 * Implementation of a DistanceCalculator. Uses the vehicle speed and calculates
 * the distance directly between the two positions (distance = sqrt(x²+y²))
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class SimpleCalculator implements IDistanceCalculator
{

	@Override
	public double[] calculate(Position from, Position to, IVehicle withVehicle)
	{
		double[] ret = new double[2];

		// first calculate the distance
		double ySeparation = to.getY() - from.getY();
		double xSeparation = to.getX() - from.getX();
		ret[0] = Math.sqrt(ySeparation * ySeparation + xSeparation * xSeparation);

		// now calculate the needed time for that distance
		ret[1] = ret[0] / withVehicle.getSpeedValue();

		return ret;
	}

}
