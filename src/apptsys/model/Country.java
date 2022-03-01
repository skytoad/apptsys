package apptsys.model;

import java.time.LocalDateTime;

public class Country {
    private int countryID;
    private String countryName;
    private LocalDateTime creationDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;

    public Country (int countryID, String countryName, LocalDateTime creationDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy) {
        this.countryID = countryID;
        this.countryName = countryName;
        this.creationDate = creationDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getCountryID() { return countryID; }
    public String getCountryName() { return countryName; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public String getCreatedBy() { return createdBy; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public String getLastUpdatedBy() { return lastUpdatedBy; }

    @Override
    public String toString() {
        return countryID + ": " + countryName;
    }

}
