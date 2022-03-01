package apptsys.model;

import java.time.LocalDateTime;

public class User {
        private int userID;
        private String userName;
        private String userPassword;
        private LocalDateTime creationDate;
        private String createdBy;
        private LocalDateTime lastUpdate;
        private String lastUpdatedBy;


        public User(int userID, String userName, String userPassword, LocalDateTime creationDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy) {
            this.userID = userID;
            this.userName = userName;
            this.userPassword = userPassword;
            this.creationDate = creationDate;
            this.createdBy = createdBy;
            this.lastUpdate = lastUpdate;
            this.lastUpdatedBy = lastUpdatedBy;
        }

        public int getUserID() { return userID; }
        public String getUserName() { return userName; }
        public String getUserPassword() { return userPassword; }
}
