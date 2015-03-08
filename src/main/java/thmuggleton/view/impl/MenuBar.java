package thmuggleton.view.impl;

import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import thmuggleton.Command;

/**
 * Defines the menu bar used in the main GUI.
 * 
 * @author Thomas Muggleton
 */
public class MenuBar extends JMenuBar {
	
	/**
	 * Auto-generated serial version ID.
	 */
	private static final long serialVersionUID = 7051668902320928807L;
	
	// Fields
	private JMenu fileMenu;
	private JMenuItem newMatch, exportImage, exit;
	
	/**
	 * Constructor to create and add all menus.
	 * 
	 * @param controllers Map between command Strings and their handlers.
	 */
	public MenuBar(Map<String, ? extends ActionListener> controllers) {
		
		// Add all menus
		this.add(createFileMenu(controllers));
		this.add(createHelpMenu(controllers));
	}

	/**
	 * Creates and returns file menu.
	 * 
	 * @param controllers Map between command Strings and their handlers.
	 * @return
	 */
	private JMenu createFileMenu(Map<String, ? extends ActionListener> controllers) {
		
		// Instantiate file menu
		fileMenu = new JMenu("File");
		
		// Create new menu item
		newMatch = new JMenuItem(Command.NEW_MATCH);
		newMatch.setActionCommand(Command.NEW_MATCH);
		newMatch.addActionListener(controllers.get(Command.NEW_MATCH));
		fileMenu.add(newMatch);
		
		// Create export image menu item
		exportImage = new JMenuItem(Command.EXPORT_IMAGE);
		exportImage.setActionCommand(Command.EXPORT_IMAGE);
		exportImage.addActionListener(controllers.get(Command.EXPORT_IMAGE));
		fileMenu.add(exportImage);
		
		// Create exit menu item
		exit = new JMenuItem(Command.EXIT);
		exit.setActionCommand(Command.EXIT);
		exit.addActionListener(controllers.get(Command.EXIT));
		fileMenu.add(exit);
		
		return fileMenu;
	}
	
	/**
	 * Creates and returns help menu.
	 * 
	 * @param controllers Map between command Strings and their handlers.
	 * @return
	 */
	private JMenu createHelpMenu(Map<String, ? extends ActionListener> controllers) {
		
		// Instantiate help menu
		JMenu helpMenu = new JMenu("Help");
		
		// Create new menu item
		JMenuItem about = new JMenuItem(Command.ABOUT);
		about.setActionCommand(Command.ABOUT);
		about.addActionListener(controllers.get(Command.ABOUT));
		helpMenu.add(about);
		
		return helpMenu;
	}
}
