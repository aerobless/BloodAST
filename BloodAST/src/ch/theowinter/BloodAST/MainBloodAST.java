package ch.theowinter.BloodAST;

import org.bukkit.plugin.java.JavaPlugin;

public class MainBloodAST extends JavaPlugin{

	//Global Parameters
	String serverName;
	String serverURL;
	boolean debugMode;
	
		/**
		 * Initializing the plugin and loading all enabled modules.
		 */
	 	@Override
	    public void onEnable(){
	 		
	 		
		 this.saveDefaultConfig();
		 logEvent("Attempting to start ADVANCED SERVER TOOLS provided by bCloud (http://blood-cloud.com)", true);
		 if (MainBloodAST.this.getConfig().getBoolean("EnableStatisticsModule")){
			 
		 }
	    }
	 
	    @Override
	    public void onDisable() {
	    	logEvent("BloodAST successfully exited", true);
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
}
