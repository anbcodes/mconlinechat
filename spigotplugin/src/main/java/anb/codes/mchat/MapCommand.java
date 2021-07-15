package anb.codes.mchat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class MapCommand implements CommandExecutor, TabCompleter {
  MChatPlugin plugin;
  static Random randNum = new Random();

  public MapCommand(MChatPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    String cmdName = cmd.getName().toLowerCase();
    String joinedArgs = String.join(" ", args);
    if (cmdName.equals("map")) {
      sender.sendMessage("Work in progress");
    }
    return false;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
    String cmdName = cmd.getName().toLowerCase();
    if (cmdName.equals("map") && sender instanceof Player) {
      List<String> list = new ArrayList<>();
      list.add("item 1");
      list.add("item 2");
      list.add("item 3");
      return list;
    }
    return null;
  }
}
