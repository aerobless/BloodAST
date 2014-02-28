package ch.theowinter.BloodAST;

import org.bukkit.plugin.java.JavaPlugin;

public class MainBloodAST extends JavaPlugin{
	
	 @Override
	    public void onEnable(){
		 this.saveDefaultConfig();
		 getLogger().info("BloodAST successfully started");
	    }
	 
	    @Override
	    public void onDisable() {
		 	getLogger().info("BloodAST successfully exited");
	    }
	    
}
