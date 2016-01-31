package genplanner.core;

import genplanner.core.messages.ISystemMessage;
import genplanner.gui.FrontendManager;

import java.util.concurrent.ConcurrentLinkedQueue;

import util.LogWriter;
import util.XMLManager;
import util.LogWriter.LogLevel;

/**
 * Main entry point of the application. Starts the GUI for interaction.
 * 
 * @author Lars George
 * @version 1.0 
 */
public class Genplanner
{
	private static Genplanner						instance		= null;
	private static final String						MODULE_NAME		= "Genplanner-Main";
	private ConcurrentLinkedQueue<ISystemMessage>	systemMessages	= null;

	private boolean									abort			= false;

	/**
	 * @param args
	 *            Arguments by calling this program (are currently not used)
	 */
	public static void main(String[] args)
	{
		LogWriter.getInstance().logToFile(LogLevel.Info, MODULE_NAME, "Starting the application");

		// calling the frontend elements to startup
		FrontendManager.getInstance();

		// start message loop
		Genplanner.getInstance().startMessageLoop();

		// the message loop has finished -> close the app
		LogWriter.getInstance().logToFile(LogLevel.Info, MODULE_NAME, "Stopping the application");

		// calling the shutdown methods to cleanly end the app
		FrontendManager.getInstance().shutdown();
		LogWriter.getInstance().shutdown();
		XMLManager.getInstance().shutdown();

		LogWriter.getInstance().logToFile(LogLevel.Info, MODULE_NAME, "Application stopped successfully");

		System.exit(0);
	}

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * Private constructor for the singleton pattern.
	 */
	private Genplanner()
	{
		systemMessages = new ConcurrentLinkedQueue<ISystemMessage>();
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------
	/**
	 * Singleton retrieve method
	 * 
	 * @return The singleton instance of this class
	 */
	public static Genplanner getInstance()
	{
		if (instance == null)
			instance = new Genplanner();

		return instance;
	}

	/**
	 * Adds a new system message to the queue to be processed
	 * 
	 * @param newMSG
	 *            The new system message
	 */
	public synchronized void addSystemMessage(ISystemMessage newMSG)
	{
		systemMessages.add(newMSG);
	}

	/**
	 * This method loops until the abort attribute is set to true.
	 * System-messages are read from the queue und managed properly. The
	 * System-message "ExitApplicationMSG" results in setting abort to true
	 * 
	 */
	public void startMessageLoop()
	{
		ISystemMessage msg = null;

		// as long as the application is running
		while (!abort)
		{
			// is there a new system message?
			msg = systemMessages.poll();
			if (msg != null)
			{
				// look for the type of the message
				switch (msg.getMessageType())
				{
				case ExitApplication:
					abort = true;
					break;
				}
			}
			else
			{
				// Wait a little bit (nothing to do currently)
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------

}
