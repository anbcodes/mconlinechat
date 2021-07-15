package anb.codes.mchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class Server extends WebSocketServer {
  private Gson gson = new Gson();
  public Set<WebSocket> clients = new CopyOnWriteArraySet<>();
  public Set<WebSocket> authenticatedClients = new CopyOnWriteArraySet<>();

  public Server(int port) {
    super(new InetSocketAddress(port));
  }

  public Server(InetSocketAddress address) {
    super(address);
  }

  public Server(int port, Draft_6455 draft) {
    super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
  }

  @Override
  public void onStart() {
    Logger.get().debug("Started server");
  }

  @Override
  public void onOpen(WebSocket socket, ClientHandshake handshake) {
    clients.add(socket);
    Logger.get().debug("New connection");
  }

  @Override
  public void onMessage(WebSocket socket, String messageEnc) {
    JsonObject message = gson.fromJson(messageEnc, JsonObject.class);
    Logger.get().debug("Got message " + message.toString());
    if (message.get("type") == null) {
      return;
    }
    String messageType = message.get("type").getAsString();

    String username = null;

    if (message.get("authID") != null) {
      username = MChatPlugin.get().users.getFromToken(message.get("authID").getAsString());
    }

    Logger.get().debug("Message type " + messageType + " username " + username);

    if (messageType.equals("auth")) {
      if (username != null) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "authSuccess");
        Logger.get().debug("Sending response " + obj.toString());
        socket.send(obj.toString());
        authenticatedClients.add(socket);
      } else {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "authFailed");
        Logger.get().debug("Sending response " + obj.toString());
        socket.send(obj.toString());
      }
    }

    if (messageType.equals("sendMessage") && username != null && message.get("message") != null
        && !message.get("message").getAsString().equals("")) {
      MChatPlugin.get().broadcastMessage("[" + username + "] " + message.get("message").getAsString());
    }

  }

  @Override
  public void onClose(WebSocket socket, int value, String s, boolean bool) {
    Logger.get().debug("Socket closed");
    clients.remove(socket);
  }

  @Override
  public void onError(WebSocket socket, Exception exception) {
    Logger.get().error("Websocket error", exception);
  }

  public void broadcastToAuthenticated(String message) {
    Logger.get().debug("Broadcasting message " + message);
    for (WebSocket client : clients) {
      if (authenticatedClients.contains(client)) {
        client.send(message);
      }
    }
  }

  public void broadcastChatMessageToAuthenticated(String message) {
    Logger.get().debug("Broadcasting chat message " + message);
    for (WebSocket client : clients) {
      if (authenticatedClients.contains(client)) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "chatMessage");
        obj.addProperty("message", message);
        client.send(obj.toString());
      }
    }
  }

}
