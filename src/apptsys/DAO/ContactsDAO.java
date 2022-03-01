package apptsys.DAO;

import apptsys.model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *  Acts as an interface between our application and our database, accessing and modifying Contact records
 */

public class ContactsDAO {

    public static ObservableList<Contact> contacts;

    /**
     * getAll queries the database for all Contact records, then creates a POJO for each one and adds to list
     * @return a complete ObservableList with all records found
     */
    public ObservableList<Contact> getAll() {
        contacts = FXCollections.observableArrayList();

        try {
            String query = "SELECT * FROM contacts";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String contactEmail = rs.getString("Email");

                Contact contact = new Contact (contactID, contactName, contactEmail);

                contacts.add(contact);
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }

        return contacts;
    }
}
