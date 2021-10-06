package commands;

import commands.util.CommandObject;
import mainBot.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * Description:<br>
 * Shutdown represents the shutdown command that is called by the admin.
 * The class extends the abstract class <code>CommandObject</code>, and inherits the
 * methods that return the command's unique information.
 * The shutdown command prints the dev message showing who requested the command, and 
 * then calls the static method in the Bot class that shuts down the program.
 * <p>
 * 
 * Date:		August 24, 2021
 * @author 		Harrison Hoggard
 * @version		1.0.0
 * @since		1.0.0
 */
public class Shutdown extends CommandObject{
	String user;
	
	/**
	 * Description:<br>
	 * Retrieves the name of the command for the handler's use.
	 * 
	 * @return	<code>"shutdown"</code>
	 * @since	1.0.0
	 */
	public String getName() {
		return "shutdown";
	}

	/**
	 * Description:<br>
	 * Retrieves the extra details about the command's execution.
	 * 
	 * @return	<code>&lt;user&gt; shut down bot;
	 * @since	1.0.0
	 */
	public String extraDetails() {
		return (user + " shut down bot;");
	}

	/**
	 * Description:<br>
	 * Retrieves the arguments needed for the command.
	 * 
	 * @return	<code>""</code>
	 * @since 1.0.0
	 */
	public String getArgs() {
		return "";
	}
	
	/**
	 * Description:<br>
	 * Retrieves the information for the command's arguments.
	 * 
	 * @return	<code>""</code>
	 * @since	1.0.0
	 */
	public String getArgInfo() {
		return "";
	}

	/**
	 * Description:<br>
	 * Retrieves the category of the command.
	 * 
	 * @return	<code>"Admin"</code>
	 * @since	1.0.0
	 */
	public String getType() {
		return "Owner";
	}

	/**
	 * Description:<br>
	 * Retrieves the description of the command.
	 * 
	 * @return	<code>shuts down the bot</code>
	 * @since	1.0.0
	 */
	public String getDesc() {
		return "shuts down the bot";
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
	 * @return	<code>true</code>
	 * @since	1.0.0
	 */
	public boolean getOwner() {
		return true;
	}

	/**
	 * Description:<br>
	 * Executes the shutdown command. First, the dev message function is called, and
	 * then the shutdown method from the Bot class is called.
	 * 
	 * @param guild			the guild that the command was executed from
	 * @param member		the member that gave the command request
	 * @param textChannel	the text channel that the command was requested in
	 * @param arg			the command itself split into separate words. This allows the bot to
	 * 						derive any other arguments made
	 * @since				1.0.0
	 */
	public void execute(Guild guild, Member member, TextChannel textChannel, String[] arg) {
		this.user = member.getEffectiveName();
		
		super.devMessage(getName(), getArgs(), guild, user);
		
		Bot.shutdown(textChannel);
	}
}
