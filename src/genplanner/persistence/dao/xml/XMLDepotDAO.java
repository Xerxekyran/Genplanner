package genplanner.persistence.dao.xml;

import genplanner.domain.depot.IDepot;
import genplanner.persistence.dao.IDepotDAO;

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
 * An implementation of an IDepotDAO. Using XML-Files for data storage and
 * retrievel.
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class XMLDepotDAO implements IDepotDAO
{
	private static final String	MODULE_NAME	= "Genplanner-XMLDepotDAO";
	private XMLLoader			xmlLoader	= null;

	@SuppressWarnings("unchecked")
	@Override
	public LinkedList<IDepot> loadDepots(String fileName) throws FileNotFoundException, ClassNotFoundException
	{
		LinkedList<IDepot> ret = new LinkedList<IDepot>();

		// read the xml file
		xmlLoader = XMLManager.getInstance().getXMLLoader(fileName);

		// extract the class of the instances that should be created
		Class<? extends IDepot> depotClass = (Class<? extends IDepot>) Class.forName(xmlLoader.getRootElement().getAttributeValue("class"));

		// for all xml elements
		Iterator<Element> it = xmlLoader.getRootElement().getChildren().iterator();
		while (it.hasNext())
		{
			IDepot depot;
			try
			{
				// create a new map holding the attributeName -> attributeValue
				// pairs
				HashMap<String, String> dataMap = new HashMap<String, String>();
				depot = depotClass.newInstance();

				// get all attributes
				Iterator<Attribute> itAttributes = it.next().getAttributes().iterator();
				while (itAttributes.hasNext())
				{
					Attribute attribute = itAttributes.next();
					dataMap.put(attribute.getName(), attribute.getValue());
				}

				depot.setData(dataMap);
				ret.add(depot);
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
