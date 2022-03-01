package apptsys.DAO;

import apptsys.model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 *  Acts as an interface between our application and our database, accessing Country records
 */

public class CountriesDAO {
    public static ObservableList<Country> countries;

    /**
     * getAll queries the database for all Country records, then creates a POJO for each one and adds to list
     * @return a complete ObservableList with all records found
     */
    public ObservableList<Country> getAll() {
        countries = FXCollections.observableArrayList();

        try {
            String query = "SELECT * FROM countries";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int countryID = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                LocalDateTime creationDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");

                Country country = new Country(countryID, countryName, creationDate, createdBy, lastUpdate, lastUpdatedBy);

                countries.add(country);
            }


        } catch (SQLException se) {

        }

        return countries;
    }

}
