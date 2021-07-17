package codes.anb.mchat;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import codes.anb.mchat.database.Database;

public class MChatPlugin extends JavaPlugin {
    public Server server;
    public Users users;
    public LoginCodes loginCodes = new LoginCodes();
    public History history;
    public WorldMap map;
    public PointTypes pointTypes;
    public List<Player> hiddenPlayers = new ArrayList<>();
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
        pointTypes = new PointTypes(db);
        users = new Users(db);
        map = new WorldMap(db);
        server = new Server(31663);
        server.setReuseAddr(true);
        server.start();
        Logger.get().info("Server starting on " + server.getAddress().getHostName() + ":" + server.getPort());

        getServer().getPluginManager().registerEvents(new EventsListener(), this);
        CommandHandler handler = new CommandHandler();
        getCommand("login").setExecutor(handler);
        getCommand("map").setExecutor(handler);
        getCommand("addmaptype").setExecutor(handler);
        getCommand("hide").setExecutor(handler);
        getCommand("show").setExecutor(handler);
    }

    public void broadcastMessage(ChatMessage msg) {
        server.broadcastChatMessageToAuthenticated(msg);
        if (msg.fromWebsite) {
            this.getServer().broadcastMessage("[" + msg.sender + "] " + msg.message);
        }
        history.add(msg);
    }
}
