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
  private MChatPlugin plugin = null;
  private Gson gson = new Gson();
  public Set<WebSocket> clients = new CopyOnWriteArraySet<>();
  public Set<WebSocket> authenticatedClients = new CopyOnWriteArraySet<>();

  public Server(MChatPlugin plugin, int port) {
    super(new InetSocketAddress(port));
    this.plugin = plugin;
  }

  public Server(MChatPlugin plugin, InetSocketAddress address) {
    super(address);
    this.plugin = plugin;
  }

  public Server(MChatPlugin plugin, int port, Draft_6455 draft) {
    super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
    this.plugin = plugin;
  }

  @Override
  public void onStart() {
    plugin.getLogger().info("Started server");
  }

  @Override
  public void onOpen(WebSocket socket, ClientHandshake handshake) {
    clients.add(socket);
  }

  @Override
  public void onMessage(WebSocket socket, String messageEnc) {
    JsonObject message = gson.fromJson(messageEnc, JsonObject.class);
    String username = null;
    if (message.get("authID") != null) {
      username = plugin.users.getNameFromID(message.get("authID").getAsString());
    }
    String type = message.get("type").getAsString();
    plugin.getLogger().info("Got " + type + " message");
    if (type.equals("SEND") && username != null && plugin != null) {
      String toSend = "[" + username + "] " + message.get("message").getAsString();
      plugin.getServer().broadcastMessage(toSend);
      plugin.sendMessage(toSend);
    } else if (type.equals("LOGIN")) {
      plugin.getLogger().info("Processing login");
      String code = message.get("code").getAsString();
      handleLogin(socket, code);
    } else if (type.equals("GET_HISTORY") && username != null) {
      int page = 1;
      if (message.get("page") != null) {
        page = message.get("page").getAsInt();
      }
      handleGetHistory(socket, page);
    } else if (type.equals("AUTH")) {
      if (username != null) {
        authenticatedClients.add(socket);
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "AUTH_SUCCESS");
        socket.send(obj.toString());
      } else {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "AUTH_FAILED");
        socket.send(obj.toString());
      }
    } else if (type.equals("GET_POINTS") && username != null) {
      List<Point> points;
      int dim = message.get("dim").getAsInt();
      if (message.get("bounds") != null) {
        Bounds bounds = gson.fromJson(message.get("bounds").getAsJsonObject(), Bounds.class);
        points = plugin.database.getPoints(bounds, dim);
      } else {
        points = plugin.database.getPoints(dim);
      }

      JsonObject obj = new JsonObject();
      obj.addProperty("type", "POINTS");
      obj.add("points", gson.toJsonTree(points));
      socket.send(obj.toString());
    }
  }

  @Override
  public void onClose(WebSocket socket, int value, String s, boolean bool) {
    clients.remove(socket);
  }

  @Override
  public void onError(WebSocket socket, Exception exception) {
    plugin.getLogger().warning(exception.getStackTrace().toString());
  }

  public void broadcastToAuthenticated(String message) {
    for (WebSocket client : clients) {
      if (authenticatedClients.contains(client)) {
        client.send(message);
      }
    }
  }

  public void broadcastMessageToAuthenticated(String message) {
    for (WebSocket client : clients) {
      if (authenticatedClients.contains(client)) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "NEW_MESSAGE");
        obj.addProperty("message", message);
        client.send(obj.toString());
      }
    }
  }

  static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  public void sendGetRequest(String reqUrl) throws IOException {
    GenericUrl url = new GenericUrl(reqUrl);
    HttpRequest request = HTTP_TRANSPORT.createRequestFactory().buildGetRequest(url);
    HttpResponse response = request.execute();
    response.disconnect();
  }

  private void handleGetHistory(WebSocket socket, int page) {
    ArrayList<String> lines = plugin.history.getLast(100, 100 * (page - 1));
    lines.add(String.valueOf(page));
    JsonObject msg = new JsonObject();
    msg.add("items", (new Gson()).toJsonTree(lines));
    msg.addProperty("type", "HISTORY_DATA");
    msg.addProperty("page", page);
    socket.send(msg.toString());
  }

  private void handleLogin(WebSocket socket, String code) {
    String authID = plugin.users.loginWithCode(code);
    if (authID != null) {
      JsonObject returnMessage = new JsonObject();
      returnMessage.addProperty("type", "LOGIN_SUCCESS");
      returnMessage.addProperty("authID", authID);
      authenticatedClients.add(socket);
      socket.send(returnMessage.toString());
    } else {
      JsonObject returnMessage = new JsonObject();
      returnMessage.addProperty("type", "LOGIN_FAILED");
      socket.send(returnMessage.toString());
    }
  }

}
