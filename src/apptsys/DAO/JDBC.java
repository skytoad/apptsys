package apptsys.DAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Java DataBase Connectivity establishes the connection with our database, allowing us to query records.
 */

public class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password


    private static Connection connection = null;  // Connection Interface
    private static PreparedStatement preparedStatement; // Prepared Statement

    /**
     * makeConnection utilizes our field data to initialize connection with DB
     */
    public static void makeConnection() {
            try {
              Class.forName(driver); // Locate Driver
              connection = DriverManager.getConnection(jdbcUrl, userName, password); // reference Connection object
              System.out.println("Connection successful!");
            } catch (ClassNotFoundException|SQLException e) {
                      System.out.println("Error:" + e.getMessage());
            }

        }

    /**
     * simply returns the connection object, once established
     * @return connection
     */
    public static Connection getConnection() {
            return connection;
        }

    /**
     * closes the connection once we're done
     */
    public static void closeConnection() {
            try {
                connection.close();
                System.out.println("Connection closed!");
            } catch (SQLException e) {
                    System.out.println(e.getMessage());
            }
        }

    /**
     * gets the current prepared statement from DB connection
     * @return current prepared statement
     * @throws SQLException
     */
    public static PreparedStatement getPS() throws SQLException {
           if (preparedStatement != null)
               return preparedStatement;
           else System.out.println("Null reference to Prepared Statement");
           return null;
       }



}