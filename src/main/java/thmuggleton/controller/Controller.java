package thmuggleton.controller;

import java.awt.event.ActionListener;

import thmuggleton.view.View;

/**
 * Interface so that the relevant view can be set in the controller.
 * 
 * @author Thomas Muggleton
 */
public interface Controller extends ActionListener {
	
	/**
	 * Sets the View in the controller.
	 * 
	 * @param view the View to set.
	 */
	public void setView(View view);

}
