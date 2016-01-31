package genplanner.exception;

/**
 * An exceptin for a wrong file format.
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class FileFormatException extends Exception
{
	private static final long	serialVersionUID	= 1L;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * A constructor with message init
	 * 
	 * @param msg
	 *            the message of the exceptions
	 */
	public FileFormatException(String msg)
	{
		super(msg);
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------

}
