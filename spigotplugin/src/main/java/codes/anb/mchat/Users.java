package codes.anb.mchat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import codes.anb.mchat.database.DatabaseQuery;

public class Users {
  private Connection con;

  public Users(Connection con) {
    this.con = con;
    try {
      Statement statement = con.createStatement();
      statement.executeUpdate("""
          CREATE TABLE IF NOT EXISTS Tokens(
            token    TEXT PRIMARY KEY,
            username TEXT,
            created  INTEGER
          )""");
    } catch (SQLException e) {
      Logger.get().error("Error creating tokens table", e);
    }
  }

  public String getFromToken(String token) {
    Logger.get().debug("Getting user from token " + token);
    try {
      ResultSet results = new DatabaseQuery(con, "SELECT * FROM Tokens WHERE token=?").setString(token).executeQuery();
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
      new DatabaseQuery(con, "INSERT INTO Tokens (token, username, created) VALUES(?, ?, ?)").setString(token)
          .setString(username).setLong(new Date().getTime()).executeUpdate();
    } catch (SQLException e) {
      Logger.get().error("Error adding user " + token + " " + username, e);
    }
  }
}
