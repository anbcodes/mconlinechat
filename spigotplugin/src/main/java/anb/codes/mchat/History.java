package anb.codes.mchat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import anb.codes.mchat.database.DatabaseQuery;

public class History {
  private Connection con;

  public History(Connection con) {
    this.con = con;
    try {
      Statement statement = con.createStatement();
      statement.executeUpdate("""
          CREATE TABLE IF NOT EXISTS History(
            sender       TEXT,
            message      TEXT,
            fromWebsite  INT,
            sent         INTEGER
          )""");
    } catch (SQLException e) {
      Logger.get().error("Error creating history table", e);
    }
  }

  public ChatMessage[] getPage(int page, int offset) {
    Logger.get().debug("Getting page " + page + "With offset " + offset);
    try {
      List<ChatMessage> history = new ArrayList<>();
      ResultSet results = new DatabaseQuery(con, "SELECT * FROM History ORDER BY sent DESC LIMIT ?")
          .setInt((page + 1) * 100 + offset).executeQuery();
      while (results.next()) {
        ChatMessage msg = new ChatMessage();
        msg.fromWebsite = results.getInt("fromWebsite") == 1;
        msg.message = results.getString("message");
        msg.sender = results.getString("sender");
        msg.sent = results.getLong("sent");
        history.add(msg);
      }

      int subStart = Math.min((page) * 100 + offset, history.size());
      int subEnd = Math.min((page + 1) * 100 + offset, history.size());

      return history.subList(subStart, subEnd).toArray(new ChatMessage[0]);
    } catch (SQLException e) {
      Logger.get().error("Failed to get history (page " + page + ")", e);
      return null;
    }
  }

  public void add(ChatMessage message) {
    Logger.get().debug("Adding message " + message);
    try {
      new DatabaseQuery(con, "INSERT INTO History (sender, message, fromWebsite, sent) VALUES(?,?,?,?)")
          .setString(message.sender).setString(message.message).setBool(message.fromWebsite).setLong(message.sent)
          .executeUpdate();
      ;
    } catch (SQLException e) {
      Logger.get().error("Failed to add item to history " + message.message, e);
    }
  }
}
