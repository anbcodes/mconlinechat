package anb.codes.mchat;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Database {
  private Connection con;
  private MChatPlugin plugin;

  public Database(MChatPlugin plugin) {
    this.plugin = plugin;
    try {
      con = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + File.separator + "plugin.db");
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void init() throws SQLException {
    Statement statement = con.createStatement();
    statement.executeQuery("""
        CREATE TABLE IF NOT EXISTS Tokens(
          token    TEXT PRIMARY KEY
          username TEXT
          created  DATETIME
        )""");
    statement.executeQuery("""
        CREATE TABLE IF NOT EXISTS History(
          sender   TEXT
          message  TEXT
          sent     DATETIME
        )""");
    statement.executeQuery("""
        CREATE TABLE IF NOT EXISTS Points(
          name     TEXT
          creator  TEXT
          type     TEXT
          created  DATETIME
          x        INT
          z        INT
          dim      INT
        )""");
  }

  public String addPoint(String name, String type, int x, int z, int dim, String creator) {
    try {
      PreparedStatement existsStatement = con.prepareStatement("""
          SELECT * FROM Points WHERE x=? AND z=? AND dim=?""");
      existsStatement.setInt(1, x);
      existsStatement.setInt(2, z);
      existsStatement.setInt(3, dim);

      ResultSet results = existsStatement.executeQuery();
      if (results == null) {
        PreparedStatement sql = con.prepareStatement("""
            INSERT INTO TABLE_NAME(name, creator, type, created, x, z, dim)
                            VALUES(?, ?, ?, ?, ?, ?, ?)""");
        sql.setString(1, name);
        sql.setString(2, creator);
        sql.setString(3, type);
        sql.setLong(4, (new Date()).getTime());
        sql.setInt(5, x);
        sql.setInt(6, z);
        sql.setInt(7, dim);
        sql.executeUpdate();
        return "Success! Added " + name + " to the map at " + x + ", " + z;
      } else {
        PreparedStatement sql = con.prepareStatement("""
            INSERT INTO TABLE_NAME(name, creator, type, created, x, z, dim)
                            VALUES(?, ?, ?, ?, ?, ?, ?)""");
        sql.setString(1, name);
        sql.setString(2, creator);
        sql.setString(3, type);
        sql.setLong(4, (new Date()).getTime());
        sql.setInt(5, x);
        sql.setInt(6, z);
        sql.setInt(7, dim);
        sql.executeUpdate();
        return "Success! Updated " + results.getString(1) + " to " + name + " on the map at " + x + ", " + z;
      }
    } catch (SQLException e) {
      plugin.getLogger().warning("Error while adding a point " + e.getMessage());

      return "There was an error while adding your point";
    }
  }

  public List<Point> getPoints() {
    try {
      PreparedStatement statement = con.prepareStatement("SELECT * FROM Points");
      ResultSet results = statement.executeQuery();
      return resultsToPoints(results);
    } catch (SQLException e) {
      plugin.getLogger().warning("Error while getting a point " + e.getMessage());
      return null;
    }
  }

  public List<Point> getPoints(int dim) {
    try {
      PreparedStatement statement = con.prepareStatement("SELECT * FROM Points WHERE dim=?");
      statement.setInt(1, dim);
      ResultSet results = statement.executeQuery();
      return resultsToPoints(results);
    } catch (SQLException e) {
      plugin.getLogger().warning("Error while getting a point " + e.getMessage());
      return null;
    }
  }

  public List<Point> getPoints(Bounds bounds, int dim) {
    try {
      PreparedStatement statement = con
          .prepareStatement("SELECT * FROM Points WHERE dim=? AND x >= ? AND x <= ? AND z >= ? AND z <= ?");
      statement.setInt(1, dim);
      statement.setInt(2, bounds.x1);
      statement.setInt(3, bounds.x2);
      statement.setInt(4, bounds.z2);
      statement.setInt(5, bounds.z2);
      ResultSet results = statement.executeQuery();
      return resultsToPoints(results);
    } catch (SQLException e) {
      plugin.getLogger().warning("Error while getting a point " + e.getMessage());
      return null;
    }
  }

  private List<Point> resultsToPoints(ResultSet results) throws SQLException {
    List<Point> points = new ArrayList<>();
    while (results.next()) {
      Point point = new Point();
      point.name = results.getString(1);
      point.creator = results.getString(2);
      point.type = results.getString(3);
      point.created = results.getInt(4);
      point.x = results.getInt(5);
      point.z = results.getInt(6);
      point.dim = results.getInt(7);
      points.add(point);
    }
    return points;
  }
}
