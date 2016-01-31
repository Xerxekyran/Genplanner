package genplanner.language;

import java.util.Locale;
import java.util.ResourceBundle;

import util.LogWriter;
import util.XMLManager;
import util.LogWriter.LogLevel;

/**
 * A manager class for the internationalization of strings used in the
 * application
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class LanguageManager
{
	private static final String		MODULE_NAME		= "Language-Manager";
	private static LanguageManager	instance		= null;

	private Locale					currentLocale	= null;
	private String					language		= "";

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * Private constructor for singleton pattern
	 */
	private LanguageManager()
	{
		try
		{
			language = XMLManager.getInstance().getXMLLoader(XMLManager.APP_CONFIG_FILE).getString("Language");
		}
		catch (Exception e)
		{
			// An error occured while reading the config file, use a default
			// behaviour
			LogWriter.getInstance().logToFile(LogLevel.Warn, MODULE_NAME, "Failed loading the configuration value for the language, using default (en). " + e.toString());
			language = "en";
		}

		// create the locale instance for the configured language settings
		setLanguage(language);
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------

	/**
	 * Sets the language this manager should use
	 * 
	 * @param lang
	 *            The choosen language as a ISO code (eg 'de' or 'en')
	 */
	public void setLanguage(String lang)
	{
		// create the locale instance for the configured language settings
		currentLocale = new Locale(lang);
	}

	/**
	 * cleans all used data
	 */
	public void shutdown()
	{

	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------
	/**
	 * Getter for the specific ResourceBundle containing the messaged in the
	 * current set language / country
	 * 
	 * @param messagesType
	 *            The "bundle" name of the messages that need to be accessed
	 * @return A ResourceBundle with the messages in the configured language
	 */
	public ResourceBundle getMessageBundle(String messagesType)
	{
		return ResourceBundle.getBundle("genplanner/language/messages/" + messagesType, currentLocale);
	}

	/**
	 * Getter for the singleton instance
	 */
	public static LanguageManager getInstance()
	{
		if (instance == null)
			instance = new LanguageManager();

		return instance;
	}
}
