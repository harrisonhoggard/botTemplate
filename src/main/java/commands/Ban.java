package commands;

import commands.util.CommandObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * Description:<br>
 * Ban represents the ban command that is called if either an admin or the bot itself calls for a ban.
 * The class extends the abstract class <code>CommandObject</code>, and inherits the methods that return 
 * the command's unique information.
 * The member to be banned is taken from the arg parameter of the execute() method. 
 * The method first checks to make sure index 2 is not empty, and if not, executes the command properly.
 * <p>
 * 
 * Date:		August 27, 2021
 * @author 		Harrison Hoggard
 * @version 	1.0.0
 * @since 		1.0.0
 */
public class Ban extends CommandObject {

	String userName;
	String guildName;
	
	/**
	 * Description:<br>
	 * Retrieves the name of the command for the handler's use.
	 * 
	 * @return 	<code>"ban"</code>
	 * @since 	1.0.0
	 */
	public String getName() {
		return "ban";
	}

	/**
	 * Description:<br>
	 * Retrieves extra details about the command's execution.
	 * 
	 * @return 	<code>"&lt;userName&gt; has been banned from &lt;guildName&gt;"</code>
	 * @since 	1.0.0
	 */
	public String extraDetails() {
		return userName + " has been banned from " + guildName;
	}
	
	/**
	 * Description:<br>
	 * Retrieves arguments needed for the command.
	 * 
	 * @return 	<code>" &lt;@member&gt;"</code>
	 * @since 	1.0.0
	 */
	public String getArgs() {
		return " <@member>";
	}

	/**
	 * Description:<br>
	 * Retrieves the information for the command's arguments.
	 * 
	 * @return 	<code>"- &lt;@member&gt;: member to ban"</code>
	 * @since 	1.0.0
	 */
	public String getArgInfo() {
		return "\n- <@member>: member to ban";
	}

	/**
	 * Description:<br>
	 * Retrieves the category of the command.
	 * 
	 * @return 	<code>"Admin"</code>
	 * @since 	1.0.0
	 */
	public String getType() {
		return "Admin";
	}

	/**
	 * Description:<br>
	 * Retrieves the description of the command.
	 * 
	 * @return 	<code>"bans the mentioned member"</code>"
	 * @since 	1.0.0
	 */
	public String getDesc() {
		return "bans the mentioned member";
	}

	/**
	 * Description:<br>
	 * Retrieves whether or not admin privileges are required to execute the command.
	 * 
	 * @return 	<code>true</code>
	 * @since 	1.0.0
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
	 * Executes the ban command.
	 * 
	 * @param guild			the guild that the command was executed from
	 * @param member		the member that gave the command request
	 * @param textChannel	the text channel that the command was requested in
	 * @param arg			the command itself split into separate words. This allows the bot to 
	 * 						derive any other arguments made
	 * @since				1.0.0
	 */
	public void execute(Guild guild, Member member, TextChannel textChannel, String[] arg) {
		
		// Verifies whether or not the member inputed the username of the person to be banned
		if (arg.length < 3)
		{
			EmbedBuilder embed = new EmbedBuilder();
			
			embed.addField(member.getEffectiveName(), "You did not tell me who to ban", true);
			textChannel.sendMessageEmbeds(embed.build()).complete();
			return;
		}
		
		// Substring removes the "<@!" and ">" characters from the ID
		Member banMember = guild.getMemberById(arg[2].substring(3, 21));
		
		userName = banMember.getEffectiveName();
		guildName = guild.getName();

		guild.ban(banMember, 0).complete();
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.addField(member.getEffectiveName(), "Banned " + banMember.getAsMention(), true);
		
		textChannel.sendMessageEmbeds(embed.build()).complete();		
	}
}
