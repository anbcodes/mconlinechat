package anb.codes.mchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
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
    // try {
    // InputStream inputStream =
    // getClass().getClassLoader().getResourceAsStream("apikey.txt");
    // String apikey = new BufferedReader(new
    // InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));

    // if (apikey != null && !apikey.equals("")) {
    // sendGetRequest("https://www.notifymydevice.com/push?ApiKey=" + apikey +
    // "&PushTitle="
    // + event.getPlayer().getDisplayName() + "%20joined%20the%20server" +
    // "&PushText=%20");
    // }
    // } catch (IOException e) {
    // Logger.get().error("Failed to read apikey file", e);
    // }
    MChatPlugin.get().broadcastMessage(new ChatMessage(event.getJoinMessage(), ""));
  }

  static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  private void sendGetRequest(String reqUrl) throws IOException {
    GenericUrl url = new GenericUrl(reqUrl);
    HttpRequest request = HTTP_TRANSPORT.createRequestFactory().buildGetRequest(url);
    HttpResponse response = request.execute();
    response.disconnect();
  }

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    Logger.get().debug("Leave " + event.getQuitMessage());
    MChatPlugin.get().broadcastMessage(new ChatMessage(event.getQuitMessage(), ""));
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
    obj.addProperty("type", "PLAYER_MOVE");
    obj.addProperty("username", event.getPlayer().getDisplayName());
    obj.addProperty("x", event.getTo().getX());
    obj.addProperty("y", event.getTo().getY());
    obj.addProperty("z", event.getTo().getZ());
    MChatPlugin.get().server.broadcastToAuthenticated(obj.toString());
  }

}
