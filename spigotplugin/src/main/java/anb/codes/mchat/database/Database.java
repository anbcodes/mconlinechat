package anb.codes.mchat.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import anb.codes.mchat.Logger;

public class Database {
  public static Connection init(String path) {
    Connection con;
    try {
      con = DriverManager.getConnection("jdbc:sqlite:" + path);
      Statement statement = con.createStatement();
      statement.executeUpdate("""
          CREATE TABLE IF NOT EXISTS Tokens(
            token    TEXT PRIMARY KEY,
            username TEXT,
            created  DATETIME
          )""");
      statement.executeUpdate("""
          CREATE TABLE IF NOT EXISTS History(
            sender   TEXT,
            message  TEXT,
            sent     DATETIME
          )""");
      statement.executeUpdate("""
          CREATE TABLE IF NOT EXISTS Points(
            name     TEXT,
            creator  TEXT,
            type     TEXT,
            created  DATETIME,
            x        INT,
            z        INT,
            dim      INT
          )""");
      return con;
    } catch (SQLException e) {
      Logger.get().error("Error initializing database ", e);
      return null;
    }

  }
}
