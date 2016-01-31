package genplanner.domain.vehicle;

import java.util.HashMap;

/**
 * This interface declares the needed methods for all vehicle classes
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public interface IVehicle
{
	/**
	 * Input method to fill the object with data
	 * 
	 * @param data
	 *            A Map of AttributeName and a string representing the value
	 */
	public void setData(HashMap<String, String> data);

	/**
	 * 
	 * @return The Name of the Vehicle
	 */
	public String getName();

	/**
	 * Returns the name of the depot this vehicle belongs to
	 * 
	 * @return The name of the depot this vehicle is connected to. if nothing is
	 *         set an empty string will be returned
	 */
	public String getBelongingDepotName();

	/**
	 * Returns the speed value of this vehicle in Minutes per KM
	 * 
	 * @return The Minute per KM value this vehicle drives (a value of 2 means
	 *         that the vehicle travels 1 km in 2 minutes)
	 */
	public double getSpeedValue();

	/**
	 * Getter of the load volume of the vehicle
	 * 
	 * @return The load volume for this vehicle
	 */
	public double getLoadVolume();
}
