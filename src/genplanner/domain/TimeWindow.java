package genplanner.domain;

/**
 * Represents a time window with a start and an end time (int values,
 * representing minutes from day start).
 * 
 * Examples: 0 = 0; 60 = 1:00 122 = 2:02
 * 
 * @author Lars George
 * 
 */
public class TimeWindow
{
	private int	startTime	= 0;
	private int	endTime		= 0;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * Creates a new TimeWindow object
	 */
	public TimeWindow()
	{
		startTime = 0;
		endTime = 0;
	}

	/**
	 * Create a new TimeWindow object with the given time values
	 * 
	 * @param startTime
	 *            Startpoint of the time window
	 * @param endTime
	 *            Endpoint of the time window
	 */
	public TimeWindow(int startTime, int endTime)
	{
		this.startTime = startTime;
		this.endTime = endTime;
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------

	/**
	 * Checks if the given time value is in the time window of this object
	 * 
	 * @param testTime
	 *            The time that should be tested against the time window (in
	 *            minutes from day start)
	 * @return True if it is in between the start and end values of the time
	 *         window
	 */
	public boolean isInWindow(int testTime)
	{
		boolean retVal = false;

		// test the time values
		if (startTime <= testTime && endTime >= testTime)
			retVal = true;

		return retVal;
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------
	public int getStartTime()
	{
		return startTime;
	}

	public void setStartTime(int startTime)
	{
		this.startTime = startTime;
	}

	public int getEndTime()
	{
		return endTime;
	}

	public void setEndTime(int endTime)
	{
		this.endTime = endTime;
	}

}
