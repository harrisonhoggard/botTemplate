package commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import commands.util.CommandObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * Description:<br>
 * Lines represents the lines command that is called by any user.
 * The class extends the abstract class <code>CommandObject</code>, and inherits the
 * methods that return the command's unique information.
 * The lines object traverses through the source code directory of the project and 
 * counts both the total number of lines of code in the project, as well as the total
 * number of files in the source code directory.
 * <p>
 * 
 * Date:		August 28, 2021
 * @author 		Harrison Hoggard
 * @version		1.0.0
 * @since		1.0.0
 */
public class Lines extends CommandObject {
	
	public static int linesOfCode = 0;
	public static int numberOfFiles = 0;
	
	/**
	 * Description:<br>
	 * Retrieves the name of the command for the handler's use.
	 * 
	 * @return	<code>"lines"</code>
	 * @since	1.0.0
	 */
	public String getName() {
		return "lines";
	}

	/**
	 * Description:<br>
	 * Retrieves the extra details about the command's execution.
	 * 
	 * @return	<code>""</code>
	 * @since	1.0.0
	 */
	public String extraDetails() {
		return "";
	}

	/**
	 * Description:<br>
	 * Retrieves the arguments needed for the command.
	 * 
	 * @return	<code>""</code>
	 * @since	1.0.0
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
	 * @return	<code>"Dev"</code>
	 * @since	1.0.0
	 */
	public String getType() {
		return "Owner";
	}

	/**
	 * Description:<br>
	 * Retrieves the description of the command.
	 * 
	 * @return	<code>"prints number of files and lines of code"</code>
	 * @since	1.0.0
	 */
	public String getDesc() {
		return "prints number of files and lines of code";
	}

	/**
	 * Description:<br>
	 * Retrieves whether or not admin privileges are required to execute the command.
	 * 
	 * @return	<code>false</code>
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
		return true;
	}

	/**
	 * Description:<br>
	 * Executes the lines command. The method traverses through the source code directory, and counts the
	 * number of lines in each file, as well as counts the total number of files in the directory.
	 * 
	 * @param guild			the guild that the command was executed from
	 * @param member		the member that gave the command request
	 * @param textChannel	the text channel that the command was requested in
	 * @param arg			the command itself split into separate words. This allows the bot to
	 * 						derive any other arguments made
	 * @since				1.0.0
	 */
	public void execute(Guild guild, Member member, TextChannel textChannel, String[] arg) {
		// If the static variable is 0, start traversing the files to count number of files and LoC
		if (linesOfCode == 0)
		{
			File file = new File("src/main/java");
			File [] dir = file.listFiles();
			
			traverseDirectory(dir, 0);
		}
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.addField(member.getEffectiveName(), "I am made of " + linesOfCode + " lines of code and docs within " + numberOfFiles + " files.", true);
		
		textChannel.sendMessageEmbeds(embed.build()).queue();
	}

	/**
	 * Description:<br>
	 * Traverses throughout the entire directory using recursion. As a file is found, the <code>numberOfFiles</code>
	 * variable is incremented, and the <code>linesOfCode</code> variable is incremented
	 * 
	 * @param file		the file / directory path to investigate. Passed as an array.
	 * @param i			the index to be used for the file array. Used to select specific
	 * 					files / folders
	 */
	public void traverseDirectory(File [] file, int i) {
		// If there are no more files, stop the current recursive incident
		if (i == file.length)
			return;
		
		if (file[i].isDirectory())
		{
			traverseDirectory(file[i].listFiles(), 0);
		}
		
		else 
		{
			try {
				linesOfCode += Files.lines(Path.of(file[i].getPath())).count();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			numberOfFiles += 1;
		}
		
		traverseDirectory(file, ++i);
	}

}
