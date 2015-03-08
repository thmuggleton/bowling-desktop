package thmuggleton.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import thmuggleton.Command;
import thmuggleton.model.Match;
import thmuggleton.model.impl.MatchImpl;
import thmuggleton.view.View;
import thmuggleton.view.impl.MainWindow;

/**
 * Initialises the application and is then dereferenced; this controller facilitates
 * the view in attaching the correct listeners to the relevant GUI components, as well
 * as setting the view for all of the controllers. 
 * 
 * @author Thomas Muggleton
 */
public class InitialisationController {
	
	/**
	 * Constructor
	 */
	public InitialisationController() {
		
		// Create model
		Match model = new MatchImpl();
		
		// Create controllers
		Map<String,Controller> commandToControllerMap = this.createControllers(model);
		
		// Create View
		View view = new MainWindow(Collections.unmodifiableMap(commandToControllerMap), model);
		
		// Add View to all controllers
		for (Controller controller : commandToControllerMap.values()) {
			controller.setView(view);
		}
	}

	/**
	 * Handles initialisation of all controllers, mapping these to the relevant commands.
	 * A Map between commands and their handlers is then returned.
	 * 
	 * @param model to set in controllers.
	 * @return Map between commands and the controller intended to handle each command.
	 */
	private Map<String,Controller> createControllers(Match model) {
		
		Map<String,Controller> commandToControllerMap = new HashMap<String,Controller>();
		
		// Create system controller
		SystemController systemController = new SystemController(model);
		commandToControllerMap.put(Command.ABOUT, systemController);
		commandToControllerMap.put(Command.EXIT, systemController);
		
		// Create match controller
		MatchController matchController = new MatchController(model);
		commandToControllerMap.put(Command.NEW_MATCH, matchController);
		commandToControllerMap.put(Command.ADD_PLAYER, matchController);
		commandToControllerMap.put(Command.ADD_SCORE, matchController);
		model.addChangeListener(matchController);
		
		// Create IO controller
		IOController ioController = new IOController();
		commandToControllerMap.put(Command.EXPORT_IMAGE, ioController);
		
		return commandToControllerMap;
	}
}
