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
import net.dv8tion.jda.api.entities.User;

/**
 * Description:<br>
 * Unban represents the unban command that is called by an admin.
 * The class extends the abstract class <code>CommandObject</code>, and inherits the 
 * methods that return the command's unique information.
 * The unban command takes the desired member to unban from the arg parameter, and 
 * then proceeds to unban said user from the guild. 
 * Afterwards, the changes are then processed and reflected in the GUI.
 * <p>
 * 
 * Date:		August 27, 2021
 * @author 		Harrison Hoggard
 * @version		1.0.0
 * @since		1.0.0
 */
public class Unban extends CommandObject {

	String userName;
	String guildName;
	
	/**
	 * Description:<br>
	 * Retrieves the name of the command for the handler's use.
	 * 
	 * @return	<code>"unban"</code>
	 * @since	1.0.0
	 */
	public String getName() {
		return "unban";
	}

	/**
	 * Description:<br>
	 * Retrieves the extra details about the command's execution.
	 * 
	 * @return	<code>&lt;userName&gt; has been unbanned from &lt;guildName&gt;"</code>
	 * @since	1.0.0
	 */
	public String extraDetails() {
		return userName + " has been unbanned from " + guildName;
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
	 * @return	<code>"- &lt;@member&gt;: member to unban"</code>
	 */
	public String getArgInfo() {
		return "\n- <@member>: member to unban";
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
	 * @return	<code>"unbans the mentioned member"</code>
	 * @since	1.0.0
	 */
	public String getDesc() {
		return "unbans the mentioned member";
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
	 * Executes the unban command.
	 * After retrieving the username from the arg parameter, the desired member is unbanned
	 * from the guild. Finally, the changes are then reflected in the GUI.
	 * 
	 * @param guild			the guild that the command was executed from
	 * @param member		the member that gave the command request
	 * @param textChannel	the text channel that the command was requested from
	 * @param arg			the command itself split into separate words. This allows
	 * 						any other arguments to be derived for specialized use
	 * @since				1.0.0
	 */
	public void execute(Guild guild, Member member, TextChannel textChannel, String[] arg) {
		
		User bannedUser = null;
		
		if (arg.length < 3)
		{
			EmbedBuilder embed = new EmbedBuilder();
			
			embed.addField(member.getEffectiveName(), "You did not tell me who to unban", true);
			textChannel.sendMessageEmbeds(embed.build()).complete();
			return;
		}
		
		String userId = arg[2].substring(3, 21);
		List<net.dv8tion.jda.api.entities.Guild.Ban> banList = guild.retrieveBanList().complete();

		guildName = guild.getName();
		for (int i = 0; i < banList.size(); i++)
		{
			if (banList.get(i).getUser().getId().compareTo(userId) == 0)
			{
				userName = banList.get(i).getUser().getName();
				bannedUser = banList.get(i).getUser();
			}
		}
		
		guild.unban(userId).complete();
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.addField(member.getEffectiveName(), "Unbanned " + bannedUser.getAsMention(), true);
		
		textChannel.sendMessageEmbeds(embed.build()).complete();		
		
		int guildIndex = Bot.jda.getGuilds().indexOf(guild);
		
		ArrayList<String> list = new ArrayList<String>();
		banList = guild.retrieveBanList().complete();
		
		if (!banList.isEmpty())
		{
			for (int k = 0; k < banList.size(); k++)
			{
				list.add((k+1) + ") " + banList.get(k).getUser().getAsMention() + " " + banList.get(k).getUser().getName());
			}
		}
		
		GuiMain.frameObject.memberPanel.bannedMemberListArray.set(guildIndex, new JList<String>(list.toArray(new String[list.size()])));
		list.clear();
		
		GuiMain.frameObject.memberPanel.tabbedPane.remove(GuiMain.frameObject.memberPanel.scrollPane);
		GuiMain.frameObject.memberPanel.tabbedPane.remove(GuiMain.frameObject.memberPanel.bannedScrollPane);
		
		GuiMain.frameObject.memberPanel.scrollPane = new JScrollPane(GuiMain.frameObject.memberPanel.memberListArray.get(guildIndex));
		GuiMain.frameObject.memberPanel.bannedScrollPane = new JScrollPane(GuiMain.frameObject.memberPanel.bannedMemberListArray.get(guildIndex));

		// Replaces the removed panel with the updated one
		GuiMain.frameObject.memberPanel.tabbedPane.add("Members", GuiMain.frameObject.memberPanel.scrollPane);
		GuiMain.frameObject.memberPanel.tabbedPane.add("Banned Members", GuiMain.frameObject.memberPanel.bannedScrollPane);
		GuiMain.frameObject.revalidate();
		
	}

}
