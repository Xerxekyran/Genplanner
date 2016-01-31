package genplanner.domain.customer;

import genplanner.domain.Position;
import genplanner.domain.TimeWindow;

import java.util.HashMap;

import util.LogWriter;
import util.LogWriter.LogLevel;

/**
 * An ICsutomer implementation that needs the following data:<br />
 * - Position<br />
 * - DemandSize<br />
 * - ServiceTime<br />
 * - DeliveryTimeWindow<br />
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class Family implements ICustomer
{
	private static final String	MODULE_NAME			= "Customer-Family";

	protected TimeWindow		deliveryTimeWindow	= new TimeWindow();
	protected Position			position			= new Position();
	protected int				serviceTime			= 0;
	protected String			name				= "";
	protected double			demandSize			= 0.0;

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
			demandSize = (Double.parseDouble(data.get("demandSize")));
			serviceTime = Integer.parseInt(data.get("serviceTime"));
			deliveryTimeWindow.setStartTime(Integer.parseInt(data.get("timeWindowStart")));
			deliveryTimeWindow.setEndTime(Integer.parseInt(data.get("timeWindowEnd")));

			LogWriter.getInstance().logToFile(LogLevel.Debug,
					MODULE_NAME,
					"Family set with: Name[" + name + "] PosX[" + position.getX() + "] PosY[" + position.getY() + "] ServiceTime[" + serviceTime + "]" + "TimeWindow [" + deliveryTimeWindow.getStartTime() + "," + deliveryTimeWindow.getEndTime() + "] Demand size["+ demandSize +"]");
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
	public Position getPosition()
	{
		return position;
	}

	@Override
	public String toString()
	{
		return name + " \t(" + position.getX() + "|" + position.getY() + ")\t service time: " + serviceTime + "\t Time window: " + this.deliveryTimeWindow.getStartTime() + " -> " + this.deliveryTimeWindow.getEndTime();
	}

	@Override
	public TimeWindow getTimeWindow()
	{
		return deliveryTimeWindow;
	}

	@Override
	public int getServiceTime()
	{
		return serviceTime;
	}

	@Override
	public double getDemandSize()
	{
		return demandSize;
	}
}
