package events.util;

import java.time.format.DateTimeFormatter;

import events.JoinedNewGuild;
import events.LeftGuild;
import events.MemberJoinedGuild;
import events.MemberLeftGuild;
import gui.GuiMain;
import mainBot.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Description:<br>
 * Abstract class that all events are an extension of.
 * Contains all descriptor methods used in describing every event.
 * Adds all the events to the JDA's event listener through the init
 * method.
 * Any "helper" methods related to events are contained here as well, such as the 
 * getTimestamp method, the devMessage method, etc.
 * <p>
 * 
 * Date:		August 24, 2021
 * @author 		Harrison Hoggard
 * @version		1.0.0
 * @since		1.0.0
 */
public abstract class EventObject extends ListenerAdapter{
	
	/**
	 * Description:<br>
	 * Abstract method that returns the name of the event class.
	 * 
	 * 
	 * @return	<code>event-class-name</code>
	 * @since	1.0.0
	 */
	public abstract String getName();
	
	/**
	 * Description:<br>
	 * Abstract method that returns the action taken from the
	 * event.
	 * 
	 * @return	<code>event-action</code>
	 * @since	1.0.0
	 */
	public abstract String getAction();
	
	/**
	 * Description:<br>
	 * Adds all the events to the JDA's event listener.
	 * 
	 * @since	1.0.0
	 */
	public static void init() {
		Bot.jda.addEventListener(new JoinedNewGuild());
		Bot.jda.addEventListener(new LeftGuild());
		Bot.jda.addEventListener(new MemberJoinedGuild());
		Bot.jda.addEventListener(new MemberLeftGuild());
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
	 * Constructs the dev message to be printed in bot the GUI and the Java console.
	 * 
	 * @param name		name of the event class that was called
	 * @param action	details of the actions of the event
	 * @param guild		guild in which the event took place
	 * @since			1.0.0
	 */
	public void devMessage(String name, String action, Guild guild) {
		System.out.println("EVENT: " + getTimestamp() + " " + guild.getName() + ": executed " + name + ": " + action + ";");
		GuiMain.frameObject.devPanel.textArea.append("\nEVENT: " + getTimestamp() + " " + guild.getName() + ": executed " + name + ": " + action + ";");
	}
}
