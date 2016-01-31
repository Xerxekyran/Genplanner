package genplanner.domain.depot;

import genplanner.domain.Position;
import genplanner.domain.TimeWindow;

import java.util.HashMap;

import util.LogWriter;
import util.LogWriter.LogLevel;

/**
 * An IDepot implementation that needs the following data:<br /> 
 * - Position<br /> 
 * - TimeWindow<br />
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class Storehouse implements IDepot
{
	private static final String	MODULE_NAME		= "Depot-Storehouse";

	protected TimeWindow		workTimeWindow	= new TimeWindow();
	protected String			name			= "";
	protected Position			position		= new Position();

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
			position.setX(Integer.parseInt(data.get("positionX")));
			position.setY(Integer.parseInt(data.get("positionY")));
			workTimeWindow.setStartTime(Integer.parseInt(data.get("timeWindowStart")));
			workTimeWindow.setEndTime(Integer.parseInt(data.get("timeWindowEnd")));
		}
		catch (Exception e)
		{
			LogWriter.getInstance().logToFile(LogLevel.Error, MODULE_NAME, e.toString());
		}

	}

	@Override
	public Position getPosition()
	{
		return position;
	}

	@Override
	public String toString()
	{
		return name + " \t(" + position.getX() + "|" + position.getY() + ")\t Time window: " + this.workTimeWindow.getStartTime() + " -> " + this.workTimeWindow.getEndTime();
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public TimeWindow getTimeWindow()
	{
		return workTimeWindow;
	}
}
