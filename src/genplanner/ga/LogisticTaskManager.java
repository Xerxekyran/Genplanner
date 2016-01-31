package genplanner.ga;

import genplanner.domain.DataSet;

import java.util.HashMap;

/**
 * This class is realized as a singleton and manages all the logistic tasks in
 * the application. New tasks can be ordered and already running can be
 * retrieved or changed using this manager class.
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class LogisticTaskManager
{
	private static LogisticTaskManager		instance		= null;
	private HashMap<Integer, LogisticTask>	logisticTasks	= null;
	private int								nextFreeKey		= 1;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * Private constructor for singleton pattern
	 */
	private LogisticTaskManager()
	{
		logisticTasks = new HashMap<Integer, LogisticTask>();
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------

	/**
	 * Creates and starts a new LogisticTask
	 * 
	 * @param data
	 *            The Data the LogsticTask should be based on
	 * @param conf
	 *            The configuration settings for the genetic algorithm. if this
	 *            param is null, default settings are used
	 * @return A unique id to identifie the created task
	 */
	public int createNewLogisticTask(DataSet data, CalculationConfiguration conf)
	{
		LogisticTask logisticTask = new LogisticTask(data, conf);
		int key = getNextKey();
		logisticTasks.put(key, logisticTask);
		Thread t = new Thread(logisticTask);
		t.start();

		return key;
	}

	/**
	 * Method to retrieve a logisticTask created by this Manager.
	 * 
	 * @param id
	 *            The id of the task that should be returned
	 * @return The LogisticTask with the given ID, null if no Task could be
	 *         found using this id
	 */
	public LogisticTask getTaskByID(int id)
	{
		return logisticTasks.get(id);
	}

	/**
	 * Singleton retriever method
	 * 
	 * @return the singleton reference of this class
	 */
	public static LogisticTaskManager getInstance()
	{
		if (instance == null)
		{
			instance = new LogisticTaskManager();
		}

		return instance;
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	/**
	 * Returns a unique Key ID
	 * 
	 * @return The next free Key for an ID
	 */
	private int getNextKey()
	{
		return nextFreeKey++;
	}
	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------

}
