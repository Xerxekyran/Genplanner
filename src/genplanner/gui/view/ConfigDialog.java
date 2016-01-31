package genplanner.gui.view;

import genplanner.ga.CalculationConfiguration;
import genplanner.ga.CalculationConfiguration.CrossoverOperator;
import genplanner.ga.CalculationConfiguration.Initialisation;
import genplanner.ga.CalculationConfiguration.NaturalSelector;
import genplanner.ga.CalculationConfiguration.SelectionOperator;
import genplanner.language.LanguageManager;
import genplanner.language.messages.GenplannerMessageBundles;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

/**
 * A Dialog that offers the ser to edit the calculation settings of LogisticTask
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class ConfigDialog extends JDialog
{
	private static final long			serialVersionUID			= 1L;

	private CalculationConfiguration	config						= new CalculationConfiguration();

	private JComboBox					cbx_naturalSelector			= null;
	private JComboBox					cbx_crossoverSelector		= null;
	private JComboBox					cbx_initialisationSelector	= null;
	private JComboBox					cbx_selectionOperator		= null;
	private JSpinner					spi_population				= null;
	private JSpinner					spi_maxGenerations			= null;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------

	/**
	 * constructor
	 * 
	 * @param parentFrame
	 *            the owner of this dialog
	 */
	public ConfigDialog(Frame parentFrame)
	{
		super(parentFrame, true);

		setSize(500, 200);

		// center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height / 2 - getHeight() / 2);

		// set the title from the language bundle
		ResourceBundle text = LanguageManager.getInstance().getMessageBundle(GenplannerMessageBundles.DIALOG_CONFIG_MESSAGES);
		setTitle(text.getString("title"));

		init();
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------
	/**
	 * sets the first needed values (components)
	 */
	private void init()
	{
		ResourceBundle text = LanguageManager.getInstance().getMessageBundle(GenplannerMessageBundles.DIALOG_CONFIG_MESSAGES);

		Container contentPane = getContentPane();
		contentPane.setLayout(new GridLayout(7, 1));

		contentPane.add(getInitialisationPanel(text));
		contentPane.add(getSelectionOperatorPanel(text));
		contentPane.add(getCrossoverPanel(text));
		contentPane.add(getNaturalSelectorPanel(text));
		contentPane.add(getPopulationPanel(text));
		contentPane.add(getMaxGenerationPanel(text));

		contentPane.add(getOkButton(text));
	}

	/**
	 * 
	 * @param text
	 *            the resource bund to retrieve the localized text
	 * @return the ok button
	 */
	private JButton getOkButton(ResourceBundle text)
	{
		JButton ret = new JButton(text.getString("btn_ok"));
		ret.addActionListener(new ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent e)
			{
				setVisible(false);
			}
		});

		return ret;
	}

	/**
	 * 
	 * @return the panel with the maximum generations selection
	 * @param text
	 *            the resource bund to retrieve the localized text
	 */
	private JPanel getMaxGenerationPanel(ResourceBundle text)
	{
		JPanel ret = new JPanel(new GridLayout(1, 2));

		JLabel label = new JLabel(text.getString("lbl_maxGenerations") + ": ");

		spi_maxGenerations = new JSpinner();
		spi_maxGenerations.setValue(300);

		ret.add(label);
		ret.add(spi_maxGenerations);

		return ret;
	}

	/**
	 * 
	 * @return the panel with the population selection
	 * @param text
	 *            the resource bund to retrieve the localized text
	 */
	private JPanel getPopulationPanel(ResourceBundle text)
	{
		JPanel ret = new JPanel(new GridLayout(1, 2));

		JLabel label = new JLabel(text.getString("lbl_population") + ": ");

		spi_population = new JSpinner();
		spi_population.setValue(50);

		ret.add(label);
		ret.add(spi_population);

		return ret;
	}

	/**
	 * 
	 * @return the panel with the crossover selection
	 * @param text
	 *            the resource bund to retrieve the localized text
	 */
	private JPanel getCrossoverPanel(ResourceBundle text)
	{
		JPanel ret = new JPanel(new GridLayout(1, 2));

		JLabel label = new JLabel(text.getString("lbl_crossover") + ": ");

		Object[] items = { CalculationConfiguration.CrossoverOperator.BCRC, CalculationConfiguration.CrossoverOperator.UOBX };
		cbx_crossoverSelector = new JComboBox(items);

		ret.add(label);
		ret.add(cbx_crossoverSelector);

		return ret;
	}

	/**
	 * 
	 * @return the panel with the selection operator selection
	 * @param text
	 *            the resource bund to retrieve the localized text
	 */
	private JPanel getSelectionOperatorPanel(ResourceBundle text)
	{
		JPanel ret = new JPanel(new GridLayout(1, 2));

		JLabel label = new JLabel(text.getString("lbl_selectionOperator") + ": ");

		Object[] items = { CalculationConfiguration.SelectionOperator.Random, CalculationConfiguration.SelectionOperator.Tournament };
		cbx_selectionOperator = new JComboBox(items);

		ret.add(label);
		ret.add(cbx_selectionOperator);

		return ret;
	}

	/**
	 * 
	 * @return the panel with the natural selector selection
	 * @param text
	 *            the resource bund to retrieve the localized text
	 */
	private JPanel getNaturalSelectorPanel(ResourceBundle text)
	{
		JPanel ret = new JPanel(new GridLayout(1, 2));

		JLabel label = new JLabel(text.getString("lbl_naturalSelector") + ": ");

		Object[] items = { CalculationConfiguration.NaturalSelector.BestChromSelector, CalculationConfiguration.NaturalSelector.WeightedRoulette };
		cbx_naturalSelector = new JComboBox(items);

		ret.add(label);
		ret.add(cbx_naturalSelector);

		return ret;
	}

	/**
	 * 
	 * @return the panel with the crossover selection
	 * @param text
	 *            the resource bund to retrieve the localized text
	 */
	private JPanel getInitialisationPanel(ResourceBundle text)
	{
		JPanel ret = new JPanel(new GridLayout(1, 2));

		JLabel label = new JLabel(text.getString("lbl_initialisation") + ": ");

		Object[] items = { CalculationConfiguration.Initialisation.NearestFirst, CalculationConfiguration.Initialisation.Random };
		cbx_initialisationSelector = new JComboBox(items);

		ret.add(label);
		ret.add(cbx_initialisationSelector);

		return ret;
	}

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------

	/**
	 * Reads the current values of the dialog chooses and returns the filled
	 * CalculationConfiguration
	 * 
	 * @return An CalculationConfiguration
	 */
	public CalculationConfiguration getConfiguration()
	{
		config.setCrossoverOperator((CrossoverOperator) cbx_crossoverSelector.getSelectedItem());
		config.setInitialisation((Initialisation) cbx_initialisationSelector.getSelectedItem());
		config.setNaturalSelector((NaturalSelector) cbx_naturalSelector.getSelectedItem());
		config.setMaxGenerations((Integer) spi_maxGenerations.getValue());
		config.setPopulationSize((Integer) spi_population.getValue());
		config.setSelectionOperator((SelectionOperator) cbx_selectionOperator.getSelectedItem());

		return this.config;
	}

}
