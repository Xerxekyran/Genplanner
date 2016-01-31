package genplanner.gui.control;

import genplanner.domain.solution.ISolution;
import genplanner.domain.solution.LogisticPlan;
import genplanner.gui.GUICache;
import genplanner.language.LanguageManager;
import genplanner.language.messages.GenplannerMessageBundles;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javax.swing.table.AbstractTableModel;

/**
 * TableModel for a Tour Table. Tells the application how the table is
 * structured and where to put the data
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class TourTableModel extends AbstractTableModel
{
	private static final long	serialVersionUID	= 1L;

	@Override
	public int getColumnCount()
	{
		return 3;
	}

	@Override
	public int getRowCount()
	{
		if (GUICache.getSolution() != null)
			return GUICache.getSolution().getPlans().get(GUICache.getSelectedSolution()).size();
		else
			return 0;
	}

	@Override
	public Object getValueAt(int row, int column)
	{
		String ret = null;
		ISolution solution = GUICache.getSolution();
		LinkedList<LogisticPlan> routes = solution.getPlans().get(GUICache.getSelectedSolution());
		LogisticPlan plan = routes.get(row);
		DecimalFormat df = new DecimalFormat("#.00");

		switch (column)
		{
		case 0:
			ret = solution.getVehicle(row).getName();
			break;
		case 1:
			ret = Integer.toString(plan.getCustomer().size());
			break;
		case 2:
			ret = df.format(plan.getTravelDistance());
			break;
		}

		return ret;
	}

	@Override
	public String getColumnName(int column)
	{
		String ret = null;

		// get localized text
		ResourceBundle text = LanguageManager.getInstance().getMessageBundle(GenplannerMessageBundles.INTERNALFRAME_SOLUTIONBROWSER_MESSAGES);

		switch (column)
		{
		case 0:
			ret = text.getString("vehicles");
			break;
		case 1:
			ret = text.getString("customers");
			break;
		case 2:
			ret = text.getString("length");
			break;
		}
		return ret;
	}

}
