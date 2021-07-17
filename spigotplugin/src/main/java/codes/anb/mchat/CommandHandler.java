package codes.anb.mchat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import jdk.jshell.execution.Util;

public class CommandHandler implements TabExecutor {
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
      Point p = new Point();
      if (args.length < 1) {
        sender.sendMessage("Usage: /map <type> [name] [x] [z] [dimension]");
        return true;
      }
      int typeID = MChatPlugin.get().pointTypes.getIdFromName(args[0]);
      if (typeID == -1) {
        sender.sendMessage("The type " + args[0] + " does not exist. (Create it with the /addmaptype command)");
        return true;
      }
      p.type = typeID;
      p.name = null;
      if (args.length > 1) {
        p.name = args[1];
      }
      p.x = player.getLocation().getBlockX();
      if (args.length > 2 && !args[2].equals("~")) {
        p.x = (int) Double.parseDouble(args[2]);
      }
      p.z = player.getLocation().getBlockZ();
      if (args.length > 3 && !args[3].equals("~")) {
        p.z = (int) Double.parseDouble(args[3]);
      }

      Environment env = player.getLocation().getWorld().getEnvironment();

      p.dimension = Utils.envToInt(env);

      if (args.length > 3 && !args[3].equals("~")) {
        String dimension = args[3].toLowerCase();
        switch (dimension) {
          case "overworld":
          case "o":
            p.dimension = 0;
            break;
          case "nether":
          case "n":
            p.dimension = 1;
            break;
          case "end":
          case "e":
            p.dimension = 2;
            break;
        }
      }

      MChatPlugin.get().map.add(p);

      sender.sendMessage("Added point to map at " + p.x + ", " + p.z);
      int dimension = p.dimension;
      Point[] points = MChatPlugin.get().map.getPoints(dimension);

      JsonObject obj = new JsonObject();
      obj.addProperty("type", "pointsData");
      obj.addProperty("dimension", dimension);
      obj.add("points", new Gson().toJsonTree(points));
      Logger.get().debug("Sending points " + obj.toString());
      MChatPlugin.get().server.broadcastToAuthenticated(obj.toString());

      return true;
    } else if (cmdName.equals("addmaptype")) {
      if (args.length < 1) {
        sender.sendMessage("Usage: /addmaptype <type>");
        return true;
      }
      if (MChatPlugin.get().pointTypes.add(args[0])) {
        sender.sendMessage("Successfuly added type " + args[0]);
        List<PointType> types = MChatPlugin.get().pointTypes.get();

        JsonObject obj = new JsonObject();
        obj.addProperty("type", "typesData");
        obj.add("types", new Gson().toJsonTree(types));
        Logger.get().debug("Sending types " + obj.toString());
        MChatPlugin.get().server.broadcastToAuthenticated(obj.toString());
      }
      return true;
    } else if (cmdName.equals("hide")) {
      MChatPlugin.get().hiddenPlayers.add(player);
      return true;
    } else if (cmdName.equals("show")) {
      MChatPlugin.get().hiddenPlayers.removeIf(p -> p == player);
      return true;
    }
    return false;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
    String cmdName = cmd.getName().toLowerCase();
    Player player = (Player) sender;
    if (cmdName.equals("map")) {
      if (args.length == 1) {
        List<String> options = MChatPlugin.get().pointTypes.get().stream().map(v -> v.name)
            .collect(Collectors.toList());
        options.removeIf((v) -> !v.startsWith(args[0]));
        return options;
      } else if (args.length == 3) {
        List<String> options = new ArrayList<>();
        options.add(Double.toString(player.getLocation().getBlockX()));
        options.removeIf((v) -> !v.startsWith(args[2]));
        return options;
      } else if (args.length == 4) {
        List<String> options = new ArrayList<>();
        options.add(Double.toString(player.getLocation().getBlockZ()));
        options.removeIf((v) -> !v.startsWith(args[3]));
        return options;
      } else if (args.length == 5) {
        List<String> options = new ArrayList<>();
        options.add("overworld");
        options.add("nether");
        options.add("end");
        options.removeIf((v) -> !v.startsWith(args[4]));
        return options;
      }
    }
    return null;
  }
}
