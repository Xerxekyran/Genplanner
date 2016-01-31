package genplanner.domain;

/**
 * This class represents a two-dimensional position. it holds an x and y value.
 * 
 * @author Lars George
 * @version 1.0
 */
public class Position
{
	private int	x	= 0;
	private int	y	= 0;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * Default empty constructor
	 */
	public Position()
	{

	}

	/**
	 * Constructor with initializing the parameters
	 * 
	 * @param x
	 *            the x position on a map
	 * @param y
	 *            the y position on a map
	 */
	public Position(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------

	/**
	 * Calculates the distance to the other position
	 * 
	 * @param distanceTo
	 *            Position of the target
	 * @return The distance between these two positions
	 */
	public double distance(Position distanceTo)
	{
		double ySeparation = distanceTo.y - y;
		double xSeparation = distanceTo.x - x;

		return Math.sqrt(ySeparation * ySeparation + xSeparation * xSeparation);
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------
	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

}
