package genplanner.persistence.dao.xml;

import genplanner.domain.customer.ICustomer;
import genplanner.persistence.dao.ICustomerDAO;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.jdom.Attribute;
import org.jdom.Element;

import util.LogWriter;
import util.XMLLoader;
import util.XMLManager;
import util.LogWriter.LogLevel;

/**
 * Implementation of an XML Customer DAO. Using XML-Files for data storage and
 * retrievel.
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class XMLCustomerDAO implements ICustomerDAO
{
	private static final String	MODULE_NAME	= "Genplanner-XMLCustomerDAO";
	private XMLLoader			xmlLoader	= null;

	@SuppressWarnings("unchecked")
	@Override
	public LinkedList<ICustomer> loadCustomers(String fileName) throws FileNotFoundException, ClassNotFoundException
	{
		LinkedList<ICustomer> ret = new LinkedList<ICustomer>();

		// read the xml file
		xmlLoader = XMLManager.getInstance().getXMLLoader(fileName);

		// extract the class of the instances that should be created
		Class<? extends ICustomer> customerClass = (Class<? extends ICustomer>) Class.forName(xmlLoader.getRootElement().getAttributeValue("class"));

		// for all xml elements
		Iterator<Element> it = xmlLoader.getRootElement().getChildren().iterator();
		while (it.hasNext())
		{
			ICustomer customer;
			try
			{
				// create a new map holding the attributeName -> attributeValue
				// pairs
				HashMap<String, String> dataMap = new HashMap<String, String>();
				customer = customerClass.newInstance();

				// get all attributes
				Iterator<Attribute> itAttributes = it.next().getAttributes().iterator();
				while (itAttributes.hasNext())
				{
					Attribute attribute = itAttributes.next();
					dataMap.put(attribute.getName(), attribute.getValue());
				}

				customer.setData(dataMap);
				ret.add(customer);
			}
			catch (InstantiationException e)
			{
				LogWriter.getInstance().logToFile(LogLevel.Error, MODULE_NAME, e.toString());
			}
			catch (IllegalAccessException e)
			{
				LogWriter.getInstance().logToFile(LogLevel.Error, MODULE_NAME, e.toString());
			}
		}

		return ret;
	}
}
