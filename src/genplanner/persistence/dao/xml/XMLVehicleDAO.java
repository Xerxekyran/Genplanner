package genplanner.persistence.dao.xml;

import genplanner.domain.vehicle.IVehicle;
import genplanner.persistence.dao.IVehicleDAO;

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
public class XMLVehicleDAO implements IVehicleDAO
{
	private static final String	MODULE_NAME	= "Genplanner-XMLVehicleDAO";
	private XMLLoader			xmlLoader	= null;

	@SuppressWarnings("unchecked")
	@Override
	public LinkedList<IVehicle> loadVehicles(String fileName) throws FileNotFoundException, ClassNotFoundException
	{
		LinkedList<IVehicle> ret = new LinkedList<IVehicle>();

		// read the xml file
		xmlLoader = XMLManager.getInstance().getXMLLoader(fileName);

		// extract the class of the instances that should be created
		Class<? extends IVehicle> vehicleClass = (Class<? extends IVehicle>) Class.forName(xmlLoader.getRootElement().getAttributeValue("class"));

		// for all xml elements
		Iterator<Element> it = xmlLoader.getRootElement().getChildren().iterator();
		while (it.hasNext())
		{
			IVehicle vehicle;
			try
			{
				// create a new map holding the attributeName -> attributeValue
				// pairs
				HashMap<String, String> dataMap = new HashMap<String, String>();
				vehicle = vehicleClass.newInstance();

				// get all attributes
				Iterator<Attribute> itAttributes = it.next().getAttributes().iterator();
				while (itAttributes.hasNext())
				{
					Attribute attribute = itAttributes.next();
					dataMap.put(attribute.getName(), attribute.getValue());
				}

				vehicle.setData(dataMap);
				ret.add(vehicle);
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
