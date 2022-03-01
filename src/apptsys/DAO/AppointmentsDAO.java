package apptsys.DAO;

import apptsys.Main;
import apptsys.model.Appointment;
import apptsys.utilities.ScheduleHelper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.*;
import java.sql.*;
import java.util.List;
import java.time.*;

/**
 *  Acts as an interface between our application and our database, accessing and modifying Appointment records
 */

public class AppointmentsDAO implements DAO<Appointment> {
    public static ObservableList<Appointment> appointments;

    /**
     * getAll queries the database for all Appointment records, then creates a POJO for each one and adds to list
     * @return a complete ObservableList with all records found
     */
    public ObservableList<Appointment> getAll() {
        appointments = FXCollections.observableArrayList();

        try {
            String query = "SELECT * FROM appointments"; // SQL query

            PreparedStatement ps = JDBC.getConnection().prepareStatement(query); // Prepared Statement

            ResultSet rs = ps.executeQuery(); // Results

            while (rs.next()) {
                ObservableValue<Integer> apptID = new ReadOnlyObjectWrapper<>(rs.getInt("Appointment_ID"));
                String title = rs.getString("Title");
                String desc = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime startTime = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime endTime = rs.getTimestamp("End").toLocalDateTime();
                LocalDateTime creationDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                Integer customerID = rs.getInt("Customer_ID");
                Integer userID = rs.getInt("User_ID");
                Integer contactID = rs.getInt("Contact_ID");

                Appointment a = new Appointment(apptID, title, desc, location, type, startTime, endTime, creationDate, createdBy, lastUpdate, lastUpdatedBy, customerID, userID, contactID);

                appointments.add(a);
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }

        return appointments;
    }

    /**
     * accepts a nearly-complete appointment object, and adds new record to the database. Once inserted, database will automatically generate an ApptID, which we add to our initial record to ensure data matches between application and database
     * @param appointment record to add (has apptID placeholder value of 1000 when added)
     */
    public void add(Appointment appointment) {

        try {
            String query = "INSERT INTO appointments VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; // SQL query

            PreparedStatement ps = JDBC.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS); // Prepared Statement
            ps.setString(1, appointment.getApptTitle());
            ps.setString(2, appointment.getApptDesc());
            ps.setString(3, appointment.getApptLocation());
            ps.setString(4, appointment.getApptType());
            ps.setTimestamp(5, Timestamp.valueOf(appointment.getApptStartDate()));
            ps.setTimestamp(6, Timestamp.valueOf(appointment.getApptEndDate()));
            ps.setTimestamp(7, Timestamp.valueOf(appointment.getApptCreationDate()));
            ps.setString(8, appointment.getApptCreatedBy());
            ps.setTimestamp(9, Timestamp.valueOf(appointment.getApptLastUpdate()));
            ps.setString(10, appointment.getApptLastUpdatedBy());
            ps.setInt(11, appointment.getApptCustomerID());
            ps.setInt(12, appointment.getApptUserID());
            ps.setInt(13, appointment.getApptContactID());

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            appointment.setApptID(rs.getInt(1));

            appointments.add(appointment);
            System.out.println("Appointment added!");

        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add appointment.");
        }

    }

    /**
     * update finds a matching record in our database, then updates fields to match the supplied appointment
     * @param appointment record to modify
     */
    public void update(Appointment appointment) {

        try {
            String query = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(query);

            ps.setString(1, appointment.getApptTitle());
            ps.setString(2, appointment.getApptDesc());
            ps.setString(3, appointment.getApptLocation());
            ps.setString(4, appointment.getApptType());
            ps.setTimestamp(5, Timestamp.valueOf(appointment.getApptStartDate()));
            ps.setTimestamp(6, Timestamp.valueOf(appointment.getApptEndDate()));
            ps.setTimestamp(7, Timestamp.valueOf(appointment.getApptCreationDate()));
            ps.setString(8, appointment.getApptCreatedBy());
            ps.setTimestamp(9, Timestamp.valueOf(appointment.getApptLastUpdate()));
            ps.setString(10, appointment.getApptLastUpdatedBy());
            ps.setInt(11, appointment.getApptCustomerID());
            ps.setInt(12, appointment.getApptUserID());
            ps.setInt(13, appointment.getApptContactID());
            ps.setInt(14, appointment.getApptID().getValue().intValue());

            ps.execute();
            System.out.println("Appointment updated!");

        } catch (SQLException se) {
            se.printStackTrace();
        }


    }

    /**
     * delete finds a record in the database and deletes it
     * @param appointment record to delete
     */
    public void delete(Appointment appointment) {
        System.out.println("Calling delete");
        try {
            String query = "DELETE FROM appointments WHERE Appointment_ID = " + appointment.getApptID().getValue();

            PreparedStatement ps = JDBC.getConnection().prepareStatement(query);

            ps.execute();
            appointments.remove(appointment);
            System.out.println("Appointment was deleted");
        } catch (SQLException se) {
            se.printStackTrace();
        }

    }
}
