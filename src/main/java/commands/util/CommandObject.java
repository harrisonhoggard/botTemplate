package commands.util;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import commands.Ban;
import commands.Help;
import commands.Kick;
import commands.Lines;
import commands.Shutdown;
import commands.Silence;
import commands.Unban;
import gui.GuiMain;
import mainBot.Bot;
import mainBot.Config;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * Description:<br>
 * Abstract class that all commands are an extension of.
 * Contains all descriptor methods used in describing every command.
 * Initializes all the commands through the init method.
 * Any "helper" methods related to commands are contained here as well, such as the
 * devMessage method, compare method, etc.
 * <p>
 * 
 * Date:		August 24, 2021
 * @author 		Harrison Hoggard
 * @version		1.0.0
 * @since		1.0.0
 */
public abstract class CommandObject {
	
	public static ArrayList<String> types;
	public static ArrayList<CommandObject> commands;
	
	/**
	 * Description:<br>
	 * Abstract method that returns the name for a command.
	 * 
	 * @return 	<code>command-name</code>
	 * @since	1.0.0
	 */
	public abstract String getName();
	
	/**
	 * Description:<br>
	 * Abstract method that returns any extra details for a command.
	 * 
	 * @return 	<code>command's-extra-details</code>
	 * @since	1.0.0
	 */
	public abstract String extraDetails();
	
	/**
	 * Description:<br>
	 * Abstract method to retrieve the command's arguments.
	 * 
	 * @return	<code>command-arguments</code>
	 * @since	1.0.0
	 */
	public abstract String getArgs();
	
	/**
	 * Description:<br>
	 * Abstract method to retrieve any extra argument information.
	 * 
	 * @return	<code>argument-info</code>
	 * @since	1.0.0
	 */
	public abstract String getArgInfo();
	
	/**
	 * Description:<br>
	 * Abstract method to retrieve the command's category.
	 * 
	 * @return	<code>command-type</code>
	 * @since	1.0.0
	 */
	public abstract String getType();
	
	/**
	 * Description:<br>
	 * Abstract method to retrieve the command's description.
	 * 
	 * @return	<code>command-description</code>
	 * @since	1.0.0
	 */
	public abstract String getDesc();
	
	/**
	 * Description:<br>
	 * Abstract method to check whether the command requires admin privileges.
	 * 
	 * @return	<code>admin-status</code>
	 * @since	1.0.0
	 */
	public abstract boolean getAdmin();
	
	/**
	 * Description:<br>
	 * Abstract method to check whether the command can only be executed by the 
	 * bot owner.
	 * 
	 * @return	<code>owner-status</code>
	 * @since	1.0.0
	 */
	public abstract boolean getOwner();
	
	/**
	 * Description:<br>
	 * Abstract method to execute the command.
	 * 
	 * @param guild			the guild that the command was executed from
	 * @param member		the member that gave the command request
	 * @param textChannel	the text channel that the command was requested from
	 * @param arg			the command itself split into separate words. This allows
	 * 						any other arguments to be derived for specialized use
	 * @since				1.0.0
	 */
	public abstract void execute(Guild guild, Member member, TextChannel textChannel, String [] arg);
	
	/**
	 * Description:<br>
	 * NEW COMMANDS ARE ADDED HERE
	 * 
	 * Adds all the commands to the static "commands" array. Once all commands have
	 * been added, the getTypes method is called to initalize the command categories.
	 * 
	 * @since	1.0.0
	 */
	public static void init() {
		
		commands = new ArrayList<>();
		types = new ArrayList<>();
		
		// Basic 
		commands.add(new Help());
		
		// Admin
		commands.add(new Ban());
		commands.add(new Kick());
		commands.add(new Silence());
		commands.add(new Unban());
		
		// Owner
		commands.add(new Lines());
		commands.add(new Shutdown());
		
		getTypes();
	}
	
	/**
	 * Description:<br>
	 * Traverses through the commands and retrieves their categories. These
	 * categories are then placed in the static "types" array to be used later
	 * in the "Help" command.
	 * 
	 * @since	1.0.0
	 */
	public static void getTypes() {
		for (CommandObject command : CommandObject.commands)
		{
			// If the types don't match, create a new entry in the types array
			if (!(types.contains(command.getType())))
				types.add(command.getType());
		}
	}
	
	/**
	 * Description:<br>
	 * Retrieves the current day and current time. Used in the dev message.
	 * 
	 * @return	<code>&lt;current time and date&gt;</code>
	 * @since	1.0.0
	 */
	public String getTimestamp() {
		return (Bot.getCurrentDate().format(DateTimeFormatter.ofPattern("E MM-dd-yyyy")) 
				+ " <" 
				+ Bot.getCurrentTime().format(DateTimeFormatter.ofPattern("hh:mm:ss a")) 
				+ ">");
	}
	
	/**
	 * Description:<br>
	 * Constructs the dev message to be printed in both the GUI and the Java console.
	 * 
	 * @param name		name of the command that was executed
	 * @param details	any extra details returned from the command's extraDetails()
	 * 					method
	 * @param guild		guild that the command was executed from
	 * @param member	member that requested a command's execution
	 * @since			1.0.0
	 */
	public void devMessage(String name, String details, Guild guild, String member) {
		System.out.println("COMMAND: " + getTimestamp() + " " + guild.getName() + ": " + member + " executed " + name + "; " + details);
		GuiMain.frameObject.devPanel.textArea.append("\nCOMMAND: " + getTimestamp() + " " + guild.getName() + ": " + member + " executed " + name + "; " + details);
	}
	
	/**
	 * Description:<br>
	 * Compares the member's requested command name to the names of the stored
	 * commands.
	 * 
	 * @param cmd	the inputed name of the command the user wants to execute.
	 * @return		<code>true</code> if the user input matches the name of a stored
	 * 				command<br>
	 * 				<code>false</code> if the user input does not match the stored
	 * 				command's name
	 * @since		1.0.0
	 */
	public boolean compare(String cmd) {
		return cmd.equalsIgnoreCase(getName());
	}
	
	/**
	 * Description:<br>
	 * Determines if the member requesting a command has permission to execute the
	 * command.
	 * 
	 * @param member	member that requested a command's execution
	 * @return			<code>true</code> if either the command requires admin
	 * 					privileges and the user has them, or the command doesn't
	 * 					require admin privileges<br>
	 * 					<code>false</code> if the command requires admin privileges 
	 * 					and the user does not have them
	 * @since			1.0.0
	 */
	public boolean hasAdminRole(Member member) {
		Role adminRole = member.getGuild().getRolesByName(Config.get("ADMIN_ROLE"), true).get(0);
		
		if (getAdmin())
			return member.getRoles().contains(adminRole);
		else
			return true;
	}
	
	/**
	 * Description:<br>
	 * Determines if the member requesting a command has permission to execute an
	 * owner-only command.
	 * 
	 * @param member	member that requested a command's execution
	 * @return			<code>true</code> if either the command requires owner 
	 * 					privileges and the member ID is present in the private.json 
	 * 					file, or the command doesn't require them<br>
	 * 					<code>false</code> if the command requires owner privileges 
	 * 					and the user does not have their ID as the bot host
	 * @since			1.0.0
	 */
	public boolean isBotOwner(Member member) {
		
		if (getOwner())
		{
			return (member.getId().compareTo(Config.get("OWNER_ID")) == 0);
		}
		else
			return true;
	}
}
