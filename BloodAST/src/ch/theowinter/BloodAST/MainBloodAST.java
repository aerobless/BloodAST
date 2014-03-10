package ch.theowinter.BloodAST;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import ch.theowinter.BloodAST.modules.PunishmentManager;
import ch.theowinter.BloodAST.modules.RewardManager;
import ch.theowinter.BloodAST.modules.StatisticsManager;
import ch.theowinter.BloodAST.utilities.LogicEngine;
import ch.theowinter.BloodAST.utilities.SQLEngine;

public class MainBloodAST extends JavaPlugin{
	//Used so that sub-classes can use methods in here
	MainBloodAST mainClass = this;
	//Used to access logic methods
	LogicEngine logic = new LogicEngine(); 

	//Parameters
	private String serverName;
	private String webServerURL;
	private int schedulerPeriod;
	
	//SQL Settings
	private String host;
	private int port;
	private String database;
	private String username;
	private String password;
	
	//Switches
	private boolean debugMode;
	private boolean opCanUseAllCommands;
	private boolean enableSQL;
	
		/**
		 * Initializing the plugin and loading all enabled modules.
		 */
	 	@Override
	    public void onEnable(){
			logEvent("Attempting to start ADVANCED SERVER TOOLS provided by bCloud (http://blood-cloud.com)", true);
	 		this.saveDefaultConfig(); //Save config if it isn't there yet
	 		loadParamatersFromConfiguration();
	 		loadSQLConfiguration();
	 		
	 		//Enabling specific modules
			if (MainBloodAST.this.getConfig().getBoolean("EnableStatisticsModule")){
				StatisticsManager statManager = new StatisticsManager(mainClass);
				statManager.StatsTracker();
				logEvent("Statistics-Module successfully loaded.", false);
			}
			if (MainBloodAST.this.getConfig().getBoolean("EnableRewardModule")){
				RewardManager rewardManager = new RewardManager(mainClass);
				rewardManager.scheduleNextReward();
				logEvent("Reward-Module successfully loaded.", false);
			}
			if (MainBloodAST.this.getConfig().getBoolean("EnablePunishmentModule") && enableSQL==true){
				//Import the commands for the punishment module.
		    	try {
					getCommand("warn").setExecutor(new PunishmentManager(this, sqlEngineFactory()));
				} catch (Exception anEx) {
					// TODO Auto-generated catch block
					anEx.printStackTrace();
				}
			}
			
			//Saving the config after we've loaded everything to get the newst version down into the file.. it's a test.
			this.saveConfig();
	    }
	 	
	    @Override
	    public void onDisable() {
	    	logEvent("BloodAST successfully exited", true);
	    } 
	    
	    private void loadParamatersFromConfiguration(){
	    	debugMode = MainBloodAST.this.getConfig().getBoolean("LogDebugMessagesToConsole");
	 		serverName = MainBloodAST.this.getConfig().getString("ServerName");
	 		webServerURL = MainBloodAST.this.getConfig().getString("WebServerURL");
	 		schedulerPeriod = MainBloodAST.this.getConfig().getInt("SchedulerPeriod");
	 		opCanUseAllCommands = MainBloodAST.this.getConfig().getBoolean("OPCanUseAllCommands");
	 		enableSQL = MainBloodAST.this.getConfig().getBoolean("EnableSQL");
	    }
	    
	    private void loadSQLConfiguration(){
	    	host = MainBloodAST.this.getConfig().getString("Host");
	    	port = MainBloodAST.this.getConfig().getInt("Port");
	    	database = MainBloodAST.this.getConfig().getString("Database");
	    	username = MainBloodAST.this.getConfig().getString("Username");
	    	password = MainBloodAST.this.getConfig().getString("Password");
	    	
	    	if(enableSQL){
		    	//TODO: check for existing tables OR create new tables
	    	}
	    	else{
	    		logEvent("SQL IS NOT ENABLED - YOU WILL NOT BE ABLE TO USE ALL FEATURES", true);
	    	}
	    }
	    
	    /**
	     * Concatenates an array from startPosition to the end of the array.
	     * Useful when dealing with commands that have an open ended text
	     * input, such as "/warn playername reason".
	     * 
	     * @param args
	     * @param arrayLength
	     * @return concatenatedArray
	     */    
	    public String[] concatenateArgs(String[] args, int arrayLength){
			return logic.concatenateArgs(args, arrayLength);
	    }
	    
	    /**
	     * Check whether the player has the required permissions and make sure that he provided
	     * sufficient arguments when entering the command. That way we can make sure that
	     * there is no array out of bound.
	     *  
	     * @param sender
	     * @param permissionNode
	     * @param args
	     * @param expectedNumberOfArgs
	     * @return success
	     */
	    public boolean permAndArgsCheck(CommandSender sender, String permissionNode, String[] args, int expectedNumberOfArgs){
	    	boolean success = false;
	    	if(permissionsCheck(sender, permissionNode)){
		    	if(args.length != expectedNumberOfArgs){
		    		sender.sendMessage(ChatColor.RED + "This command is expecting "+expectedNumberOfArgs+" arguments, but you sent "+args.length);
		    		sender.sendMessage(ChatColor.RED + "Try again or ask the server-administrator for help.");
		    	}
		    	else{
		    		success = true;
		    	}
	    	}
			return success;
	    }
	    
	    /**
	     * Checks whether the player has the required permission or if he is op.
	     * Depending on the config.yml checking "op"-status can be turned off.
	     * 
	     * @param sender
	     * @param permissionNode
	     * @return playerHasRequiredPermission
	     */
	    public boolean permissionsCheck(CommandSender sender, String permissionNode){
	    	boolean playerHasRequiredPermission = false;
	    	playerHasRequiredPermission = sender.hasPermission(permissionNode);
	    	
	    	if (opCanUseAllCommands==true){
	    		if (sender.isOp()){
	    			playerHasRequiredPermission = true;
	    		}
	    	}
	    	if(playerHasRequiredPermission == false){
	    		sender.sendMessage(ChatColor.RED + "Sorry you don't have the required permission.");
	    	}
			return playerHasRequiredPermission;
	    }
	    
	    /**
	     * Display debug logs in the server console. If force log is true the message
	     * will be displayed even if debugging is turned off.
	     * 
	     * @param input
	     * @param forceLog
	     */
	    public void logEvent(String input, boolean forceLog){
	    	if (debugMode==true || forceLog==true){
			 	getLogger().info("BloodAST: "+input);
	    	}
	    }

		public String getServerName() {
			return serverName;
		}
		public String getWebServerURL() {
			return webServerURL;
		}

		public int getSchedulerPeriod() {
			return schedulerPeriod;
		}
		
		public boolean getSQLStatus() {
			return enableSQL;
		}
		
		public SQLEngine sqlEngineFactory() throws Exception{
			SQLEngine sql = new SQLEngine(host, port, database, username, password);
			return sql;
		}
}
