package apptsys.view_controller;

import apptsys.Main;
import apptsys.model.Appointment;
import apptsys.model.Country;
import apptsys.model.Customer;
import apptsys.model.Division;
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

/**
 * Provides programmatic support to the Manage Appointments Screen, allowing for interactivity.
 */
public class ManageCustomersController implements Initializable {


    public TableView<Customer> custTable;
    public TableColumn<Customer, Integer> custIDColumn;
    public TableColumn<Customer, String> custNameColumn;
    public TableColumn<Customer, String> custAddressColumn;
    public TableColumn<Customer, String> custPostalCodeColumn;
    public TableColumn<Customer, String> custPhoneColumn;
    public TableColumn<Customer, LocalDateTime> creationDateColumn;
    public TableColumn<Customer, String> createdByColumn;
    public TableColumn<Customer, LocalDateTime> lastUpdateColumn;
    public TableColumn<Customer, String> updatedByColumn;
    public TableColumn<Customer, Integer> divisionIDColumn;
    public Button addCustButton;
    public Button updateCustButton;
    public Button deleteCustButton;
    public Pane modifyDataPane;
    public TextField custNameField;
    public TextField custAddressField;
    public TextField custPostalCodeField;
    public TextField custPhoneField;
    public TextField custIDField;
    public ComboBox<Country> countryComboBox;
    public ComboBox<Division> divisionComboBox;
    public Button saveCustButton;
    public Button cancelButton;
    public Button returnToMainButton;

    public ObservableList<Country> countriesList = Main.countriesDAO.getAll();
    public ObservableList<Division> divisionsList = Main.divisionsDAO.getAll();

    private String addOrUpdate = null;

    /**
     * intializes UI
     * Lambda utilized to create cellValueFactory, thus allowing for ObservableValue to automatically update in table view (implemented to assist with generated ID keys from DB)
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle){
        custTable.setItems(Main.customersDAO.getAll());
        countryComboBox.setItems(countriesList);
        divisionComboBox.setItems(divisionsList);

        custIDColumn.setCellValueFactory(cellData -> cellData.getValue().getCustID());  // Lambda for  cellData to automatically update custID after DAO changes

        custNameColumn.setCellValueFactory(new PropertyValueFactory<>("custName"));
        custAddressColumn.setCellValueFactory(new PropertyValueFactory<>("custAddress"));
        custPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("custPhone"));
        custPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("custPostalCode"));
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        createdByColumn.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        lastUpdateColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        updatedByColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        divisionIDColumn.setCellValueFactory(new PropertyValueFactory<>("divisionID"));

    }

    /**
     * responds to button press, revealing input fields and tracking our "add or update mode"
     * @param actionEvent
     */
    public void onAddCust(ActionEvent actionEvent) {
        addOrUpdate = "Add";
        clearTextFields();
        modifyDataPane.setDisable(false);

    }

    /**
     * responds to button press, populating input fields with selected customer data and tracking our "add or update mode"
     * @param actionEvent
     */
    public void onUpdateCust(ActionEvent actionEvent) {
        addOrUpdate = "Update";
        Customer customer = custTable.getSelectionModel().getSelectedItem();

        custIDField.setText(customer.getCustID().getValue().toString());
        custNameField.setText(customer.getCustName());
        custPhoneField.setText(customer.getCustPhone());
        custAddressField.setText(customer.getCustAddress());
        custPostalCodeField.setText(customer.getCustPostalCode());

        // Lambda utilized with filters (Predicate)
        Division custDiv = divisionsList.stream().filter(d -> d.getDivisionID() == customer.getDivisionID()).findFirst().get();

        // Uses return of previous filter to filter countries
        countryComboBox.setValue(countriesList.stream().filter(c -> c.getCountryID() == custDiv.getCountryID()).findFirst().get());
        divisionComboBox.setValue(custDiv);

        modifyDataPane.setDisable(false);

    }

    /**
     * responds to button press, and utilizes CustomersDAO to save/update appointment.
     * @param actionEvent
     */
    public void onSaveCust(ActionEvent actionEvent) {
        System.out.println("Saving customer.");

        String custName = custNameField.getText();
        String custAddress = custAddressField.getText();
        String custPostalCode = custPostalCodeField.getText();
        String custPhone = custPhoneField.getText();
        LocalDateTime creationDate = LocalDateTime.now().withNano(0);
        String createdBy = Main.activeUser.getUserName();
        LocalDateTime lastUpdate = LocalDateTime.now().withNano(0);
        String lastUpdatedBy = Main.activeUser.getUserName();
        Integer divisionID = divisionComboBox.getSelectionModel().getSelectedItem().getDivisionID();

        try {

            if (addOrUpdate == "Add") {
                Customer customer = new Customer(null, custName, custAddress, custPostalCode, custPhone, creationDate, createdBy, lastUpdate, lastUpdatedBy, divisionID);

                Main.customersDAO.add(customer);

            } else if (addOrUpdate  == "Update") {
                ObservableValue<Integer> custID = new ReadOnlyObjectWrapper<>(Integer.parseInt(custIDField.getText()));

                Customer customer = new Customer(custID, custName, custAddress, custPostalCode, custPhone, creationDate, createdBy, lastUpdate, lastUpdatedBy, divisionID);

                Main.customersDAO.update(customer);
            }


            modifyDataPane.setDisable(true);

        } catch (Exception e) {
            e.printStackTrace();

        }

        custTable.setItems(Main.customersDAO.getAll());
        clearTextFields();
    }

    /**
     * responds to button press, hiding input fields
     * @param actionEvent
     */
    public void onCancelCust(ActionEvent actionEvent) {
        modifyDataPane.setDisable(true);
        clearTextFields();
    }

    /**
     * reponds to button press, utilizing CustomersDAO to delete selected customer
     * Lambda utilized to filter ObservableList
     * @param actionEvent
     */
    public void onDeleteCust(ActionEvent actionEvent) {
        ObservableList<Appointment> appointments = Main.appointmentsDAO.getAll();
        Customer customer = custTable.getSelectionModel().getSelectedItem();

        List<Appointment> filteredApptsList = appointments.stream()
                .filter(a -> a.getApptCustomerID() == customer.getCustID().getValue().intValue())
                .collect(Collectors.toList());

        if (filteredApptsList.size() == 0) {
            Main.customersDAO.delete(customer);
            JOptionPane.showMessageDialog(null, "Customer was deleted.");
        } else {
            JOptionPane.showMessageDialog(null, "Remove all related appointments before deleting customer.");
        }

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
     * helper function to clear text fields
     */
    public void clearTextFields() { // Helper function
        custNameField.clear();
        custPhoneField.clear();
        custAddressField.clear();
        custPostalCodeField.clear();
        countryComboBox.getSelectionModel().select(0);
        divisionComboBox.getSelectionModel().select(0);
    }

    /**
     * responds to combobox, filtering Divisions to show only those which match the selected country
     * Lambda utilized to filter ObservableList
     * @param actionEvent
     */
    public void onCountryBox(ActionEvent actionEvent) {
        Country selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();
        ObservableList<Division> newDivList = divisionsList.stream().filter(d -> d.getCountryID() == selectedCountry.getCountryID()).collect(Collectors.toCollection(FXCollections::observableArrayList));
        divisionComboBox.setItems(newDivList);
    }
}