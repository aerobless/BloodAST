package ch.theowinter.BloodAST.modules;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import ch.theowinter.BloodAST.MainBloodAST;

public class RewardManager {
	private MainBloodAST main;
	
	//Loaded Parameters
	String rewardText = "";
	int rewardPeriod = 0;
	
	//Temporary Parameters
	HashMap<Player, Integer> onlinePlayers = new HashMap<Player, Integer>();
	
	public RewardManager(MainBloodAST mainClass) {
		super();
		main = mainClass;
	}
	
    public void scheduleNextReward(){
    	main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            @Override
            public void run() {
               	Player[] currentlyOnlinePlayerArray = Bukkit.getServer().getOnlinePlayers();
            	updateAndRewardOnlinePlayers(currentlyOnlinePlayerArray);
            	scheduleNextReward();
            }
        }, rewardPeriod*60*20L  ); //20L = 1second
    }
	
   private void updateAndRewardOnlinePlayers(Player[] currentlyOnlinePlayerArray){
    	for (int i=0; i<currentlyOnlinePlayerArray.length; i++){
    		Integer alreadyLoggedTime = onlinePlayers.get(currentlyOnlinePlayerArray[i]);
    		if (alreadyLoggedTime != null){
    			if (alreadyLoggedTime.intValue()>rewardPeriod){
    				giveReward(currentlyOnlinePlayerArray[i]);
    				alreadyLoggedTime = 1;
    			}
		    	onlinePlayers.put(currentlyOnlinePlayerArray[i], (new Integer(alreadyLoggedTime.intValue()+4)));
    		}
    		else {
		    	onlinePlayers.put(currentlyOnlinePlayerArray[i], Integer.valueOf(1));
    		}
    	}
    }
    
    private void giveReward(Player player){
		Location location = player.getLocation();
		player.getWorld().playSound(location,Sound.LEVEL_UP,1, 0);
		
		player.sendMessage(rewardText);
		Bukkit.getServer().dispatchCommand(main.getServer().getConsoleSender(), "money give "+player.getName()+" 50");
		main.logEvent("BloodStats rewarded: "+player.getName()+" after "+rewardPeriod+"min", false);
    }
}
