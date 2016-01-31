package genplanner.gui.control;

import genplanner.gui.view.MapView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller class (Listener) for the MapView InternalFrame
 * 
 * @author Lars George
 * @version 1.0
 * 
 */
public class MapViewListener implements ActionListener
{
	@SuppressWarnings("unused")
	private MapView	view	= null;

	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	/**
	 * The default constructor with the view the listener is hooked to
	 * 
	 * @param view
	 *            the view object this controller belongs to
	 */
	public MapViewListener(MapView view)
	{
		setView(view);
	}

	// ------------------------------------------------------------------------
	// Public Methods
	// ------------------------------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent e)
	{
	}

	// ------------------------------------------------------------------------
	// Private Methods
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// Get- / Set- Methods
	// ------------------------------------------------------------------------
	public void setView(MapView view)
	{
		this.view = view;
	}
}
