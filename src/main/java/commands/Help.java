package commands;

import java.awt.Color;

import commands.util.CommandObject;
import mainBot.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * Description:<br>
 * Help represents the help command that is called by any user. 
 * The class extends the abstract class <code>CommandObject</code>, and inherits the methods 
 * that return the command's unique information.
 * The program determines if the input has a specific command to retrieve information on, or if 
 * there is no specific command, thereby needing to retrieve information for every command. 
 * After determining this, an embed is created containing the necessary information in the 
 * following format:
 * <br>
 * 		"&lt;command-name&gt; &lt;command-arguments&gt;: &lt;command-description&gt;
 * <br>		&lt;command-argument&gt;: &lt;argument-description&gt;"
 * <br>
 * After creating this embed, it is sent to the text channel that the command request was sent
 * from.
 * <p>
 * 
 * Date:		August 24, 2021
 * @author 		Harrison Hoggard
 * @version 	1.0.0
 * @since 		1.0.0
 */
public class Help extends CommandObject{
	
	public boolean wasSingle = false;
	public String singleCommand;
	
	/**
	 * Description:<br>
	 * Retrieves the name of the command for the handler's use.
	 * 
	 * @return 	<code>"help"</code>
	 * @since 	1.0.0
	 */
	public String getName() {
		return "help";
	}
	
	/**
	 * Description:<br>
	 * Retrieves the extra details about the command's execution.
	 * 
	 * @return 	<code>""</code> if the embed contains information on every command;
	 * 			<code>"user request command &lt;command-name&gt;"</code> if a specific
	 * 			command was requested.
	 * @since	1.0.0
	 */
	public String extraDetails() {
		if (wasSingle == false)
			return "";
		wasSingle = false;
		return "user requested command \"" + singleCommand + "\";";
	}
	
	/**
	 * Description:<br>
	 * Retrieves arguments needed for the command.
	 * 
	 * @return	<code>" &lt;command&gt;"</code>
	 * @since	1.0.0
	 */
	public String getArgs() {
		new Ban();
		return " <command>";
	}
	
	/**
	 * Description:<br>
	 * Retrieves the information for command's arguments.
	 * 
	 * @return 	<code>"- &lt;command&gt;: name of command"</code>
	 * @since	1.0.0
	 */
	public String getArgInfo() {
		return "\n- <command>: name of command";
	}

	/**
	 * Description:<br>
	 * Retrieves the category of the command.
	 * 
	 * @return	<code>"Basic"</code>
	 * @since	1.0.0
	 */
	public String getType() {
		return "Basic";
	}

	/**
	 * Description:<br>
	 * Retrieves the description of the command.
	 * 
	 * @return 	<code>"gets info on commands"</code>
	 * @since	1.0.0
	 */
	public String getDesc() {
		return "gets info on commands";
	}
	
	/**
	 * Description:<br>
	 * Retrieves whether or not admin privileges are required to execute the command.
	 * 
	 * @return 	<code>false</code>
	 * @since	1.0.0
	 */
	public boolean getAdmin() {
		return false;
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
	 * Executes the help command after determining whether to send info on a single
	 * command, or all commands.
	 * 
	 * @param guild			the guild that the command was executed from
	 * @param member		the member that gave the command request
	 * @param textChannel	the text channel that the command was requested in
	 * @param arg			the command itself split into separate words. This allows the bot to 
	 * 						derive any other arguments made
	 * @since				1.0.0
	 */
	public void execute(Guild guild, Member member, TextChannel textChannel, String[] arg) {
		
		// If there is a specific command present in the arg parameter, execute singleEmbedBuild
		if (arg.length >= 3)
			singleEmbedBuild(arg[2], textChannel, member);
		
		else
			allEmbedBuild(textChannel, member);
		
	}
	
	/**
	 * Description:<br>
	 * Retrieves information on a specific command.
	 * 
	 * @param cmd		the specific command to retrieve information about
	 * @param channel	the text channel to send the retrieved information to
	 * @since 			1.0.0
	 */
	public void singleEmbedBuild(String cmd, TextChannel channel, Member member) {

		wasSingle = true;
		singleCommand = cmd;
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Commands: " + Config.get("COMMAND_PREFIX")  + " <command>");
		eb.setColor(Color.cyan);
		
		boolean commandFound = false;
		int i = 0;
		
		for (CommandObject command : CommandObject.commands) {
			if (command.compare(cmd)) {
				
				commandFound = true;

				break;
			}
			
			i++;
		}
		
		if (commandFound == true)
		{
			CommandObject command = CommandObject.commands.get(i);
			if (command.isBotOwner(member))
			{
				eb.addField(command.getType(), command.getName() + command.getArgs() + ": " + command.getDesc() + command.getArgInfo(), false);
			}
			else
			{
				eb.addField("Command not found", cmd + " has not been found. Be sure to use \"" + Config.get("COMMAND_PREFIX") + " help\" for more info", false);
			}
		}
		
		else
		{
			eb.addField("Command not found", cmd + " has not been found. Be sure to use \"" + Config.get("COMMAND_PREFIX") + " help\" for more info", false);
		}

		channel.sendMessageEmbeds(eb.build()).queue();
	}
	
	/**
	 * Description:<br>
	 * Retrieves the information on all commands. Commands are sorted by their
	 * categories.
	 * 
	 * @param channel	text channel that the command request came from
	 * @since			1.0.0
	 */
	public void allEmbedBuild(TextChannel channel, Member member) {
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Commands: " + Config.get("COMMAND_PREFIX") + " <commands>");
		eb.setColor(Color.cyan);
		int i = 0;
		
		// String builder to create body of embed
		StringBuilder sb = new StringBuilder();
		
		CommandObject finalCommand = null;
		
		for (CommandObject command : CommandObject.commands)
		{
			// If command type is equal to the currently set type, append the string to add command info
			if (CommandObject.types.get(i).compareTo(command.getType()) == 0)
			{
				sb.append(command.getName() + command.getArgs() + ": " + command.getDesc() + command.getArgInfo() + "\n");
			}
			
			// Otherwise, add the embed field with the current command type and string and move on to the next command type
			else
			{
				eb.addField(CommandObject.types.get(i), sb.toString(), false);
				
				sb.delete(0, sb.length());
				
				if (command.isBotOwner(member))
				{
					// Adds the current command info to the now blank string builder
					sb.append(command.getName() + command.getArgs() + ": " + command.getDesc() + command.getArgInfo() + "\n");
				}

				i++;
			}
			finalCommand = command;
		}

		if (finalCommand.isBotOwner(member))
		{
			eb.addField(CommandObject.types.get(i), sb.toString(), false);
		}
		
		channel.sendMessageEmbeds(eb.build()).queue();
	}
}
