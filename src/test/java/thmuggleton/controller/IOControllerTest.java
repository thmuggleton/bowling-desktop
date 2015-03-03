package thmuggleton.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.Tested;
import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

import thmuggleton.Constants;
import thmuggleton.view.View;

/**
 * Tests for the IOController controller class.
 * <p>
 * Adopts BDD naming conventions suggested by
 * <a href="http://dannorth.net/introducing-bdd/">Dan North</a>.
 * <p>
 * Takes a 'mockist' TDD approach to focus on behavioural expectations 
 * since this is well-suited to testing a controller. (See
 * <a href="http://martinfowler.com/articles/mocksArentStubs.html">Mocks Aren't Stubs</a>).
 * <p>
 * However, to avoid coupling the test too closely to the implementation,
 * looser expectations have been set where these do not affect the central
 * purpose of the test.
 * 
 * @author Thomas Muggleton
 */
@RunWith(JMockit.class) 
public class IOControllerTest {

	@Tested private IOController controller;
	@Injectable private View view;
	
	/**
	 * Tests that the controller presents the user with a dialog box for file
	 * selection when exporting an image of the scoreboard and then takes no
	 * further action if the user presses 'Cancel'.
	 */
	@Test
	public void shouldTakeNoFurtherActionIfUserPressesCancelWhenExportingAScoreboardImage(
			@Mocked final JFileChooser mockedFileChooser) {
		
		/* **************
		 *  Record phase
		 * **************/
		new StrictExpectations() {{
			
			// Setup file chooser
			new JFileChooser();
			mockedFileChooser.setCurrentDirectory((File) any);
			mockedFileChooser.setFileFilter((FileNameExtensionFilter) any);
			
			// Show file selection dialog
			mockedFileChooser.showSaveDialog(view.getWindow());
			result = JFileChooser.CANCEL_OPTION;			
		}};
		
		/* **************
		 *  Replay phase
		 * **************/
		controller.exportScoreboardImage();
	}
	
	/**
	 * Tests that the controller presents the user with a dialog box for file
	 * selection when exporting an image of the scoreboard and then writes
	 * the image to this file after the user inputs a filename with no extension.
	 * 
	 * @throws IOException 
	 */
	@Test
	public void shouldCorrectlyExportScoreboardImageToPathWithNoExtensionSelectedByUser(
			@Mocked final JFileChooser mockedFileChooser,
			@Mocked final BufferedImage mockedScoreboardImage,
			@Mocked final File mockedSelectedFile,
			@Mocked final File mockedOuputFile,
			@Mocked final ImageIO mockedImageWriter) throws IOException {
		
		/* **************
		 *  Record phase
		 * **************/
		final String testInputFilePath = "/filepath/without/extension";
		final String expectedOutputFilePath = testInputFilePath + "." + Constants.IMAGE_EXPORT_FORMAT;
		
		new StrictExpectations() {{
			
			// Setup file chooser
			new JFileChooser();
			new File(anyString);
			mockedFileChooser.setCurrentDirectory((File) any);
			mockedFileChooser.setFileFilter((FileNameExtensionFilter) any);
			
			// Show file selection dialog
			mockedFileChooser.showSaveDialog(view.getWindow());
			result = JFileChooser.APPROVE_OPTION;
			
			// Retrieve scoreboard image from View
			view.getScoreboardImage();
			result = mockedScoreboardImage;
			
			// Retrieve filepath selected by user
			mockedFileChooser.getSelectedFile();
			result = mockedSelectedFile;
			mockedSelectedFile.getAbsolutePath();
			result = testInputFilePath;
			
			// Create new output file after formatting input path
			new File(expectedOutputFilePath);
			
			// Write file
			ImageIO.write(mockedScoreboardImage, anyString, (File) any);
		}};
		
		/* **************
		 *  Replay phase
		 * **************/
		controller.exportScoreboardImage();
	}
	
	/**
	 * Tests that the controller presents the user with a dialog box for file
	 * selection when exporting an image of the scoreboard and then writes the
	 * image to this file after the user inputs a filename with an invalid
	 * extension.
	 * 
	 * @throws IOException
	 */
	@Test
	public void shouldCorrectlyExportScoreboardImageToPathWithInvalidExtension(
			@Mocked final JFileChooser mockedFileChooser,
			@Mocked final BufferedImage mockedScoreboardImage,
			@Mocked final File mockedSelectedFile,
			@Mocked final File mockedOuputFile,
			@Mocked final ImageIO mockedImageWriter) throws IOException {
		
		/* **************
		 *  Record phase
		 * **************/
		final String testInputFilePath = "/filepath/with/extension.invalidExtension";
		final String expectedOutputFilePath = "/filepath/with/extension." + Constants.IMAGE_EXPORT_FORMAT;
		
		new StrictExpectations() {{
			
			// Setup file chooser
			new JFileChooser();
			new File(anyString);
			mockedFileChooser.setCurrentDirectory((File) any);
			mockedFileChooser.setFileFilter((FileNameExtensionFilter) any);
			
			// Show file selection dialog
			mockedFileChooser.showSaveDialog(view.getWindow());
			result = JFileChooser.APPROVE_OPTION;
			
			// Retrieve scoreboard image from View
			view.getScoreboardImage();
			result = mockedScoreboardImage;
			
			// Retrieve filepath selected by user
			mockedFileChooser.getSelectedFile();
			result = mockedSelectedFile;
			mockedSelectedFile.getAbsolutePath();
			result = testInputFilePath;
			
			// Create new output file after formatting input path
			new File(expectedOutputFilePath);
			
			// Write file
			ImageIO.write(mockedScoreboardImage, anyString, (File) any);
		}};
		
		/* **************
		 *  Replay phase
		 * **************/
		controller.exportScoreboardImage();
	}
}
