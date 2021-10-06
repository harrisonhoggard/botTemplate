package gui;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;


import mainBot.Bot;
import net.dv8tion.jda.api.entities.Guild;

/**
 * Description:<br>
 * The GuildPanel class is responsible for constructing 
 * the guild panel, which contains the list of guilds 
 * that the bot is currently present in.
 * <p>
 * 
 * Date:		August 30, 2021
 * @author 		Harrison Hoggard
 * @version		1.0.0
 * @since		1.0.0
 */
public class GuildPanel extends JPanel{
	
	// Gets rid of missing serialVersionUID warnings
	private static final long serialVersionUID = 1L;
	
	public JList<String> guildList;
	public JScrollPane scrollPane;

	int guildListSize;
	int cellSize;

	/**
	 * Description:<br>
	 * This constructor is responsible for constructing
	 * the guild panel, which is located in the top left
	 * corner of the GUI. This panel contains the list
	 * of guilds that the bot is currently in.
	 * 
	 * @since	1.0.0
	 */
	public GuildPanel() {
    
		ArrayList<String> list = new ArrayList<>();
		
		List<Guild> guilds = Bot.jda.getGuilds();
		
		for (int i = 0; i < guilds.size(); i++)
		{
			list.add((i + 1) + ") " + guilds.get(i).getName());
		}

		guildList = new JList<>(list.toArray(new String[list.size()]));
		scrollPane = new JScrollPane(guildList);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		guildList.setSelectedIndex(0);
		cellSize = 8;

		guildListSize = guildList.getModel().getSize();

		setPreferredSize(new Dimension(186, 280));
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);

		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Guilds"));

	}
	
	/**
	 * Description:<br>
	 * Responsible for removing and re-adding the 
	 * panels with the new information. Even though there
	 * is a lot more than there probably needs to be in 
	 * here, it ensures that it'll work without error the
	 * most consistently.
	 * 
	 * @since	1.0.0
	 */
	public void refreshPane() {
		
		remove(scrollPane);
		guildList.removeAll();
		scrollPane.removeAll();
	    
		ArrayList<String> list = new ArrayList<>();
		
		List<Guild> guilds = Bot.jda.getGuilds();
		
		for (int i = 0; i < guilds.size(); i++)
		{
			list.add((i + 1) + ") " + guilds.get(i).getName());
		}

		guildList = new JList<>(list.toArray(new String[list.size()]));
		scrollPane = new JScrollPane(guildList);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		guildList.setSelectedIndex(0);
		cellSize = 8;

		guildListSize = guildList.getModel().getSize();
		
		add(scrollPane, BorderLayout.CENTER);
	}
}
