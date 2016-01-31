package genplanner.gui.view;

import genplanner.gui.control.MainFrameListener;
import genplanner.language.LanguageManager;
import genplanner.language.messages.GenplannerMessageBundles;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowListener;
import java.util.ResourceBundle;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * The MainFrame is the main GUI element. It has a menu and a DesktopPane to
 * display the other internal frames.
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class MainFrame extends JFrame implements IFrame
{

	private static final long	serialVersionUID	= 1L;
	private final int			width				= 900;
	private final int			height				= 800;

	private JMenuBar			mba_menuBar			= null;
	private JPanel				jContentPane		= null;
	private JDesktopPane		jDesktopPane		= null;

	private JMenu				menu_file			= null;
	private JMenuItem			mi_exit				= null;
	private JMenuItem			mi_new				= null;
	private JMenuItem			mi_save				= null;
	private JMenuItem			mi_start			= null;
	private JMenuItem			mi_pause			= null;

	private JMenu				menu_windows		= null;
	private JMenuItem			mi_map				= null;
	private JMenu				menu_3DMap			= null;
	private JMenuItem			mi_open3DMap		= null;
	private JMenuItem			mi_close3DMap		= null;
	private JMenuItem			mi_chart			= null;
	private JMenuItem			mi_sBrowser			= null;

	private JMenu				menu_help			= null;
	private JMenuItem			mi_about			= null;
	private JMenuItem			mi_help				= null;
	private JMenuItem			mi_logfile			= null;

	private JMenu				menu_language		= null;
	private JMenuItem			mi_de				= null;
	private JMenuItem			mi_en				= null;

	MainFrameListener			listener			= null;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * 
	 */
	public MainFrame()
	{
		super();

		// init the components
		init();

		// init the listeners
		listener = new MainFrameListener(this);
		setActionListener(listener);
		setWindowListener(listener);
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------
	/**
	 * Opens the default frames for application start
	 */
	public void showDefaultInternalFrames()
	{
		listener.showDefaultInternalFrames();
	}

	@Override
	public void setLocalizedText()
	{
		// get localized text
		ResourceBundle text = LanguageManager.getInstance().getMessageBundle(GenplannerMessageBundles.MAIN_FRAME_MESSAGES);

		// title
		setTitle(text.getString("title"));

		// Menu
		menu_file.setText(text.getString("file"));
		mi_exit.setText(text.getString("exit"));
		mi_exit.setToolTipText(text.getString("exit_tooltip"));
		mi_new.setText(text.getString("new"));
		mi_save.setText(text.getString("save"));
		mi_start.setText(text.getString("start"));
		mi_pause.setText(text.getString("pause"));

		menu_windows.setText(text.getString("windows"));
		mi_map.setText(text.getString("map"));
		menu_3DMap.setText(text.getString("map3D"));
		mi_open3DMap.setText(text.getString("open3DMap"));
		mi_close3DMap.setText(text.getString("close3DMap"));
		mi_chart.setText(text.getString("chart"));
		mi_sBrowser.setText(text.getString("solutionBrowser"));

		menu_help.setText(text.getString("help"));
		mi_about.setText(text.getString("about"));
		mi_help.setText(text.getString("help"));
		mi_logfile.setText(text.getString("logfile"));

		menu_language.setText(text.getString("language"));
		mi_de.setText(text.getString("de"));
		mi_en.setText(text.getString("en"));
	}

	/**
	 * 
	 * @param al
	 */
	public void setActionListener(ActionListener al)
	{
		mi_exit.addActionListener(al);
		mi_about.addActionListener(al);
		mi_de.addActionListener(al);
		mi_en.addActionListener(al);
		mi_new.addActionListener(al);
		mi_start.addActionListener(al);
		mi_map.addActionListener(al);
		mi_open3DMap.addActionListener(al);
		mi_close3DMap.addActionListener(al);
		mi_chart.addActionListener(al);
		mi_sBrowser.addActionListener(al);
		mi_pause.addActionListener(al);
		mi_save.addActionListener(al);
		mi_help.addActionListener(al);
		mi_logfile.addActionListener(al);
	}

	/**
	 * redirects this request to the listener class of the main frame.
	 * 
	 * @param internalFrame
	 *            the internal frame that was closed
	 */
	public void internalFrameClosed(JInternalFrame internalFrame)
	{
		this.listener.internalFrameClosed(internalFrame);
	}

	@Override
	public Container getContentPane()
	{
		return this.jDesktopPane;
	}

	/**
	 * 
	 * @param wl
	 */
	public void setWindowListener(WindowListener wl)
	{
		addWindowListener(wl);
	}

	/**
	 * Sets the 3D Menu items enabled / disabled
	 * 
	 * @param mapIsVisible
	 *            is the 3d map currently visible?
	 */
	public void refresh3DMenuItems(boolean mapIsVisible)
	{
		mi_close3DMap.setEnabled(mapIsVisible);
		mi_open3DMap.setEnabled(!mapIsVisible);
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------
	/**
	 * Initializes the visible components
	 */
	private void init()
	{
		// customize the content pane
		setContentPane(getJContentPane());

		// background color
		getContentPane().setBackground(Color.WHITE);

		// size
		setSize(width, height);

		// center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screenSize.width / 2 - width / 2, screenSize.height / 2 - height / 2);

		// init the menu
		initMenuBar();

		// set the text for all components
		setLocalizedText();
	}

	/**
	 * This Method initialiszed the ContentPane
	 * 
	 * @return
	 */
	private JPanel getJContentPane()
	{
		if (jContentPane == null)
		{
			jContentPane = new JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.add(getJDesktopPane(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes the jDesktopPane
	 * 
	 * @return JDesktopPane
	 */
	private JDesktopPane getJDesktopPane()
	{
		if (jDesktopPane == null)
		{
			jDesktopPane = new JDesktopPane();
		}
		return jDesktopPane;
	}

	/**
	 * Create all elements for the menu bar
	 */
	private void initMenuBar()
	{

		mba_menuBar = new JMenuBar();

		// ------
		// file
		// ------
		menu_file = new JMenu();
		mi_new = new JMenuItem();
		mi_new.setActionCommand(MainFrameListener.COMMAND_NEW);
		menu_file.add(mi_new);

		mi_start = new JMenuItem();
		mi_start.setActionCommand(MainFrameListener.COMMAND_START);
		menu_file.add(mi_start);

		mi_save = new JMenuItem();
		mi_save.setActionCommand(MainFrameListener.COMMAND_SAVE);
		menu_file.add(mi_save);

		mi_pause = new JMenuItem();
		mi_pause.setActionCommand(MainFrameListener.COMMAND_PAUSE);
		menu_file.add(mi_pause);

		mi_exit = new JMenuItem();
		mi_exit.setMnemonic(KeyEvent.VK_F);
		mi_exit.setActionCommand(MainFrameListener.COMMAND_EXIT);
		menu_file.add(mi_exit);

		// ------
		// about
		// ------
		menu_help = new JMenu();
		mi_about = new JMenuItem();
		mi_about.setMnemonic(KeyEvent.VK_A);
		mi_about.setActionCommand(MainFrameListener.COMMAND_ABOUT);
		menu_help.add(mi_about);

		mi_help = new JMenuItem();
		mi_help.setMnemonic(KeyEvent.VK_H);
		mi_help.setActionCommand(MainFrameListener.COMMAND_HELP);
		menu_help.add(mi_help);
		
		mi_logfile = new JMenuItem();
		mi_logfile.setMnemonic(KeyEvent.VK_L);
		mi_logfile.setActionCommand(MainFrameListener.COMMAND_LOGFILE);
		menu_help.add(mi_logfile);

		// ------
		// windows
		// ------
		menu_windows = new JMenu();
		mi_map = new JMenuItem();
		mi_map.setMnemonic(KeyEvent.VK_M);
		mi_map.setActionCommand(MainFrameListener.COMMAND_MAP);
		menu_windows.add(mi_map);

		mi_chart = new JMenuItem();
		mi_chart.setMnemonic(KeyEvent.VK_C);
		mi_chart.setActionCommand(MainFrameListener.COMMAND_CHART);
		menu_windows.add(mi_chart);

		mi_sBrowser = new JMenuItem();
		mi_sBrowser.setMnemonic(KeyEvent.VK_B);
		mi_sBrowser.setActionCommand(MainFrameListener.COMMAND_SOLUTIOUNBROWSER);
		menu_windows.add(mi_sBrowser);

		menu_3DMap = new JMenu();
		mi_open3DMap = new JMenuItem();
		mi_open3DMap.setActionCommand(MainFrameListener.COMMAND_OPEN3DMAP);
		menu_3DMap.add(mi_open3DMap);

		mi_close3DMap = new JMenuItem();
		mi_close3DMap.setActionCommand(MainFrameListener.COMMAND_CLOSE3DMAP);
		menu_3DMap.add(mi_close3DMap);
		menu_windows.add(menu_3DMap);

		// ------
		// language
		// ------
		menu_language = new JMenu();

		mi_de = new JMenuItem();
		mi_de.setActionCommand(MainFrameListener.COMMAND_DE);
		menu_language.add(mi_de);

		mi_en = new JMenuItem();
		mi_en.setActionCommand(MainFrameListener.COMMAND_EN);
		menu_language.add(mi_en);

		// ------
		mba_menuBar.add(menu_file);
		mba_menuBar.add(menu_help);
		mba_menuBar.add(menu_language);
		mba_menuBar.add(menu_windows);

		setJMenuBar(mba_menuBar);
	}

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------

}
