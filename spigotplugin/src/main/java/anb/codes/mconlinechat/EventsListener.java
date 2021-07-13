package anb.codes.mconlinechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventsListener implements Listener {

  private MCOnlineChatPlugin plugin;

  public EventsListener(MCOnlineChatPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    plugin.getLogger().info("Join " + event.getJoinMessage());
    try {
      InputStream inputStream = getClass().getClassLoader().getResourceAsStream("apikey.txt");
      String apikey = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));

      if (apikey != null && !apikey.equals("")) {
        plugin.server.sendGetRequest("https://www.notifymydevice.com/push?ApiKey=" + apikey + "&PushTitle="
            + event.getPlayer().getDisplayName() + "%20joined%20the%20server" + "&PushText=%20");
      }
    } catch (IOException e) {
      plugin.getLogger().warning("Failed to read apikey file");
      plugin.getLogger().warning(e.getMessage());
    }
    plugin.sendMessage(event.getJoinMessage());
  }

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    plugin.getLogger().info("Leave " + event.getQuitMessage());
    plugin.sendMessage(event.getQuitMessage());
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    plugin.sendMessage(event.getDeathMessage());
  }

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent event) {
    String format = event.getFormat();
    String playerName = event.getPlayer().getDisplayName();
    String message = event.getMessage();
    plugin.sendMessage(String.format(format, playerName, message));
  }

}
