package apptsys.DAO;

import apptsys.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 *  Acts as an interface between our application and our database, accessing User records
 */

public class UsersDAO {
    public static ObservableList<User> users;

    /**
     * getAll queries the database for all User records, then creates a POJO for each one and adds to list
     * @return a complete ObservableList with all records found
     */
    public ObservableList<User> getAll() {
       try {
           users = FXCollections.observableArrayList();

           String query = "SELECT * FROM users";

           PreparedStatement ps = JDBC.getConnection().prepareStatement(query);

           ResultSet rs = ps.executeQuery();

           while (rs.next()) {
               int userID = rs.getInt("User_ID");
               String userName = rs.getString("User_Name");
               String userPassword = rs.getString("Password");
               LocalDateTime creationDate = rs.getTimestamp("Create_Date").toLocalDateTime();
               String createdBy = rs.getString("Created_By");
               LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
               String lastUpdatedBy = rs.getString("Last_Updated_By");

               User user = new User(userID, userName, userPassword, creationDate, createdBy, lastUpdate, lastUpdatedBy);

               users.add(user);
               System.out.println(user.getUserName());
           }


       } catch (SQLException se) {
           se.printStackTrace();
       }
        return users;
    }
}
