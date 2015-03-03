package thmuggleton.view.impl;

import java.awt.event.ActionListener;

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
	 * @param controller
	 */
	public MenuBar(ActionListener controller) {
		
		// Add all menus
		this.add(createFileMenu(controller));
		this.add(createHelpMenu(controller));
	}

	/**
	 * Creates and returns file menu.
	 * 
	 * @param controller
	 * @return
	 */
	private JMenu createFileMenu(ActionListener controller) {
		
		// Instantiate file menu
		fileMenu = new JMenu("File");
		
		// Create new menu item
		newMatch = new JMenuItem(Command.NEW_MATCH);
		newMatch.setActionCommand(Command.NEW_MATCH);
		newMatch.addActionListener(controller);
		fileMenu.add(newMatch);
		
		// Create export image menu item
		exportImage = new JMenuItem(Command.EXPORT_IMAGE);
		exportImage.setActionCommand(Command.EXPORT_IMAGE);
		exportImage.addActionListener(controller);
		fileMenu.add(exportImage);
		
		// Create exit menu item
		exit = new JMenuItem(Command.EXIT);
		exit.setActionCommand(Command.EXIT);
		exit.addActionListener(controller);
		fileMenu.add(exit);
		
		return fileMenu;
	}
	
	/**
	 * Creates and returns help menu.
	 * 
	 * @param controller
	 * @return
	 */
	private JMenu createHelpMenu(ActionListener controller) {
		
		// Instantiate help menu
		JMenu helpMenu = new JMenu("Help");
		
		// Create new menu item
		JMenuItem about = new JMenuItem(Command.ABOUT);
		about.setActionCommand(Command.ABOUT);
		about.addActionListener(controller);
		helpMenu.add(about);
		
		return helpMenu;
	}
}
