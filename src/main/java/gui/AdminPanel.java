package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Description:<br>
 * The AdminPanel class is responsible for constructing
 * the panel that contains these administration tools:
 * <li>Ban
 * <li>Unban
 * <li>Kick
 * <li>Silence
 * <p>
 * These tools are displayed on the top-right corner of
 * the GUI, and these tools are clickable buttons that
 * apply actions to a selected guild member. The buttons
 * are arranged in the manner set by the defined 
 * GridBagConstraints.
 * <p>
 * 
 * Date:		August 30, 2021
 * @author 		Harrison Hoggard
 * @version		1.0.0
 * @since		1.0.0
 */
public class AdminPanel extends JPanel {
	
	// Gets rid of missing serialVersionUID warnings
	private static final long serialVersionUID = 1L;

	JButton banButton, unbanButton, kickButton, silenceButton;

	GridBagConstraints gbc;

	/**
	 * Description:<br>
	 * The constructor of the class. This method is responsible
	 * for defining the buttons, setting the GridBagLayout and 
	 * its constraints, and creating the titled border for the
	 * panel.
	 * 
	 * @since	1.0.0
	 */
	public AdminPanel() {
		
		banButton = new JButton("Ban");
		unbanButton = new JButton("Unban");
		kickButton = new JButton("Kick");
		silenceButton = new JButton("Silence");

		/* 
		 * Setting dimensions of each button ensures that the
		 * GUI layout is properly displayed
		 */
		banButton.setPreferredSize(new Dimension(90, 36));
		unbanButton.setPreferredSize(new Dimension(90, 36));
		kickButton.setPreferredSize(new Dimension(90, 36));
		silenceButton.setPreferredSize(new Dimension(90, 36));

		// Tool tips displayed when a user hovers their cursor over a button
		banButton.setToolTipText("Bans the selected user from the guild");
		unbanButton.setToolTipText("Unbans the selected user from the guild");
		kickButton.setToolTipText("Kicks the selected user from the guild");
		silenceButton.setToolTipText("Silences the selected user from speaking/chatting");

		setPreferredSize(new Dimension(546, 513));
		setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;

		// Ban button constraints
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(banButton, gbc);

		// Unban button constraints
		gbc.gridy = 1;
		add(unbanButton, gbc);

		// Kick button constraints
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(kickButton, gbc);

		// Silence button constraints
		gbc.gridy = 1;
		add(silenceButton, gbc);

		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Admin Tools"));
	}
}
