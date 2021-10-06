package events;

import events.util.EventObject;
import gui.GuiMain;
import mainBot.Bot;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;

/**
 * Description:<br>
 * The JoinedNewGuild class defines the event for when the bot joins
 * a new guild. 
 * The class extends the <code>EventObject</code> abstract class, which
 * defines its descriptor methods.
 * Whenever the event is triggered, the bot initializes the new guild 
 * by adding the necessary roles and channels, and adding the guild in 
 * the GUI. Afterwards, the GUI is updated with the members part of the 
 * new guild.
 * <p>
 * 
 * Date:		August 24, 2021
 * @author 		Harrison Hoggard
 * @version		1.0.0
 * @since		1.0.0
 */
public class JoinedNewGuild extends EventObject{

	String guildName;
	
	/**
	 * Description:<br>
	 * Retrieves the name of the event.
	 * 
	 * @return	<code>"JoinedNewGuild"</code>
	 * @since	1.0.0
	 */
	public String getName() {
		return "JoinedNewGuild";
	}

	/**
	 * Description:<br>
	 * Retrieves the actions of the event.
	 * 
	 * @return	<code>"Bot joined guild &lt;guildName&gt;"</code>
	 * @since	1.0.0
	 */
	public String getAction() {
		return "Bot joined guild " + guildName;
	}

	/**
	 * Description:<br>
	 * The actions that the bot takes whenever the event takes place.
	 * Here, the bot initializes the new guild, and then adds the 
	 * guild and its members into the GUI.
	 * 
	 * @param event		the GuildJoinEvent variable storing any
	 * 					information related to the event, such as
	 * 					the guild, its members, etc.
	 * @since			1.0.0
	 */
	public void onGuildJoin(GuildJoinEvent event) {
		
		// Initializes guild for bot use
		Bot.guildInit(event.getGuild(), Bot.jda.getGuilds().size() - 1);
		
		guildName = event.getGuild().getName();
		
		GuiMain.frameObject.guildPanel.refreshPane();
		
		GuiMain.frameObject.memberPanel.refreshPane();
		
		// Applies the edits
		GuiMain.frameObject.revalidate();

		// Ensures the actionlisteners are still working
		GuiMain.frameObject.restartListeners();
		
		super.devMessage(getName(), getAction(), event.getGuild());
	}
}
