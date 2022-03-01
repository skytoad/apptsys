package apptsys.model;

import java.time.LocalDateTime;

public class Division {
    private int divisionID;
    private String divisionName;
    private LocalDateTime creationDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int countryID;

    public Division(int divisionID, String divisionName, LocalDateTime creationDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int countryID) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.creationDate = creationDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryID = countryID;
    }

    public int getDivisionID() { return divisionID; }
    public String getDivisionName() { return divisionName; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public String getCreatedBy() { return createdBy; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public String getLastUpdatedBy() { return lastUpdatedBy; }
    public int getCountryID() { return countryID; }

    @Override
    public String toString() {
        return divisionID + ": " + divisionName;
    }
}
