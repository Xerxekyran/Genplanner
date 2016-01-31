package genplanner.persistence.dao;

import genplanner.domain.customer.ICustomer;

import java.io.FileNotFoundException;
import java.util.LinkedList;

/**
 * A customer Data Access Object interface that handles storage and retrieval
 * data using XML files
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public interface ICustomerDAO extends IDAO
{
	/**
	 * This method loads all customer in the given source and returns them as a
	 * list.
	 * 
	 * @param fileName
	 *            the path to a file containing information about customers
	 * @return A list of customer as described in the given file
	 * @throws FileNotFoundException
	 *             If the given file could not be found
	 * @throws ClassNotFoundException
	 *             If the class in the XML-File could not be found (wrong data
	 *             or version)
	 */
	public LinkedList<ICustomer> loadCustomers(String fileName) throws FileNotFoundException, ClassNotFoundException;
}
