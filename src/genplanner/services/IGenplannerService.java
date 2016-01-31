package genplanner.services;

import genplanner.domain.DataSet;
import genplanner.domain.solution.ISolution;
import genplanner.exception.FileFormatException;
import genplanner.ga.CalculationConfiguration;
import genplanner.ga.ELogisticTaskStatus;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import util.Pair;

/**
 * The interface of a Genplanner Service offering methods to calculate logistic
 * plans using genetic algorithms
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public interface IGenplannerService extends IService
{
	/**
	 * Creates a new logistc plan calculation object and returns the id of it.
	 * The parameters point to the configuration file that hols the information
	 * about the test case data like customers, vehicles and depots
	 * 
	 * @param path
	 *            the path to the configuration file (without the file itself)
	 * @param configurationFileName
	 *            the name of the configuration file
	 * @param conf
	 *            a configuration object for the logistic plan calculation
	 * @return the id of the created logistic task. this id can be used to get
	 *         the solution later on or change the status of the task to pause
	 *         and so on
	 * @throws FileNotFoundException
	 * @throws FileFormatException
	 */
	public int createLogisticPlanCalculation(String path, String configurationFileName, CalculationConfiguration conf) throws FileNotFoundException, FileFormatException;

	/**
	 * Creates a new logistc plan calculation object and returns the id of it.
	 * 
	 * @param data
	 *            the data the logistic task should be based on
	 * @param conf
	 *            a configuration object for the logistic plan calculation
	 * @return the id of the created logistic task. this id can be used to get
	 *         the solution later on or change the status of the task to pause
	 *         and so on
	 */
	public int createLogisticPlanCalculation(DataSet data, CalculationConfiguration conf);

	/**
	 * Tells the logistic task to change its status.
	 * 
	 * @param logisticTaskID
	 *            the id of the task that should be changed
	 * @param status
	 *            the new status of the task
	 * @throws NoSuchElementException
	 */
	void setStatusOfLogisticPlanCalculation(int logisticTaskID, ELogisticTaskStatus status) throws NoSuchElementException;

	/**
	 * This method returns the current result object for a LogisticTask with the
	 * given id.
	 * 
	 * @param id
	 *            the id of the logisticTask the result should belong to
	 * @return an ISolution object containing the current solution data for the
	 *         calculation, null if no task could be found for that id
	 */
	public ISolution getCurrentSolutionForLogisticTaskID(int id);

	/**
	 * This method returns the fitness history for a LogisticTask with the given
	 * id.
	 * 
	 * @param id
	 *            the id of the logisticTask the result should belong to
	 * @return a list of generation|value pair values representing the fitness
	 *         history
	 */
	public LinkedList<Pair<Double, Double>> getFitnessHistoryForLogisticTaskID(int id);

	/**
	 * This method returns the average fitness history for a LogisticTask with
	 * the given id.
	 * 
	 * @param id
	 *            the id of the logisticTask the result should belong to
	 * @return a list of generation|value pair values representing the average
	 *         fitness history
	 */
	public LinkedList<Pair<Double, Double>> getAverageFitnessHistoryForLogisticTaskID(int id);

	/**
	 * Returns the current status of the logistic task with the given id
	 * 
	 * @param id
	 *            the id of the logistic task that should be handled
	 * @return the status of the logistic task. if no logistic task could be
	 *         found with the given id, the status Error is returned
	 */
	public ELogisticTaskStatus getStatusOfLogisticTask(int id);

	/**
	 * Saves the given solution to a file with the given filename
	 * 
	 * @param solution
	 *            The solution that should be saved
	 * @param solutionIndex
	 *            the index of the solution part that should be saved (a
	 *            solution contains multiple plans)
	 * @param fileName
	 *            the name of the file that should be saved
	 * @return true if saving was done correctly, else false
	 */
	public boolean saveSolution(ISolution solution, int solutionIndex, String fileName);

}