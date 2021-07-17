package codes.anb.mchat;

import java.util.Date;

public class ChatMessage {
  public String message;
  public long sent;
  public String sender;
  public boolean fromWebsite;

  public ChatMessage() {

  }

  public ChatMessage(String message, String sender, long sent) {
    this.message = message;
    this.sender = sender;
    this.sent = sent;
    this.fromWebsite = false;
  }

  public ChatMessage(String message, String sender) {
    this.message = message;
    this.sender = sender;
    this.sent = new Date().getTime();
    this.fromWebsite = false;
  }
}
