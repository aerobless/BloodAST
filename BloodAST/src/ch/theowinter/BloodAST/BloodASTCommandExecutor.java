package ch.theowinter.BloodAST;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BloodASTCommandExecutor implements CommandExecutor {
	private MainBloodAST main;

	public BloodASTCommandExecutor(MainBloodAST mainClass) {
		super();
		main = mainClass;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if(cmd.getName().equalsIgnoreCase("bCloud")){ // If the player typed /basic then do the following...
    		sender.sendMessage("hihihi");
    		return true;
    	}
		return false;
	}
}
