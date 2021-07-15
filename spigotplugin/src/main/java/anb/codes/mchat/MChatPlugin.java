package anb.codes.mchat;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

public class MChatPlugin extends JavaPlugin {
    public Users users;
    public Server server;
    public History history;
    public Database database;

    @Override
    public void onDisable() {
        try {
            server.stop();
        } catch (IOException | InterruptedException e) {
            getLogger().warning(e.getStackTrace().toString());
        }
        // Don't log disabling, Spigot does that for you automatically!
    }

    @Override
    public void onEnable() {
        getDataFolder().mkdir();
        database = new Database(this);
        history = new History("history.txt", this);
        users = new Users("users.json", this);
        server = new Server(this, 41663);
        server.setReuseAddr(true);
        server.start();
        this.getLogger().info("Server starting on " + server.getAddress().getHostName() + ":" + server.getPort());
        // Don't log enabling, Spigot does that for you automatically!
        getServer().getPluginManager().registerEvents(new EventsListener(this), this);
        // Commands enabled with following method must have entries in plugin.yml
        getCommand("shout").setExecutor(new ChatCommand(this));
        getCommand("login").setExecutor(new ChatCommand(this));
    }

    public void sendMessage(String msg) {
        server.broadcastMessageToAuthenticated(msg);
        history.add(msg);
    }
}