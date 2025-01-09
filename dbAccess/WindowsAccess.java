package dbAccess;

/**
 * Implements management of a Microsoft Access database.
 * @author Mike Smith University of Brighton
 * @version 2.1
 */
class WindowsAccess extends DBAccess {
  @Override
  public void loadDriver() {
    try {
      Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
      System.out.println("Windows Access driver loaded successfully.");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Failed to load Windows Access driver", e);
    }
  }

  @Override
  public String urlOfDatabase() {
    return "jdbc:odbc:cshop";
  }
}
