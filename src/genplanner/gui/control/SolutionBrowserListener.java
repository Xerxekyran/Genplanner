package genplanner.gui.control;

import genplanner.domain.solution.CustomerEntry;
import genplanner.domain.solution.ISolution;
import genplanner.domain.solution.LogisticPlan;
import genplanner.ga.constraint.ConstraintViolation;
import genplanner.gui.GUICache;
import genplanner.gui.view.SolutionBrowserView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

/**
 * Listener class for the SolutionBrowser (controller)
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class SolutionBrowserListener extends MouseAdapter implements ActionListener
{
	@SuppressWarnings("unused")
	private static final String	MODULE_NAME			= "Genplanner-GUI::SolutionBrowser";
	public static final String	COMMAND_NEXT		= "com_next";
	public static final String	COMMAND_PREVIOUS	= "com_prev";

	private SolutionBrowserView	view				= null;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * 
	 * @param view
	 *            the view this controller belongs to
	 */
	public SolutionBrowserListener(SolutionBrowserView view)
	{
		this.view = view;
		refreshFields();
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------
	@Override
	public void mouseClicked(MouseEvent e)
	{
		// int column = view.getTbl_tours().columnAtPoint(e.getPoint());
		int row = view.getTbl_tours().rowAtPoint(e.getPoint());
		GUICache.setSelectedTour(row);
		view.getTxt_tourDetail().setText(GUICache.getSolution().getTourDetailsAsString(GUICache.getSelectedSolution(), row));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();
		// -------------------------------------------------------
		// COMMAND_NEXT
		// -------------------------------------------------------
		if (command.equals(COMMAND_NEXT))
		{
			GUICache.setSelectedSolution(GUICache.getSelectedSolution() + 1);
			refreshFields();
		}
		// -------------------------------------------------------
		// COMMAND_NEXT
		// -------------------------------------------------------
		else if (command.equals(COMMAND_PREVIOUS))
		{
			GUICache.setSelectedSolution(GUICache.getSelectedSolution() - 1);
			refreshFields();
		}
	}

	/**
	 * Refreshes all fields with the current selected solution data
	 */
	public void refreshFields()
	{
		ISolution solution = GUICache.getSolution();
		if (solution != null)
		{
			int numCustomer = 0;
			int numVehicle = 0;
			int numTimeWindowConstraints = 0;
			int numVehicleLoadConstraints = 0;
			int numWorkTimeConstraints = 0;
			double totalTravelDistance = 0.0;

			// look in every plan for global overview information
			for (LogisticPlan plan : solution.getPlans().get(GUICache.getSelectedSolution()))
			{
				numCustomer += plan.getCustomer().size();
				numVehicle += 1;
				totalTravelDistance += plan.getTravelDistance();

				for (ConstraintViolation viol : plan.getViolations())
				{
					switch (viol.getType())
					{
					case TimeWindow:
						numTimeWindowConstraints++;
						break;
					case VehicleLoad:
						numVehicleLoadConstraints++;
						break;
					case WorkTime:
						numWorkTimeConstraints++;
						break;
					}
				}

				for (CustomerEntry customerEntry : plan.getCustomer())
				{
					for (ConstraintViolation viol : customerEntry.getViolations())
					{
						switch (viol.getType())
						{
						case TimeWindow:
							numTimeWindowConstraints++;
							break;
						case VehicleLoad:
							numVehicleLoadConstraints++;
							break;
						case WorkTime:
							numWorkTimeConstraints++;
							break;
						}
					}
				}
			}

			DecimalFormat df_short = new DecimalFormat("#.00");
			DecimalFormat df_long = new DecimalFormat("#.000000");

			view.getTxt_customerCount().setText(Integer.toString(numCustomer));
			view.getTxt_vehicleCount().setText(Integer.toString(numVehicle));

			view.getLbl_solutionCounter().setText((GUICache.getSelectedSolution() + 1) + " / " + solution.getPlans().size());
			view.getLbl_fitnessValue().setText(df_long.format(solution.getFitnessValue(GUICache.getSelectedSolution())));

			view.getLbl_travelDistanceValue().setText(df_short.format(totalTravelDistance));

			view.getLbl_workTimeConstraintCount().setText(Integer.toString(numWorkTimeConstraints));
			view.getLbl_vehicleLoadConstraintCount().setText(Integer.toString(numVehicleLoadConstraints));
			view.getLbl_timeWindowConstraintCount().setText(Integer.toString(numTimeWindowConstraints));

			view.getTbl_tours().setModel(new TourTableModel());
			view.getTxt_tourDetail().setText("");
		}
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------

}
