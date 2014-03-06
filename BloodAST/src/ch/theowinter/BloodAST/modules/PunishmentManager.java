package ch.theowinter.BloodAST.modules;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ch.theowinter.BloodAST.MainBloodAST;
import ch.theowinter.BloodAST.utilities.SQLEngine;

public class PunishmentManager implements CommandExecutor {
	private MainBloodAST main;
	private SQLEngine sql;

	public PunishmentManager(MainBloodAST mainClass) {
		super();
		main = mainClass;
		if(main.getSQLStatus()){
			try {
				sql = new SQLEngine(null, 0, null, null, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
            		if(warnPlayer(sender.getName(), args[0], args[1])){
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
	
	public boolean warnPlayer(String sender, String playername, String warnMessage){
		boolean success = false;
		Player player = main.getServer().getPlayer(playername);
		if(player != null){
			player.sendMessage(ChatColor.RED+"DISCIPLINARY WARNING: "+warnMessage);
			logWarning(sender, playername, warnMessage);
			success = true;
		}
		return success;
	}
	
	public boolean logWarning(String sender, String playername, String warnMessage){
		boolean success = true;
		if(main.getSQLStatus()){
			try {
				//TODO: make sure injections aren't possible, pre-made statements etc.
				long currentUnixTime = System.currentTimeMillis() / 1000L;
				try {
					sql.insertUpdate("INSERT INTO  '"+sql.getDBName()+"'.'pm_warnings' ("
							+ "'warned' ,'warned_by' ,'warn_reason' ,'warn_time' ,'server')"
							+ "VALUES ('"+playername+"',  '"+sender+"',  '"+warnMessage+"',  '"+currentUnixTime+"',  '"+main.getServerName()+"');");
				} catch (ClassNotFoundException anEx) {
					// TODO Auto-generated catch block
					anEx.printStackTrace();
				}
			} catch (SQLException e) {
				//TODO: add semi-automated error reporting
				main.logEvent("Serious SQL Error - You should try to update the plugin or report this error to the developer!", false);
				success = false;
			}
		}
		main.logEvent(sender+" has warned "+playername+" with: "+warnMessage, true);
		return success;
	}

}
