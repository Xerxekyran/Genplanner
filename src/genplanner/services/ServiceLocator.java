package genplanner.services;

import java.util.HashMap;

/**
 * Class that offers service objects
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class ServiceLocator
{
	/**
	 * Liste der verfügbaren Services
	 */
	private HashMap<EServices, IService>	services	= new HashMap<EServices, IService>();

	/**
	 * Instanz für das Singleton Pattern
	 */
	private static ServiceLocator			instance	= null;

	/**
	 * private ctor (used for the singleton pattern)
	 */
	private ServiceLocator()
	{
		// create the services and add them to the list of available services
		IGenplannerService genplannerService = new GenplannerService();
		setService(EServices.Genplanner, genplannerService);
	}

	/**
	 * Sets a service to the list of available services
	 * 
	 * @param serviceType
	 *            The type of the service
	 * @param service
	 *            Reference to the service object
	 */
	private void setService(EServices serviceType, IService service)
	{
		services.put(serviceType, service);
	}

	/**
	 * Singleton Get-MEthod
	 * 
	 * @return Instance of the ServiceLocator
	 */
	public static ServiceLocator getInstance()
	{
		if (instance == null)
			instance = new ServiceLocator();

		return instance;
	}

	/**
	 * Getter for the available services
	 * 
	 * @param serviceType
	 *            The type of the servcice that should be retrieved
	 * @return A service-object reference belonging to the given type. null if
	 *         their exists nu such service.
	 */
	public IService getService(EServices serviceType)
	{
		return services.get(serviceType);
	}
}
