package ch.theowinter.BloodAST;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ch.theowinter.BloodAST.modules.PunishmentManager;
import ch.theowinter.BloodAST.modules.RewardManager;
import ch.theowinter.BloodAST.modules.StatisticsManager;

public class MainBloodAST extends JavaPlugin{
	//Used so that sub-classes can use methods in here
	MainBloodAST mainClass = this;

	//Global Parameters
	private String serverName;
	private String webServerURL;
	private int schedulerPeriod;
	
	//Global Switches
	boolean debugMode;
	boolean opCanUseAllCommands;
	
		/**
		 * Initializing the plugin and loading all enabled modules.
		 */
	 	@Override
	    public void onEnable(){
			logEvent("Attempting to start ADVANCED SERVER TOOLS provided by bCloud (http://blood-cloud.com)", true);
	 		this.saveDefaultConfig(); //Save config if it isn't there yet
	 		loadMainSettings();
	 		loadCommands();
	 		
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
	    
	    private void loadMainSettings(){
	    	debugMode = MainBloodAST.this.getConfig().getBoolean("LogDebugMessagesToConsole");
	 		serverName = MainBloodAST.this.getConfig().getString("ServerName");
	 		webServerURL = MainBloodAST.this.getConfig().getString("WebServerURL");
	 		schedulerPeriod = MainBloodAST.this.getConfig().getInt("SchedulerPeriod");
	 		opCanUseAllCommands = MainBloodAST.this.getConfig().getBoolean("OPCanUseAllCommands");
	    }
	    
	    private void loadCommands(){
	    	//getCommand("basic").setExecutor(new BloodASTCommandExecutor(this));
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

}
