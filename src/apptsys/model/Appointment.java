package apptsys.model;

import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;

import java.time.*;
import java.sql.*;

public class Appointment {
   private ObservableValue<Integer> apptID;
   private String title;
   private String desc;
   private String location;
   private String type;
   private LocalDateTime startDate;
   private LocalDateTime endDate;
   private LocalDateTime creationDate;
   private String createdBy;
   private LocalDateTime lastUpdate;
   private String lastUpdatedBy;
   private Integer customerID;
   private Integer userID;
   private Integer contactID;

   public Appointment (ObservableValue<Integer> apptID, String title, String desc, String location, String type, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime creationDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int customerID, int userID, int contactID)
   {
       this.apptID = apptID;
       this.title = title;
       this.desc = desc;
       this.location = location;
       this.type = type;
       this.startDate = startDate;
       this.endDate = endDate;
       this.creationDate = creationDate;
       this.createdBy = createdBy;
       this.lastUpdate = lastUpdate;
       this.lastUpdatedBy = lastUpdatedBy;
       this.customerID = customerID;
       this.userID = userID;
       this.contactID = contactID;

   }

    public ObservableValue<Integer> getApptID() { return apptID; }
    public String getApptTitle() { return title; }
    public String getApptDesc() { return desc; }
    public String getApptLocation() { return location; }
    public String getApptType() { return type; }
    public LocalDateTime getApptEndDate() { return endDate; }
    public LocalDateTime getApptStartDate() { return startDate; }
    public LocalDateTime getApptCreationDate() { return creationDate; }
    public String getApptCreatedBy() { return createdBy; }
    public LocalDateTime getApptLastUpdate() { return lastUpdate; }
    public String getApptLastUpdatedBy() { return lastUpdatedBy; }
    public Integer getApptCustomerID() { return customerID; }
    public Integer getApptUserID() { return userID; }
    public Integer getApptContactID() { return contactID; }

    public void setApptID(int newID) { this.apptID = new ReadOnlyObjectWrapper<>(newID); }

    @Override
    public String toString(){ return apptID.getValue().intValue() + ", " + title + ", " + type + ", " + desc + ", " +  startDate + ", " + endDate  + ", " + customerID; }



}
