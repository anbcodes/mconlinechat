package anb.codes.mchat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor {
  MChatPlugin plugin;

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    String cmdName = cmd.getName().toLowerCase();
    if (cmdName.equals("login")) {
      String code = MChatPlugin.get().loginCodes.makeCode(sender.getName());
      sender.sendMessage("Visit https://mchat.anb.codes/?code=" + code + " to log in or use this login code " + code);
      return true;
    }

    return false;
  }
}
