package apptsys.DAO;

import apptsys.Main;
import apptsys.model.Customer;
import apptsys.utilities.ScheduleHelper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;


/**
 *  Acts as an interface between our application and our database, accessing and modifying Customer records
 */

public class CustomersDAO implements DAO<Customer> {
    public static ObservableList<Customer> customers;

    /**
     * getAll queries the database for all Customer records, then creates a POJO for each one and adds to list
     * @return a complete ObservableList with all records found
     */
    public ObservableList<Customer> getAll() {
        customers = FXCollections.observableArrayList();

        try {
            String query = "SELECT * FROM customers"; // SQL query

            PreparedStatement ps = JDBC.getConnection().prepareStatement(query); // Prepared Statement

            ResultSet rs = ps.executeQuery(); // Results


            while (rs.next()) {
                ObservableValue<Integer> custID = new ReadOnlyObjectWrapper<>(rs.getInt("Customer_ID"));
                String custName = rs.getString("Customer_Name");
                String custAddress = rs.getString("Address");
                String custPostalCode = rs.getString("Postal_Code");
                String custPhone = rs.getString("Phone");
                LocalDateTime creationDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                Integer divisionID = rs.getInt("Division_ID");


                Customer customer = new Customer(custID, custName, custAddress, custPostalCode, custPhone, creationDate, createdBy, lastUpdate, lastUpdatedBy, divisionID);

                customers.add(customer);
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }

        return customers;
    }

    /**
     * accepts a nearly-complete appointment object, and adds new record to the database. accepts a nearly-complete appointment object, and adds new record to the database. Once inserted, database will automatically generate an ApptID, which we add to our initial record to ensure data matches between application and database
     * @param customer record to add (custID is null until key is returned)
     */
    public void add(Customer customer) {
        String custName = customer.getCustName();
        String custAddress = customer.getCustAddress();
        String custPostalCode = customer.getCustPostalCode();
        String custPhone = customer.getCustPhone();
        LocalDateTime creationDate = customer.getCreationDate();
        String createdBy = customer.getCreatedBy();
        LocalDateTime lastUpdate = customer.getLastUpdate();
        String lastUpdatedBy = customer.getLastUpdatedBy();
        Integer divisionID = customer.getDivisionID();

        try {
            String query = "INSERT INTO customers VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; // SQL query

            PreparedStatement ps = JDBC.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS); // Prepared Statement
            ps.setString(1, custName);
            ps.setString(2, custAddress);
            ps.setString(3, custPostalCode);
            ps.setString(4, custPhone);
            ps.setTimestamp(5, Timestamp.valueOf(creationDate));
            ps.setString(6, createdBy);
            ps.setTimestamp(7, Timestamp.valueOf(lastUpdate));
            ps.setString(8, lastUpdatedBy);
            ps.setInt(9, divisionID);

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            customer.setCustID(rs.getInt(1));

            customers.add(customer);
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, Main.rb.getString("failedtoaddcustomer"));
        }


    }

    /**
     * update finds a matching record in our database, then updates fields to match the supplied appointment
     * @param customer customer to update
     */
    public void update(Customer customer) {
        String custName = customer.getCustName();
        String custAddress = customer.getCustAddress();
        String custPostalCode = customer.getCustPostalCode();
        String custPhone = customer.getCustPhone();
        LocalDateTime creationDate = customer.getCreationDate();
        String createdBy = customer.getCreatedBy();
        LocalDateTime lastUpdate = customer.getLastUpdate();
        String lastUpdatedBy = customer.getLastUpdatedBy();
        Integer divisionID = customer.getDivisionID();

        System.out.println(customer.getCustID().getValue());

        try {
            String query = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(query);

            ps.setString(1, custName);
            ps.setString(2, custAddress);
            ps.setString(3, custPostalCode);
            ps.setString(4, custPhone);
            ps.setTimestamp(5, Timestamp.valueOf(creationDate));
            ps.setString(6, createdBy);
            ps.setTimestamp(7, Timestamp.valueOf(lastUpdate));
            ps.setString(8, lastUpdatedBy);
            ps.setInt(9, divisionID);
            ps.setInt(10,  customer.getCustID().getValue().intValue());

            ps.execute();
            System.out.println("Customer was updated");

        } catch (SQLException se) {
            se.printStackTrace();
        }


    }

    /**
     * deletes matching record from database
     * @param customer customer to delete
     */
    public void delete(Customer customer) {
        try {
            String query = "DELETE FROM customers WHERE Customer_ID = " + customer.getCustID().getValue();

            PreparedStatement ps = JDBC.getConnection().prepareStatement(query);

            ps.execute();
            customers.remove(customer);
        } catch (SQLException se) {
            se.printStackTrace();
        }

    }

}
