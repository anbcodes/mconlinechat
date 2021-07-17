package codes.anb.mchat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import codes.anb.mchat.database.DatabaseQuery;

public class PointTypes {
  private Connection con;

  PointTypes(Connection con) {
    this.con = con;
    try {
      Statement statement = con.createStatement();
      statement.executeUpdate("""
          CREATE TABLE IF NOT EXISTS PointTypes(
            id        INTEGER PRIMARY KEY AUTOINCREMENT,
            name      TEXT NOT NULL UNIQUE,
            image     TEXT
          )""");
    } catch (SQLException e) {
      Logger.get().error("Failed to create types table", e);
    }
  }

  public int getIdFromName(String name) {
    try {
      ResultSet results = new DatabaseQuery(con, "SELECT * FROM PointTypes WHERE name = ?").setString(name)
          .executeQuery();
      if (results.next()) {
        return results.getInt("id");
      } else {
        return -1;
      }
    } catch (SQLException e) {
      Logger.get().error("Error getting id from name " + name, e);
      return -1;
    }
  }

  public boolean add(String name) {
    try {
      new DatabaseQuery(con, "INSERT INTO PointTypes(name) VALUES(?)").setString(name).executeUpdate();
      return true;
    } catch (SQLException e) {
      Logger.get().error("Error adding type " + name, e);
      return false;
    }
  }

  public List<PointType> get() {
    try {
      List<PointType> types = new ArrayList<>();
      ResultSet results = new DatabaseQuery(con, "SELECT * FROM PointTypes").executeQuery();
      while (results.next()) {
        PointType t = new PointType();
        t.id = results.getInt("id");
        t.name = results.getString("name");
        t.image = results.getString("image");
        types.add(t);
      }
      return types;
    } catch (SQLException e) {
      Logger.get().error("Error getting types ", e);
      return new ArrayList<>();
    }
  }
}
