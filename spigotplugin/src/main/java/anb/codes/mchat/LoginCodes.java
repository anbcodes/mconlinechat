package anb.codes.mchat;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class LoginCodes {
  private Map<String, String> codesToAuthID = new HashMap<>();

  public String makeCode(String username) {
    String code = getRandomChars(5, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    String token = getRandomChars(20, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");

    codesToAuthID.put(code, token);
    MChatPlugin.get().users.add(token, username);
    return code;
  }

  public String login(String code) {
    String authID = codesToAuthID.get(code);
    codesToAuthID.remove(code);
    return authID;
  }

  public String authIDForCode(String code) {
    return codesToAuthID.get(code);
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
}
