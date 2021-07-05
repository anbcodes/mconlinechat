package anb.codes.mconlinechat;

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
