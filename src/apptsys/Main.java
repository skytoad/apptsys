package apptsys;

import apptsys.DAO.*;
import apptsys.model.Appointment;
import apptsys.model.User;
import apptsys.view_controller.MainScreenController;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.*;

import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Tyler Astle
 */

/**
 * Main is responsible for initializing our resource bundle, all DAOs,keeping track of our active user, and loading the Log In screen.
 */
public class Main extends Application {

    public static ResourceBundle rb = ResourceBundle.getBundle("apptsys/baseRB", Locale.getDefault());

    public static AppointmentsDAO appointmentsDAO = new AppointmentsDAO();
    public static ContactsDAO contactsDAO = new ContactsDAO();
    public static CountriesDAO countriesDAO = new CountriesDAO();
    public static CustomersDAO customersDAO = new CustomersDAO();
    public static DivisionsDAO divisionsDAO = new DivisionsDAO();
    public static UsersDAO usersDAO = new UsersDAO();

    public static User activeUser;

    /**
     * Start is where we'll load the Log In screen
     * @param primaryStage the only stage we'll use throughout the application
     * @throws Exception such as FileNotFound if FXMLLoader fails
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        JDBC.makeConnection();

        Parent root = FXMLLoader.load(getClass().getResource("view_controller/LogIn.fxml"));
        primaryStage.setTitle("Appointment Scheduling System");
        primaryStage.setScene(new Scene(root, 600, 250));
        primaryStage.centerOnScreen();
        primaryStage.show();

    }


    public static void main(String[] args) { launch(args); }

}
