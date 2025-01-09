package dbAccess;

/**
 * Implements management of a MySQL database on Linux.
 * @author Mike Smith University of Brighton
 * @version 2.1
 */
class LinuxAccess extends DBAccess {
  @Override
  public void loadDriver() {
    try {
      Class.forName("org.gjt.mm.mysql.Driver").getDeclaredConstructor().newInstance();
      System.out.println("Linux MySQL driver loaded successfully.");
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException("Failed to load Linux MySQL driver", e);
    }
  }

  @Override
  public String urlOfDatabase() {
    return "jdbc:mysql://localhost/cshop?user=root";
  }
}
