package anb.codes.mchat;

import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
  MChatPlugin plugin;

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    String cmdName = cmd.getName().toLowerCase();
    Player player = (Player) sender;
    if (cmdName.equals("login")) {
      String code = MChatPlugin.get().loginCodes.makeCode(sender.getName());
      sender.sendMessage("Visit https://mchat.anb.codes/?code=" + code + " to log in or use this login code " + code);
      return true;
    } else if (cmdName.equals("map")) {
      String type = args[0];
      String name = args[1];
      int x = player.getLocation().getBlockX();
      if (args.length > 2 && !args[2].equals("~")) {
        x = Integer.parseInt(args[2]);
      }
      int z = player.getLocation().getBlockZ();
      if (args.length > 3 && !args[3].equals("~")) {
        z = Integer.parseInt(args[3]);
      }

      Environment env = player.getLocation().getWorld().getEnvironment();

      int dimension = env == Environment.NORMAL ? 0 : 1;
      if (args.length > 3 && !args[3].equals("~")) {
        z = Integer.parseInt(args[3]);
      }

      return true;
    }

    return false;
  }
}
