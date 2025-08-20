package Controllers;

import Models.Login;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.IOException;


public class LoginController {

    @FXML
    private Button loginBtn;
    @FXML
    private Label loginStatus;
    @FXML
    private PasswordField passwordPF;
    @FXML
    private TextField usernameTF;


    public void initialize(){

        // Disable the button initially
        loginBtn.setDisable(true);

        usernameTF.textProperty().addListener((obs, oldVal, newVal) -> updateLoginButton());
        passwordPF.textProperty().addListener((obs, oldVal, newVal) -> updateLoginButton());
    }

    private void updateLoginButton() {
        loginBtn.setDisable(usernameTF.getText().trim().isEmpty() || passwordPF.getText().trim().isEmpty());
    }

    @FXML
    void onLoginClick(ActionEvent event) {
        handleLogin();
    }

    public void handleLogin(){
        Login loginAttempt = new Login(usernameTF.getText(), passwordPF.getText());

        if (loginAttempt.validate()){
            loginStatus.setText("Login successful!");

            // Create a PauseTransition of 1 second (1000 milliseconds)
            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));

            // Set an action to execute after the pause
            pause.setOnFinished(event -> {
                loadLoanApplicationView(); // This will be called after the 1-second pause
            });

            // Start the pause
            pause.play();

        }  else {
            loginStatus.setText("Wrong username or password!");
            usernameTF.requestFocus();
            usernameTF.selectAll();
        }
    }

    private void loadLoanApplicationView() {
        try {
            // Load the loan application view
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ca/workshop3/loan-application-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // get the current Stage (the window) that the button (loginBtn) belongs to.
            Stage stage = (Stage) loginBtn.getScene().getWindow();
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
