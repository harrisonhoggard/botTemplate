package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Description:<br>
 * The DevPanel class is responsible for constructing 
 * the text box that displays any events or commands 
 * executed from any guild, or any dev message that would
 * appear in the Java terminal. The text box cannot be 
 * typed into, as it is only used to display information.
 * The panel appears in the bottom half of the GUI window.
 * <p>
 * 
 * Date:		August 30, 2021
 * @author 		Harrison Hoggard
 * @version		1.0.0
 * @since		1.0.0
 */
public class DevPanel extends JPanel{
	
	// Gets rid of missing serialVersionUID warnings
	private static final long serialVersionUID = 1L;
	
	public JTextArea textArea;
	public JScrollPane scrollPane;

	/**
	 * Description:<br>
	 * Constructor for the class. The method is responsible
	 * for defining the text area that displays information,
	 * as well as the scroll pane that allows the text
	 * area to be scrollable. 
	 * 
	 * @since	1.0.0
	 */
	public DevPanel() {
		textArea = new JTextArea(5, 20);
		textArea.setBackground(Color.WHITE);
		textArea.setEditable(false);
		scrollPane = new JScrollPane(textArea);

		setPreferredSize(new Dimension(388, 383));
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Dev"));
		textArea.setText("DEV CONSOLE:\n");
	}
}
