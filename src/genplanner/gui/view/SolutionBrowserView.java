package genplanner.gui.view;

import genplanner.gui.FrontendManager;
import genplanner.gui.control.SolutionBrowserListener;
import genplanner.gui.control.TourTableModel;
import genplanner.language.LanguageManager;
import genplanner.language.messages.GenplannerMessageBundles;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

/**
 * The Solution browser GUI class
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class SolutionBrowserView extends JInternalFrame implements IFrame
{
	private JLabel				lbl_tours						= null;
	private JTable				tbl_tours						= null;

	private JLabel				lbl_details						= null;
	private JTextPane			txt_tourDetail					= null;

	private JLabel				lbl_overview					= null;
	private JLabel				lbl_vehicle						= null;
	private JLabel				lbl_vehicleCount				= null;
	private JLabel				lbl_customer					= null;
	private JLabel				lbl_customerCount				= null;

	private JLabel				lbl_timeWindowConstraint		= null;
	private JLabel				lbl_timeWindowConstraintCount	= null;

	private JLabel				lbl_workTimeConstraint			= null;
	private JLabel				lbl_workTimeConstraintCount		= null;

	private JLabel				lbl_vehicleLoadConstraint		= null;
	private JLabel				lbl_vehicleLoadConstraintCount	= null;

	private JLabel				lbl_fitness						= null;
	private JLabel				lbl_fitnessValue				= null;

	private JLabel				lbl_travelDistance				= null;
	private JLabel				lbl_travelDistanceValue			= null;

	private JLabel				lbl_solutionCounter				= null;

	private JButton				btn_previous					= null;
	private JButton				btn_next						= null;

	SolutionBrowserListener		listener						= null;

	private static final long	serialVersionUID				= 1L;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * Default Constructor
	 */
	public SolutionBrowserView()
	{
		this.setMaximizable(true);
		this.setIconifiable(true);
		this.setResizable(true);

		this.setClosable(true);
		addInternalFrameListener(FrontendManager.getInstance());

		this.setSize(884, 430);
		this.setVisible(true);

		init();

		listener = new SolutionBrowserListener(this);
		setActionListener(listener);
		setMouseListener(listener);

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
		btn_next.addActionListener(al);
		btn_previous.addActionListener(al);
	}

	/**
	 * 
	 * @param ml
	 */
	public void setMouseListener(MouseListener ml)
	{
		tbl_tours.addMouseListener(ml);
	}

	/**
	 * Refreshes the values ofthe selecteda data
	 */
	public void refreshFields()
	{
		listener.refreshFields();
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------
	/**
	 * 
	 */
	private void init()
	{
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		// define some special fonts
		Font headline1 = new Font("Arial", Font.BOLD, 16);
		Font headline2 = new Font("Arial", Font.BOLD, 12);

		// -------------------
		// creating the elements
		// -------------------

		// ------------------------

		lbl_overview = new JLabel();
		lbl_overview.setFont(headline1);

		lbl_customer = new JLabel();
		lbl_customer.setFont(headline2);
		lbl_customerCount = new JLabel();
		JPanel customerPanel = new JPanel();
		customerPanel.setLayout(new BorderLayout());
		customerPanel.add(lbl_customer, BorderLayout.WEST);
		customerPanel.add(lbl_customerCount, BorderLayout.CENTER);

		lbl_vehicle = new JLabel();
		lbl_vehicle.setFont(headline2);
		lbl_vehicleCount = new JLabel();
		JPanel vehiclePanel = new JPanel(new BorderLayout());
		vehiclePanel.add(lbl_vehicle, BorderLayout.WEST);
		vehiclePanel.add(lbl_vehicleCount, BorderLayout.CENTER);

		lbl_timeWindowConstraint = new JLabel();
		lbl_timeWindowConstraint.setFont(headline2);
		lbl_timeWindowConstraintCount = new JLabel();
		JPanel timeWindowConstraintPanel = new JPanel(new BorderLayout());
		timeWindowConstraintPanel.add(lbl_timeWindowConstraint, BorderLayout.WEST);
		timeWindowConstraintPanel.add(lbl_timeWindowConstraintCount, BorderLayout.CENTER);

		lbl_workTimeConstraint = new JLabel();
		lbl_workTimeConstraint.setFont(headline2);
		lbl_workTimeConstraintCount = new JLabel();
		JPanel workTimeConstraintPanel = new JPanel(new BorderLayout());
		workTimeConstraintPanel.add(lbl_workTimeConstraint, BorderLayout.WEST);
		workTimeConstraintPanel.add(lbl_workTimeConstraintCount, BorderLayout.CENTER);

		lbl_vehicleLoadConstraint = new JLabel();
		lbl_vehicleLoadConstraint.setFont(headline2);
		lbl_vehicleLoadConstraintCount = new JLabel();
		JPanel vehicleLoadConstraintPanel = new JPanel(new BorderLayout());
		vehicleLoadConstraintPanel.add(lbl_vehicleLoadConstraint, BorderLayout.WEST);
		vehicleLoadConstraintPanel.add(lbl_vehicleLoadConstraintCount, BorderLayout.CENTER);

		lbl_travelDistance = new JLabel();
		lbl_travelDistance.setFont(headline2);
		lbl_travelDistanceValue = new JLabel();
		JPanel travelDistancePanel = new JPanel(new BorderLayout());
		travelDistancePanel.add(lbl_travelDistance, BorderLayout.WEST);
		travelDistancePanel.add(lbl_travelDistanceValue, BorderLayout.CENTER);

		lbl_fitness = new JLabel();
		lbl_fitness.setFont(headline2);
		lbl_fitnessValue = new JLabel();
		JPanel fitnessPanel = new JPanel();
		fitnessPanel.setLayout(new BorderLayout());
		fitnessPanel.add(lbl_fitness, BorderLayout.WEST);
		fitnessPanel.add(lbl_fitnessValue, BorderLayout.CENTER);

		// ------------------------

		JPanel overviewPanel = new JPanel(new GridLayout(4, 1));
		overviewPanel.add(fitnessPanel);
		overviewPanel.add(customerPanel);
		overviewPanel.add(travelDistancePanel);
		overviewPanel.add(new JLabel(""));

		JPanel contraintsPanel = new JPanel(new GridLayout(4, 1));
		contraintsPanel.add(timeWindowConstraintPanel);
		contraintsPanel.add(workTimeConstraintPanel);
		contraintsPanel.add(vehicleLoadConstraintPanel);
		contraintsPanel.add(new JLabel(""));

		JPanel overviewConstraintPanel = new JPanel(new GridLayout(1, 2));
		overviewConstraintPanel.add(overviewPanel);
		overviewConstraintPanel.add(contraintsPanel);

		// ------------------------
		lbl_tours = new JLabel();
		lbl_tours.setFont(headline1);
		tbl_tours = new JTable();
		tbl_tours.setModel(new TourTableModel());
		tbl_tours.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPaneTours = new JScrollPane();
		scrollPaneTours.setViewportView(tbl_tours);
		JPanel tourPanel = new JPanel(new BorderLayout());
		tourPanel.add(lbl_tours, BorderLayout.NORTH);
		tourPanel.add(scrollPaneTours, BorderLayout.CENTER);

		// ------------------------
		lbl_details = new JLabel();
		lbl_details.setFont(headline1);
		txt_tourDetail = new JTextPane();
		txt_tourDetail.setSize(1024, 400);
		JScrollPane scrollPaneDetails = new JScrollPane();
		scrollPaneDetails.setViewportView(txt_tourDetail);
		JPanel detailsPanel = new JPanel(new BorderLayout());
		detailsPanel.add(lbl_details, BorderLayout.NORTH);
		detailsPanel.add(scrollPaneDetails, BorderLayout.CENTER);

		// ------------------------
		lbl_solutionCounter = new JLabel("0 / 0");
		lbl_solutionCounter.setHorizontalAlignment(SwingConstants.CENTER);

		btn_previous = new JButton();
		btn_previous.setActionCommand(SolutionBrowserListener.COMMAND_PREVIOUS);

		btn_next = new JButton();
		btn_next.setActionCommand(SolutionBrowserListener.COMMAND_NEXT);

		JPanel buttonPane = new JPanel(new BorderLayout());
		buttonPane.add(btn_previous, BorderLayout.WEST);
		buttonPane.add(lbl_solutionCounter, BorderLayout.CENTER);
		buttonPane.add(btn_next, BorderLayout.EAST);

		// -------------------
		// adding elements to the contentPane
		// -------------------
		JPanel top = new JPanel();
		top.setLayout(new GridLayout(2, 1));
		top.add(lbl_overview);
		top.add(overviewConstraintPanel);

		contentPane.add(top, BorderLayout.NORTH);

		JPanel center = new JPanel();
		center.setLayout(new GridLayout(2, 1));
		center.add(tourPanel);
		center.add(detailsPanel);
		contentPane.add(center, BorderLayout.CENTER);

		contentPane.add(buttonPane, BorderLayout.SOUTH);
	}

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------

	@Override
	public void setLocalizedText()
	{
		// get localized text
		ResourceBundle text = LanguageManager.getInstance().getMessageBundle(GenplannerMessageBundles.INTERNALFRAME_SOLUTIONBROWSER_MESSAGES);

		// title
		setTitle(text.getString("title"));

		lbl_overview.setText(text.getString("lbl_overview"));
		lbl_tours.setText(text.getString("lbl_tours"));
		lbl_details.setText(text.getString("lbl_details"));
		lbl_customer.setText(text.getString("lbl_customer") + ": ");
		lbl_vehicle.setText(text.getString("lbl_vehicle") + ": ");
		lbl_fitness.setText(text.getString("lbl_fitness") + ": ");
		lbl_travelDistance.setText(text.getString("lbl_travlDistance") + ": ");
		lbl_vehicleLoadConstraint.setText(text.getString("lbl_vehicleLoadConstraint") + ": ");
		lbl_workTimeConstraint.setText(text.getString("lbl_workTimeConstraint") + ": ");
		lbl_timeWindowConstraint.setText(text.getString("lbl_timeWindowConstraint") + ": ");

		btn_next.setText(text.getString("btn_next"));
		btn_previous.setText(text.getString("btn_previous"));
	}

	public JLabel getLbl_tours()
	{
		return lbl_tours;
	}

	public JTable getTbl_tours()
	{
		return tbl_tours;
	}

	public JLabel getLbl_details()
	{
		return lbl_details;
	}

	public JTextPane getTxt_tourDetail()
	{
		return txt_tourDetail;
	}

	public JLabel getLbl_overview()
	{
		return lbl_overview;
	}

	public JLabel getLbl_vehicle()
	{
		return lbl_vehicle;
	}

	public JLabel getTxt_vehicleCount()
	{
		return lbl_vehicleCount;
	}

	public JLabel getLbl_customer()
	{
		return lbl_customer;
	}

	public JLabel getTxt_customerCount()
	{
		return lbl_customerCount;
	}

	public JButton getBtn_previous()
	{
		return btn_previous;
	}

	public JButton getBtn_next()
	{
		return btn_next;
	}

	public JLabel getLbl_solutionCounter()
	{
		return lbl_solutionCounter;
	}

	public JLabel getLbl_fitnessValue()
	{
		return lbl_fitnessValue;
	}

	public JLabel getLbl_travelDistanceValue()
	{
		return lbl_travelDistanceValue;
	}

	public JLabel getLbl_timeWindowConstraintCount()
	{
		return lbl_timeWindowConstraintCount;
	}

	public JLabel getLbl_vehicleLoadConstraintCount()
	{
		return lbl_vehicleLoadConstraintCount;
	}

	public JLabel getLbl_workTimeConstraintCount()
	{
		return lbl_workTimeConstraintCount;
	}

}
