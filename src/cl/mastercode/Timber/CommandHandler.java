package cl.mastercode.Timber;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class CommandHandler implements CommandExecutor {

	String noPermission= ChatColor.DARK_RED + "You don't have permission to do this!";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] strings) {
        if(strings.length>0){
	        if ("reload".equalsIgnoreCase(strings[0])) {
	            if (CheckPermissions(sender,"damageindicator.admin")) {
	            	Main.splugin.reload();
		            sender.sendMessage(ChatColor.GREEN + "Reloaded config!");
		            return true;
	            }
	        }
        }else{
        	String version = Bukkit.getServer().getPluginManager().getPlugin("Timber").getDescription().getVersion();
        	sender.sendMessage(ChatColor.DARK_AQUA+"<===== Timber! "+version+" =====>");
        	sender.sendMessage(ChatColor.DARK_AQUA+"/timber reload");
        }
        return true;
	}
	public static boolean IsPlayer(CommandSender sender){
        if (sender instanceof Player) {
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Only a Player can use this command");
		return false;
	}
	public static boolean CheckPermissions(CommandSender sender,String permission){
        if (sender.hasPermission(permission)) {
            return true;
        }
        sender.sendMessage(ChatColor.RED + "You don't have permission to use this command this!");
		return false;
	}
	public static boolean CheckArguments(CommandSender sender,int args ,int num){
		if(args!=num){
			sender.sendMessage(ChatColor.RED+"Invalid number of arguments");
			return false;
		}
		return true;
	}
	public static int GetInt(CommandSender sender, String text){
		int amount = -1;
		try{
			amount = Integer.parseInt(text);
			return amount;
		}catch(NumberFormatException e){
			sender.sendMessage(ChatColor.RED+"Invalid given amount");
			return -1;
		}
	}
	
	
}
