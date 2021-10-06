package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import commands.Ban;
import commands.Kick;
import commands.Silence;
import commands.Unban;
import mainBot.Bot;
import mainBot.Config;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * Description:<br>
 * The GuiFrame class is the frame that contains
 * the individual panels used in the GUI. This class
 * is responsible for calling for the construction
 * of each panel, adding those panels the GUI, 
 * defining actions for certain components, as well
 * as updating certain parts of the GUI that cannot
 * be updated anywhere else. The panels displayed 
 * are:
 * <li>Guilds Panel
 * <li>Members Panel
 * <li>Admin Panel
 * <li>Dev Panel
 * <p>
 * 
 * Date: 		August 30, 2021
 * @author 		Harrison Hoggard
 * @version		1.0.0
 * @since		1.0.0
 */
public class GuiFrame extends JFrame implements ActionListener, ListSelectionListener {
	
	// Gets rid of missing serialVersionUID warnings
	private static final long serialVersionUID = 1L;
	
	public GuildPanel guildPanel;
	public MemberPanel memberPanel;
	public AdminPanel adminPanel;
	public DevPanel devPanel;

	GridBagConstraints gbc;

	/**
	 * Description:<br>
	 * Constructor used to build the frame to display
	 * in the GUI. This method is responsible for setting
	 * up the layout to place panels where they need to go, 
	 * calling for each panel's construction, and assigning
	 * actions to some of the components in the panels, such 
	 * as buttons, or selectable lists.
	 * 
	 * @since	1.0.0
	 */
	public GuiFrame() {
		
		setTitle(Config.get("BOT_NAME"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		setLayout(new GridBagLayout());

		guildPanel = new GuildPanel();

		/*
		 * This variable is used when initializing and 
		 * updating the lists displayed in the guilds and members 
		 * panels.
		 */
		int guildListSize = getGuildListSize();

		memberPanel = new MemberPanel(guildListSize);
		adminPanel = new AdminPanel();
		devPanel = new DevPanel();

		gbc = new GridBagConstraints();
		gbc.weightx = 0.8;
		gbc.weighty = 0.6;

		gbc.insets = new Insets(1, 1, 1, 1);

		// Guild panel constraints
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		add(guildPanel, gbc);

		// Member panel constraints
		gbc.gridx = 1;
		add(memberPanel, gbc);

		// Dev panel constraints
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 4;
		add(devPanel, gbc);

		// Admin panel constraints
		gbc.weightx = 0.6;
		gbc.weighty = 0.6;
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 2;
		add(adminPanel, gbc);

		/*
		 *  Locking the size ensures the panels go where they're 
		 *  supposed to be.
		 */
		setSize(950, 700);
		
		// Initializes the action listeners of the panels
		restartListeners();
	}
	
	/**
	 * Description:<br>
	 * Initializes the necessary action listeners of certain
	 * components in the panels.
	 * 
	 * @since	1.0.0
	 */
	public void restartListeners() {

		// Allows actions to be taken by selecting a guild in the list
		guildPanel.guildList.getSelectionModel().addListSelectionListener(this);

		// Allows buttons to have meaning
		adminPanel.banButton.addActionListener(this);
		adminPanel.unbanButton.addActionListener(this);
		adminPanel.kickButton.addActionListener(this);
		adminPanel.silenceButton.addActionListener(this);
		
		
	}

	/**
	 * Description:<br>
	 * Defines the actions to take by pressing the buttons
	 * in the Admin Panel. Each action calls the associated
	 * command class and executes them. Each action derives 
	 * the user from the selectable members list, and applies
	 * the desired action to said user. Afterwards, the dev
	 * message is displayed.
	 * 
	 * @since	1.0.0
	 */
	public void actionPerformed(ActionEvent e) {
		
		int guildIndex = guildPanel.guildList.getSelectedIndex();
		
		Guild guild = Bot.jda.getGuilds().get(guildIndex);
		ArrayList<String> memberList = new ArrayList<>();
		
		// The text channel used to display 
		TextChannel channel = guild.getTextChannelsByName("general", true).get(0);
		
		if (e.getSource() == adminPanel.banButton)
		{
			memberList.add("!u");
			memberList.add("command");

			if (memberPanel.memberListArray.get(guildIndex).getSelectedValue() == null)
			{
				this.devPanel.textArea.append("\nDEV WARNING: Could not ban anyone because you did not select a member");
				return;
			}
			
			String builtUserName = buildUserName(memberPanel.memberListArray.get(guildIndex).getSelectedValue());
			memberList.add("<@!" + guild.getMembersByEffectiveName(builtUserName, true).get(0).getId() + ">");
			
			String [] member = memberList.toArray(new String [memberList.size()]);
			Ban ban = new Ban();
			
			ban.execute(guild, guild.getSelfMember(), channel, member);
			ban.devMessage(ban.getName(), ban.extraDetails(), guild, Config.get("BOT_NAME"));
			
		}
		
		else if (e.getSource() == adminPanel.unbanButton) 
		{
			memberList.add("!u");
			memberList.add("command");

			if (memberPanel.bannedMemberListArray.get(guildIndex).getSelectedValue() == null)
			{
				this.devPanel.textArea.append("\nDEV WARNING: Could not unban anyone because you did not select a banned member");
				return;
			}
			
			String builtUserName = buildUserName(memberPanel.bannedMemberListArray.get(guildIndex).getSelectedValue());
			memberList.add(" " + builtUserName.substring(0, 21));
			
			String [] member = memberList.toArray(new String [memberList.size()]);
			
			Unban unban = new Unban();
			
			unban.execute(guild, guild.getSelfMember(), channel, member);
			unban.devMessage(unban.getName(), unban.extraDetails(), guild, Config.get("BOT_NAME"));
		}
		
		else if (e.getSource() == adminPanel.kickButton) 
		{
			memberList.add("!u");
			memberList.add("command");

			if (memberPanel.memberListArray.get(guildIndex).getSelectedValue() == null)
			{
				this.devPanel.textArea.append("\nDEV WARNING: Could not kick anyone because you did not select a member");
				return;
			}
			
			String builtUserName = buildUserName(memberPanel.memberListArray.get(guildIndex).getSelectedValue());
			memberList.add("<@!" + guild.getMembersByEffectiveName(builtUserName, true).get(0).getId() + ">");
			
			String [] member = memberList.toArray(new String [memberList.size()]);
			Kick kick = new Kick();
			
			kick.execute(guild, guild.getSelfMember(), channel, member);
			kick.devMessage(kick.getName(), kick.extraDetails(), guild, Config.get("BOT_NAME"));
		}
		
		else if (e.getSource() == adminPanel.silenceButton) 
		{
			memberList.add("!u");
			memberList.add("command");

			if (memberPanel.memberListArray.get(guildIndex).getSelectedValue() == null)
			{
				this.devPanel.textArea.append("\nDEV WARNING: Could not silence anyone because you did not select a member");
				return;
			}
			
			String builtUserName = buildUserName(memberPanel.memberListArray.get(guildIndex).getSelectedValue());
			memberList.add("<@!" + guild.getMembersByEffectiveName(builtUserName, true).get(0).getId() + ">");
			
			String [] member = memberList.toArray(new String [memberList.size()]);
			Silence silence = new Silence();
			
			silence.execute(guild, guild.getSelfMember(), channel, member);
			silence.devMessage(silence.getName(), silence.extraDetails(), guild, Config.get("BOT_NAME"));
		}
	}

	/**
	 * Description:<br>
	 * This method is responsible for assigning actions
	 * for whenever a user selects an entry of the list
	 * displayed in the guilds panel. Afterwards, the 
	 * members panel is refreshed to display the members
	 * of the selected guild.
	 * 
	 * @since	1.0.0
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == guildPanel.guildList.getSelectionModel())
		{
			// Retrieves the index of the guild list that is currently selected
			int index = guildPanel.guildList.getSelectedIndex();
	
			// Removes and edits the panel 
			memberPanel.tabbedPane.remove(memberPanel.scrollPane);
			memberPanel.tabbedPane.remove(memberPanel.bannedScrollPane);
			
			memberPanel.scrollPane = new JScrollPane(memberPanel.memberListArray.get(index));
			memberPanel.bannedScrollPane = new JScrollPane(memberPanel.bannedMemberListArray.get(index));
	
			// Replaces the removed panel with the updated one
			memberPanel.tabbedPane.add("Members", memberPanel.scrollPane);
			memberPanel.tabbedPane.add("Banned Members", memberPanel.bannedScrollPane);
			this.revalidate();
		}
	}
	
	/**
	 * Description:<br>
	 * This method is responsible for constructing the username
	 * from the selected value in the list.
	 * 
	 * @param	selectedValue		the selected entry of the members
	 * 								list, which contains the username
	 * 								of the person on which the desired
	 * 								action needs to be applied to
	 * @return	&lt;sb.toString&gt;
	 * @since  	1.0.0
	 */
	public String buildUserName(String selectedValue) {
		
		String [] splitValue = selectedValue.split(" ");
		StringBuilder sb = new StringBuilder();
		
		// Index is 1 so the entry number is left out
		int i = 1;
		while (i != splitValue.length)
		{
			sb.append(splitValue[i]);
				
			if (++i < splitValue.length)
			{
				sb.append(" ");
			}
		}
		
		return sb.toString();
	}

	/**
	 * Description:<br>
	 * This method is responsible for returning the size of
	 * the guild list. This variable is used to determine 
	 * the number of entries in the membersListArray, which
	 * is an array of lists used when selecting a certain 
	 * guild.
	 * 
	 * @return	&lt;guildListSize&gt;
	 * @since	1.0.0
	 */
	public int getGuildListSize() {
		return guildPanel.guildListSize;
	}
}
