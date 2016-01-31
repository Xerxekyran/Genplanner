package genplanner.gui.view;

import genplanner.gui.FrontendManager;
import genplanner.gui.GUICache;
import genplanner.language.LanguageManager;
import genplanner.language.messages.GenplannerMessageBundles;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import util.Pair;
import de.progra.charting.DefaultChart;
import de.progra.charting.event.ChartDataModelEvent;
import de.progra.charting.event.ChartDataModelListener;
import de.progra.charting.model.EditableChartDataModel;
import de.progra.charting.render.LineChartRenderer;
import de.progra.charting.swing.ChartPanel;

/**
 * This class represents an InternalFrame displaying the LineChart based on the
 * given data
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class LineChartView extends JInternalFrame implements IFrame, ChartDataModelListener
{
	EditableChartDataModel		data						= null;
	private ChartPanel			chartPanel					= null;
	private JPanel				jContentPane				= null;
	private static final long	serialVersionUID			= 1L;
	private int					lastDrawnFitnessValue		= 0;
	private int					lastDrawnAvgFitnessValue	= 0;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * 
	 */
	public LineChartView()
	{
		super();

		this.setMaximizable(true);
		this.setIconifiable(true);
		this.setResizable(true);

		this.setClosable(true);
		addInternalFrameListener(FrontendManager.getInstance());

		this.setBounds(485, 430, 400, 310);
		this.setContentPane(getJContentPane());
		this.setVisible(true);

		initGraph();

		setLocalizedText();
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------

	/**
	 * Resets the graph (like creating a new LineChart Frame)
	 */
	public void resetGraph()
	{
		lastDrawnAvgFitnessValue = 0;
		lastDrawnFitnessValue = 0;
		jContentPane = new JPanel();
		jContentPane.setLayout(new java.awt.BorderLayout());
		setContentPane(jContentPane);
		initGraph();
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);

		int i = 0;
		// fitness values
		if (GUICache.getFitnessHistory() != null)
		{
			LinkedList<Pair<Double, Double>> values = GUICache.getFitnessHistory();
			synchronized (values)
			{
				if (values.size() > lastDrawnFitnessValue)
				{
					for (Pair<Double, Double> node : values)
					{
						if (i >= lastDrawnFitnessValue)
						{
							addData(0, node.firstValue, node.secondValue);

							lastDrawnFitnessValue = i;
						}
						i++;
					}

				}
			}
		}
		i = 0;
		// average fitness
		if (GUICache.getAvgFitnessHistory() != null)
		{
			LinkedList<Pair<Double, Double>> values = GUICache.getAvgFitnessHistory();
			synchronized (values)
			{
				if (values.size() > lastDrawnAvgFitnessValue)
				{
					for (Pair<Double, Double> node : values)
					{
						if (i >= lastDrawnAvgFitnessValue)
						{
							addData(1, node.firstValue, node.secondValue);

							lastDrawnAvgFitnessValue = i;
						}
						i++;
					}
				}
			}
		}
	}

	@Override
	public void chartDataChanged(ChartDataModelEvent arg0)
	{
		// the data changed, so we have to repaint
		chartPanel.revalidate();
		repaint();
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------
	/**
	 * 
	 */
	private void initGraph()
	{
		// get localized text
		ResourceBundle text = LanguageManager.getInstance().getMessageBundle(GenplannerMessageBundles.INTERNALFRAME_LINECHART_MESSAGES);

		double[][] model = { { 0.0, 0.000001 }, { 0.0, 0.000001 } }; // Create
		// data
		// array

		double[] columns = { 0.0, 1.0 }; // Create x-axis values

		String[] rows = { text.getString("best_fitness"), text.getString("avg_fitness") }; // Create
																							// data
		// set titl

		// String[] rows = { "best fitness", "avg fitness" };

		// Create an editable chart data model
		data = new EditableChartDataModel(model, columns, rows);

		// Creating the Swing ChartPanel
		chartPanel = new ChartPanel(data, " ", DefaultChart.LINEAR_X_LINEAR_Y);

		// Adding ChartRenderer
		chartPanel.addChartRenderer(new LineChartRenderer(chartPanel.getCoordSystem(), data), 1);

		chartPanel.setVisible(true);
		getJContentPane().add(chartPanel, BorderLayout.CENTER);
	}

	/**
	 * Adds a data value to the diagram
	 * 
	 * @param curveNumber
	 *            The number of the curve the data should be added.
	 * @param x_val
	 *            The x value
	 * @param y_val
	 *            The y value
	 */
	private void addData(int curveNumber, double x_val, double y_val)
	{
		data.insertValue(curveNumber, new Double(y_val), new Double(x_val));
		this.chartDataChanged(null);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return JPanel
	 */
	private JPanel getJContentPane()
	{
		if (jContentPane == null)
		{
			jContentPane = new JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
		}
		return jContentPane;
	}

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------

	@Override
	public void setLocalizedText()
	{
		// get localized text
		ResourceBundle text = LanguageManager.getInstance().getMessageBundle(GenplannerMessageBundles.INTERNALFRAME_LINECHART_MESSAGES);

		// title
		setTitle(text.getString("title"));
	}

}
