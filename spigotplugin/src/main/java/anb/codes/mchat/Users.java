package anb.codes.mchat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import anb.codes.mchat.database.DatabaseQuery;

public class Users {
  public String getFromToken(String token) {
    Logger.get().debug("Getting user from token " + token);
    try {
      ResultSet results = new DatabaseQuery("SELECT * FROM Tokens WHERE token=?").setString(token).executeQuery();
      results.next();
      return results.getString("username");
    } catch (SQLException e) {
      Logger.get().error("Error finding user " + token, e);
      return null;
    }
  }

  public void add(String token, String username) {
    Logger.get().debug("Adding user " + username + " with token " + token);
    try {
      new DatabaseQuery("INSERT INTO Tokens (token, username, created) VALUES(?, ?, ?)").setString(token)
          .setString(username).setLong(new Date().getTime()).executeUpdate();
    } catch (SQLException e) {
      Logger.get().error("Error adding user " + token + " " + username, e);
    }
  }
}
