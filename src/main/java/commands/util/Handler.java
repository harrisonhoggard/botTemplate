package commands.util;

import mainBot.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * Description:<br>
 * The Handler class is responsible for parsing the user input and derive
 * desired command from said input. After extracting the second index from
 * the arg parameter, that data is compared to each command name. If the 
 * command was found, check if user has privileges to execute. If true,
 * the command is executed, otherwise, let user know they need admin privileges.
 * If a command was not found, then let the user know no such command exists. 
 * <p>
 * 
 * Date:		August 24, 2021
 * @author 		Harrison Hoggard
 * @version		1.0.0
 * @since		1.0.0
 */
public class Handler {
	
	/**
	 * Description:<br>
	 * Constructor that derives the command name, and executes if it is found
	 * and the user has admin privileges.
	 * 
	 * @param guild			guild that the command execution was requested from
	 * @param textChannel	the text channel that the command request was sent
	 * 						from
	 * @param member		the member that sent the command request
	 * @param arg			user input containing the command request
	 * @since				1.0.0
	 */
	public Handler(Guild guild, TextChannel textChannel,  Member member,  String [] arg) {
		
		String cmd = arg[1];
		
		boolean commandFound = false;
		
		EmbedBuilder embed = new EmbedBuilder();
		
		// Traverses through the CommandObject array to search for a match in the command name
		for (CommandObject command : CommandObject.commands)
		{
			// Compare method defined in the abstract CommandObject class
			if (command.compare(cmd)) 
			{
				commandFound = true;
				
				if (command.hasAdminRole(member) && command.isBotOwner(member))
				{
					command.execute(guild, member, textChannel, arg);
					
					command.devMessage(command.getName(), command.extraDetails(), guild, member.getEffectiveName());
				}
				
				else 
				{
					embed.addField(member.getEffectiveName(), "You do not have the required privileges required for this command.", true);
					
					textChannel.sendMessageEmbeds(embed.build()).queue();
				}
				
				break;
			}
		}
		
		if (!commandFound) {
			embed.addField(member.getEffectiveName(), "I don't know that command. Type in \"" + Config.get("COMMAND_PREFIX") + " help\" for more information on commands.", true);
			
			textChannel.sendMessageEmbeds(embed.build()).queue();
		}
	}
}
