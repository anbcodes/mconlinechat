package codes.anb.mchat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import codes.anb.mchat.database.DatabaseQuery;

public class WorldMap {
  private Connection con;

  WorldMap(Connection con) {
    this.con = con;
    try {
      Statement statement = con.createStatement();
      statement.executeUpdate("""
          CREATE TABLE IF NOT EXISTS Points(
            id        INTEGER PRIMARY KEY AUTOINCREMENT,
            name      TEXT,
            creator   TEXT,
            type      TEXT,
            created   INTEGER,
            x         INT,
            z         INT,
            dimension INT
          )""");
    } catch (SQLException e) {
      Logger.get().error("Failed to create points table", e);
    }
  }

  public void add(Point point) {
    try {
      new DatabaseQuery(con,
          "INSERT INTO Points (name, creator, type, created, x, z, dimension) VALUES (?, ?, ?, ?, ?, ?, ?)")
              .setString(point.name).setString(point.creator).setInt(point.type).setInt(point.created).setInt(point.x)
              .setInt(point.z).setInt(point.dimension).executeUpdate();
    } catch (SQLException e) {
      Logger.get().error("Failed to add point " + point.name, e);
    }
  }

  public void updateName(int id, String name) {
    try {
      new DatabaseQuery(con, "UPDATE Points SET name = ? WHERE id=?").setString(name).setInt(id).executeUpdate();
    } catch (SQLException e) {
      Logger.get().error("Failed to update name to " + name + " for id " + id, e);
    }
  }

  public void updateType(int id, String type) {
    try {
      new DatabaseQuery(con, "UPDATE Points SET type = ? WHERE id=?").setString(type).setInt(id).executeUpdate();
    } catch (SQLException e) {
      Logger.get().error("Failed to update type to " + type + " for id " + id, e);
    }
  }

  public void updateCreator(int id, String creator) {
    try {
      new DatabaseQuery(con, "UPDATE Points SET creator = ? WHERE id=?").setString(creator).setInt(id).executeUpdate();
    } catch (SQLException e) {
      Logger.get().error("Failed to update creator to " + creator + " for id " + id, e);
    }
  }

  public void updateCreated(int id, int created) {
    try {
      new DatabaseQuery(con, "UPDATE Points SET created = ? WHERE id=?").setInt(created).setInt(id).executeUpdate();
    } catch (SQLException e) {
      Logger.get().error("Failed to update created to " + created + " for id " + id, e);
    }
  }

  public void updateLoc(int id, int x, int z, int dim) {
    try {
      new DatabaseQuery(con, "UPDATE Points SET x = ?, z = ?, dimension = ? WHERE id=?").setInt(x).setInt(z).setInt(dim)
          .setInt(id).executeUpdate();
    } catch (SQLException e) {
      Logger.get().error("Failed to update location for id " + id, e);
    }
  }

  public Point[] getPoints(int dimension) {
    try {
      ResultSet results = new DatabaseQuery(con, "SELECT * FROM Points WHERE dimension = ?").setInt(dimension)
          .executeQuery();
      List<Point> points = new ArrayList<>();
      while (results.next()) {
        Point p = new Point();
        p.id = results.getInt(1);
        p.name = results.getString(2);
        p.creator = results.getString(3);
        p.type = results.getInt(4);
        p.created = results.getInt(5);
        p.x = results.getInt(6);
        p.z = results.getInt(7);
        p.dimension = results.getInt(8);
        points.add(p);
      }
      return points.toArray(new Point[0]);

    } catch (SQLException e) {
      Logger.get().error("Failed get points in dimension " + dimension, e);
      return null;
    }
  }

  public Point getPoint(int id) {
    try {
      ResultSet results = new DatabaseQuery(con, "SELECT * FROM Points WHERE id = ?").setInt(id).executeQuery();
      if (results.next()) {
        Point p = new Point();
        p.id = results.getInt(1);
        p.name = results.getString(2);
        p.creator = results.getString(3);
        p.type = results.getInt(4);
        p.created = results.getInt(5);
        p.x = results.getInt(6);
        p.z = results.getInt(7);
        p.dimension = results.getInt(8);
        return p;
      } else {
        return null;
      }
    } catch (SQLException e) {
      Logger.get().error("Failed get point for id " + id, e);
      return null;
    }
  }
}
