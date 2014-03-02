package ch.theowinter.BloodAST.modules;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ch.theowinter.BloodAST.MainBloodAST;

public class PunishmentManager implements CommandExecutor {
	private MainBloodAST main;

	public PunishmentManager(MainBloodAST mainClass) {
		super();
		main = mainClass;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if(cmd.getName().equalsIgnoreCase("warn")){
    		String[] goodArgs = main.concatenateArgs(args, 2);
    		if (main.permAndArgsCheck(sender, "blood.punishment.warn", goodArgs, 2)){
    		   	if(args[0].equals(sender.getName())){
            		sender.sendMessage("Sorry.. but you can't warn yourself dude.");
        		}
        		else{
            		if(warnPlayer(args[0], args[1])){
            			sender.sendMessage("Successfully warned "+args[0]);
            		}
            		else{
            			sender.sendMessage("Unable to warn "+args[0]+". Are you sure you spelled his name right?");
            		}
        		}
    		}
    	}
		return true;
	}
	
	public boolean warnPlayer(String playername, String warnMessage){
		boolean success = false;
		Player player = main.getServer().getPlayer(playername);
		if(player != null){
			player.sendMessage(ChatColor.RED+"DISCIPLINARY WARNING: "+warnMessage);
			success = true;
		}
		return success;
	}

}
