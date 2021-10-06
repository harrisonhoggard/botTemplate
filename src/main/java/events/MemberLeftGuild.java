package events;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.JScrollPane;

import events.util.EventObject;
import gui.GuiMain;
import mainBot.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;

/**
 * Description:<br>
 * The MemberLeftGuild class defines the event for when someone is removed
 * from a guild, whether by choice or by force.
 * The class extends the <code>EventObject</code> abstract class, which defines
 * its descriptor methods.
 * Whenever the event is triggered, the bot updates the member list of
 * that guild to exclude the member that was removed, and those updates are
 * applied to the GUI using the new list.
 * <p>
 * 
 * Date:		August 24, 2021
 * @author 		Harrison Hoggard
 * @version		1.0.0
 * @since		1.0.0
 */
public class MemberLeftGuild extends EventObject{

	String userName;
	String guildName;
	
	/**
	 * Description:<br>
	 * Retrieves the name of the event.
	 * 
	 * @return	<code>"MemberLeftGuild"</code>
	 * @since	1.0.0
	 */
	public String getName() {
		return "MemberLeftGuild";
	}

	/**
	 * Description:<br>
	 * Retrieves the actions of the event.
	 * 
	 * @return	<code>"&lt;userName&gt; left the guild &lt;guildName&gt;"</code>
	 * @since	1.0.0
	 */
	public String getAction() {
		return userName + " left the guild " + guildName;
	}

	
	/**
	 * Description:<br>
	 * The actions that the bot takes whenever the event takes place.
	 * Here, the members list of the guild involved is updated in order to
	 * reflect the changes caused by the event. This list is then used to 
	 * update the GUI.
	 * 
	 * @param event		The GuildMemberRemoveEvent variable storing
	 * 					any info related to the user leaving, such
	 * 					as the user, the guild involved, etc.
	 * @since			1.0.0
	 */
	public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
		
		Guild guild = event.getGuild();
		
		userName = event.getMember().getEffectiveName();
		guildName = guild.getName();

		ArrayList<String> list = new ArrayList<>();
		
		int guildIndex = Bot.jda.getGuilds().indexOf(event.getGuild());
		
		List<Member> members = event.getGuild().getMembers();

		// Updates list of members in the server
		for (int j = 0; j < members.size(); j++)
		{
			list.add((j+1) + ") " + members.get(j).getEffectiveName());
		}
		
		ArrayList<String> list2 = new ArrayList<String>();
		
		List<net.dv8tion.jda.api.entities.Guild.Ban> banList = guild.retrieveBanList().complete();
		
		if (!banList.isEmpty())
		{
			for (int k = 0; k < banList.size(); k++)
			{
				// Inputs a banned user's name and ID into the array
				list2.add((k+1) + ") " + banList.get(k).getUser().getAsMention() + " " + banList.get(k).getUser().getName());
			}
		}
		
		// Sets the GUI's banned member list to the updated list
		GuiMain.frameObject.memberPanel.memberListArray.set(guildIndex, new JList<String>(list.toArray(new String[list.size()])));
		GuiMain.frameObject.memberPanel.bannedMemberListArray.set(guildIndex, new JList<String>(list2.toArray(new String[list2.size()])));
		list.clear();
		list2.clear();
	
		// Removes the two scroll panes from the tabbed pane in the members pane
		GuiMain.frameObject.memberPanel.tabbedPane.remove(GuiMain.frameObject.memberPanel.scrollPane);
		GuiMain.frameObject.memberPanel.tabbedPane.remove(GuiMain.frameObject.memberPanel.bannedScrollPane);
		
		// Adds the updated panes to the member pane
		GuiMain.frameObject.memberPanel.scrollPane = new JScrollPane(GuiMain.frameObject.memberPanel.memberListArray.get(guildIndex));
		GuiMain.frameObject.memberPanel.bannedScrollPane = new JScrollPane(GuiMain.frameObject.memberPanel.bannedMemberListArray.get(guildIndex));

		// Adds the updated member panes to the tabbed pane
		GuiMain.frameObject.memberPanel.tabbedPane.add("Members", GuiMain.frameObject.memberPanel.scrollPane);
		GuiMain.frameObject.memberPanel.tabbedPane.add("Banned Members", GuiMain.frameObject.memberPanel.bannedScrollPane);
		
		// Refreshes the GUI
		GuiMain.frameObject.revalidate();
	
		super.devMessage(getName(), getAction(), event.getGuild());
	}
}
