package genplanner.domain.customer;

import genplanner.domain.Position;
import genplanner.domain.TimeWindow;

import java.util.HashMap;

/**
 * This interface declares the needed methods for all customer classes
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public interface ICustomer
{
	/**
	 * Input method to fill the object with data
	 * 
	 * @param data
	 *            A Map of AttributeName and a string representing the value
	 */
	public void setData(HashMap<String, String> data);

	/**
	 * Getter of the name
	 * 
	 * @return The name of the customer
	 */
	public String getName();

	/**
	 * Getter of the position
	 * 
	 * @return The position of the customer
	 */
	public Position getPosition();

	/**
	 * Getter of the time window
	 * 
	 * @return The time window of the customer
	 */
	public TimeWindow getTimeWindow();

	/**
	 * Getter of the service time (time that is needed to work at the customers
	 * home)
	 * 
	 * @return The service time of the customer
	 */
	public int getServiceTime();

	/**
	 * Getter of the demand size (e.g. size of the package that was ordered)
	 * 
	 * @return The demand size of the customer
	 */
	public double getDemandSize();
}
