package anb.codes.mchat;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import anb.codes.mchat.database.Database;
import anb.codes.mchat.database.DatabaseQuery;

public class MChatPlugin extends JavaPlugin {
    public Server server;
    public Users users = new Users();
    public LoginCodes loginCodes = new LoginCodes();
    public Connection db;

    private static MChatPlugin instance;

    static public MChatPlugin get() {
        return MChatPlugin.instance;
    }

    @Override
    public void onDisable() {
        try {
            server.stop();
        } catch (IOException | InterruptedException e) {
            Logger.get().error("Error stopping websocket server", e);
        }
        // Don't log disabling, Spigot does that for you automatically!
    }

    @Override
    public void onEnable() {
        MChatPlugin.instance = this;
        getDataFolder().mkdir();
        db = Database.init(this.getDataFolder() + File.separator + "plugin.db");
        users.add("faketoken", "anbcodes");
        server = new Server(41663);
        server.setReuseAddr(true);
        server.start();
        Logger.get().info("Server starting on " + server.getAddress().getHostName() + ":" + server.getPort());

        getServer().getPluginManager().registerEvents(new EventsListener(), this);
    }

    public void broadcastMessage(ChatMessage msg) {
        server.broadcastChatMessageToAuthenticated(msg);
        if (msg.fromWebsite) {
            this.getServer().broadcastMessage("[" + msg.sender + "] " + msg.message);
        } else if (msg.sender != null && !msg.sender.equals("")) {
            this.getServer().broadcastMessage("<" + msg.sender + "> " + msg.message);
        }
    }
}
