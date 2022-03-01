package apptsys.view_controller;

import apptsys.Main;
import apptsys.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javax.swing.*;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides programmatic support to the Manage Appointments Screen, allowing for interactivity.
 */
public class ViewReportsController {
    public Button appointmentByTypeButton;
    public Button scheduleByContactButton;
    public Button customersByCountryButton;
    public Label reportTextArea;
    public String reportText;
    public Button returnToMainButton;

    /**
     * responds to button press, generating Appointments by Type and Month report
     * Lambda utilized to filter ObservableList
     * @param actionEvent
     */
    public void onAppointmentsButton(ActionEvent actionEvent) {
        reportText = "";

        ObservableList<Appointment> appointments = Main.appointmentsDAO.getAll();
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "October", "November", "December"};

        for (String month : months) {
            appointments = Main.appointmentsDAO.getAll();

            reportText = reportText.concat(month + "\n");

            appointments = appointments.stream()
                    .filter(a -> a
                            .getApptStartDate()
                            .getMonth()
                            .getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                            .equals(month))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));


            Map<String, Long> apptTypeCount = appointments.stream().collect(Collectors.groupingBy(Appointment::getApptType, Collectors.counting()));

            reportText = reportText.concat(apptTypeCount.toString() + "\n\n");

        }

        reportTextArea.setText(reportText);
    }

    /**
     * responds to button press, generating Schedule for each Contact report
     * @param actionEvent
     */
    public void onScheduleButton(ActionEvent actionEvent) {
        reportText = "";
        ObservableList<Contact> contacts = Main.contactsDAO.getAll();
        ObservableList<Appointment> appointments = Main.appointmentsDAO.getAll();

        for (Contact contact : contacts) {
            reportText = reportText.concat(contact.toString() + "\n");
            for (Appointment appointment : appointments) {
                if (appointment.getApptContactID() == contact.getContactID()) {
                    reportText = reportText.concat("      " + appointment + "\n");
                }
            }
            reportText = reportText.concat("\n");
        }

        reportTextArea.setText(reportText);

    }

    /**
     * responds to button press, generating Customers by Country report
     * Lambda utilized to filter ObservableList
     * @param actionEvent
     */
    public void onCustomersButton(ActionEvent actionEvent) {
        reportText = "";
        ObservableList<Country> countries = Main.countriesDAO.getAll();
        ObservableList<Division> divisions = Main.divisionsDAO.getAll();
        ObservableList<Customer> customers = Main.customersDAO.getAll();

        for (Country country : countries) {
            reportText = reportText.concat(country.toString() + "\n");

            for (Customer customer : customers) {
                Division customerDiv = divisions.stream().filter(d -> d.getDivisionID() == customer.getDivisionID()).findFirst().get();

                if (customerDiv.getCountryID() == country.getCountryID()) {
                    reportText = reportText.concat(customer + "\n");
                }
            }

            reportText = reportText.concat("\n");
        }

        reportTextArea.setText(reportText);

    }

    /**
     * responds to button press, re-loading Main Screen
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
}
