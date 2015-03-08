package thmuggleton.controller;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import thmuggleton.Command;
import thmuggleton.Constants;
import thmuggleton.view.View;

/**
 * Controller to handle reading from and writing to the file system.
 * 
 * @author Thomas Muggleton
 */
public class IOController implements Controller {
	
	// Fields
	private View view;

	/**
	 * Sets the given view for this controller.
	 */
	@Override
	public void setView(View view) {
		this.view = view;
	}
	
	/**
	 * Handler method for action events.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String command = e.getActionCommand();
		
		switch (command) {
		
		case (Command.EXPORT_IMAGE) :
			this.exportScoreboardImage();
			break;
		}
	}
	
	/**
	 * Exports the currently displayed scoreboard as an image file.
	 */
	private void exportScoreboardImage() {
		
		// Create file chooser dialog for image formats 
		/// to initialise in working directory
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		chooser.setFileFilter(new FileNameExtensionFilter(Constants.IMAGE_FILTER_DESCRIPTION,
				Constants.IMAGE_EXPORT_FORMAT));
		
		// Retrieve file path
		int option = chooser.showSaveDialog(view.getWindow());
		
		if (option == JFileChooser.APPROVE_OPTION) {
			
			// Create image and target file object
			BufferedImage scoreboardImage = view.getScoreboardImage();
			File outputFile = this.createFile(
					chooser.getSelectedFile().getAbsolutePath(),
					Constants.IMAGE_EXPORT_FORMAT);
			
			// Try to write image to file
			try {
				ImageIO.write(scoreboardImage, Constants.IMAGE_EXPORT_FORMAT, outputFile);
			}
			catch (IOException e) {
				
				// Feedback error to user
				JOptionPane.showMessageDialog(view.getWindow(),
								String.format("The following error occurred while writing the image file:%n%s",
										e.getMessage()),
						"Failed to write image", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Returns a file having taken an absolute path and ensuring that
	 * the desired file extension is appended.
	 * 
	 * @param absolutePath a String containing the absolute path for
	 * the target file.
	 * @param desiredExtension a String containing the intended extension
	 * for the target file.
	 * @return File object with the given absolute path and intended
	 * extension.
	 */
	private File createFile(String absolutePath, String desiredExtension) {
		
		// Declare desired filepath
		StringBuilder desiredFilepath = new StringBuilder();
		
		// Remove current file extension if present
		int dotIndex =  absolutePath.lastIndexOf(".");
		
		if (dotIndex > 0)
			desiredFilepath.append(absolutePath.substring(0, dotIndex));
		else
			desiredFilepath.append(absolutePath);
		
		// Append desired extension
		desiredFilepath.append("." + desiredExtension);
		
		return new File(desiredFilepath.toString());
	}
}
