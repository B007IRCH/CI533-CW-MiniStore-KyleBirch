package dbAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Implements generic management of a database.
 * @author Kyle Birch
 * @version 2.1
 */
public class DBAccess {
  private static final String URL = "jdbc:derby:catshop.db;create=true";
  private static final String USERNAME = "app";
  private static final String PASSWORD = "app";

  // Load Derby Driver
  public void loadDriver() {
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
      System.out.println("Derby driver loaded successfully.");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Failed to load Derby driver", e);
    }
  }

  // Get database connection
  public Connection getConnection() {
    try {
      return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    } catch (SQLException e) {
      throw new RuntimeException("Failed to connect to the database", e);
    }
  }

  public String urlOfDatabase() {
    return URL;
  }

  public String username() {
    return USERNAME;
  }

  public String password() {
    return PASSWORD;
  }
}
