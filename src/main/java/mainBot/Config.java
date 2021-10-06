package mainBot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.json.simple.JSONObject;

/**
 * Description:<br>
 * The Config class is responsible for gathering all the necessary
 * information used for starting and running the bot. This information
 * includes:
 * <li>the bot's token (most important information)
 * <li>the desired command prefix
 * <li>the admin role name
 * <li>the custom activity
 * <li>the bot's name
 * <li>the bot owner's member ID
 * <br>
 * All this information is inputed by the user in the generate 
 * "private.json" file in the json folder. This allows for extra security
 * so the bot's token is not in the actual source code. 
 * When the class's init() method is called, it generates the "private.json"
 * file if it isn't already, and then parses the information in that file. 
 * This parsed information is divided into the proper key/value pairs into 
 * a new private JSON object. Once it is confirmed that the token has been
 * inputed into this new object, then the program's initialization sequence
 * can continue.
 * <p>
 * 
 * Date:		August 24, 2021
 * @author 		Harrison Hoggard
 * @version		1.0.0
 * @since		1.0.0
 */
public class Config {

	private static final String TOKEN = "TOKEN";
	private static final String COMMAND_PREFIX = "COMMAND_PREFIX";
	private static final String ADMIN_ROLE = "ADMIN_ROLE";
	private static final String ACTIVITY = "ACTIVITY";
	private static final String BOT_NAME = "BOT_NAME";
	private static final String OWNER_ID = "OWNER_ID";
	
	// This stores the raw parsed data from the JSON file
	private static JSONObject privateJsonObject;
	
	// This stores each key/value pair taken from the privateJsonObject
	private static JSONObject defaults;
	
	// Boolean variable used to tell if the token has been copied down properly
	private static boolean initialized;
	
	/**
	 * Description:<br>
	 * Returns whether or not the token was properly initialized
	 * 
	 * @return	initialized		<code>true</code> if the token was 
	 * 							initialized;
	 * 							<code>false</code> if the token was
	 * 							not initialized.
	 * @since					1.0.0
	 */
	public static boolean isInitialized() {
		return initialized;
	}
	
	/**
	 * Description:<br>
	 * Responsible for initializing the information taken from the private
	 * JSON file. Initiailly the <code>initialized</code> variable is false
	 * because the token hasn't been taken (duh). After parsing the information
	 * into the <code>privateJsonObject</code> variable, then the data is 
	 * transferred into the <code>defaults</code> JSON object. Once the token
	 * is verified, <code>initialized</code> is set to true.
	 * 
	 * @since	1.0.0
	 */
	@SuppressWarnings("unchecked")
	public static void init() {
		
		initialized = false;
		
		privateJsonObject = initJson();
		
		if (privateJsonObject == null)
		{
			System.out.println("Currently no values in the private.json file");
			
			Bot.shutdown();
		}
		
		defaults = new JSONObject();
		defaults.put(TOKEN, getToken());
		defaults.put(ADMIN_ROLE, getAdminRole());
		defaults.put(COMMAND_PREFIX, getCommandPrefix());
		defaults.put(ACTIVITY, getActivity());
		defaults.put(BOT_NAME, getBotName());
		defaults.put(OWNER_ID, getOwnerId());
		
		if (get("TOKEN") != null) {
			initialized = true;
		}
	}
	
	/**
	 * Description:<br>
	 * Responsible for initializing the "private.json" file. If it does
	 * not exist, then the file is created and the proper keys are 
	 * written inside, lets the user know that the information needs to
	 * be filled out in the file, and then shuts down.
	 * If the file exists, then the information is parsed.
	 * 
	 * @return	<code>parsed-JSON-data</code>
	 * @since	1.0.0
	 */
	private static JSONObject initJson() {
		File tempFile = new File("json/private.json");
		
		if (!(tempFile.exists()))
		{
			File tempDir = new File("json");
			if (!(tempDir.exists()))
			{
				tempDir.mkdirs();
			}
			try {
				tempFile.createNewFile();
				
				System.out.println("Created private.json");
				
				try (Writer out = new FileWriter(tempFile.getCanonicalPath()))
				{
					out.write("{\n\t\"TOKEN\": \"token for the bot\","
							+ "\n\t\"COMMAND_PREFIX\": \"what you first enter to execute commands\","
							+ "\n\t\"ADMIN_ROLE\": \"name of the admin role to manage bot\","
							+ "\n\t\"ACTIVITY\": \"what is displayed under the bot's profile\""
							+ "\n\t\"BOT_NAME\": \"name of the bot\""
							+ "\n\t\"OWNER_ID\": \"Discord ID of the owner\""
							+ "\n}");
					
					out.flush();
					out.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}
		
		return Bot.parseJson("json/private.json");
	}
	
	/**
	 * Description:<br>
	 * Retrieves the token from the <code>privateJsonObject</code>
	 * variable.
	 * 
	 * @return	<code>bot-token</code>
	 * @since	1.0.0
	 */
	private static String getToken() {
		return privateJsonObject.get(TOKEN).toString();
	}
	
	/**
	 * Description:<br>
	 * Retrieves the custom command prefix from the 
	 * <code>privateJsonObject</code> variable.
	 * 
	 * @return	<code>command-prefix</code>
	 * @since	1.0.0
	 */
	private static String getCommandPrefix() {
		return privateJsonObject.get(COMMAND_PREFIX).toString();
	}
	
	/**
	 * Description:<br>
	 * Retrieves the name of the admin role from the 
	 * <code>privateJsonObject</code> variable.
	 * 
	 * @return	<code>admin-role-name</code>
	 * @since	1.0.0
	 */
	private static String getAdminRole() {
		return privateJsonObject.get(ADMIN_ROLE).toString();
	}
	
	/**
	 * Description:<br>
	 * Retrieves the custom activity status from the 
	 * <code>privateJsonObject</code> variable.
	 * 
	 * @return	<code>custom-activity</code>
	 * @since	1.0.0
	 */
	private static String getActivity() {
		return privateJsonObject.get(ACTIVITY).toString();
	}
	
	/**
	 * Description:<br>
	 * Retrieves the bot name from the <code>privateJsonObject</code>
	 * variable.
	 * 
	 * @return	<code>bot-name</code>
	 * @since	1.0.0
	 */
	private static String getBotName() {
		return privateJsonObject.get(BOT_NAME).toString();
	}
	
	/**
	 * Description:<br>
	 * Retrieves the owner's member ID from the <code>privateJsonObject</code>
	 * variable.
	 * 
	 * @return	<code>owner-ID</code>
	 * @since	1.0.0
	 */
	private static String getOwnerId() {
		return privateJsonObject.get(OWNER_ID).toString();
	}
	
	/**
	 * Description:<br>
	 * Retrieves the value of any key stored inside the <code>
	 * defaults</code> variable.
	 * 
	 * @return	<code>key's-value</code>
	 * @since	1.0.0
	 */
	public static String get(String key) {
		return defaults.get(key).toString();
	}
}
