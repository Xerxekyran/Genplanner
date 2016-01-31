package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * This class supports methods for managing xml files.
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class XMLLoader
{
	private static String	xmlFile	= "";

	// XML-Document that holds the configuration elements
	private Document		xmlDoc	= null;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * Default constructor
	 * 
	 * @throws FileNotFoundException
	 */
	public XMLLoader(String xmlFilePath) throws FileNotFoundException
	{
		// first validation of the path
		if (xmlFilePath == null || xmlFilePath == "")
		{
			throw new FileNotFoundException("Empty path not valid.");
		}

		xmlFile = xmlFilePath;

		System.out.println("[ConfigLoader]: Loading configuration " + xmlFile);

		// XML-Builder Instanz holen
		SAXBuilder xmlBuilder = new SAXBuilder();

		try
		{
			// try to read the xml file
			xmlDoc = xmlBuilder.build(new File(xmlFile));
		}
		catch (JDOMException e)
		{
			System.out.println("[ConfigLoader]: Failed loading the " + xmlFile + " file [Wrong Format]: " + e.toString());
			throw new FileNotFoundException(e.toString());
		}
		catch (IOException e)
		{
			System.out.println("[ConfigLoader]: Failed loading the " + xmlFile + " file [IO Error]: " + e.toString());
			throw new FileNotFoundException(e.toString());
		}
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------

	/**
	 * Method to read double type configuration values
	 * 
	 * @param key
	 *            Name of the configuration key
	 * @return The value for the given key as a double object
	 * @throws NumberFormatException
	 *             If the value could not be cast to a double
	 * @throws NoSuchElementException
	 *             If no entry for the given key could be found
	 */
	public double getDouble(String key) throws NumberFormatException, NoSuchElementException
	{
		return Double.parseDouble(getElement(key).getText());
	}

	/**
	 * Method to read integer type configuration values
	 * 
	 * @param key
	 *            Name of the configuration key
	 * @return The value for the given key as a integer object
	 * @throws NumberFormatException
	 *             If the value could not be cast to a double
	 * @throws NoSuchElementException
	 *             If no entry for the given key could be found
	 */
	public int getInt(String key) throws NumberFormatException, NoSuchElementException
	{
		return Integer.parseInt(getElement(key).getText());
	}

	/**
	 * Method to read String type configuration values
	 * @param variableName Name of the configuration key
	 * @return  The value for the given key as a String object
	 * @throws NoSuchElementException  If no entry for the given key could be found
	 */
	public String getString(String variableName) throws NoSuchElementException
	{
		return getElement(variableName).getText();
	}

	/**
	 * Returns an XML-Element with the given name
	 * 
	 * @param name
	 *            Name of the XML-Element
	 * @return Reference to the XML-Element with the given name
	 * @throws NoSuchElementException
	 *             If no element could be found for that name
	 */
	public Element getElement(String name) throws NoSuchElementException
	{
		Element el = xmlDoc.getRootElement().getChild(name);
		if (el == null)
			throw new NoSuchElementException("No such element in configuration file [ " + name + " ]");

		return el;
	}

	/**
	 * Getter for the root element of the xml file
	 * 
	 * @return The root element of the managed xml file
	 */
	public Element getRootElement()
	{
		return xmlDoc.getRootElement();
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------
}
