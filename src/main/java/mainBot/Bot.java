package mainBot;

import java.awt.Color;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;

import javax.security.auth.login.LoginException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import commands.util.CommandObject;
import commands.util.Handler;
import events.util.EventObject;
import gui.GuiMain;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

/**
 * Description:<br>
 * The "main" class that is responsible for initializing 
 * the bot, as well as housing any utility methods that
 * fulfill common tasks for Discord, such as joining 
 * voice channels, or retrieving permissions for roles.
 * <br>
 * The execution of the program starts in the main method, 
 * which starts the launching process of the bot.<br>
 * This launch
 * process initalizes the configuration file, which contains
 * any necessary information used for the bot to run, such 
 * as its API token. After confirming that the token, which
 * is the most important piece of data, is stored successfully,
 * then the start method is called.<br>
 * This start process is responsible for initializing the bot
 * application using the API token, as well as initalizing the
 * guilds the bot is present in by setting up the proper roles
 * and guild channels. After initializing each individual 
 * guild, the CommandObject and EventObject initializations 
 * are called. Finally, the GUI is initialized, displaying
 * all the data that was generated here.<br>
 * Once everything is initialized, the bot starts listening for 
 * events, either from a guild or from the GUI.
 * <p>
 * 
 * Date:		August 24, 2021
 * @author 		Harrison Hoggard
 * @version		1.0.0
 * @since		1.0.0
 */
public class Bot extends ListenerAdapter {
	
	public static JDA jda;
	public static boolean isJoined;
	
	/**
	 * Description:<br>
	 * The main method...
	 * 
	 * @param  	args					the standard arguments when
	 * 									starting the program from
	 * 									a terminal
	 * @throws 	LoginException
	 * @throws 	InterruptedException
	 * @since	1.0.0
	 */
	public static void main(String [] args) throws LoginException, InterruptedException {
		
		boolean status = launch();
		
		// Adds extra layer of verification
		if (status != true) {
			System.out.println("Could not load bot");
		}
	}

	/**
	 * Description:<br>
	 * The launch method is responsible for initializing the 
	 * configuration setup, which contains all the necessary 
	 * information used for the bot to run. 
	 * 
	 * @return 	start()					the boolean status of the
	 * 									method that starts the bot
	 * @throws 	InterruptedException
	 * @throws 	LoginException
	 * @since	1.0.0
	 */
	public static boolean launch() throws InterruptedException, LoginException {
		
		if (!Config.isInitialized())
		{
			Config.init();
		}
		
		return start();
	}
	
	/**
	 * Description:<br>
	 * This method is responsible for initializing each individual guild, 
	 * initializing the commands and events, and finally initializing 
	 * the GUI to display the generated data.
	 * 
	 * @return	false	if the token wasn't generated in the config class
	 * 			true	if everything was initialized
	 * @throws 	InterruptedException
	 * @throws 	LoginException
	 * @since	1.0.0
	 */
	public static boolean start() throws InterruptedException, LoginException {
		
		// Safety net ensuring bot token is initalized
		if (Config.get("TOKEN").isEmpty())
			return false;
		
		jda = JDABuilder
				.createDefault(Config.get("TOKEN").toString(), EnumSet.allOf(GatewayIntent.class))
				.setChunkingFilter(ChunkingFilter.ALL)
				.setMemberCachePolicy(MemberCachePolicy.ALL)
				.enableCache(EnumSet.allOf(CacheFlag.class))
				.enableIntents(EnumSet.allOf(GatewayIntent.class))
				.build();
		
		/*
		 *  This ensures that the bot is fully initialized before setting 
		 *  up other aspects, such as the GUI
		 */
		jda.awaitReady();
		
		setActivity(Config.get("ACTIVITY").toString());
		
		for (int i = 0; i < jda.getGuilds().size(); i++)
		{
			guildInit(jda.getGuilds().get(i), i);
		}
		
		CommandObject.init();
		
		EventObject.init();
		
		GuiMain.init();
		
		// Adds the Bot class as an event listener. Enables the onGuildMessageReceived() method below
		jda.addEventListener(new Bot());
		
		return true;
	}
	
	/**
	 * Description:<br>
	 * Sets up each guild that the bot is either loaded up in, or joins while it is running.
	 * The set up involves verifying whether or not the general text channel, admin role, 
	 * bot role, or silenced role are all created. If they aren't then the bot creates them
	 * with the correct attributes (permissions, position, etc.).
	 * 
	 * @param guild		a guild that the bot is in
	 * @since			1.0.0
	 */
	public static void guildInit(Guild guild, int i) {
		
		/*
		 *  Certain commands require admin privileges, so if the 
		 *  guild doesn't have a role with the role name in the 
		 *  Config, a role is created with every permission.
		 */
		if (guild.getRolesByName(Config.get("ADMIN_ROLE"), true).isEmpty())
		{
			// Creates the admin role
			guild.createRole()
				.setName(Config.get("ADMIN_ROLE"))
				.setColor(Color.red)
				.setMentionable(true)
				.setPermissions(getAdminPermissions())
				.complete();
		}
		
		/*
		 * The general channel allows the bot to send a message
		 * when a channel wasn't specified in the action.
		 */
		if (guild.getTextChannelsByName("general", true).isEmpty())
		{
			guild.createTextChannel("general")
				.complete();
			
			TextChannel channel = guild.getTextChannelsByName("general", true).get(0);
			
			channel.createPermissionOverride(guild.getPublicRole())
				.setDeny(Permission.VIEW_CHANNEL)
				.complete();
			
			channel.createPermissionOverride(guild.getRolesByName(Config.get("ADMIN_ROLE"), true).get(0))
				.setAllow(Permission.VIEW_CHANNEL)
				.complete();
			
			channel.getManager()
				.setTopic("The channel that " + Config.get("BOT_NAME") + " created. Dev messages will be sent here.")
				.complete();
		
			channel.sendMessage("Move the new \"Bot\" role to the top").complete();
		}
		
		// Helps the bot stand out in the members list
		if (guild.getRolesByName("Bot", true).isEmpty())
		{
			guild.createRole()
				.setName("Bot")
				.setColor(Color.blue)
				.setMentionable(true)
				.setPermissions(getAdminPermissions())
				.complete();
			
			guild.addRoleToMember(guild.getSelfMember(), guild.getRolesByName("Bot", true).get(0)).complete();
		}
		
		/*
		 *  The silenced role prevents users from talking/sending 
		 *  messages in text and voice channels
		 */
		if (guild.getRolesByName("Silenced", true).isEmpty())
		{
			guild.createRole()
				.setName("Silenced")
				.setColor(Color.black)
				.setMentionable(true)
				.setPermissions(getSilencedPermissions())
				.complete();
			
			@SuppressWarnings("unused")
			int j;
			
			if (guild.getBotRole().getPosition() - 1 < 0)
				j = 0;
			else
				j = guild.getBotRole().getPosition() - 1;
			
			guild.modifyRolePositions()
				.selectPosition(guild.getRolesByName("Silenced", true).get(0))
				.moveTo(guild.getRolesByName("Bot", true).get(0).getPosition() - 1)
				.complete();
			
			Role silentRole = guild.getRolesByName("Silenced", true).get(0);
			
			/*
			 *  Sets permissions of the silenced role for each text 
			 *  and voice channel
			 */
			for (int k = 0; k < guild.getChannels().size(); k++)
			{
				GuildChannel channel = guild.getChannels().get(k);
				
				if (channel.getType() == ChannelType.TEXT)
				{
					channel.createPermissionOverride(silentRole)
						.setDeny(Permission.ALL_TEXT_PERMISSIONS)
						.setAllow(Permission.MESSAGE_HISTORY)
						.complete();
				}
				else if (channel.getType() == ChannelType.VOICE)
					channel.createPermissionOverride(silentRole)
						.setDeny(Permission.ALL_VOICE_PERMISSIONS)
						.setAllow(Permission.VOICE_CONNECT)
						.complete();
			}
		}
		
	}
	
	/**
	 * Description:<br>
	 * Reads a JSON file and parses all the information into a JSON
	 * object.
	 * 
	 * @param 	filePath		the directory of the desired JSON file to parse.
	 * @return					<code>json-object</code> if the object was
	 * 							retrieved successfully;
	 * 							<code>null</code> if the object was not parsed
	 * 							successfully.
	 * @since					1.0.0
	 */
	public static JSONObject parseJson(String filePath) {
		
		JSONParser parser = new JSONParser();
		Object obj;
		
		try {
			obj = parser.parse(new FileReader(filePath));
			JSONObject json = (JSONObject) obj;
			
			return json;
		} catch (IOException | ParseException e) {
			return null;
		}
	}
	
	/**
	 * Description:<br>
	 * The method inherited from the ListenerAdapter class. Whenever 
	 * a message is sent to a guild that the bot is in, an event variable 
	 * is passed, and the method below is executed.
	 * 
	 * @param	event		the GuildMessageReceivedEvent that contains 
	 * 						information related to the event that was 
	 * 						passed, such as the message, the author, channel, 
	 * 						etc.
	 * @since				1.0.0
	 */
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String message  = event.getMessage().getContentRaw();
		Guild guild = event.getGuild();
		TextChannel textChannel = event.getChannel();
		Member member = event.getMember();
		
		String [] arg = message.split(" ");
		
		if (!member.getUser().isBot())
		{
			// Checks if the first string is the command prefix
			if (arg[0].compareTo(Config.get("COMMAND_PREFIX")) == 0)
			{
				@SuppressWarnings("unused")
				Handler commandHandler = new Handler(guild, textChannel,  member,  arg);
			}
		}
	}
	
	/**
	 * Description:<br>
	 * Responsible for shutting down the program.
	 * 
	 * @since	1.0.0
	 */
	public static void shutdown() {
		System.exit(0);
	}
	
	/**
	 * Alternative shutdown method with a text channel variable as 
	 * a parameter. Most appropriately used when the shutdown command
	 * was executed from a guild.
	 * 
	 * @param 	channel		text channel variable to send the message 
	 * 						letting a user know the bot is shutting 
	 * 						down.
	 * @since				1.0.0
	 */
	public static void shutdown(TextChannel channel) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.addField("Admin", "Shutting down now", true);
		
		channel.sendMessageEmbeds(embed.build()).complete();
		
		shutdown();
	}
	
	/**
	 * Description:<br>
	 * Responsible for connecting the bot to a guild's voice channel. 
	 * If the bot is already connected to a channel, let the user know. 
	 * Otherwise, connect to the requested voice channel.
	 * 
	 * @param 	textChannel		text channel to send the messages in.
	 * @param	voiceChannel	voice channel for the bot to connect to.
	 * @since					1.0.0
	 */
	public static void joinVC(TextChannel textChannel, VoiceChannel voiceChannel) {

		Guild guild = textChannel.getGuild();
		AudioManager manager = guild.getAudioManager();
		
		if (isJoined)
		{
			// Sends a message to user letting them know it's already connected, and then stays connected to vc
			textChannel.sendMessage("Already connected to voice channel \"" + manager.getConnectedChannel().getName() + "\"").queue();
			return;
		}
		
		try 
		{
			textChannel.sendMessage("Joining the voice channel \"" + voiceChannel.getName() + "\"").queue();
			manager.openAudioConnection(voiceChannel);
			isJoined = true;
		} catch(Exception e) {
			textChannel.sendMessage("Could not join");
		}
	}

	/**
	 * Description:<br>
	 * Responsible for disconnected the bot from a guild's voice channel.
	 * If the bot is not connected to one, let the user know. Otherwise, 
	 * disconnect the bot from the current channel. 
	 * 
	 * @param 	textChannel		text channel to send the messages in.
	 * @since					1.0.0
	 */
	public static void leaveVC(TextChannel textChannel) {
		// Determines if the bot is already not connected to a voice channel
		if (!isJoined)
		{
			// Sends a message to user letting them know it's not connected
			textChannel.sendMessage("I'm not even connected to a voice channel ").queue();
			return;
		}
		
		AudioManager manager = textChannel.getGuild().getAudioManager();
		
		textChannel.sendMessage("Leaving the voice channel \"" + manager.getConnectedChannel().getName() + "\"").queue();
		
		manager.closeAudioConnection();
		
		isJoined = false;
	}
	
	/**
	 * Description:<br>
	 * Responsible for setting a custom activity for the bot's profile
	 * to display under its username. 
	 * 
	 * @param 	activity		string with the custom activity to 
	 * 							display on the profile.
	 * @since					1.0.0
	 */
	public static void setActivity(String activity) {
		jda.getPresence().setActivity(Activity.playing(activity));
	}
	
	/**
	 * Description:<br>
	 * Responsible for retrieving the current date. Useful for noting
	 * when events take place. Used by default for the dev messages.
	 * 
	 * @return	<code>&lt;current-date&gt;</code>
	 * @since	1.0.0
	 */
	public static LocalDate getCurrentDate() {
		return LocalDate.now();
	}
	
	/**
	 * Description:<br>
	 * Responsible for retrieving the current time. Like the current
	 * date, it is also by default used for the dev messages.
	 * 
	 * @return	<code>&lt;current-time&gt;</code>
	 * @since	1.0.0
	 */
	public static LocalTime getCurrentTime() {
		return LocalTime.now();
	}
	
	/**
	 * Description:<br>
	 * Responsible for setting up the proper permissions for the 
	 * newly created silenced role . All the permissions give do 
	 * not allow the user to communicate in any way; they can only 
	 * spectate what others are saying in text/voice channels.
	 * 
	 * @return	<code>permissions</code>
	 * @since	1.0.0
	 */
	private static Collection<Permission> getSilencedPermissions() {
		Collection<Permission> permissions = new ArrayList<>();
		
		permissions.add(Permission.VIEW_CHANNEL);
		permissions.add(Permission.MESSAGE_READ);
		permissions.add(Permission.MESSAGE_HISTORY);
		permissions.add(Permission.VOICE_CONNECT);
		permissions.add(Permission.NICKNAME_CHANGE);
		permissions.add(Permission.NICKNAME_MANAGE);
		
		return permissions;
	}
	
	/**
	 * Description:<br>
	 * Responsible for setting up the proper permissions for the
	 * newly created admin role. The admin role contains every 
	 * permission, with the exception of the "ADMINISTRATOR" 
	 * permission. This allows the owner of the guild to better 
	 * control what channels admins have access to, which adds
	 * an extra safeguard while also allowing members with this 
	 * role to actually moderate the server.
	 * 
	 * @return	<code>permissions</code>
	 * @since	1.0.0
	 */
	private static Collection<Permission> getAdminPermissions() {
		Collection<Permission> permissions = new ArrayList<>();
		
		permissions.add(Permission.CREATE_INSTANT_INVITE);
		permissions.add(Permission.KICK_MEMBERS);
		permissions.add(Permission.BAN_MEMBERS);
		permissions.add(Permission.MANAGE_CHANNEL);
		permissions.add(Permission.MESSAGE_ADD_REACTION);
		permissions.add(Permission.VIEW_AUDIT_LOGS);
		permissions.add(Permission.PRIORITY_SPEAKER);
		permissions.add(Permission.VOICE_STREAM);
		permissions.add(Permission.VIEW_CHANNEL);
		permissions.add(Permission.MESSAGE_READ);
		permissions.add(Permission.MESSAGE_WRITE);
		permissions.add(Permission.MESSAGE_TTS);
		permissions.add(Permission.MESSAGE_EMBED_LINKS);
		permissions.add(Permission.MESSAGE_ATTACH_FILES);
		permissions.add(Permission.MESSAGE_HISTORY);
		permissions.add(Permission.MESSAGE_MENTION_EVERYONE);
		permissions.add(Permission.MESSAGE_EXT_EMOJI);
		permissions.add(Permission.VIEW_GUILD_INSIGHTS);
		permissions.add(Permission.VOICE_CONNECT);
		permissions.add(Permission.VOICE_MUTE_OTHERS);
		permissions.add(Permission.VOICE_DEAF_OTHERS);
		permissions.add(Permission.VOICE_MOVE_OTHERS);
		permissions.add(Permission.VOICE_USE_VAD);
		permissions.add(Permission.NICKNAME_CHANGE);
		permissions.add(Permission.NICKNAME_MANAGE);
		permissions.add(Permission.MANAGE_ROLES);
		permissions.add(Permission.MANAGE_WEBHOOKS);
		permissions.add(Permission.MANAGE_EMOTES);
		permissions.add(Permission.VOICE_SPEAK);
		permissions.add(Permission.MESSAGE_MANAGE);
		permissions.add(Permission.MANAGE_SERVER);
		permissions.add(Permission.USE_SLASH_COMMANDS);
		
		return permissions;
	}
}
