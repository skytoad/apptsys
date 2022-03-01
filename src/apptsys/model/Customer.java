package apptsys.model;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Customer {
    private ObservableValue<Integer> custID;
    private String custName;
    private String custAddress;
    private String custPostalCode;
    private String custPhone;
    private LocalDateTime creationDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int divisionID;

    public Customer (ObservableValue<Integer> custID, String custName, String custAddress, String custPostalCode, String custPhone, LocalDateTime creationDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int divisionID){
        this.custID = custID;
        this.custName = custName;
        this.custAddress = custAddress;
        this.custPostalCode = custPostalCode;
        this.custPhone = custPhone;
        this.creationDate = creationDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionID = divisionID;
    }

    public ObservableValue<Integer> getCustID() { return custID; }
    public String getCustName() { return custName; }
    public String getCustAddress() { return custAddress; }
    public String getCustPostalCode() { return custPostalCode; }
    public String getCustPhone() { return custPhone; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public String getCreatedBy() { return createdBy; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public String getLastUpdatedBy() { return lastUpdatedBy; }
    public int getDivisionID() { return divisionID; }

    public void setCustID(Integer newID) { this.custID = new ReadOnlyObjectWrapper<>(newID); }

    @Override
    public String toString() { return "CustID: " + custID.getValue().intValue() + ", Name: " + custName; }

}

