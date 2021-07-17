package codes.anb.mchat.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import codes.anb.mchat.Logger;

public class Database {
  public static Connection init(String path) {
    Connection con;
    try {
      con = DriverManager.getConnection("jdbc:sqlite:" + path);
      return con;
    } catch (SQLException e) {
      Logger.get().error("Error initializing database ", e);
      return null;
    }
  }
}
