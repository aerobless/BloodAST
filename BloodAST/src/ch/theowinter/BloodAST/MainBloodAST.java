package ch.theowinter.BloodAST;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import ch.theowinter.BloodAST.modules.RewardManager;
import ch.theowinter.BloodAST.modules.StatisticsManager;

public class MainBloodAST extends JavaPlugin{
	//test auto complile on jenkins
	//Used so that sub-classes can use methods in here
	MainBloodAST mainClass = this;

	//Global Parameters
	private String serverName;
	private String webServerURL;
	private int schedulerPeriod;
	boolean debugMode;
	
		/**
		 * Initializing the plugin and loading all enabled modules.
		 */
	 	@Override
	    public void onEnable(){
			logEvent("Attempting to start ADVANCED SERVER TOOLS provided by bCloud (http://blood-cloud.com)", true);
	 		this.saveDefaultConfig();
	 		loadMainSettings();
	 		
	 		//Enabling specific modules:
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
	    }
	 
	    @Override
	    public void onDisable() {
	    	logEvent("BloodAST successfully exited", true);
	    } 
	    
	    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
	    	if(cmd.getName().equalsIgnoreCase("basic")){ // If the player typed /basic then do the following...
	    		sender.sendMessage("hihihi");
	    		return true;
	    	}
	    	
	    	return false; 
	    }
	    
	    public void loadMainSettings(){
	    	debugMode = MainBloodAST.this.getConfig().getBoolean("LogDebugMessagesToConsole");
	 		serverName = MainBloodAST.this.getConfig().getString("ServerName");
	 		webServerURL = MainBloodAST.this.getConfig().getString("WebServerURL");
	 		schedulerPeriod = MainBloodAST.this.getConfig().getInt("SchedulerPeriod");
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
