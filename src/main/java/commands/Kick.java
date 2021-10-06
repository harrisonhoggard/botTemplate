package commands;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.JScrollPane;

import commands.util.CommandObject;
import gui.GuiMain;
import mainBot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * Description:<br>
 * Kick represents the kick command that is called by a user with admin privileges.
 * The class extends the abstract class <code>CommandObject</code>, and inherits the 
 * methods that return the command's unique information.
 * The kick object extracts the member to be kicked from the <code>arg</code> 
 * parameter. After then assigning values to the instance variables, the member is 
 * then kicked from the guild.
 * <p>
 * 
 * Date:		August 27, 2021
 * @author 		Harrison Hoggard
 * @version		1.0.0
 * @since		1.0.0
 */
public class Kick extends CommandObject{

	String userName;
	String guildName;
	
	/**
	 * Description:<br>
	 * Retrieves the name of the command for the handler's use.
	 * 
	 * @return	<code>"help"</code>
	 * @since	1.0.0
	 */
	public String getName() {
		return "kick";
	}

	/**
	 * Description:<br>
	 * Retrieves the extra details about the command's execution.
	 * 
	 * @return	<code>"&lt;userName&gt; has been kicked from &lt;guildName&gt;"</code>
	 * @since	1.0.0
	 */
	public String extraDetails() {
		return userName + " has been kicked from " + guildName;
	}

	/**
	 * Description:<br>
	 * Retrieves arguments needed for the command.
	 * 
	 * @return	<code>" &lt;@member&gt;"</code>
	 * @since	1.0.0
	 */
	public String getArgs() {
		return " <@member>";
	}

	/**
	 * Description:<br>
	 * Retrieves the information for the command's arguments.
	 * 
	 * @return	<code>"- &lt;@member&gt;: member to kick"</code>
	 * @since	1.0.0
	 */
	public String getArgInfo() {
		return "\n- <@member>: member to kick";
	}

	/**
	 * Description:<br>
	 * Retrieves the category of the command.
	 * 
	 * @return	<code>"Admin"</code>
	 * @since	1.0.0
	 */
	public String getType() {
		return "Admin";
	}

	/**
	 * Description:<br>
	 * Retrieves the description of the command.
	 * 
	 * @return	<code>"kicks the specified user"</code>
	 * @since	1.0.0
	 */
	public String getDesc() {
		return "kicks the specified user";
	}

	/**
	 * Description:<br>
	 * Retrieves whether or not admin privileges are required to execute the command.
	 * 
	 * @return	<code>true</code>
	 * @since	1.0.0
	 */
	public boolean getAdmin() {
		return true;
	}
	
	/**
	 * Description:<br>
	 * Retrieves whether or not the command can only be executed by the bot host.
	 * 
	 * @return	<code>false</code>
	 * @since	1.0.0
	 */
	public boolean getOwner() {
		return false;
	}

	/**
	 * Description:<br>
	 * Executes the kick command. After kicking, the changes are reflected in the GUI
	 * 
	 * @param guild			the guild that the command was executed from
	 * @param member		the member that gave the command request
	 * @param textChannel	the text channel that the command was requested in
	 * @param arg			the command itself split into separate words. This allows the bot to 
	 * 						derive any other arguments made
	 * @since				1.0.0
	 */
	public void execute(Guild guild, Member member, TextChannel textChannel, String[] arg) {
		
		// Verifies whether or not the member inputed the username of the person to be kicked
		if (arg.length < 3)
		{
			EmbedBuilder embed = new EmbedBuilder();
			
			embed.addField(member.getEffectiveName(), "You did not tell me who to kick", true);
			textChannel.sendMessageEmbeds(embed.build()).complete();
			return;
		}
		
		Member kickMember = guild.getMemberById(arg[2].substring(3, 21));
		
		userName = kickMember.getEffectiveName();
		guildName = guild.getName();
		
		guild.kick(kickMember).queue();
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.addField(member.getEffectiveName(), "Kicked " + kickMember.getAsMention(), true);
		
		textChannel.sendMessageEmbeds(embed.build()).complete();		
		
		// Used to retrieve the proper JList of the member / banned member arrays
		int guildIndex = Bot.jda.getGuilds().indexOf(guild);
		
		ArrayList<String> list = new ArrayList<String>();
		
		List<net.dv8tion.jda.api.entities.Guild.Ban> banList = guild.retrieveBanList().complete();
		
		if (!banList.isEmpty())
		{
			for (int k = 0; k < banList.size(); k++)
			{
				// Inputs a banned user's name and ID into the array
				list.add((k+1) + ") " + banList.get(k).getUser().getAsMention() + " " + banList.get(k).getUser().getName());
			}
		}
		
		// Sets the GUI's banned member list to the updated list
		GuiMain.frameObject.memberPanel.bannedMemberListArray.set(guildIndex, new JList<String>(list.toArray(new String[list.size()])));
		list.clear();
	
		// Removes the two scroll panes from the tabbed pane in the members pane
		GuiMain.frameObject.memberPanel.tabbedPane.remove(GuiMain.frameObject.memberPanel.scrollPane);
		GuiMain.frameObject.memberPanel.tabbedPane.remove(GuiMain.frameObject.memberPanel.bannedScrollPane);
		
		// Adds the updated panes to the member pane
		GuiMain.frameObject.memberPanel.scrollPane = new JScrollPane(GuiMain.frameObject.memberPanel.memberListArray.get(guildIndex));
		GuiMain.frameObject.memberPanel.bannedScrollPane = new JScrollPane(GuiMain.frameObject.memberPanel.bannedMemberListArray.get(guildIndex));

		// Adds the updated member panes to the tabbed pane
		GuiMain.frameObject.memberPanel.tabbedPane.add("Members", GuiMain.frameObject.memberPanel.scrollPane);
		GuiMain.frameObject.memberPanel.tabbedPane.add("Banned Members", GuiMain.frameObject.memberPanel.bannedScrollPane);
		
		// Refreshes the GUI
		GuiMain.frameObject.revalidate();
	}

}
