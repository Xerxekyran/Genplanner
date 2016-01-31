package genplanner.domain.depot;

import genplanner.domain.Position;
import genplanner.domain.TimeWindow;

import java.util.HashMap;

/**
 * This interface declares the needed methods for all depot classes
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public interface IDepot
{
	/**
	 * Input method to fill the object with data
	 * 
	 * @param data
	 *            A Map of AttributeName and a string representing the value
	 */
	public void setData(HashMap<String, String> data);

	/**
	 * Getter for the position
	 * 
	 * @return The position of the depot
	 */
	public Position getPosition();

	/**
	 * Getter for the name
	 * 
	 * @return The name of the depot
	 */
	public String getName();

	/**
	 * Getter of the time window
	 * 
	 * @return The time window of the depot
	 */
	public TimeWindow getTimeWindow();
}
