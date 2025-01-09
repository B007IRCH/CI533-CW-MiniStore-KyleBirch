package dbAccess;

/**
 * Apache Derby database access.
 * @author Kyle Birch
 * @version 2.1
 */
public class DerbyAccess extends DBAccess {
  private static final String URLdb = "jdbc:derby:catshop.db;create=true";
  private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

  /**
   * Load the Apache Derby database driver.
   */
  @Override
  public void loadDriver() {
    try {
      Class.forName(DRIVER).getDeclaredConstructor().newInstance();
      System.out.println("Derby driver loaded successfully.");
    } catch (ReflectiveOperationException e) {
      // Catch ReflectiveOperationException to include ClassNotFoundException and other reflection-related exceptions
      throw new RuntimeException("Failed to load Derby driver", e);
    }
  }

  @Override
  public String urlOfDatabase() {
    return URLdb;
  }
}
