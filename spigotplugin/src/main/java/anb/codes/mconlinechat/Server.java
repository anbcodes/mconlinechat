package anb.codes.mconlinechat;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class Server extends WebSocketServer {
  private MCOnlineChatPlugin plugin = null;
  public Set<WebSocket> clients = new CopyOnWriteArraySet<>();
  public Set<WebSocket> authenticatedClients = new CopyOnWriteArraySet<>();

  public Server(MCOnlineChatPlugin plugin, int port) {
    super(new InetSocketAddress(port));
    this.plugin = plugin;
  }

  public Server(MCOnlineChatPlugin plugin, InetSocketAddress address) {
    super(address);
    this.plugin = plugin;
  }

  public Server(MCOnlineChatPlugin plugin, int port, Draft_6455 draft) {
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
    Message message = Message.fromJSON(messageEnc);
    String username = plugin.users.getNameFromID(message.authID);
    if (message.type.equals("SEND") && username != null && plugin != null) {
      String toSend = "[" + username + "] " + message.data[0];
      plugin.getServer().broadcastMessage(toSend);
      plugin.sendMessage(toSend);
    } else if (message.type.equals("LOGIN")) {
      plugin.getLogger().info("Processing login");
      String code = message.data[0];
      String authID = plugin.users.loginWithCode(code);
      if (authID != null) {
        Message msg = new Message("LOGIN_SUCCESS", authID);
        authenticatedClients.add(socket);
        socket.send(msg.toJSON());
      } else {
        socket.send(new Message("LOGIN_FAILED").toJSON());
      }
    } else if (message.type.equals("GET_HISTORY") && username != null) {
      int page = Integer.parseInt(message.data[0]);
      ArrayList<String> lines = plugin.history.getLast(100, 100 * (page - 1));
      lines.add(String.valueOf(page));
      Message msg = new Message("HISTORY_DATA", lines.toArray(new String[0]));
      socket.send(msg.toJSON());
    } else if (message.type.equals("AUTH")) {
      if (username != null) {
        authenticatedClients.add(socket);
      }
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

  public void broadcastToAuthenticated(Message message) {
    for (WebSocket client : clients) {
      if (authenticatedClients.contains(client)) {
        client.send(message.toJSON());
      }
    }
  }

  public void broadcastMessageToAuthenticated(String message) {
    for (WebSocket client : clients) {
      if (authenticatedClients.contains(client)) {
        client.send(new Message("NEW_MESSAGE", message).toJSON());
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

}
