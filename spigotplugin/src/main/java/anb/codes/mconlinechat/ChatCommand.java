package anb.codes.mcbot.spigotplugin;

import java.util.Random;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ChatCommand implements CommandExecutor {
    MCOnlineChatPlugin plugin;
    static Random randNum = new Random();

    public ChatCommand(MCOnlineChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String cmdName = cmd.getName().toLowerCase();
        String joinedArgs = String.join(" ", args);
        if (cmdName.equals("shout")) {
            String msg = sender.getName() + " shouted " + joinedArgs;

            plugin.sendMessage(msg);
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),
                    "tellraw @a {\"text\":\"" + msg + "\",\"bold\":true}");

            return true;
        } else if (cmdName.equals("login")) {
            String code = plugin.users.getNewLoginCodeForUser(sender.getName());
            sender.sendMessage("Your login code is " + code);
            return true;
        }
        return false;

    }
}
