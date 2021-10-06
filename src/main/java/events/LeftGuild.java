package events;

import events.util.EventObject;
import gui.GuiMain;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;

/**
 * Description:<br>
 * The LeftGuild class defines the event for when the bot leaves a guild.
 * The class extends the <code>EventObject</code> abstract class, which defines
 * its descriptor methods.
 * Whenever the event is triggered, the bot updates its current guild list,
 * meaning the new guild list doesn't possess the guild the bot left. This
 * list is used to update the GUI and the members list used in the GUI.
 * <p>
 * 
 * Date:		August 24, 2021
 * @author 		Harrison Hoggard
 * @version		1.0.0
 * @since		1.0.0
 */
public class LeftGuild extends EventObject{

	String guildName;
	
	/**
	 * Description:<br>
	 * Retrieves the name of the event.
	 * 
	 * @return	<code>"LeftGuild"</code>
	 * @since	1.0.0
	 */
	public String getName() {
		return "LeftGuild";
	}

	/**
	 * Description:<br>
	 * Retrieves the actions of the event.
	 * 
	 * @return	<code>"Bot left guild &lt;guildName&gt;"</code>
	 * @since	1.0.0
	 */
	public String getAction() {
		return "Bot left guild " + guildName;
	}

	/**
	 * Description:<br>
	 * The actions that the bot takes whenever the event takes place.
	 * After the bot leaves a guild, an updated guild list is created that
	 * excludes the guild that was left. This list is used in updating the GUI
	 * to reflect the changes made.
	 * 
	 * @param event		the GuildLeaveEVent variable storing info
	 * 					related to the bot leaving a guild, such
	 * 					as the guild, its members, etc.
	 * @since			1.0.0
	 */
	public void onGuildLeave(GuildLeaveEvent event) {
		
		guildName = event.getGuild().getName();
		
		GuiMain.frameObject.guildPanel.refreshPane();
		
		GuiMain.frameObject.memberPanel.refreshPane();
		
		// Revalidates the updates to the frame
		GuiMain.frameObject.revalidate();
		
		// Ensures the actionlisteners are still working
		GuiMain.frameObject.restartListeners();
		
		super.devMessage(getName(), getAction(), event.getGuild());
	}
	
}
