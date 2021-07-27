package codes.anb.mchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventsListener implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Logger.get().debug("Join " + event.getJoinMessage());
    try {
      InputStream inputStream = getClass().getClassLoader().getResourceAsStream("apikey.txt");
      String apikey = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));

      if (apikey != null && !apikey.equals("")) {
        sendGetRequest("https://www.notifymydevice.com/push?ApiKey=" + apikey + "&PushTitle="
            + event.getPlayer().getDisplayName() + "%20joined%20the%20server" + "&PushText=%20");
      }
    } catch (IOException e) {
      Logger.get().error("Failed to read apikey file", e);
    }
    MChatPlugin.get().broadcastMessage(new ChatMessage(event.getJoinMessage(), ""));
  }

  static final HttpClient client = HttpClient.newBuilder().build();

  private void sendGetRequest(String reqUrl) throws IOException {
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(reqUrl)).timeout(Duration.ofMinutes(2)).GET().build();
    client.sendAsync(request, BodyHandlers.ofString());
  }

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    Logger.get().debug("Leave " + event.getQuitMessage());
    MChatPlugin.get().broadcastMessage(new ChatMessage(event.getQuitMessage(), ""));

    try {
      InputStream inputStream = getClass().getClassLoader().getResourceAsStream("apikey.txt");
      String apikey = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));

      if (apikey != null && !apikey.equals("")) {
        sendGetRequest("https://www.notifymydevice.com/push?ApiKey=" + apikey + "&PushTitle="
            + event.getPlayer().getDisplayName() + "%20left%20the%20server" + "&PushText=%20");
      }
    } catch (IOException e) {
      Logger.get().error("Failed to read apikey file", e);
    }
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    Logger.get().debug("Death " + event.getDeathMessage());
    MChatPlugin.get().broadcastMessage(new ChatMessage(event.getDeathMessage(), ""));
  }

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent event) {
    String format = event.getFormat();
    String playerName = event.getPlayer().getDisplayName();
    String message = event.getMessage();
    MChatPlugin.get().broadcastMessage(new ChatMessage(message, playerName));
    Logger.get().debug("Chat " + String.format(format, playerName, message));
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    JsonObject obj = new JsonObject();
    if (MChatPlugin.get().hiddenPlayers.indexOf(event.getPlayer()) == -1) {
      obj.addProperty("type", "playerMove");
      obj.addProperty("username", event.getPlayer().getDisplayName());
      obj.addProperty("x", event.getTo().getX());
      obj.addProperty("y", event.getTo().getY());
      obj.addProperty("z", event.getTo().getZ());
      obj.addProperty("dimension", Utils.envToInt(event.getTo().getWorld().getEnvironment()));
      MChatPlugin.get().server.broadcastToAuthenticated(obj.toString());
    }

  }

}
