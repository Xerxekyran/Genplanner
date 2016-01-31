package genplanner.domain.vehicle;

import java.util.HashMap;

import util.LogWriter;
import util.LogWriter.LogLevel;

/**
 * An IVehicle implementation that need the following data:<br /> 
 * - kmPerMinutes (speed)<br /> 
 * - loadVolume<br /> 
 * - belongsToDepot (the name of the depot)<br />
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class Truck implements IVehicle
{
	private static final String	MODULE_NAME			= "Vehicle-Truck";

	protected String			name				= "";
	protected double			kmPerMinutes		= 1.0;
	protected String			belongsToDepotName	= "";
	protected double			loadVolume			= 0.0;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------
	@Override
	public void setData(HashMap<String, String> data)
	{
		try
		{
			name = data.get("name");
			kmPerMinutes = Double.parseDouble(data.get("kmPerMinutes"));
			loadVolume = Double.parseDouble(data.get("loadVolume"));
			belongsToDepotName = data.get("belongsToDepot");
		}
		catch (Exception e)
		{
			LogWriter.getInstance().logToFile(LogLevel.Error, MODULE_NAME, e.toString());
		}

	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return name;
	}

	@Override
	public double getSpeedValue()
	{
		return kmPerMinutes;
	}

	@Override
	public String getBelongingDepotName()
	{
		return belongsToDepotName;
	}

	@Override
	public double getLoadVolume()
	{
		return loadVolume;
	}
}
