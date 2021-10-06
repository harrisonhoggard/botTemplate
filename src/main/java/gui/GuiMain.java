package gui;

/**
 * Description:<br>
 * The "main" class responsible for initializing the GUI.
 * <p>
 * 
 * Date:		August 30, 2021
 * @author 		Harrison Hoggard
 * @version		1.0.0
 * @since		1.0.0
 */
public class GuiMain {

	// Static GUI Object
	public static GuiFrame frameObject;
	
	public static void init() {
		/*
		 *  This allows the GUI to be refreshed from 
		 *  anywhere in the program that would affect
		 *  the data displayed.
		 */
		frameObject = new GuiFrame();
	}
}
