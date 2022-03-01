package apptsys.view_controller;

import apptsys.Main;
import apptsys.model.Appointment;
import apptsys.utilities.ScheduleHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.collections.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Provides programmatic support to the Main Screen, allowing for interactivity.
 */
public class MainScreenController implements Initializable {

    public ToggleGroup MonthWeek;

    public Button manageCustomersButton;
    public Button manageAppointmentsButton;

    public TableView<Appointment> apptsTable;
    public TableColumn<Appointment, Integer> apptIDColumn;
    public TableColumn<Appointment, String> titleColumn;
    public TableColumn<Appointment, String> descColumn;
    public TableColumn<Appointment, String> locationColumn;
    public TableColumn<Appointment, String> typeColumn;
    public TableColumn<Appointment, LocalDateTime> startColumn;
    public TableColumn<Appointment, LocalDateTime> endColumn;
    public TableColumn<Appointment, Integer> customerIDColumn;
    public TableColumn<Appointment, Integer> userIDColumn;
    public TableColumn<Appointment, String> contactColumn;

    public ObservableList<Appointment> apptsList = FXCollections.observableArrayList();

    public static boolean upcomingMessageShown;

    /**
     * intializes UI, and checks for upcoming appointments
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Fill list and tableview, defaulting to current month
        onMonthRadio(null);

        // Set cellfactories for each column
        apptIDColumn.setCellValueFactory(cellData -> cellData.getValue().getApptID());
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("apptTitle"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("apptDesc"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("apptLocation"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("apptType"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("apptContactID"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("apptStartDate"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("apptEndDate"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("apptCustomerID"));
        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("apptUserID"));

        // Check for upcoming appointments
        if (!upcomingMessageShown) { ;

            if (ScheduleHelper.getUpcoming(Main.activeUser.getUserID()).size() > 0) {
                JOptionPane.showMessageDialog(null, "Upcoming appointments:\n" + ScheduleHelper.getUpcoming(Main.activeUser.getUserID()));
            } else {
                JOptionPane.showMessageDialog(null, "No upcoming appointments.");
            }

            upcomingMessageShown = true;
        }

    }

    /**
     * on button press, loads Manage Customers screen
     */
    public void onManageCustomers() {
        try {
            Stage primaryStage = (Stage) manageAppointmentsButton.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/apptsys/view_controller/ManageCustomers.fxml"));

            Parent root = loader.load();

            primaryStage.setScene(new Scene(root, 1000, 600));
            primaryStage.show();

        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load apptsys.Manage Customers");
        }
    }

    /**
     * on button press, loads Manage Appointments screen
     */
    public void onManageAppointments() {
        try {
            Stage primaryStage = (Stage) manageAppointmentsButton.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/apptsys/view_controller/ManageAppointments.fxml"));

            Parent root = loader.load();

            primaryStage.setScene(new Scene(root, 1000, 600));
            primaryStage.show();

        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load apptsys.Manage Appointments");
        }
    }

    public void onViewReports(ActionEvent actionEvent) {
        try {
            Stage primaryStage = (Stage) manageAppointmentsButton.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/apptsys/view_controller/ViewReports.fxml"));

            Parent root = loader.load();

            primaryStage.setScene(new Scene(root, 600, 800));
            primaryStage.centerOnScreen();
            primaryStage.show();

        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load apptsys.Manage Customers");
        }

    }


    /**
     * on radio toggle, filters appointments to show only those that match userID and are within the same calendar month (NOT 28/29/30/31 days)
     * Lambdas utilized to filter ObservableList
     *  @param actionEvent
     */
    public void onMonthRadio(ActionEvent actionEvent) {
        apptsList = Main.appointmentsDAO.getAll();

        List<Appointment> filteredAppts = apptsList.stream()
                .filter(a -> a.getApptUserID() == Main.activeUser.getUserID())
                .filter(a -> a.getApptStartDate().getMonth() == LocalDate.now().getMonth())
                .collect(Collectors.toList());

        apptsTable.setItems(FXCollections.observableList(filteredAppts));
    }

    /**
     * on radio toggle, filters appointments to show only those that match userID and are within 7 days of current time
     * Lambdas utilized to filter ObservableList
     * @param actionEvent
     */
    public void onWeekRadio(ActionEvent actionEvent) {
        apptsList = Main.appointmentsDAO.getAll();

        List<Appointment> filteredAppts = apptsList.stream()
                .filter(a -> a.getApptUserID() == Main.activeUser.getUserID())
                .filter(a -> a.getApptStartDate().getDayOfYear() - LocalDate.now().getDayOfYear() <= 7)
                .filter(a -> a.getApptStartDate().getDayOfYear() - LocalDate.now().getDayOfYear() >= 0)
                .collect(Collectors.toList());

        apptsTable.setItems(FXCollections.observableList(filteredAppts));
    }
}
