package anb.codes.mchat;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;

import org.bukkit.plugin.java.JavaPlugin;

import anb.codes.mchat.database.Database;

public class MChatPlugin extends JavaPlugin {
    public Server server;
    public Users users;
    public LoginCodes loginCodes = new LoginCodes();
    public History history;
    public WorldMap map;
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
        history = new History(db);
        users = new Users(db);
        map = new WorldMap(db);
        users.add("faketoken", "anbcodes");
        server = new Server(41663);
        server.setReuseAddr(true);
        server.start();
        Logger.get().info("Server starting on " + server.getAddress().getHostName() + ":" + server.getPort());

        getServer().getPluginManager().registerEvents(new EventsListener(), this);
        getCommand("login").setExecutor(new CommandHandler());
    }

    public void broadcastMessage(ChatMessage msg) {
        server.broadcastChatMessageToAuthenticated(msg);
        if (msg.fromWebsite) {
            this.getServer().broadcastMessage("[" + msg.sender + "] " + msg.message);
        }
        history.add(msg);
    }
}
