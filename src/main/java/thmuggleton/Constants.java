package thmuggleton;

/**
 * Interface defining constants used throughout the application.
 * 
 * @author Thomas Muggleton
 */
public interface Constants {

	/* **************************
	 *  10-pin bowling constants
	 * **************************/
	public static final int TOTAL_PINS = 10;
	public static final int MAX_BONUS_POINTS = 20;
	public static final int NUMBER_OF_FRAMES = 10;
	public static final int MAX_NUMBER_OF_PLAYERS = 6;
	
	/* *******************************
	 *  Export image format constants
	 * *******************************/
	public static final String IMAGE_FILTER_DESCRIPTION = "PNG files";
	public static final String IMAGE_EXPORT_FORMAT = "png";
	
	/**
	 * Specifies the path within the src/main/resources/ directory for the 
	 * image to display when a player has won the bowling match.
	 * <p>
	 * The image was retrieved from 
	 * <a href="http://www.wpclipart.com/recreation/trophy/gold_trophy_graphic.png.html">wpclipart</a>
	 * which states that the image is in the public domain.
	 */
	public static final String PATH_TO_TROPHY_IMAGE = "images/gold_trophy_graphic.png";
	
	/**
	 * Contains HTML for what is displayed in the 'About' dialog. 
	 */
	public static final String ABOUT_DIALOG_MESSAGE =
			  "<html>"
			+ "	<head></head>"
			+ "	<body>"
			+ "		<h2>10-pin Bowling Scorer</h2>"
			+ "		<p>&copy 2015 Thomas Muggleton.</p>"
			+ "		<p>"
			+ "		<p>Published under the GNU General Public License, version 3."
			+ "		<p>See <a href='http://www.gnu.org/copyleft/gpl.html'>http://www.gnu.org/copyleft/gpl.html</a> for details."
			+ "		<p>"
			+ "		<p>For source code and documentation, visit:"
			+ "		<p><a href='https://github.com/thmuggleton/bowling-desktop'>https://github.com/thmuggleton/bowling-desktop</a>"
			+ "		<p>"
			+ "		<p>Trophy image was taken from"
			+ "		<p><a href='http://www.wpclipart.com/recreation/trophy/gold_trophy_graphic.png.html'>"
			+ "		http://www.wpclipart.com/recreation/trophy/gold_trophy_graphic.png.html</a>"
			+ "		<p>which states that the image is in the public domain."
			+ "	</body>"
			+ "</html>";
}
