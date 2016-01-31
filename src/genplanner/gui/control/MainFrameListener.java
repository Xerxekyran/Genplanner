package genplanner.gui.control;

import genplanner.core.Genplanner;
import genplanner.core.messages.ExitApplicationMsg;
import genplanner.ga.CalculationConfiguration;
import genplanner.ga.ELogisticTaskStatus;
import genplanner.gui.FrontendManager;
import genplanner.gui.GUICache;
import genplanner.gui.view.ConfigDialog;
import genplanner.gui.view.LineChartView;
import genplanner.gui.view.MainFrame;
import genplanner.gui.view.MapView;
import genplanner.gui.view.MapView3D;
import genplanner.gui.view.SolutionBrowserView;
import genplanner.language.LanguageManager;
import genplanner.services.EServices;
import genplanner.services.IGenplannerService;
import genplanner.services.ServiceLocator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import util.BrowserControl;
import util.LogWriter;
import util.LogWriter.LogLevel;

/**
 * Listener class for the MainFrame (controller)
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class MainFrameListener implements WindowListener, ActionListener
{
	private static final String	MODULE_NAME					= "Genplanner-GUI::MainFrame";
	public static final String	COMMAND_SOLUTIOUNBROWSER	= "com_browser";
	public static final String	COMMAND_ABOUT				= "com_about";
	public static final String	COMMAND_HELP				= "com_help";
	public static final String	COMMAND_LOGFILE				= "com_logfile";	
	public static final String	COMMAND_EXIT				= "com_exit";
	public static final String	COMMAND_PAUSE				= "com_pause";
	public static final String	COMMAND_NEW					= "com_new";
	public static final String	COMMAND_SAVE				= "com_save";
	public static final String	COMMAND_START				= "com_start";
	public static final String	COMMAND_MAP					= "com_map";
	public static final String	COMMAND_OPEN3DMAP			= "com_open3DMap";
	public static final String	COMMAND_CLOSE3DMAP			= "com_close3DMap";
	public static final String	COMMAND_CHART				= "com_chart";
	public static final String	COMMAND_DE					= "com_de";
	public static final String	COMMAND_EN					= "com_en";
	private Integer				logisticTaskID				= 0;
	private MapView3D			map3D						= null;
	private MapView				map							= null;
	private LineChartView		chart						= null;
	private SolutionBrowserView	sBrowser					= null;
	private MainFrame			view						= null;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------

	public MainFrameListener(MainFrame view)
	{
		this.view = view;

		view.refresh3DMenuItems(false);
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	public void setView(MainFrame mainFrame)
	{
		view = mainFrame;
	}

	/**
	 * This method should be invoced in the case, that an internal frame was
	 * close
	 * 
	 * @param internalFrame
	 */
	public void internalFrameClosed(JInternalFrame internalFrame)
	{
		if (internalFrame.equals(map))
		{
			map = null;
		}
		else if (internalFrame.equals(chart))
		{
			chart = null;
		}
		else if (internalFrame.equals(sBrowser))
		{
			sBrowser = null;
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0)
	{
	}

	@Override
	public void windowClosed(WindowEvent arg0)
	{
	}

	@Override
	public void windowClosing(WindowEvent arg0)
	{
		Genplanner.getInstance().addSystemMessage(new ExitApplicationMsg());
	}

	@Override
	public void windowDeactivated(WindowEvent arg0)
	{
	}

	@Override
	public void windowDeiconified(WindowEvent arg0)
	{
	}

	@Override
	public void windowIconified(WindowEvent arg0)
	{
	}

	@Override
	public void windowOpened(WindowEvent arg0)
	{
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();
		// -------------------------------------------------------
		// COMMAND_ABOUT
		// -------------------------------------------------------
		if (command.equals(COMMAND_ABOUT))
		{
			JOptionPane.showMessageDialog(view, "Dieser Prototyp entstand im Rahmen meiner Bachelorarbeit.\n" + "Titel: \"Logistikplanung unter Verwendung von Genetischen Algorithmen.\"\n\n" + "Kontakt: lars.george@gmx.net\n" + "© 2010 Lars George");
		}
		// -------------------------------------------------------
		// COMMAND_HELP
		// -------------------------------------------------------
		else if (command.equals(COMMAND_HELP))
		{
			File f = new File("help/index.html");
			BrowserControl.displayURL(f.getAbsolutePath());
		}
		// -------------------------------------------------------
		// COMMAND_LOGFILE
		// -------------------------------------------------------
		else if (command.equals(COMMAND_LOGFILE))
		{
			File f = new File("logfile.html");
			BrowserControl.displayURL(f.getAbsolutePath());
		}
		// -------------------------------------------------------
		// COMMAND_NEW
		// -------------------------------------------------------
		else if (command.equals(COMMAND_NEW))
		{
			String path = "";
			String fileName = "";

			// FileChooser to select the configuration file
			JFileChooser jfc = new JFileChooser();
			jfc.setFileFilter(new FileFilter()
			{
				@Override
				public String getDescription()
				{
					return "Solution Files";
				}

				@Override
				public boolean accept(File f)
				{
					return f.getName().toLowerCase().endsWith("_configuration.xml") || f.isDirectory();
				}
			});

			int state = jfc.showOpenDialog(null);
			if (state == JFileChooser.APPROVE_OPTION)
			{
				try
				{
					path = jfc.getSelectedFile().getParent();
					fileName = jfc.getSelectedFile().getName();

					// open the configuration dialog
					ConfigDialog dialog = new ConfigDialog(view);
					dialog.setVisible(true);
					CalculationConfiguration config = dialog.getConfiguration();

					// if we have an old logisticTask, stop this one first
					if (GUICache.hasLogisticTask())
					{
						((IGenplannerService) ServiceLocator.getInstance().getService(EServices.Genplanner)).setStatusOfLogisticPlanCalculation(GUICache.getLogisticTaskID(), ELogisticTaskStatus.Stopped);
					}

					// create the actual logistic task
					logisticTaskID = Integer.valueOf(((IGenplannerService) ServiceLocator.getInstance().getService(EServices.Genplanner)).createLogisticPlanCalculation(path, fileName, config));

					GUICache.setLogisticTaskID(logisticTaskID);

					if (chart != null)
					{
						chart.resetGraph();
					}

					if (map != null)
					{
						map.repaint();
					}

					if (sBrowser != null)
					{
						sBrowser.refreshFields();
					}
				}
				catch (Exception ex)
				{
					LogWriter.getInstance().logToFile(LogLevel.Error, MODULE_NAME, "Error while creating a new Logistic Task: " + ex.toString());
					System.err.println(ex.toString());
				}
			}
		}
		// -------------------------------------------------------
		// COMMAND_MAP
		// -------------------------------------------------------
		else if (command.equals(COMMAND_MAP))
		{
			openInternal2DMapFrame();
		}
		// -------------------------------------------------------
		// COMMAND_OPEN3DMAP
		// -------------------------------------------------------
		else if (command.equals(COMMAND_OPEN3DMAP))
		{
			openExternal3DViewFrame();
		}
		// -------------------------------------------------------
		// COMMAND_CLOSE3DMAP
		// -------------------------------------------------------
		else if (command.equals(COMMAND_CLOSE3DMAP))
		{
			closeExternal3DViewFrame();
		}
		// -------------------------------------------------------
		// COMMAND_CHART
		// -------------------------------------------------------
		else if (command.equals(COMMAND_CHART))
		{
			openInternalChartFrame();
		}
		// -------------------------------------------------------
		// COMMAND_SOLUTIOUNBROWSER
		// -------------------------------------------------------
		else if (command.equals(COMMAND_SOLUTIOUNBROWSER))
		{
			openInternalSolutionBrowserFrame();
		}
		// -------------------------------------------------------
		// COMMAND_START
		// -------------------------------------------------------
		else if (command.equals(COMMAND_START))
		{
			if (GUICache.hasLogisticTask())
			{
				((IGenplannerService) ServiceLocator.getInstance().getService(EServices.Genplanner)).setStatusOfLogisticPlanCalculation(logisticTaskID, ELogisticTaskStatus.Running);
			}

		}
		// -------------------------------------------------------
		// COMMAND_SAVE
		// -------------------------------------------------------
		else if (command.equals(COMMAND_SAVE))
		{
			if (GUICache.hasLogisticTask())
			{
				String path = "";

				// FileChooser to select the configuration file
				JFileChooser jfc = new JFileChooser();
				int state = jfc.showSaveDialog(null);
				if (state == JFileChooser.APPROVE_OPTION)
				{
					try
					{
						path = jfc.getSelectedFile().getPath();

						if (!((IGenplannerService) ServiceLocator.getInstance().getService(EServices.Genplanner)).saveSolution(GUICache.getSolution(), GUICache.getSelectedSolution(), path))
						{
							JOptionPane.showMessageDialog(view, "Error while saving the solution. Further details are in the log file.");
						}

					}
					catch (Exception ex)
					{
						LogWriter.getInstance().logToFile(LogLevel.Error, MODULE_NAME, "Could not save the solution File. " + ex.toString());
						System.err.println(ex.toString());
					}
				}

			}

		}
		// -------------------------------------------------------
		// COMMAND_PAUSE
		// -------------------------------------------------------
		else if (command.equals(COMMAND_PAUSE))
		{
			if (GUICache.hasLogisticTask())
			{
				((IGenplannerService) ServiceLocator.getInstance().getService(EServices.Genplanner)).setStatusOfLogisticPlanCalculation(logisticTaskID, ELogisticTaskStatus.Paused);
			}
		}
		// -------------------------------------------------------
		// COMMAND_DE
		// -------------------------------------------------------
		else if (command.equals(COMMAND_DE))
		{
			LanguageManager.getInstance().setLanguage("de");
			FrontendManager.getInstance().refreshLocalizedText();
		}
		// -------------------------------------------------------
		// COMMAND_EN
		// -------------------------------------------------------
		else if (command.equals(COMMAND_EN))
		{
			LanguageManager.getInstance().setLanguage("en");
			FrontendManager.getInstance().refreshLocalizedText();
		}
		// -------------------------------------------------------
		// COMMAND_EXIT
		// -------------------------------------------------------
		else if (command.equals(COMMAND_EXIT))
		{
			Genplanner.getInstance().addSystemMessage(new ExitApplicationMsg());
		}
	}

	/**
	 * Opens the default frames for application start
	 */
	public void showDefaultInternalFrames()
	{
		// create the internal frames for map / solution browser and chart at
		// startup as default
		openInternal2DMapFrame();
		openInternalChartFrame();
		openInternalSolutionBrowserFrame();
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	/**
	 * opens the JInternalFrame containing the 2D Map View
	 */
	private void openInternal2DMapFrame()
	{
		if (map == null)
		{
			map = new MapView();
			FrontendManager.getInstance().registerNewInternalFrame(map);
		}
		map.setVisible(true);
	}

	/**
	 * opens the external Frame containing the 3D Map View
	 */
	private void openExternal3DViewFrame()
	{
		if (map3D == null)
		{
			map3D = new MapView3D();
			Thread map3DThread = new Thread(map3D);
			map3DThread.start();
			view.refresh3DMenuItems(true);
		}
	}

	/**
	 * closes the external Frame containing the 3D Map View
	 */
	private void closeExternal3DViewFrame()
	{
		if (map3D != null)
		{
			map3D.closeMapView3D();
			map3D = null;
			view.refresh3DMenuItems(false);
		}
	}

	/**
	 * opens the JInternalFrame containing the Chart View
	 */
	private void openInternalChartFrame()
	{
		if (chart == null)
		{
			chart = new LineChartView();
			FrontendManager.getInstance().registerNewInternalFrame(chart);
		}
		chart.setVisible(true);
	}

	/**
	 * opens the JInternalFrame containing the SolutionBrowser View
	 */
	private void openInternalSolutionBrowserFrame()
	{
		if (sBrowser == null)
		{
			sBrowser = new SolutionBrowserView();
			FrontendManager.getInstance().registerNewInternalFrame(sBrowser);
		}
		sBrowser.setVisible(true);
	}
	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------
}
