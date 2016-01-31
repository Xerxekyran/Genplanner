package genplanner.gui;

import genplanner.gui.view.IFrame;
import genplanner.gui.view.MainFrame;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import util.LogWriter;
import util.LogWriter.LogLevel;

/**
 * A manager class for the front end. This class controls what kind of GUI
 * elements are used.
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class FrontendManager implements Runnable, InternalFrameListener
{
	private static final String		MODULE_NAME		= "GUI-Manager";
	private static FrontendManager	instance		= null;

	private MainFrame				mainFrame		= null;
	private ArrayList<IFrame>		internalFrames	= new ArrayList<IFrame>();

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * Private constructor for singleton pattern
	 */
	private FrontendManager()
	{
		try
		{
			// try to set the look and feel of the current operating system
			// (vista/xp/mac ...)
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			LogWriter.getInstance().logToFile(LogLevel.Error, MODULE_NAME, "Error setting the system look and feel: " + e.toString());
		}

		// let the creation process take place in the "Event-Dispatch-Thread"
		SwingUtilities.invokeLater(this);
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------
	@Override
	public void run()
	{
		// create the main window
		mainFrame = new MainFrame();
		mainFrame.setVisible(true);
		mainFrame.showDefaultInternalFrames();
	}

	/**
	 * cleans all used data
	 */
	public void shutdown()
	{
		mainFrame.dispose();
	}

	/**
	 * Tells all childs to renew their localized text values
	 */
	public void refreshLocalizedText()
	{
		mainFrame.setLocalizedText();

		for (IFrame frame : internalFrames)
		{
			frame.setLocalizedText();
		}
	}

	/**
	 * tries to invoce the repaint method on all managed frames
	 */
	public void repaintAll()
	{
		for (IFrame frame : internalFrames)
		{
			try
			{
				((JInternalFrame) frame).repaint();
			}
			catch (Exception e)
			{
				LogWriter.getInstance().logToFile(LogLevel.Warn, MODULE_NAME, "Was not able to cast internal Frame correctly:" + e.toString());
			}
		}
	}

	/**
	 * 
	 * @param internalFrame
	 */
	public void registerNewInternalFrame(IFrame internalFrame)
	{
		LogWriter.getInstance().logToFile(LogLevel.Debug, MODULE_NAME, "Registering new internal Frame: " + internalFrame.getClass());
		internalFrames.add(internalFrame);
		mainFrame.getContentPane().add((Component) internalFrame);
	}

	/**
	 * 
	 * @return the current MainFrame instance
	 */
	public MainFrame getMainFrame()
	{
		return mainFrame;
	}

	@Override
	public void internalFrameActivated(InternalFrameEvent e)
	{
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent e)
	{
		if (mainFrame != null && e.getInternalFrame() != null)
		{
			LogWriter.getInstance().logToFile(LogLevel.Debug, MODULE_NAME, "Deleting internal Frame: " + e.getInternalFrame().getClass());
			internalFrames.remove(e.getInternalFrame());
			mainFrame.internalFrameClosed(e.getInternalFrame());
		}
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent e)
	{
	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent e)
	{
	}

	@Override
	public void internalFrameDeiconified(InternalFrameEvent e)
	{
	}

	@Override
	public void internalFrameIconified(InternalFrameEvent e)
	{
	}

	@Override
	public void internalFrameOpened(InternalFrameEvent e)
	{
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------
	/**
	 * Getter for the singleton instance
	 */
	public static FrontendManager getInstance()
	{
		if (instance == null)
			instance = new FrontendManager();

		return instance;
	}
}
