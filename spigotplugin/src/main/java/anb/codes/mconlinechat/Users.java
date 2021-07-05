package anb.codes.mconlinechat;

import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Users {
  private Map<String, String> users = new HashMap<>();
  private Map<String, String> loginCodes = new HashMap<>();
  private PluginDataFile usersFile;

  Users(String filename, MCOnlineChatPlugin plugin) {
    usersFile = new PluginDataFile(plugin, "users.json");
    loadUsersFromFile();
  }

  public String loginWithCode(String code) {
    String authIDForCode = loginCodes.get(code);
    if (authIDForCode != null) {
      loginCodes.remove(code);
      return authIDForCode;
    } else {
      return null;
    }
  }

  public String getNameFromID(String authID) {
    return users.get(authID);
  }

  public String getNewLoginCodeForUser(String username) {
    String code = getRandomChars(5, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    String authID = getRandomChars(25, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");
    loginCodes.put(code, authID);
    users.put(authID, username);
    saveUsersToFile();
    return code;
  }

  private String getRandomChars(int count, String options) {
    return getRandomChars(count, options.toCharArray());
  }

  private String getRandomChars(int count, char[] chars) {
    SecureRandom random = new SecureRandom();
    String generatedSeq = "";
    for (int i = 0; i < count; i++) {
      generatedSeq += chars[random.nextInt(chars.length)];
    }
    return generatedSeq;
  }

  private void saveUsersToFile() {
    usersFile.overwrite(new Gson().toJson(users));
  }

  private void loadUsersFromFile() {
    String json = usersFile.read();
    Type empMapType = new TypeToken<Map<String, String>>() {
    }.getType();
    Map<String, String> newUsers = new Gson().fromJson(json, empMapType);
    if (newUsers == null) {
      users = new HashMap<>();
    } else {
      users = newUsers;
    }
  }
}
