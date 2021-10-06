package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import mainBot.Bot;
import net.dv8tion.jda.api.entities.Guild.Ban;
import net.dv8tion.jda.api.entities.Member;

/**
 * Description:<br>
 * The MemberPanel class is responsible for generating
 * the list of members associated for each guild. It is
 * also responsible for generating the tabbed panels that
 * display both the banned and unbanned members of each
 * guild.
 * <p>
 * 
 * Date:		August 30, 2021
 * @author 		Harrison Hoggard
 * @version		1.0.0
 * @since		1.0.0
 */
public class MemberPanel extends JPanel {
	
	// Gets rid of missing serialVersionUID warnings
	private static final long serialVersionUID = 1L;
	
	public ArrayList<JList<String>> memberListArray;
	public ArrayList<JList<String>> bannedMemberListArray;
	public JScrollPane scrollPane, bannedScrollPane;
	public JTabbedPane tabbedPane;
	public int guildListSize;

	/**
	 * Description:<br>
	 * The method responsible for initializing the members
	 * lists for each guild, and displaying them in a tabbed 
	 * pane in the top-center panel in the GUI. 
	 * Each guild has two lists associated:
	 * <li>present members
	 * <li>banned members
	 * 
	 * @param guildListSize		the number of guilds the
	 * 							bot is currently present
	 * 							in
	 * @since					1.0.0
	 */
	public MemberPanel(int guildListSize) {
		
		this.guildListSize = guildListSize;
		
		// Used for the members of the guild
		ArrayList<String> list = new ArrayList<String>();
		List<Member> members;
		memberListArray = new ArrayList<JList<String>>();

		for (int i = 0; i < guildListSize; i++) {
			
			members = Bot.jda.getGuilds().get(i).getMembers();
			
			for (int j = 0; j < members.size(); j++)
			{
				list.add((j+1) + ") " + members.get(j).getEffectiveName());
			}

			memberListArray.add(new JList<String>(list.toArray(new String[list.size()])));
			list.clear();
		}

		// Used to store the banned members of a guild
		ArrayList<String> list2 = new ArrayList<String>();
		List<Ban> banList;
		bannedMemberListArray = new ArrayList<JList<String>>();
		
		for (int j = 0; j < guildListSize; j++)
		{
			banList = Bot.jda.getGuilds().get(j).retrieveBanList().complete();
			
			if (!banList.isEmpty())
			{
				for (int k = 0; k < banList.size(); k++)
				{
					
					list2.add((k+1) + ") " + banList.get(k).getUser().getAsMention() + " " + banList.get(j).getUser().getName());
				}
			}
			
			bannedMemberListArray.add(new JList<String>(list2.toArray(new String[list2.size()])));
			list2.clear();
		}
		
		scrollPane = new JScrollPane(memberListArray.get(0));
		bannedScrollPane = new JScrollPane(bannedMemberListArray.get(0));
		tabbedPane = new JTabbedPane();
		
		tabbedPane.addTab("Members", scrollPane);
		tabbedPane.addTab("Banned Members", bannedScrollPane);

		setPreferredSize(new Dimension(202, 280));
		setLayout(new BorderLayout());
		add(tabbedPane, BorderLayout.CENTER);

		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Members"));
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
		
		this.guildListSize = GuiMain.frameObject.getGuildListSize();
		
		remove(tabbedPane);
		scrollPane.removeAll();
		bannedScrollPane.removeAll();
		tabbedPane.removeAll();
		memberListArray.clear();
		bannedMemberListArray.clear();
		
		// Used for the members of the guild
		ArrayList<String> list = new ArrayList<String>();
		List<Member> members;
		memberListArray = new ArrayList<JList<String>>();

		for (int i = 0; i < guildListSize; i++) {
			
			members = Bot.jda.getGuilds().get(i).getMembers();
			
			for (int j = 0; j < members.size(); j++)
			{
				list.add((j+1) + ") " + members.get(j).getEffectiveName());
			}

			memberListArray.add(new JList<String>(list.toArray(new String[list.size()])));
			list.clear();
		}

		// Used to store the banned members of a guild
		ArrayList<String> list2 = new ArrayList<String>();
		List<Ban> banList;
		bannedMemberListArray = new ArrayList<JList<String>>();
		
		for (int j = 0; j < guildListSize; j++)
		{
			banList = Bot.jda.getGuilds().get(j).retrieveBanList().complete();
			
			if (!banList.isEmpty())
			{
				for (int k = 0; k < banList.size(); k++)
				{
					
					list2.add((k+1) + ") " + banList.get(k).getUser().getAsMention() + " " + banList.get(j).getUser().getName());
				}
			}
			
			bannedMemberListArray.add(new JList<String>(list2.toArray(new String[list2.size()])));
			list2.clear();
		}
		
		scrollPane = new JScrollPane(memberListArray.get(0));
		bannedScrollPane = new JScrollPane(bannedMemberListArray.get(0));
		tabbedPane = new JTabbedPane();
		
		tabbedPane.addTab("Members", scrollPane);
		tabbedPane.addTab("Banned Members", bannedScrollPane);
		
		add(tabbedPane, BorderLayout.CENTER);
	}
}
