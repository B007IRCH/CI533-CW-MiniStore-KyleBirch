package dbAccess;

/**
 * Implements management of an Apache Derby database.
 * To be created.
 * @author Mike Smith University of Brighton
 * @version 2.1
 */
class DerbyCreateAccess extends DBAccess {
  private static final String URLdb = "jdbc:derby:catshop.db;create=true";
  private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

  @Override
  public void loadDriver() {
    try {
      Class.forName(DRIVER).getDeclaredConstructor().newInstance();
      System.out.println("Derby Create driver loaded successfully.");
    } catch (ReflectiveOperationException e) {
      // Catch ReflectiveOperationException to include ClassNotFoundException and other reflection-related exceptions
      throw new RuntimeException("Failed to load Derby Create driver", e);
    }
  }

  @Override
  public String urlOfDatabase() {
    return URLdb;
  }
}
