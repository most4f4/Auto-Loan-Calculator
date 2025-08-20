package ca.workshop3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AutoLoanApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        // Display Login View initially
        FXMLLoader fxmlLoader = new FXMLLoader(AutoLoanApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Auto Loan System");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}