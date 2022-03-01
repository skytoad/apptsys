package apptsys.view_controller;

import apptsys.Main;
import apptsys.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Provides programmatic support to the Log In Screen, allowing for interactivity.
 */
public class LogInController implements Initializable {


    public TextField userIDField;
    public TextField passwordField;
    public Button logInButton;

    public Label mainLabel;
    public Label connectingFromLabel;

    private String username;
    private String password;

    public static List<User> allUsers = Main.usersDAO.getAll();


    /**
     * initializes UI
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainLabel.setText(Main.rb.getString("mainlabel"));
        connectingFromLabel.setText(Main.rb.getString("connectingfrom") + ZoneId.systemDefault());
        userIDField.setPromptText(Main.rb.getString("user"));
        passwordField.setPromptText(Main.rb.getString("password"));
        logInButton.setText(Main.rb.getString("loginbutton"));

        // Putting this here to ensure upcoming message is only shown once
        MainScreenController.upcomingMessageShown = false;

    }

    /**
     * logs input to userID field
     * @param keyEvent event for a key being pressed
     */
    public void onUserIDPressed(KeyEvent keyEvent) {
        username = userIDField.getText();
    }

    /**
     * logs input to password field
     * @param keyEvent event for a key being pressed
     */
    public void onPassPressed(KeyEvent keyEvent) {
        password = passwordField.getText();
    }

    /**
     * on button press, takes username and password field data, and looks for match in User table of DB. If match is found, load Main Screen. Also writes login attempts to .txt file
     * @param actionEvent event for the button being pressed
     * @throws IOException
     */
    public void onLogInButton(ActionEvent actionEvent) throws IOException {
        PrintWriter loginActivityPW =  new PrintWriter(new FileWriter("login_activity.txt", true)); // Credit to Qiang Jin https://stackoverflow.com/questions/9961292/write-to-text-file-without-overwriting-in-java

        loginActivityPW.print("username: " + username + ", " + ZonedDateTime.now() + ", attempt ");

        try {
            User user = allUsers.stream().filter(u -> u.getUserName().equals(username)).findFirst().orElse(null);


            if (password.equals(user.getUserPassword())) {
                Main.activeUser = user;

                loginActivityPW.print("successful" + "\n");

                Stage primaryStage = (Stage) logInButton.getScene().getWindow();
                primaryStage.hide();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/apptsys/view_controller/MainScreen.fxml"));
                Parent root = loader.load();
                primaryStage.setScene(new Scene(root, 1000, 600));
                primaryStage.centerOnScreen();
                primaryStage.show();


            } else {
                JOptionPane.showMessageDialog(null, Main.rb.getString("loginfailed"));
                loginActivityPW.print("failed" + "\n");
            }

        } catch(NullPointerException|FileNotFoundException xe) {
            xe.printStackTrace();
            JOptionPane.showMessageDialog(null, Main.rb.getString("loginfailed"));
            loginActivityPW.print("failed" + "\n");
        }

        loginActivityPW.close();
    }

}
