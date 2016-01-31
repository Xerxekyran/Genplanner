package genplanner.gui.view;

import genplanner.domain.solution.CustomerEntry;
import genplanner.domain.solution.ISolution;
import genplanner.domain.solution.LogisticPlan;
import genplanner.gui.FrontendManager;
import genplanner.gui.GUICache;
import genplanner.gui.control.MapViewListener;
import genplanner.language.LanguageManager;
import genplanner.language.messages.GenplannerMessageBundles;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Random;
import java.util.ResourceBundle;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * This class represents an InternalFrame displaying the Map based on the given
 * data
 * 
 * @author Lars George
 * @version 1.0 
 * 
 */
public class MapView extends JInternalFrame implements IFrame
{
	private static int			OFFSET_X			= 50;
	private static int			OFFSET_Y			= 50;
	private static int			DEPOT_WIDTH			= 9;
	private static int			DEPOT_HEIGHT		= 7;
	private static int			CUSTOMER_WIDTH		= 4;
	private static int			CUSTOMER_HEIGHT		= 4;

	private JPanel				map					= null;
	private JLabel				lbl_solutionCounter	= null;
	private static final long	serialVersionUID	= 1L;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * 
	 */
	public MapView()
	{
		this.setMaximizable(true);
		this.setIconifiable(true);
		this.setResizable(true);
		
		this.setClosable(true);
		addInternalFrameListener(FrontendManager.getInstance());
		
		this.setBounds(0, 430, 400, 310);
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		
		initMap();

		MapViewListener listener = new MapViewListener(this);
		setActionListener(listener);
		
		setLocalizedText();
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------
	/**
	 * 
	 * @param al
	 */
	public void setActionListener(ActionListener al)
	{

	}

	@Override
	public void paint(Graphics gr)
	{
		super.paint(gr);
		float[] dashArray = { 10.0f, 10.0f };

		BasicStroke normalStroke = new BasicStroke(1.8f, // Breite
				BasicStroke.CAP_SQUARE, // End Style
				BasicStroke.JOIN_ROUND, // Join Style
				1f, // Limit für Join
				dashArray, // Strichelung
				0 // offset in Pixeln f. Strichelung
		);
		
		BasicStroke highlightedStroke = new BasicStroke(2.8f);

		Graphics2D g = (Graphics2D) gr;
		g.setStroke(highlightedStroke);

		// draw the map border
		double mapLength = this.getWidth() - 2 * OFFSET_X;
		double mapHeight = this.getHeight() - 2 * OFFSET_Y;

		g.setColor(Color.WHITE);
		g.drawRect(OFFSET_X, OFFSET_Y, (int) mapLength, (int) mapHeight);

		// get the DataSet from the belonging LogisticTask
		ISolution allSolutions = GUICache.getSolution();

		// if we have something to draw
		if (allSolutions != null && allSolutions.getPlans() != null)
		{
			// refresh the text
			lbl_solutionCounter.setText(Integer.toString(GUICache.getSelectedSolution() + 1));

			LinkedList<LogisticPlan> selectedSolution = allSolutions.getPlans().get(GUICache.getSelectedSolution());

			// first of all, get highest value to be able to normalize the
			// coordinates correctly in the borders
			double biggestX = Double.MIN_VALUE;
			double biggestY = Double.MIN_VALUE;

			for (LogisticPlan plan : selectedSolution)
			{
				if (plan.getDepot().getPosition().getX() > biggestX)
					biggestX = plan.getDepot().getPosition().getX();
				if (plan.getDepot().getPosition().getY() > biggestY)
					biggestY = plan.getDepot().getPosition().getY();

				for (CustomerEntry custEnt : plan.getCustomer())
				{
					if (custEnt.getCustomer().getPosition().getX() > biggestX)
						biggestX = custEnt.getCustomer().getPosition().getX();
					if (custEnt.getCustomer().getPosition().getY() > biggestY)
						biggestY = custEnt.getCustomer().getPosition().getY();
				}
			}

			// increase biggest values to have the whole point in the map (the
			// rectangle would be outside else)
			biggestX += 10;
			biggestY += 10;
			// ----------------------------------
			// draw every customer
			// ----------------------------------
			g.setColor(Color.BLACK);
			for (LogisticPlan plan : selectedSolution)
			{
				for (CustomerEntry custEnt : plan.getCustomer())
				{
					g.fillRect(((int) (custEnt.getCustomer().getPosition().getX() / biggestX * mapLength)) + OFFSET_X - CUSTOMER_WIDTH, ((int) (custEnt.getCustomer().getPosition().getY() / biggestY * mapHeight)) + OFFSET_Y - CUSTOMER_HEIGHT, CUSTOMER_WIDTH * 2, CUSTOMER_HEIGHT * 2);
				}
			}

			// ----------------------------------
			// draw the depots
			// ----------------------------------
			g.setColor(Color.MAGENTA);

			for (LogisticPlan plan : selectedSolution)
			{
				g.fillRect(((int) (plan.getDepot().getPosition().getX() / biggestX * mapLength)) + OFFSET_X - DEPOT_WIDTH, ((int) (plan.getDepot().getPosition().getY() / biggestY * mapHeight)) + OFFSET_Y - DEPOT_HEIGHT, DEPOT_WIDTH * 2, DEPOT_HEIGHT * 2);

			}

			// ----------------------------------
			// draw the routes
			// ----------------------------------
			Random rand = new Random();
			float hue = 0.5f;
			float sat = 0.5f;
			float bright = 0.5f;

			for (int tourIndex = 0; tourIndex < selectedSolution.size(); tourIndex++)
			{
				LogisticPlan lp = selectedSolution.get(tourIndex);

				if (tourIndex == GUICache.getSelectedTour())
				{
					g.setStroke(highlightedStroke);

				}
				else
				{
					g.setStroke(normalStroke);
				}

				// Random color for each path
				hue = rand.nextFloat();
				// sat = rand.nextFloat();
				// bright = rand.nextFloat();
				g.setColor(Color.getHSBColor(hue, sat, bright));

				// if we have at least one customer on this plan, draw line
				// from depot to first and last customer
				if (lp.getCustomer().size() > 0)
				{
					g.drawLine(((int) (lp.getDepot().getPosition().getX() / biggestX * mapLength)) + OFFSET_X,
							((int) (lp.getDepot().getPosition().getY() / biggestY * mapHeight)) + OFFSET_Y,
							((int) (lp.getCustomer().get(0).getCustomer().getPosition().getX() / biggestX * mapLength)) + OFFSET_X,
							((int) (lp.getCustomer().get(0).getCustomer().getPosition().getY() / biggestY * mapHeight)) + OFFSET_Y);

					g.drawLine(((int) (lp.getDepot().getPosition().getX() / biggestX * mapLength)) + OFFSET_X,
							((int) (lp.getDepot().getPosition().getY() / biggestY * mapHeight)) + OFFSET_Y,
							((int) (lp.getCustomer().get(lp.getCustomer().size() - 1).getCustomer().getPosition().getX() / biggestX * mapLength)) + OFFSET_X,
							((int) (lp.getCustomer().get(lp.getCustomer().size() - 1).getCustomer().getPosition().getY() / biggestY * mapHeight)) + OFFSET_Y);

				}

				// for every customer on the plan
				for (int i = 0; i < lp.getCustomer().size() - 1; i++)
				{
					g.drawLine(((int) (lp.getCustomer().get(i).getCustomer().getPosition().getX() / biggestX * mapLength)) + OFFSET_X,
							((int) (lp.getCustomer().get(i).getCustomer().getPosition().getY() / biggestY * mapHeight)) + OFFSET_Y,
							((int) (lp.getCustomer().get(i + 1).getCustomer().getPosition().getX() / biggestX * mapLength)) + OFFSET_X,
							((int) (lp.getCustomer().get(i + 1).getCustomer().getPosition().getY() / biggestY * mapHeight)) + OFFSET_Y);
				}
			}

		}
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------
	/**
	 * 
	 */
	private void initMap()
	{
		lbl_solutionCounter = new JLabel("N/A");
		getContentPane().add(lbl_solutionCounter, BorderLayout.NORTH);
		lbl_solutionCounter.setHorizontalAlignment(SwingConstants.CENTER);

		map = new JPanel();
		map.setVisible(true);
		map.setBackground(Color.LIGHT_GRAY);
		getContentPane().add(map, BorderLayout.CENTER);
	}

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------

	@Override
	public void setLocalizedText()
	{
		// get localized text
		ResourceBundle text = LanguageManager.getInstance().getMessageBundle(GenplannerMessageBundles.INTERNALFRAME_MAP_MESSAGES);

		// title
		setTitle(text.getString("title"));
	}

}
