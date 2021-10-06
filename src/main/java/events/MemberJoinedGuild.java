package events;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.JScrollPane;

import events.util.EventObject;
import gui.GuiMain;
import mainBot.Bot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

/**
 * Description:<br>
 * The MemberJoinedGuild class defines the event for when someone joins a
 * guild that the bot resides in.
 * The classs extends the <code>EventObject</code> abstract class, which
 * defines its descriptor methods.
 * Whenever the event is triggered, the bot updates the member list of 
 * that guild to include the member that joined, and then updates the GUI
 * using that list.
 * <p>
 * 
 * Date:		August 24, 2021
 * @author 		Harrison Hoggard
 * @version		1.0.0
 * @since		1.0.0
 */
public class MemberJoinedGuild extends EventObject{

	String userName;
	String guildName;
	
	/**
	 * Description:<br>
	 * Retrieves the name of the event.
	 * 
	 * @return	<code>"MemberJoinedGuild"</code>
	 * @since	1.0.0
	 */
	public String getName() {
		return "MemberJoinedGuild";
	}

	/**
	 * Description:<br>
	 * Retrieves the actions of the event.
	 * 
	 * @return	<code>"&lt;userName&gt; joined the guild &lt;guildName&gt;"</code>
	 * @since	1.0.0
	 */
	public String getAction() {
		return userName + " joined the guild " + guildName;
	}

	/**
	 * Description:<br>
	 * The actions that the bot takes whenever the event takes place.
	 * Here, the members list of the guild involved is updated in order to 
	 * reflect the changes caused by the event. This list is then used
	 * to update the GUI.
	 * 
	 * @param event		The GuildMemberJoinEvent variable storing
	 * 					any info related to the user joining, such
	 * 					as the user, the guild involved, etc.
	 * @since			1.0.0
	 */
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		
		userName = event.getMember().getEffectiveName();
		guildName = event.getGuild().getName();

		ArrayList<String> list = new ArrayList<>();
		
		int guildIndex = Bot.jda.getGuilds().indexOf(event.getGuild());
		
		List<Member> members = event.getGuild().getMembers();

		// Updates list of members in the guild
		for (int j = 0; j < members.size(); j++)
		{
			list.add((j+1) + ") " + members.get(j).getEffectiveName());
		}
		
		// Replaces the data at the guild's index
		GuiMain.frameObject.memberPanel.memberListArray.set(guildIndex, new JList<String>(list.toArray(new String[list.size()])));

		// Removes and edits the panel 
		GuiMain.frameObject.memberPanel.tabbedPane.remove(GuiMain.frameObject.memberPanel.scrollPane);
		GuiMain.frameObject.memberPanel.tabbedPane.remove(GuiMain.frameObject.memberPanel.bannedScrollPane);
		
		GuiMain.frameObject.memberPanel.scrollPane = new JScrollPane(GuiMain.frameObject.memberPanel.memberListArray.get(guildIndex));
		GuiMain.frameObject.memberPanel.bannedScrollPane = new JScrollPane(GuiMain.frameObject.memberPanel.bannedMemberListArray.get(guildIndex));

		// Replaces the removed panel with the updated one
		GuiMain.frameObject.memberPanel.tabbedPane.add("Members", GuiMain.frameObject.memberPanel.scrollPane);
		GuiMain.frameObject.memberPanel.tabbedPane.add("Banned Members", GuiMain.frameObject.memberPanel.bannedScrollPane);
		GuiMain.frameObject.revalidate();
		
		super.devMessage(getName(), getAction(), event.getGuild());
	}
}
