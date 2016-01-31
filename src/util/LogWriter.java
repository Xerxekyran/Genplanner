package util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Class to write a logfile. Supports three different types of loglevels:
 * (Error, Info, Warn, Debug). The output is filtered depending on the loglevel
 * set to the logwriter object. It is implemented as a singleton.
 * 
 * @author Lars George
 * @version 2.2 - HTML
 * 
 */
public class LogWriter
{
	/**
	 * Enumeration of the possible loglevels
	 * 
	 * @author Lars George
	 */
	public enum LogLevel
	{
		Error, Warn, Info, Debug
	}

	private LogLevel			logLevel	= LogLevel.Debug;	

	private String				logFile		= "logfile.html";

	private static LogWriter	instance	= new LogWriter();
	
	private static final String DATE_FORMAT_NOW = "dd-MM-yyy HH:mm:ss";


	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * Default constructor
	 */
	private LogWriter()
	{
		try
		{
			String strLogLevel = XMLManager.getInstance().getXMLLoader(XMLManager.APP_CONFIG_FILE).getString("LogLevel");

			// Not case-sensitive while trying to read the loglevel
			if (strLogLevel.toLowerCase().equals("debug"))
				logLevel = LogLevel.Debug;
			else if (strLogLevel.toLowerCase().equals("info"))
				logLevel = LogLevel.Info;
			else if (strLogLevel.toLowerCase().equals("warn"))
				logLevel = LogLevel.Warn;
			else if (strLogLevel.toLowerCase().equals("error"))
				logLevel = LogLevel.Error;
			else
				throw new Exception("Cant read LogLevel [" + strLogLevel + "]");

			// Read the file which should be used to write into
			logFile = XMLManager.getInstance().getXMLLoader(XMLManager.APP_CONFIG_FILE).getString("LogFile");

		}
		catch (Exception e)
		{
			System.out.println("[LogWriter]: Error while loading the configurations from the ConfigLoader. Using standard values. "
					+ e.toString());
			logFile = "logfile.html";
			logLevel = LogLevel.Info;
		}

		try
		{
			// Delete the file first
			deleteLogFile();

			String text = "<html> \n"
					+ "  <head><title>Logfile</title></head>\n"
					+ "    <style type='text/css'> \n"
					+ "      .error { background-color : #FF0033;}\n"
					+ "      .debug { background-color : #99CCFF;}\n"
					+ "      .info  { background-color : #FFFF99;}\n"
					+ "      .warn  { background-color : #FF8C00;}\n"
					+ "      .system  { border : solid black 2px; background-color : silver; font-weight : bold;}\n"
					+ "    </style>" + "  <body> \n" + "  <h1>Logfile</h1> \n";

			// Write the head information into the logfile
			writeToFile(text);

			writeToFile(getSystemString("Using LogLevel: "
					+ logLevel.toString()));

		}
		catch (Exception e)
		{
			System.out.println("Logger could not be initialized:"
					+ e.toString());
		}
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------
	/**
	 * Singleton get method
	 * 
	 * @return The singleton object of this class
	 */
	public static LogWriter getInstance()
	{
		return instance;
	}

	/**
	 * Deletes the logfile
	 */
	public void deleteLogFile()
	{
		try
		{
			File f = new File(logFile);
			f.delete();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Writes the given string into the logfile, if the given logfile is "more
	 * important" than the loglevel of this object
	 * 
	 * @param level
	 *            What kind of logmessage is this entry
	 * @param text
	 *            The logmessage
	 */
	public synchronized void logToFile(LogLevel level, String moduleName, String text)
	{
		try
		{
			// Check for the loglevel
			if ((level == LogLevel.Warn && logLevel == LogLevel.Error)
					|| (level == LogLevel.Info && (logLevel == LogLevel.Error || logLevel == LogLevel.Warn))
					|| (level == LogLevel.Debug && (logLevel == LogLevel.Error
							|| logLevel == LogLevel.Warn || logLevel == LogLevel.Info)))
				return;

			switch (level)
			{
			case Debug:
				text = getDebugString(moduleName, text);
				break;
			case Error:
				text = getErrorString(moduleName, text);
				break;
			case Info:
				text = getInfoString(moduleName, text);
				break;
			case Warn:
				text = getWarnString(moduleName, text);
				break;
			}

			// Write the text into the logfile
			writeToFile(text);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Writes the end tags for the HTML logfile
	 */
	public void shutdown()
	{
		try
		{
			// Open the file to write
			FileOutputStream fileOutStream = new FileOutputStream(logFile, true);

			String text = "</body></html>";

			// Write all characters into the file
			for (int i = 0; i < text.length(); i++)
				fileOutStream.write((byte) text.charAt(i));

			// Close the file
			fileOutStream.close();
		}
		catch (Exception e)
		{
			System.out.println("Logfile could not be finalized properly: "
					+ e.toString());
		}
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------
	private String getCurrentTime()
	{
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
	    return sdf.format(cal.getTime());
	}
	
	private String getErrorString(String moduleName, String txt)
	{
		return "<div class='error'>[ERROR "+ getCurrentTime() +"] [Module: "+ moduleName +"] " + txt + "</div>";
	}

	private String getInfoString(String moduleName, String txt)
	{
		return "<div class='info'>[INFO "+ getCurrentTime() +"] [Module: "+ moduleName +"] " + txt + "</div>";
	}

	private String getDebugString(String moduleName, String txt)
	{
		return "<div class='debug'>[DEBUG "+ getCurrentTime() +"] [Module: "+ moduleName +"] " + txt + "</div>";
	}

	private String getWarnString(String moduleName, String txt)
	{
		return "<div class='warn'>[WARNING "+ getCurrentTime() +"] [Module: "+ moduleName +"] " + txt + "</div>";
	}

	private String getSystemString(String txt)
	{
		return "<div class='system'>" + txt + "</div>";
	}

	/**
	 * Method that actually writes into the logfile
	 * 
	 * @param text
	 *            The text to write
	 */
	private void writeToFile(String text)
	{
		try
		{
			// open file for writing
			FileOutputStream fileOutStream = new FileOutputStream(logFile, true);

			// write all characters into the file
			for (int i = 0; i < text.length(); i++)
				fileOutStream.write((byte) text.charAt(i));

			// add a newline
			fileOutStream.write((byte) '\n');

			// close the file
			fileOutStream.close();
		}
		catch (Exception e)
		{
			System.out.println("Failed writing into Logfile." + e.toString());
		}
	}

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------
	/**
	 * 
	 * @param level
	 *            The new LogLevel for this LogWriter
	 */
	public void setLogLevel(LogLevel level)
	{
		this.logLevel = level;

		writeToFile(getSystemString("Using LogLevel: " + level.toString()));
	}
}
