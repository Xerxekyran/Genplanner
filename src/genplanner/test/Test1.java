package genplanner.test;

import genplanner.domain.solution.ISolution;
import genplanner.ga.CalculationConfiguration;
import genplanner.ga.ELogisticTaskStatus;
import genplanner.ga.CalculationConfiguration.CrossoverOperator;
import genplanner.ga.CalculationConfiguration.Initialisation;
import genplanner.services.EServices;
import genplanner.services.IGenplannerService;
import genplanner.services.ServiceLocator;
import util.LogWriter;

/**
 * A console test class to get fast results without any GUI
 * 
 * @author Lars George
 * @version 0.7
 * 
 */
public class Test1
{
	public static void main(String args[])
	{
		try
		{
			LogWriter.getInstance();

			IGenplannerService genplannerService = ((IGenplannerService) ServiceLocator.getInstance().getService(EServices.Genplanner));
			CalculationConfiguration config = new CalculationConfiguration();
			config.setInitialisation(Initialisation.Random);
			config.setCrossoverOperator(CrossoverOperator.UOBX);

			int key = genplannerService.createLogisticPlanCalculation("data", "testCase1_configuration.xml", config);
			genplannerService.setStatusOfLogisticPlanCalculation(key, ELogisticTaskStatus.Running);
			System.out.println("WHAIT START");
			Thread.sleep(3000);
			System.out.println("WHAIT END");
			ISolution solution = genplannerService.getCurrentSolutionForLogisticTaskID(key);
			System.out.println("Solution: " + solution.getTourDetailsAsString(0, 0));
			System.out.println("Solution: " + solution.getTourDetailsAsString(0, 1));
			System.out.println("Solution: " + solution.getTourDetailsAsString(0, 2));

		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
