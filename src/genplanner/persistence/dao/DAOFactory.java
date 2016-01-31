package genplanner.persistence.dao;

import genplanner.persistence.dao.xml.XMLCustomerDAO;
import genplanner.persistence.dao.xml.XMLDepotDAO;
import genplanner.persistence.dao.xml.XMLSolutionDAO;
import genplanner.persistence.dao.xml.XMLVehicleDAO;

import java.util.HashMap;

/**
 * Factory class for Data Access Objects
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class DAOFactory
{
	private HashMap<Class<? extends IDAO>, IDAO>	daos		= new HashMap<Class<? extends IDAO>, IDAO>();
	private static DAOFactory						instance	= null;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * Private constructor for the singleton pattern
	 */
	private DAOFactory()
	{
		// create the CustomerDAO
		ICustomerDAO customerDAO = new XMLCustomerDAO();
		IVehicleDAO vehicleDAO = new XMLVehicleDAO();
		IDepotDAO depotDAO = new XMLDepotDAO();
		ISolutionDAO solutionDAO = new XMLSolutionDAO();

		// Put the DAOs into the list
		addDao(ICustomerDAO.class, customerDAO);
		addDao(IVehicleDAO.class, vehicleDAO);
		addDao(IDepotDAO.class, depotDAO);
		addDao(ISolutionDAO.class, solutionDAO);
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------
	/**
	 * 
	 * @param newClass
	 *            the new class that should be available in the factory
	 * @param dao
	 *            the DAO object itself that will be returned if requested
	 */
	private void addDao(Class<? extends IDAO> newClass, IDAO dao)
	{
		daos.put(newClass, dao);
	}

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------
	/**
	 * Singleton retrieving method
	 * 
	 * @return the reference to the singleton DAOFactory object
	 */
	public static DAOFactory getInstance()
	{
		if (instance == null)
			instance = new DAOFactory();

		return instance;
	}

	/**
	 * 
	 * @param type
	 *            Class of the DAO that should be retrieved
	 * @return a DAO-object of the given type, null if no belonging DAO could be
	 *         found
	 */
	public IDAO getDaoForClass(Class<? extends IDAO> type)
	{
		return daos.get(type);
	}
}
