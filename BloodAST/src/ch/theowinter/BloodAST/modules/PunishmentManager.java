package ch.theowinter.BloodAST.modules;

import java.sql.SQLException;
import java.util.ArrayList;

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
	
	public PunishmentManager(MainBloodAST mainClass, SQLEngine sqlEngine) {
		super();
		main = mainClass;
		sql = sqlEngine;
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
			try {
				logWarning(sender, playername, warnMessage, main.getServerName());
			} catch (SQLException anEx) {
				main.logEvent("SQL Exception - Are your tables setup correctly?", true);
				anEx.printStackTrace();
			}
			main.logEvent(sender+" has warned "+playername+" with: "+warnMessage, true);
			success = true;
		}
		return success;
	}
	
	public boolean logWarning(String sender, String playername, String warnMessage, String serverName) throws SQLException{
		boolean success = true;
			long currentUnixTime = System.currentTimeMillis() / 1000L;
			ArrayList<String[]> data = new ArrayList<String[]>();
			data.add(new String[] {"warned",playername});
			data.add(new String[] {"warned_by",sender});
			data.add(new String[] {"warn_reason",warnMessage});
			data.add(new String[] {"warn_time",String.valueOf(currentUnixTime)});
			data.add(new String[] {"server",serverName});
			sql.runPreparedStatement(sql.insertQueryGenerator("pm_warnings", data), data);
		return success;
	}
}
