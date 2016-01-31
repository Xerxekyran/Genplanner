package genplanner.persistence.dao.xml;

import genplanner.domain.solution.CustomerEntry;
import genplanner.domain.solution.ISolution;
import genplanner.domain.solution.LogisticPlan;
import genplanner.persistence.dao.ISolutionDAO;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.LinkedList;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import util.LogWriter;
import util.LogWriter.LogLevel;

/**
 * An implementation of an ISolutionDAO. Using XML-Files for data storage and
 * retrievel.
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class XMLSolutionDAO implements ISolutionDAO
{
	private static final String	MODULE_NAME	= "XMLSolutionDAO";

	@Override
	public ISolution loadSolution(String fileName) throws FileNotFoundException
	{
		ISolution ret = null;

		return ret;
	}

	@Override
	public boolean saveSolution(ISolution solution, int solutionIndex, String fileName)
	{
		boolean ret = false;

		try
		{
			// get the lsit of logistic plans that should be persisted
			LinkedList<LogisticPlan> plans = solution.getPlans().get(solutionIndex);
			
			// create the xml structure
			Document doc = new Document();
			Element root = new Element("LogisticPlanSolution");
			doc.setRootElement(root);
			
			// for every plan -> vehicle
			for(LogisticPlan plan : plans)
			{
				// add the general data for the tour
				Element tour = new Element("Tour");
				
				Element vehicle = new Element("Vehicle");
				vehicle.setAttribute("Name", plan.getVehicle().getName());
				
				Element depot = new Element("Depot");								
				depot.setAttribute("Name", plan.getDepot().getName());
				depot.setAttribute("Position_X", Integer.toString(plan.getDepot().getPosition().getX()));
				depot.setAttribute("Position_Y", Integer.toString(plan.getDepot().getPosition().getY()));
				
				tour.addContent(vehicle);
				tour.addContent(depot);
				
				// the customers that are served due to this plan
				Element customers = new Element("Customers");
				for(CustomerEntry custEntr : plan.getCustomer())
				{
					Element customer = new Element("Customer");
					customer.setAttribute("Name", custEntr.getCustomer().getName());
					customer.setAttribute("Position_X", Integer.toString(custEntr.getCustomer().getPosition().getX()));
					customer.setAttribute("Position_Y", Integer.toString(custEntr.getCustomer().getPosition().getY()));
					
					customers.addContent(customer);
				}
				tour.addContent(customers);				

				root.addContent(tour);
			}
		
			// write the xml data to a file with the given name
			if(!fileName.endsWith(".xml"))
				fileName = fileName + ".xml";
			FileWriter writer = new FileWriter(fileName);
			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
			out.output(doc, writer);			
			
			ret = true;
		}
		catch (Exception e)
		{
			LogWriter.getInstance().logToFile(LogLevel.Error, MODULE_NAME, "Error while saving a solution: " + e.toString());
		}

		return ret;
	}
}
