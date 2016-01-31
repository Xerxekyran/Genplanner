package genplanner.core.messages;

/**
 * A message that will initiate the shutdown of the application
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class ExitApplicationMsg implements ISystemMessage
{

	@Override
	public ESystemMessages getMessageType()
	{
		return ESystemMessages.ExitApplication;
	}

}
