package anb.codes.mcbot.spigotplugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Message {
  public String type;
  public String[] data;
  public String authID;

  Message(String type, String... data) {
    this.type = type;
    this.data = data;
  }

  public String toJSON() {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    return gson.toJson(this);
  }

  public static Message fromJSON(String json) {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    return gson.fromJson(json, Message.class);
  }
}
