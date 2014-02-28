package ch.theowinter.BloodAST;

import org.bukkit.plugin.java.JavaPlugin;

public class MainBloodAST extends JavaPlugin{

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
			 
			}
	    }
	 
	    @Override
	    public void onDisable() {
	    	logEvent("BloodAST successfully exited", true);
	    } 
	    
	    public void loadMainSettings(){
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
			 	getLogger().info(input);
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
