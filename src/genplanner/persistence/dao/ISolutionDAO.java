package genplanner.persistence.dao;

import genplanner.domain.solution.ISolution;

import java.io.FileNotFoundException;

/**
 * A solution Data Access Object interface that handles storage and retrieval
 * data using XML files
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public interface ISolutionDAO extends IDAO
{

	/**
	 * Loading a solution from a given file (needs to have special format, like
	 * the one from the save method)
	 * 
	 * @param fileName
	 *            the name of the file the data should be loaded from
	 * @return the loaded solution object
	 * @throws FileNotFoundException
	 */
	ISolution loadSolution(String fileName) throws FileNotFoundException;

	/**
	 * saves a solution to a file in an xml format
	 * 
	 * @param solution
	 *            the solution that should be saved
	 * @param fileName
	 *            the name of the file the data will be saved to
	 * @return true if the saving was done successfully, otherwise false
	 */
	boolean saveSolution(ISolution solution, int solutionIndex, String fileName);

}
