package anb.codes.mchat.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import anb.codes.mchat.MChatPlugin;

public class DatabaseQuery {
  private Connection db;
  private PreparedStatement statement;
  private int currentLoc = 1;

  public DatabaseQuery(Connection db, String query) throws SQLException {
    this.db = db;
    statement = db.prepareStatement(query);
  }

  public DatabaseQuery(String query) throws SQLException {
    this.db = MChatPlugin.get().db;
    statement = db.prepareStatement(query);
  }

  public DatabaseQuery setQuery(String query) throws SQLException {
    statement = db.prepareStatement(query);
    return this;
  }

  public DatabaseQuery setInt(int number, int loc) throws SQLException {
    statement.setInt(loc, number);
    return this;
  }

  public DatabaseQuery setInt(int number) throws SQLException {
    setInt(number, currentLoc);
    currentLoc += 1;
    return this;
  }

  public DatabaseQuery setLong(long number, int loc) throws SQLException {
    statement.setLong(loc, number);
    return this;
  }

  public DatabaseQuery setLong(long number) throws SQLException {
    setLong(number, currentLoc);
    currentLoc += 1;
    return this;
  }

  public DatabaseQuery setString(String string, int loc) throws SQLException {
    statement.setString(loc, string);
    return this;
  }

  public DatabaseQuery setString(String string) throws SQLException {
    setString(string, currentLoc);
    currentLoc += 1;
    return this;
  }

  public DatabaseQuery setBool(Boolean bool, int loc) throws SQLException {
    statement.setBoolean(loc, bool);
    return this;
  }

  public DatabaseQuery setBool(Boolean bool) throws SQLException {
    setBool(bool, currentLoc);
    currentLoc += 1;
    return this;
  }

  public void executeUpdate() throws SQLException {
    statement.executeUpdate();
  }

  public ResultSet executeQuery() throws SQLException {
    return statement.executeQuery();
  }
}
