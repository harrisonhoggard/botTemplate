package commands;

import commands.util.CommandObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * Description:<br>
 * Silence represents the silence command that is called by an admin.
 * The class extends the abstract class <code>CommandObject</code>, and inherits the
 * methods that return the command's unique information.
 * The silence command gives the desired member the "Silenced" guild role, which takes
 * away their speaking privileges in both text and voice channels.
 * <p>
 * 
 * Date:		August 27, 2021
 * @author 		Harrison Hoggard
 * @version		1.0.0
 * @since		1.0.0
 */
public class Silence extends CommandObject{

	String userName;
	
	// Variable used in deciding if a member is silenced or unsilenced
	public String state;
	
	/**
	 * Description:<br>
	 * Retrieves the name of the command for the handler's use.
	 * 
	 * @return	<code>"silence"</code>
	 * @since	1.0.0
	 */
	public String getName() {
		return "silence";
	}

	/**
	 * Description:<br>
	 * Retrieves the extra details about the command's execution.
	 * 
	 * @return	<code>&lt;userName&gt; has been &lt;state&gt;
	 * @since	1.0.0
	 */
	public String extraDetails() {
		return userName + " has been " + state;
	}

	/**
	 * Description:<br>
	 * Retrieves the arguments needed for the command.
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
	 * @return	<code>"- &lt;@member&gt;: member to silence"</code>
	 * @since	1.0.0
	 */
	public String getArgInfo() {
		return "\n- <@member>: member to silence";
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
	 * @return	<code>"silences the specified user"</code>
	 * @since	1.0.0
	 */
	public String getDesc() {
		return "silences the specified user";
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
	 * Executes the silence command. 
	 * First, the arg parameter is checked to see if the desired member was inputed,
	 * and warns the requesting member to add it in.
	 * Afterwards, the desired member is retrieved and then given the "silenced" role,
	 * taking away their speaking privileges.
	 * 
	 * @param guild			the guild that the command was executed from
	 * @param member		the member that gave the command request
	 * @param textChannel	the text channel that the command was requested in
	 * @param arg			the command itself split into separate words. This allows any
	 * 						other arguments to be derived for specialized use
	 * @since				1.0.0
	 */
	public void execute(Guild guild, Member member, TextChannel textChannel, String[] arg) {
		
		if (arg.length < 3)
		{
			EmbedBuilder embed = new EmbedBuilder();
			
			embed.addField(member.getEffectiveName(), "You did not tell me who to silence", true);
			textChannel.sendMessageEmbeds(embed.build()).complete();
			return;
		}
		
		EmbedBuilder embed = new EmbedBuilder();
		
		Member silenceMember = guild.getMemberById(arg[2].substring(3, 21));
		userName = silenceMember.getEffectiveName();
		
		Role silenceRole = guild.getRolesByName("Silenced", true).get(0);
		
		// If the member already has the role, the role is removed and the user unsilenced
		if (silenceMember.getRoles().contains(silenceRole))
		{
			guild.removeRoleFromMember(silenceMember, silenceRole).queue();
			
			state = "unsilenced";

			embed.addField(member.getEffectiveName(), extraDetails(), true);
			
			textChannel.sendMessageEmbeds(embed.build()).queue();
			return;
		}
		
		// Adds the role to the member
		state = "silenced";
		guild.addRoleToMember(silenceMember, silenceRole).queue();

		embed.addField(member.getEffectiveName(), extraDetails(), true);
		
		textChannel.sendMessageEmbeds(embed.build()).queue();
	}

}
