package util;

import genplanner.domain.Position;
import genplanner.domain.TimeWindow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * A class that converts data from external resources into the own XML format.
 * Currently only the lackner format is supported.
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class TestDataConverter
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		String inputFile = "D:/Java Workspace/Genplanner/data/raw testdata/lackner/C101.txt";

		inputFile = getFilenameWithChooser();

		convertLackner(inputFile, "D:/Java Workspace/Genplanner/data/");
	}

	private static String getFilenameWithChooser()
	{
		String ret = "";
		// FileChooser to select the configuration file
		JFileChooser jfc = new JFileChooser();
		int state = jfc.showSaveDialog(null);
		if (state == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				ret = jfc.getSelectedFile().getPath();

			}
			catch (Exception ex)
			{
				System.err.println(ex.toString());
			}
		}

		return ret;
	}

	/**
	 * Reads a TXT file in the "lackner" format and creates the xml files
	 * 
	 * @param inputFile
	 */
	private static void convertLackner(String inputFile, String outputPath)
	{
		try
		{
			System.out.println("Now parsing the Lackner file format...");

			String line = "";
			String[] currentSeperatedLine = null;
			int lineCounter = 0;

			String testCaseName = "";
			int numVehicle = 0;
			int vehicleCapacity = 0;

			Position depotPosition = new Position();
			TimeWindow depotTimeWindow = new TimeWindow();

			ArrayList<Position> customerPositions = new ArrayList<Position>();
			ArrayList<TimeWindow> customerTimeWindows = new ArrayList<TimeWindow>();
			ArrayList<Integer> customerDemands = new ArrayList<Integer>();
			ArrayList<Integer> customerServiceTimes = new ArrayList<Integer>();

			BufferedReader buffReader = new BufferedReader(new FileReader(inputFile));
			while ((line = buffReader.readLine()) != null)
			{
				lineCounter++;

				// head data
				if (lineCounter == 1)
				{
					testCaseName = line;
				}
				// vehicle data
				else if (lineCounter == 5)
				{
					currentSeperatedLine = getValuesWithoutSpaces(line);
					numVehicle = Integer.parseInt(currentSeperatedLine[0]);
					vehicleCapacity = Integer.parseInt(currentSeperatedLine[1]);
				}
				// depot
				else if (lineCounter == 10)
				{
					currentSeperatedLine = getValuesWithoutSpaces(line);

					depotPosition.setX(Integer.parseInt(currentSeperatedLine[1]));
					depotPosition.setY(Integer.parseInt(currentSeperatedLine[2]));

					depotTimeWindow.setStartTime(Integer.parseInt(currentSeperatedLine[4]));
					depotTimeWindow.setEndTime(Integer.parseInt(currentSeperatedLine[5]));
				}
				// customer
				else if (lineCounter > 10)
				{
					currentSeperatedLine = getValuesWithoutSpaces(line);

					customerPositions.add(new Position(Integer.parseInt(currentSeperatedLine[1]), Integer.parseInt(currentSeperatedLine[2])));
					customerDemands.add(new Integer(Integer.parseInt(currentSeperatedLine[3])));
					customerTimeWindows.add(new TimeWindow(Integer.parseInt(currentSeperatedLine[4]), Integer.parseInt(currentSeperatedLine[5])));
					customerServiceTimes.add(new Integer(Integer.parseInt(currentSeperatedLine[6])));
				}
			}
			System.out.println("Successfully parsed an input file with " + lineCounter + " lines");

			writeXMLFiles(outputPath, testCaseName, numVehicle, vehicleCapacity, depotPosition, depotTimeWindow, customerPositions, customerTimeWindows, customerDemands, customerServiceTimes);
		}
		catch (Exception e)
		{
			System.out.println("ERROR: " + e.toString());
		}
	}

	/**
	 * writes the data to the xml files
	 * 
	 * @param testCaseName
	 * @param numVehicle
	 * @param vehicleCapacity
	 * @param depotPosition
	 * @param depotTimeWindow
	 * @param customerPositions
	 * @param customerTimeWindows
	 * @param customerDemands
	 * @param customerServiceTimes
	 */
	private static void writeXMLFiles(	String path,
										String testCaseName,
										int numVehicle,
										int vehicleCapacity,
										Position depotPosition,
										TimeWindow depotTimeWindow,
										ArrayList<Position> customerPositions,
										ArrayList<TimeWindow> customerTimeWindows,
										ArrayList<Integer> customerDemands,
										ArrayList<Integer> customerServiceTimes)
	{
		System.out.println("Now writing the data to own xml format...");
		try
		{

			Document doc;
			Element root;
			FileWriter writer;
			XMLOutputter out;

			// -------------------------------------------------------------------------------
			// testCase configuration
			// -------------------------------------------------------------------------------
			doc = new Document();
			root = new Element("DataSet");
			root.setAttribute("id", testCaseName);
			doc.setRootElement(root);

			Element customers = new Element("Customers");
			customers.setAttribute("file", "customers/" + testCaseName + "_customers.xml");
			root.addContent(customers);

			Element depots = new Element("Depots");
			depots.setAttribute("file", "depots/" + testCaseName + "_depots.xml");
			root.addContent(depots);

			Element vehicles = new Element("Vehicles");
			vehicles.setAttribute("file", "vehicles/" + testCaseName + "_vehicles.xml");
			root.addContent(vehicles);

			// write the xml data to the file
			writer = new FileWriter(path + testCaseName + "_configuration.xml");
			out = new XMLOutputter(Format.getPrettyFormat());
			out.output(doc, writer);

			System.out.println("Successfully created configuration xml file.");

			// -------------------------------------------------------------------------------
			// vehicles
			// -------------------------------------------------------------------------------
			doc = new Document();
			root = new Element("Vehicles");
			root.setAttribute("class", "genplanner.domain.vehicle.Truck");
			doc.setRootElement(root);

			for (int i = 0; i < numVehicle; i++)
			{
				// add the general data for the tour
				Element vehicle = new Element("Vehicle");
				vehicle.setAttribute("name", Integer.toString(i));
				vehicle.setAttribute("kmPerMinutes", Double.toString(1.0));
				vehicle.setAttribute("belongsToDepot", Integer.toString(0));
				vehicle.setAttribute("loadVolume", Integer.toString(vehicleCapacity));

				root.addContent(vehicle);
			}

			// write the xml data to the file
			writer = new FileWriter(path + "vehicles/" + testCaseName + "_vehicles.xml");
			out = new XMLOutputter(Format.getPrettyFormat());
			out.output(doc, writer);

			System.out.println("Successfully created vehicle xml file with " + numVehicle + " vehicles");

			// -------------------------------------------------------------------------------
			// depots
			// -------------------------------------------------------------------------------
			doc = new Document();
			root = new Element("Depots");
			root.setAttribute("class", "genplanner.domain.depot.Storehouse");
			doc.setRootElement(root);

			Element depot = new Element("Depot");
			depot.setAttribute("name", Integer.toString(0));
			depot.setAttribute("positionX", Integer.toString(depotPosition.getX()));
			depot.setAttribute("positionY", Integer.toString(depotPosition.getY()));
			depot.setAttribute("timeWindowStart", Integer.toString(depotTimeWindow.getStartTime()));
			depot.setAttribute("timeWindowEnd", Integer.toString(depotTimeWindow.getEndTime()));
			root.addContent(depot);

			// write the xml data to the file
			writer = new FileWriter(path + "depots/" + testCaseName + "_depots.xml");
			out = new XMLOutputter(Format.getPrettyFormat());
			out.output(doc, writer);
			System.out.println("Successfully created depot xml file");

			// -------------------------------------------------------------------------------
			// customers
			// -------------------------------------------------------------------------------
			doc = new Document();
			root = new Element("Customers");
			root.setAttribute("class", "genplanner.domain.customer.Family");
			doc.setRootElement(root);

			for (int i = 0; i < customerPositions.size(); i++)
			{
				// add the general data for the tour
				Element customer = new Element("Customer");

				customer.setAttribute("name", Integer.toString(i + 1));
				customer.setAttribute("positionX", Integer.toString(customerPositions.get(i).getX()));
				customer.setAttribute("positionY", Integer.toString(customerPositions.get(i).getY()));
				customer.setAttribute("serviceTime", Integer.toString(customerServiceTimes.get(i)));
				customer.setAttribute("demandSize", Integer.toString(customerDemands.get(i)));
				customer.setAttribute("timeWindowStart", Integer.toString(customerTimeWindows.get(i).getStartTime()));
				customer.setAttribute("timeWindowEnd", Integer.toString(customerTimeWindows.get(i).getEndTime()));

				root.addContent(customer);
			}

			System.out.println("Successfully created customer xml file with " + customerPositions.size() + " customers");

			// write the xml data to the file
			writer = new FileWriter(path + "customers/" + testCaseName + "_customers.xml");
			out = new XMLOutputter(Format.getPrettyFormat());
			out.output(doc, writer);

			System.out.println("Successfully done complete.");
		}
		catch (Exception e)
		{
			System.out.println("Error while writing the xml files: " + e.toString());
		}
	}

	/**
	 * 
	 * @param textLine
	 * @return
	 */
	private static String[] getValuesWithoutSpaces(String textLine)
	{
		ArrayList<String> values = new ArrayList<String>();

		boolean lastReadWasASpace = true;
		StringBuilder currentPart = new StringBuilder();

		for (int i = 0; i < textLine.length(); i++)
		{
			if (lastReadWasASpace && textLine.charAt(i) != ' ')
			{
				currentPart = new StringBuilder();
				currentPart.append(textLine.charAt(i));
				lastReadWasASpace = false;
			}
			else if (!lastReadWasASpace && textLine.charAt(i) != ' ')
			{
				currentPart.append(textLine.charAt(i));
			}
			else if (!lastReadWasASpace && textLine.charAt(i) == ' ')
			{
				values.add(currentPart.toString());
				lastReadWasASpace = true;
			}
		}
		if (currentPart.length() > 0)
		{
			values.add(currentPart.toString());
		}

		String[] ret = new String[values.size()];
		values.toArray(ret);

		return ret;
	}

}
