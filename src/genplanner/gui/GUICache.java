package genplanner.gui;

import genplanner.domain.solution.ISolution;
import genplanner.ga.ELogisticTaskStatus;
import genplanner.services.EServices;
import genplanner.services.IGenplannerService;
import genplanner.services.ServiceLocator;

import java.util.Date;
import java.util.LinkedList;

import util.Pair;

/**
 * This class holds cached data and retrieves new data if needed from the
 * service layer
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class GUICache
{
	private static Date								lastAccess			= new Date();

	private static Integer							logisticTaskID		= -1;
	private static int								selectedSolution	= 0;
	private static int								selectedTour		= -1;
	private static ISolution						currentSolution		= null;
	private static LinkedList<Pair<Double, Double>>	fittnesHistory		= null;
	private static LinkedList<Pair<Double, Double>>	avgFittnesHistory	= null;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	/**
	 * Checks if this object data is up to date
	 * 
	 * @return true if nothing should be changed
	 */
	private static boolean isUpToDate()
	{
		boolean ret = false;
		// for now only check if we did something in the last second, to avoid
		// requesting the service too fast getting
		if (((new Date().getTime()) - lastAccess.getTime()) > 1000)
			ret = false;

		lastAccess = new Date();

		return ret;
	}

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------
	/**
	 * Getter of the newest version of the solution.
	 * 
	 * @return the solution in the cache. if it is not up to date, the cache
	 *         will be refreshed first.
	 */
	public static ISolution getSolution()
	{
		if (GUICache.currentSolution == null || !isUpToDate())
		{
			IGenplannerService service = (IGenplannerService) ServiceLocator.getInstance().getService(EServices.Genplanner);
			if (service != null)
			{
				GUICache.currentSolution = service.getCurrentSolutionForLogisticTaskID(GUICache.logisticTaskID);
			}
		}

		return GUICache.currentSolution;
	}

	/**
	 * Getter of the newest version of the fitness history.
	 * 
	 * @return the fitness history in the cache. if it is not up to date, the
	 *         cache will be refreshed first.
	 */
	public static LinkedList<Pair<Double, Double>> getFitnessHistory()
	{
		if (GUICache.fittnesHistory == null || !isUpToDate())
		{
			IGenplannerService service = (IGenplannerService) ServiceLocator.getInstance().getService(EServices.Genplanner);
			if (service != null)
			{
				GUICache.fittnesHistory = service.getFitnessHistoryForLogisticTaskID(GUICache.logisticTaskID);
			}
		}

		return GUICache.fittnesHistory;
	}

	/**
	 * Getter of the newest version of average fitness history.
	 * 
	 * @return the average fitness history. in the cache. if it is not up to
	 *         date, the cache will be refreshed first.
	 */
	public static LinkedList<Pair<Double, Double>> getAvgFitnessHistory()
	{
		if (GUICache.fittnesHistory == null || !isUpToDate())
		{
			IGenplannerService service = (IGenplannerService) ServiceLocator.getInstance().getService(EServices.Genplanner);
			if (service != null)
			{
				GUICache.avgFittnesHistory = service.getAverageFitnessHistoryForLogisticTaskID(GUICache.logisticTaskID);
			}
		}

		return GUICache.avgFittnesHistory;
	}

	/**
	 * Getter of the logisticTaskID
	 * 
	 * @return the selected logisticTaskID
	 */
	public static Integer getLogisticTaskID()
	{
		return logisticTaskID;
	}

	/**
	 * Setter for the selected logisticTaskID. Stops the old task if one was
	 * selected before.
	 * 
	 * @param logisticTaskID
	 *            the newly selected logisticTaskID
	 */
	public static void setLogisticTaskID(Integer logisticTaskID)
	{
		if (GUICache.logisticTaskID != -1)
		{
			// stop old task first
			IGenplannerService service = (IGenplannerService) ServiceLocator.getInstance().getService(EServices.Genplanner);
			if (service != null)
			{
				service.setStatusOfLogisticPlanCalculation(GUICache.logisticTaskID, ELogisticTaskStatus.Stopped);
			}
		}

		GUICache.logisticTaskID = logisticTaskID;
	}

	/**
	 * Getter of the selectedSolution index
	 * 
	 * @return the selectedSolution index
	 */
	public static int getSelectedSolution()
	{
		return selectedSolution;
	}

	/**
	 * Setter for the selectedSolution index
	 * 
	 * @param selectedSolution
	 *            the newly selected solutionIndex
	 */
	public static void setSelectedSolution(int selectedSolution)
	{
		// check for valid values only
		if (currentSolution == null)
			return;
		if (currentSolution.getPlans().size() <= selectedSolution || selectedSolution < 0)
			return;

		GUICache.selectedTour = -1;
		GUICache.selectedSolution = selectedSolution;
		FrontendManager.getInstance().repaintAll();
	}

	/**
	 * Returns true / false if a logisticTask was created and selected before
	 * 
	 * @return true if a logisticTask is selected, otherwise false
	 */
	public static boolean hasLogisticTask()
	{
		if (logisticTaskID == -1)
			return false;
		else
			return true;
	}

	/**
	 * Getter of the selecteTour index
	 * 
	 * @return the index of the selected tour
	 */
	public static int getSelectedTour()
	{
		return selectedTour;
	}

	/**
	 * Setter of the selectedTour index
	 * 
	 * @param selectedTour
	 *            the index of the newly selectedTour
	 */
	public static void setSelectedTour(int selectedTour)
	{
		// check for valid values only
		if (currentSolution == null || selectedSolution < 0)
			return;
		if (currentSolution.getPlans().get(selectedSolution).size() <= selectedTour || selectedTour < 0)
			return;

		GUICache.selectedTour = selectedTour;
		FrontendManager.getInstance().repaintAll();
	}

}
