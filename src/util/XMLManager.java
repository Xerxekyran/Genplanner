package util;

import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * The XMLManager manages XMLLoader objects so that only one exists for each xml
 * file that should be read by the application.
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class XMLManager
{
	/**
	 * The standard config file for the application
	 */
	public static String				APP_CONFIG_FILE	= "config.xml";

	private static XMLManager			instance		= null;

	private HashMap<String, XMLLoader>	xmlLoaders		= null;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * Private constructor for singleton pattern
	 */
	private XMLManager()
	{
		xmlLoaders = new HashMap<String, XMLLoader>();
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------
	/**
	 * cleans all used data
	 */
	public void shutdown()
	{

	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------
	/**
	 * Getter for the singleton instance
	 */
	public static XMLManager getInstance()
	{
		if (instance == null)
			instance = new XMLManager();

		return instance;
	}

	/**
	 * 
	 * @param fileName
	 *            A valid filename of an XML file that should be handled by a
	 *            XMLLoader object
	 * @return A XMLLoader instance for the gen filename. If this method is
	 *         called more than once with the same fileName, the same XMLLoader
	 *         instance is returned
	 * @throws FileNotFoundException
	 *             Throws this exception if the corresponding XMLLoader could
	 *             not find the given file
	 */
	public XMLLoader getXMLLoader(String fileName) throws FileNotFoundException
	{
		// try to get an already instantiated xmlLoader
		XMLLoader ret = xmlLoaders.get(fileName);

		// if this is the first time
		if (ret == null)
		{
			// create a new XMLLoader
			ret = new XMLLoader(fileName);
			xmlLoaders.put(fileName, ret);
		}

		return ret;
	}
}
