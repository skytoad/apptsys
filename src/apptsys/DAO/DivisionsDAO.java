package apptsys.DAO;

import apptsys.model.Country;
import apptsys.model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 *  Acts as an interface between our application and our database, accessing Division records
 */

public class DivisionsDAO {

    public static ObservableList<Division> divisions;

    /**
     * getAll queries the database for all Appointment records, then creates a POJO for each one and adds to list
     * @return a complete ObservableList with all records found
     */
    public ObservableList<Division> getAll() {
        divisions = FXCollections.observableArrayList();

        try {
            String query = "SELECT * FROM first_level_divisions";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int divisionID = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                LocalDateTime creationDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int countryID = rs.getInt("Country_ID");

                Division division = new Division(divisionID, divisionName, creationDate, createdBy, lastUpdate, lastUpdatedBy, countryID);

                divisions.add(division);
            }


        } catch (SQLException se) {

        }

        return divisions;
    }
}
