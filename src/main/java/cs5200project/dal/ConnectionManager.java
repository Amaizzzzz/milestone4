package cs5200project.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Use ConnectionManager to connect to your database instance.
 *
 * ConnectionManager uses the MySQL Connector/J driver to connect to
 * your local MySQL instance.
 *
 * In our example, we will create a DAO (data access object) java
 * class to interact with each MySQL table. The DAO java classes will
 * use ConnectionManager to open and close connections.
 *
 *
 * Before using, you must edit the values for PASSWORD and SCHEMA
 * below.  PASSWORD must be the MySQL password you configured when you
 * installed MySQL server, and SCHEMA must be the name of the database
 * schema that the application will use.
 */
public class ConnectionManager {

  private final static String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
  private final static String URL = "jdbc:mysql://localhost:3306/GameDB";
  private final static String USER = "root";
  private final static String PASSWORD = "password";
  
  static {
    try {
      Class.forName(DRIVER_CLASS);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to load MySQL driver", e);
    }
  }
  
  public static Connection getConnection() throws SQLException {
    Properties info = new Properties();
    info.put("user", USER);
    info.put("password", PASSWORD);
    return DriverManager.getConnection(URL, info);
  }
  
  public static void closeConnection(Connection connection) {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  // User to connect to your database instance. By default, this is "root2".
  private static final String USER_DEFAULT = "root2";
  // Password for the user.
  private static final String PASSWORD_DEFAULT = "NMUvn9s8";
  // URI to your database server. If running on the same machine, then
  // this is "localhost".
  private static final String HOSTNAME = "localhost";
  // Port to your database server. By default, this is 3307.
  private static final int PORT = 3306;
  // Name of the MySQL schema that contains your tables.
  private static final String SCHEMA = "cs5200project";
  // Default timezone for MySQL server.
  private static final String TIMEZONE = "UTC";
  // SSL setting
  private static final String SSL = "false";

  /**
   * Private default constructor to prevent instantiation.
   */
  private ConnectionManager() { }

  /** Get the connection to the database instance. */
  public static Connection getConnectionDefault() throws SQLException {
    Properties connectionProperties = new Properties();
    connectionProperties.put("user", USER_DEFAULT);
    connectionProperties.put("password", PASSWORD_DEFAULT);
    connectionProperties.put("serverTimezone", TIMEZONE);
    connectionProperties.put("useSSL", SSL);
    try {
      // Ensure the JDBC driver is loaded by retrieving the runtime
      // Class descriptor.  Otherwise, Tomcat may have issues loading
      // libraries in the proper order.  One alternative is calling
      // this in the HttpServlet init() override.
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      throw new SQLException(e);
    }
    return DriverManager.getConnection(
      String.format(
        "jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true",
        HOSTNAME,
        PORT,
        SCHEMA
      ),
      connectionProperties
    );
  }

  /**
   * Open and return a connection to the database server that is not
   * associated with a particular schema.  We use this for operations
   * that delete and re-create the schema; see Driver.java for an example.
   */
  public static Connection getSchemalessConnection() throws SQLException {
    Properties connectionProperties = new Properties();
    connectionProperties.put("user", USER_DEFAULT);
    connectionProperties.put("password", PASSWORD_DEFAULT);
    connectionProperties.put("serverTimezone", TIMEZONE);
    connectionProperties.put("useSSL", SSL);
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      throw new SQLException(e);
    }
    return DriverManager.getConnection(
      String.format(
        "jdbc:mysql://%s:%d?useSSL=false&allowPublicKeyRetrieval=true",
        HOSTNAME,
        PORT
        ),
      connectionProperties
    );
  }
}
