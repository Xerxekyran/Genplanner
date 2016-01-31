package genplanner.services;

import genplanner.domain.DataSet;
import genplanner.domain.solution.ISolution;
import genplanner.exception.FileFormatException;
import genplanner.ga.CalculationConfiguration;
import genplanner.ga.ELogisticTaskStatus;
import genplanner.ga.LogisticTask;
import genplanner.ga.LogisticTaskManager;
import genplanner.persistence.dao.DAOFactory;
import genplanner.persistence.dao.ICustomerDAO;
import genplanner.persistence.dao.IDepotDAO;
import genplanner.persistence.dao.ISolutionDAO;
import genplanner.persistence.dao.IVehicleDAO;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import util.LogWriter;
import util.Pair;
import util.XMLLoader;
import util.XMLManager;
import util.LogWriter.LogLevel;

/**
 * Implementation of a Genplanner-Service. Offers methods to control genetic
 * algorithm calculations
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class GenplannerService implements IGenplannerService
{
	private static final String	MODULE_NAME		= "GenplannerService";

	private ICustomerDAO		customerDAO		= null;
	private IVehicleDAO			vehicleDAO		= null;
	private IDepotDAO			depotDAO		= null;
	private ISolutionDAO		solutionDAO		= null;
	private LogisticTaskManager	logisticManager	= null;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * 
	 */
	public GenplannerService()
	{
		logisticManager = LogisticTaskManager.getInstance();
		customerDAO = (ICustomerDAO) DAOFactory.getInstance().getDaoForClass(ICustomerDAO.class);
		vehicleDAO = (IVehicleDAO) DAOFactory.getInstance().getDaoForClass(IVehicleDAO.class);
		depotDAO = (IDepotDAO) DAOFactory.getInstance().getDaoForClass(IDepotDAO.class);
		solutionDAO = (ISolutionDAO) DAOFactory.getInstance().getDaoForClass(ISolutionDAO.class);
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------
	@Override
	public ISolution getCurrentSolutionForLogisticTaskID(int id)
	{
		ISolution ret = null;

		LogisticTask logTask = logisticManager.getTaskByID(id);
		if (logTask != null)
		{
			ret = logTask.getCurrentSolution();
		}
		return ret;
	}

	@Override
	public ELogisticTaskStatus getStatusOfLogisticTask(int id)
	{
		ELogisticTaskStatus ret = ELogisticTaskStatus.Error;

		LogisticTask logTask = logisticManager.getTaskByID(id);
		if (logTask != null)
		{
			ret = logTask.getStatus();
		}
		return ret;
	}

	@Override
	public int createLogisticPlanCalculation(String path, String configurationFileName, CalculationConfiguration conf) throws FileNotFoundException, FileFormatException
	{
		XMLLoader loader = XMLManager.getInstance().getXMLLoader(path + "/" + configurationFileName);
		try
		{
			DataSet data = new DataSet();

			// get the lists of used data
			data.setCustomers(customerDAO.loadCustomers(path + "/" + loader.getElement("Customers").getAttributeValue("file")));
			data.setDepots(depotDAO.loadDepots(path + "/" + loader.getElement("Depots").getAttributeValue("file")));
			data.setVehicles(vehicleDAO.loadVehicles(path + "/" + loader.getElement("Vehicles").getAttributeValue("file")));

			return logisticManager.createNewLogisticTask(data, conf);
		}
		catch (Exception e)
		{
			LogWriter.getInstance().logToFile(LogLevel.Error, MODULE_NAME, e.toString());
			throw new FileFormatException("Error parsing configuration file: " + path + configurationFileName + " [" + e.toString() + "]");
		}
	}

	@Override
	public int createLogisticPlanCalculation(DataSet data, CalculationConfiguration conf)
	{
		return logisticManager.createNewLogisticTask(data, conf);
	}

	@Override
	public void setStatusOfLogisticPlanCalculation(int logisticTaskID, ELogisticTaskStatus status) throws NoSuchElementException
	{
		LogisticTask task = logisticManager.getTaskByID(logisticTaskID);
		if (task == null)
			throw new NoSuchElementException("No LogisticTask could be found with this ID: " + logisticTaskID);

		// if we are setting the status from paused to running, check if the
		// maximum number of generations had been reached -> set the max higher
		if (task.getStatus().equals(ELogisticTaskStatus.Paused) && status.equals(ELogisticTaskStatus.Running))
		{
			task.increaseGenerationcounterIfAtMax();
		}

		task.setStatus(status);
	}

	@Override
	public LinkedList<Pair<Double, Double>> getFitnessHistoryForLogisticTaskID(int id)
	{
		LinkedList<Pair<Double, Double>> ret = null;

		LogisticTask logTask = logisticManager.getTaskByID(id);
		if (logTask != null)
		{
			ret = logTask.getFitnessHistory();
		}
		return ret;
	}

	@Override
	public LinkedList<Pair<Double, Double>> getAverageFitnessHistoryForLogisticTaskID(int id)
	{
		LinkedList<Pair<Double, Double>> ret = null;

		LogisticTask logTask = logisticManager.getTaskByID(id);
		if (logTask != null)
		{
			ret = logTask.getAvgFitnessHistory();
		}
		return ret;
	}

	@Override
	public boolean saveSolution(ISolution solution, int solutionIndex, String fileName)
	{
		return solutionDAO.saveSolution(solution, solutionIndex, fileName);
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------

}
