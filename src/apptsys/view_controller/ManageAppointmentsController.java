package apptsys.view_controller;

import apptsys.Main;
import apptsys.model.*;
import apptsys.utilities.ScheduleHelper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

/**
 * Provides programmatic support to the Manage Appointments Screen, allowing for interactivity.
 */
public class ManageAppointmentsController implements Initializable {

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
    public ObservableList<Contact> contactList = FXCollections.observableArrayList();

    public Pane modifyDataPane;
    public TextField apptIDField;
    public Button addApptButton;
    public Button updateApptButton;
    public Button saveApptButton;
    public Button cancelButton;
    public Button deleteApptButton;
    public ComboBox contactComboBox;
    public TextField apptLocationField;
    public TextField startTimeField;
    public TextField endTimeField;
    public TextField apptTitleField;
    public TextField apptDescField;
    public TextField custIDField;
    public TextField userIDField;
    public Button returnToMainButton;
    public TextField apptTypeField;

    private String addOrUpdate = null;

    /**
     * initalizes UI
     * Lambda utilized to create cellValueFactory, thus allowing for ObservableValue to automatically update in table view (implemented to assist with generated ID keys from DB)
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        apptsList = Main.appointmentsDAO.getAll();
        contactList = Main.contactsDAO.getAll();
        contactComboBox.setItems(contactList);
        System.out.println(contactList);

        apptsTable.setItems(apptsList);

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

    }

    /**
     * responds to button press, revealing input fields and tracking our "add or update mode"
     * @param actionEvent
     */
    public void onAddAppt(ActionEvent actionEvent) {
        addOrUpdate = "Add";
        clearTextFields();
        modifyDataPane.setDisable(false);

    }

    /**
     * responds to button press, populating input fields with selected appointment data and tracking our "add or update mode"
     * Lambda utilized to filter ObservableList
     * @param actionEvent
     */
    public void onUpdateAppt(ActionEvent actionEvent) {
        addOrUpdate = "Update";
        Appointment appointment = apptsTable.getSelectionModel().getSelectedItem();

        apptIDField.setText(appointment.getApptID().getValue().toString());
        apptTitleField.setText(appointment.getApptTitle());
        apptDescField.setText(appointment.getApptDesc());
        apptLocationField.setText(appointment.getApptLocation());
        apptTypeField.setText(appointment.getApptType());
        startTimeField.setText(appointment.getApptStartDate().toString());
        endTimeField.setText(appointment.getApptEndDate().toString());
        custIDField.setText(appointment.getApptCustomerID().toString());
        userIDField.setText(appointment.getApptUserID().toString());

        Contact apptContact = contactList.stream().filter(c -> c.getContactID() == appointment.getApptContactID()).findFirst().get();
        contactComboBox.setValue(apptContact);

        modifyDataPane.setDisable(false);
    }

    /**
     * responds to button press, and utilizes AppointmentsDAO to save/update appointment.
     * @param actionEvent
     */
    public void onSaveAppt(ActionEvent actionEvent) {
        ObservableValue<Integer> tempID = new ReadOnlyObjectWrapper<>(1000);
        String title = apptTitleField.getText();
        String desc = apptDescField.getText();
        String location = apptLocationField.getText();
        String type = apptTypeField.getText();
        LocalDateTime creationDate = LocalDateTime.now().withNano(0);
        String createdBy = Main.activeUser.getUserName();
        LocalDateTime lastUpdate = LocalDateTime.now().withNano(0);
        String lastUpdatedBy = Main.activeUser.getUserName();
        Integer customerID = Integer.parseInt(custIDField.getText());
        Integer userID = Integer.parseInt(userIDField.getText());
        Integer contactID = contactList.get(contactComboBox.getSelectionModel().getSelectedIndex()).getContactID();

        try {
            LocalDateTime startTime = LocalDateTime.parse(startTimeField.getText(), ISO_LOCAL_DATE_TIME);
            LocalDateTime endTime = LocalDateTime.parse(endTimeField.getText(), ISO_LOCAL_DATE_TIME);

            if (addOrUpdate == "Add") {
                Appointment appointment = new Appointment(tempID, title, desc, location, type, startTime, endTime, creationDate, createdBy, lastUpdate, lastUpdatedBy, customerID, userID, contactID);

                if (validateInput(appointment)) {
                    System.out.println("Adding appointment...");
                    Main.appointmentsDAO.add(appointment);
                } else {
                    return;
                }

            } else if (addOrUpdate  == "Update") {
                ObservableValue<Integer> apptID = new ReadOnlyObjectWrapper<>(Integer.parseInt(apptIDField.getText()));

                Appointment appointment = new Appointment(apptID, title, desc, location, type, startTime, endTime, creationDate, createdBy, lastUpdate, lastUpdatedBy, customerID, userID, contactID);

                if (validateInput(appointment)) {
                    Main.appointmentsDAO.update(appointment);
                } else {
                    return;
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Improperly formatted time.");
            return;
        }

        modifyDataPane.setDisable(true);
        clearTextFields();
        apptsTable.setItems(Main.appointmentsDAO.getAll());
    }

    /**
     * responds to button press, hiding and clearing input fields
     * @param actionEvent
     */
    public void onCancelAppt(ActionEvent actionEvent) {
        modifyDataPane.setDisable(true);
        clearTextFields();
    }

    /**
     * responds to button press, utilizing AppointmentsDAO to delete selected appointment
     * @param actionEvent
     */
    public void onDeleteAppt(ActionEvent actionEvent) {
        Appointment appointment = apptsTable.getSelectionModel().getSelectedItem();
        JOptionPane.showMessageDialog(null, appointment.getApptID().getValue() + ") " + appointment.getApptType() + " was deleted.");
        Main.appointmentsDAO.delete(appointment);


    }

    /**
     * responds to button press, re-loading the Main Screen
     * @param actionEvent
     */
    public void onReturnToMain(ActionEvent actionEvent) {
        try {
            Stage primaryStage = (Stage) returnToMainButton.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/apptsys/view_controller/MainScreen.fxml"));

            Parent root = loader.load();

            primaryStage.setScene(new Scene(root, 1000, 600));
            primaryStage.show();

        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load apptsys.Main Screen");
        }

    }

    /**
     * helper function to validate input before submitting appointment records to DAO
     * Lambda utilized to filter ObservableList
     * @param appointment
     * @return
     */
    public boolean validateInput(Appointment appointment) {
        if (ScheduleHelper.checkOverlap(appointment)) {
            JOptionPane.showMessageDialog(null, "Failed to add: appointment time overlaps with existing appointment.");
            return false;
        }

        if (!ScheduleHelper.checkWithinBusinessHours(appointment)) {
            JOptionPane.showMessageDialog(null, "Failed to add: appointment time outside of business hours.");
            return false;
        }

        if (!appointment.getApptStartDate().isBefore(appointment.getApptEndDate())) {
            JOptionPane.showMessageDialog(null, "End time must be after start time.");
            return false;
        }

        ObservableList<Customer> customers = Main.customersDAO.getAll();
        List<Customer> filteredCusts = customers.stream()
                .filter(c -> c.getCustID().getValue().intValue() == appointment.getApptCustomerID().intValue())
                .collect(Collectors.toList());

        if (filteredCusts.size() < 1) {
            JOptionPane.showMessageDialog(null, "Customer not found.");
            return false;
        }

        if (appointment.getApptUserID() != 1 && appointment.getApptUserID() != 2) {
            JOptionPane.showMessageDialog(null, "User not found.");
            return false;
        }

        return true;
    }

    /**
     * helper function to clear all text fields
     */
    public void clearTextFields() { // Helper function
        apptIDField.setText("N/A");
        apptTitleField.clear();
        apptDescField.clear();
        apptLocationField.clear();
        apptTypeField.clear();
        startTimeField.clear();
        endTimeField.clear();
        contactComboBox.getSelectionModel().select(0);
        custIDField.clear();
        userIDField.clear();

    }

}