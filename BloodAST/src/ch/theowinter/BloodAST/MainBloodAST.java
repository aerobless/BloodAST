package ch.theowinter.BloodAST;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import ch.theowinter.BloodAST.modules.PunishmentManager;
import ch.theowinter.BloodAST.modules.RewardManager;
import ch.theowinter.BloodAST.modules.StatisticsManager;
import ch.theowinter.BloodAST.utilities.LogicEngine;

public class MainBloodAST extends JavaPlugin{
	//Used so that sub-classes can use methods in here
	MainBloodAST mainClass = this;
	//Used to access logic methods
	LogicEngine logic = new LogicEngine(); 

	//Global Parameters
	private String serverName;
	private String webServerURL;
	private int schedulerPeriod;
	
	//Global Switches
	boolean debugMode;
	boolean opCanUseAllCommands;
	boolean enableSQL;
	
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
			if (MainBloodAST.this.getConfig().getBoolean("EnablePunishmentModule")){
				//Import the commands for the punishment module.
		    	getCommand("warn").setExecutor(new PunishmentManager(this));
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
	    	//TODO: init basic SQL channel
	    	//TODO: check for existing tables OR create new tables
	    	//TODO: display warning for not using SQL
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

}
