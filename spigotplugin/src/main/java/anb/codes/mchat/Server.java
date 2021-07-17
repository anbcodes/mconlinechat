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

    String messageType = getMessageType(message);
    Logger.get().debug("Message type " + messageType);

    if (messageType.equals("auth")) {
      handleAuth(socket, message);
    } else if (messageType.equals("sendMessage")) {
      handleSendMessage(message);
    } else if (messageType.equals("login")) {
      handleLogin(socket, message);
    } else if (messageType.equals("requestHistory")) {
      handleRequestHistory(socket, message);
    } else if (messageType.equals("requestPoints")) {
      handleRequestPoints(socket, message);
    }
  }

  private String getMessageType(JsonObject message) {
    if (message.get("type") == null) {
      return null;
    }
    return message.get("type").getAsString();
  }

  private String getUsername(JsonObject message) {
    if (message.get("authID") != null) {
      return MChatPlugin.get().users.getFromToken(message.get("authID").getAsString());
    }
    return null;
  }

  private void handleAuth(WebSocket socket, JsonObject message) {
    String username = getUsername(message);

    if (username != null) {
      JsonObject obj = new JsonObject();
      obj.addProperty("type", "authSuccess");
      obj.addProperty("authID", message.get("authID").getAsString());
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

  private void handleSendMessage(JsonObject message) {
    String username = getUsername(message);

    if (username == null || message.get("message") == null || message.get("message").getAsString().equals("")) {
      return;
    }
    ChatMessage msg = new ChatMessage(message.get("message").getAsString(), username);
    msg.fromWebsite = true;
    MChatPlugin.get().broadcastMessage(msg);
  }

  private void handleLogin(WebSocket socket, JsonObject message) {
    if (message.get("code") == null || message.get("code").getAsString().equals("")) {
      return;
    }
    String code = message.get("code").getAsString();
    String authID = MChatPlugin.get().loginCodes.login(code);
    if (authID != null) {
      JsonObject obj = new JsonObject();
      obj.addProperty("type", "authSuccess");
      obj.addProperty("authID", authID);
      Logger.get().debug("Sending response " + obj.toString());
      socket.send(obj.toString());
    } else {
      JsonObject obj = new JsonObject();
      obj.addProperty("type", "authFailed");
      Logger.get().debug("Sending response " + obj.toString());
      socket.send(obj.toString());
    }
  }

  private void handleRequestHistory(WebSocket socket, JsonObject message) {
    String username = getUsername(message);
    if (message.get("page") == null || message.get("offset") == null || username == null) {
      return;
    }

    int page = message.get("page").getAsInt();
    int offset = message.get("offset").getAsInt();
    ChatMessage[] history = MChatPlugin.get().history.getPage(page, offset);

    JsonObject obj = new JsonObject();
    obj.addProperty("type", "historyData");
    obj.addProperty("page", page);
    obj.addProperty("offset", offset);
    obj.add("items", gson.toJsonTree(history));
    socket.send(obj.toString());
  }

  private void handleRequestPoints(WebSocket socket, JsonObject message) {
    String username = getUsername(message);
    if (message.get("dimension") == null || username == null) {
      return;
    }

    int dimension = message.get("dimension").getAsInt();
    Point[] points = MChatPlugin.get().map.getPoints(dimension);

    JsonObject obj = new JsonObject();
    obj.addProperty("type", "pointsData");
    obj.addProperty("dimension", dimension);
    obj.add("points", gson.toJsonTree(points));
    socket.send(obj.toString());
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

  public void broadcastChatMessageToAuthenticated(ChatMessage message) {
    Logger.get().debug("Broadcasting chat message " + message);
    for (WebSocket client : clients) {
      if (authenticatedClients.contains(client)) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "chatMessage");
        obj.add("message", gson.toJsonTree(message));
        client.send(obj.toString());
      }
    }
  }

}
