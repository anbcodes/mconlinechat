package anb.codes.mchat;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class WorldMap {
  private Connection con;

  WorldMap(Connection con) {
    this.con = con;
    try {
      Statement statement = con.createStatement();
      statement.executeUpdate("""
          CREATE TABLE IF NOT EXISTS Points(
            name     TEXT,
            creator  TEXT,
            type     TEXT,
            created  INTEGER,
            x        INT,
            z        INT,
            dim      INT
          )""");
    } catch (SQLException e) {
      Logger.get().error("Failed to create points table", e);
    }
  }
}
