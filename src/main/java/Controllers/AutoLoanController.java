package Controllers;

import Models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AutoLoanController {

    private List<FixedRateLoan> loanList = new ArrayList<>();
    private FixedRateLoan selectedLoan;

    // Customer Information Attributes
    @FXML private TextField nameTF;
    @FXML private TextField phoneTF;
    @FXML private ComboBox<String> provinceComB;
    @FXML private TextField cityTF;

    //Vehicle Information Attributes
    @FXML private ToggleGroup vehicleTypeGroup;
    @FXML private RadioButton carRB;
    @FXML private RadioButton truckRB;
    @FXML private RadioButton vanRB;
    @FXML private ToggleGroup vehicleAgeGroup;
    @FXML private RadioButton newRB;
    @FXML private RadioButton usedRB;
    @FXML private TextField priceTF;

    // Loan Information Attributes
    @FXML private TextField downPaymentTF;
    @FXML private RadioButton interest099RB;
    @FXML private RadioButton interest199RB;
    @FXML private RadioButton interest299RB;
    @FXML private RadioButton interestOtherRB;
    @FXML private ToggleGroup interestRateGroup;
    @FXML private TextField otherInterestRateTF;
    @FXML private Slider loanDurationSlider;
    @FXML private ToggleGroup paymentFreqGroup;
    @FXML private RadioButton weeklyRB;
    @FXML private RadioButton biWeeklyRB;
    @FXML private RadioButton monthlyRB;

    //Buttons
    @FXML private Button calculateBtn;
    @FXML private Button saveRatesBtn;
    @FXML private Button amortizationBtn;

    // Status
    @FXML private Label statusLabel;

    public void initialize() {
        provinceComB.getItems().addAll(
                "Alberta", "British Columbia", "Manitoba", "New Brunswick",
                "Newfoundland and Labrador", "Nova Scotia","Nunavut", "Ontario",
                "Prince Edward Island", "Quebec", "Saskatchewan", "Yukon"
        );

        // Set default value (e.g., Ontario)
        provinceComB.setValue("Ontario");

        calculateBtn.setDisable(true);
        saveRatesBtn.setDisable(true);
        amortizationBtn.setDisable(true);
        otherInterestRateTF.setDisable(true);

        nameTF.textProperty().addListener((obs, oldVal, newVal) -> updateButtons());
        phoneTF.textProperty().addListener((obs, oldVal, newVal) -> updateButtons());
        cityTF.textProperty().addListener((obs, oldVal, newVal) -> updateButtons());
        priceTF.textProperty().addListener((obs, oldVal, newVal) -> updateButtons());
        downPaymentTF.textProperty().addListener((obs, oldVal, newVal) -> updateButtons());

        interestRateGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle)->{
            if (newToggle == interestOtherRB) {
                otherInterestRateTF.setDisable(false);
            } else {
                otherInterestRateTF.setDisable(true);
                otherInterestRateTF.clear(); // Clear if not needed
            }
        });
    }

    public void updateButtons(){
        calculateBtn.setDisable(nameTF.getText().trim().isEmpty() || phoneTF.getText().trim().isEmpty() || cityTF.getText().trim().isEmpty() ||
                priceTF.getText().trim().isEmpty() || downPaymentTF.getText().trim().isEmpty());

        saveRatesBtn.setDisable(nameTF.getText().trim().isEmpty() || phoneTF.getText().trim().isEmpty() || cityTF.getText().trim().isEmpty() ||
                priceTF.getText().trim().isEmpty() || downPaymentTF.getText().trim().isEmpty());

        amortizationBtn.setDisable(nameTF.getText().trim().isEmpty() || phoneTF.getText().trim().isEmpty() || cityTF.getText().trim().isEmpty() ||
                priceTF.getText().trim().isEmpty() || downPaymentTF.getText().trim().isEmpty());

    }

    @FXML void clearBtn(ActionEvent event) {
        clearFields();
    }

    public void clearFields() {
        nameTF.clear();
        phoneTF.clear();
        cityTF.clear();
        provinceComB.setValue("Ontario");
        carRB.setSelected(true);
        newRB.setSelected(true);
        priceTF.clear();
        downPaymentTF.clear();
        interest099RB.setSelected(true);
        loanDurationSlider.setValue(12);
        biWeeklyRB.setSelected(true);
        statusLabel.setText("");
    }

    @FXML void calculateBtn(ActionEvent event) {
        calculateLoan();
    }

    public void calculateLoan() {
        // validate the form input first
        if (!validateForm()) {
            return; // Stop execution if validation fails
        }

        setSelectedLoan();

        // Calculate the loan payment
        double payment = selectedLoan.calculatePayment();

        // Format the output
        String output = "Estimated Fixed Rate Loan Payment: " + String.format("$%.2f ", payment) + ((RadioButton) paymentFreqGroup.getSelectedToggle()).getText();

        // Set the status label
        statusLabel.setText(output);
    }

    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        // Validate phone number (10 digits)
        if (!phoneTF.getText().matches("\\d{10}")) {
            errors.append("Phone number must be exactly 10 digits.\n");
        }

        // Validate price (only digits, positive)
        double price = 0;
        if (!priceTF.getText().matches("\\d+(\\.\\d+)?")) {
            errors.append("Price must be a positive number.\n");
        } else {
            price = Double.parseDouble(priceTF.getText());
            if (price <= 0) {
                errors.append("Price must be greater than zero.\n");
            }
        }

        // Validate down payment (only digits, positive, not greater than price)
        double downPayment = 0;
        if (!downPaymentTF.getText().matches("\\d+(\\.\\d+)?")) {
            errors.append("Down payment must be a positive number.\n");
        } else {
            downPayment = Double.parseDouble(downPaymentTF.getText());
            if (downPayment <= 0) {
                errors.append("Down payment must be greater than zero.\n");
            } else if (downPayment > price) {
                errors.append("Down payment cannot be greater than the price.\n");
            }
        }

        // Validate interest rate (if 'Other' is selected, ensure it's a valid number)
        if (interestOtherRB.isSelected()) {
            if (!otherInterestRateTF.getText().matches("\\d+(\\.\\d+)?")) {
                errors.append("Interest rate must be a valid number.\n");
            }
        }

        // Show errors if any
        if (errors.length() > 0) {
            statusLabel.setText(errors.toString());
            return false;
        }

        return true;
    }

    // Setter for loan
    public void setSelectedLoan() {
        double price = Double.parseDouble(priceTF.getText());
        double downPayment = Double.parseDouble(downPaymentTF.getText());
        double interestRate = getSelectedInterestRate(); // Get the selected interest rate
        int loanDuration = (int) loanDurationSlider.getValue(); // Get the loan duration
        String paymentFrequency = ((RadioButton) paymentFreqGroup.getSelectedToggle()).getText(); // Get the selected payment frequency


        Customer customer = new Customer(
                nameTF.getText(),      // Extract customer name from text field
                phoneTF.getText(),     // Extract phone number from text field
                cityTF.getText(),      // Extract city from text field
                provinceComB.getValue() // Extract province from combo box
        );

        Vehicle vehicle = new Vehicle(
                ((RadioButton) vehicleTypeGroup.getSelectedToggle()).getText(),  // Get vehicle type from selected toggle button
                ((RadioButton) vehicleAgeGroup.getSelectedToggle()).getText(),   // Get vehicle age from selected toggle button
                price  // Get price from the text field
        );

        this.selectedLoan = new FixedRateLoan(
                price,            // The loan amount calculated
                downPayment,      // The down payment entered
                interestRate,     // The selected interest rate
                loanDuration,     // The selected loan duration (in months)
                paymentFrequency, // The selected payment frequency (e.g., monthly, bi-weekly)
                customer,         // The customer object
                vehicle           // The vehicle object
        );
    }

    private double getSelectedInterestRate() {
        if (interest099RB.isSelected()) {
            return 0.99;
        } else if (interest199RB.isSelected()) {
            return 1.99;
        } else if (interest299RB.isSelected()) {
            return 2.99;
        } else if (interestOtherRB.isSelected()) {
            return Double.parseDouble(otherInterestRateTF.getText());
        }
        return 0;
    }

    @FXML void saveRatesBtn(ActionEvent event) {
        saveCurrentRate();
    }

    public void saveCurrentRate() {
        if (!validateForm()){
            return;
        }

        setSelectedLoan();

        loanList.add(selectedLoan);  // Add the loan to the in-memory list

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Loan Saved");
        alert.setHeaderText(null);
        alert.setContentText("The loan has been successfully saved!");
        alert.showAndWait();
    }

    @FXML void showRatesBtn(ActionEvent event) {
        openSavedRatesView();
    }

    public void openSavedRatesView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/workshop3/saved-rates-view.fxml"));
            Parent root = loader.load();

            // Get controller instance
            SavedRateController controller = loader.getController();

            // Pass the loan list to SavedRateController
            controller.setSavedRates(this.getLoanList(),  this::loadSelectedLoan);
            //controller.setSavedRates(loanList, selectedLoan -> loadLoanIntoForm(selectedLoan));

            // Show new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Saved Rates");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSelectedLoan(FixedRateLoan selectedLoan){

        this.selectedLoan = selectedLoan;
        // Customer Information
        nameTF.setText(selectedLoan.getCustomer().getName());
        phoneTF.setText(selectedLoan.getCustomer().getPhone());
        cityTF.setText(selectedLoan.getCustomer().getCity());
        provinceComB.setValue(selectedLoan.getCustomer().getProvince());
        // Vehicle Information
        String type = selectedLoan.getVehicle().getType();
        if (type.equals("Truck")){
            truckRB.setSelected(true);
        } else if (type.equals("Family Van")){
            vanRB.setSelected(true);
        } else {
            carRB.setSelected(true);
        }

        String age = selectedLoan.getVehicle().getAge();
        if (age.equals("Used")){
            usedRB.setSelected(true);
        } else {
            newRB.setSelected(true);
        }

        priceTF.setText(String.valueOf(selectedLoan.getVehicle().getPrice()));

        // Loan Information
        downPaymentTF.setText(String.valueOf(selectedLoan.getDouwnPayment()));
        double interestRate = selectedLoan.getInterestRate();
        if (interestRate == 0.99){
            interest099RB.setSelected(true);
        } else if (interestRate == 1.99){
            interest199RB.setSelected(true);
        } else if (interestRate == 2.99){
            interest299RB.setSelected(true);
        } else {
            otherInterestRateTF.setDisable(false);
            interestOtherRB.setSelected(true);
            otherInterestRateTF.setText(String.valueOf(interestRate));
        }

        loanDurationSlider.setValue(selectedLoan.getDuration());

        // Set Payment Frequency
        String frequency = selectedLoan.getFrequency();
        if (frequency.equals("Monthly")) {
            monthlyRB.setSelected(true);
        } else if (frequency.equals("Bi-Weekly")) {
            biWeeklyRB.setSelected(true);
        } else {
            weeklyRB.setSelected(true);
        }


    }

    @FXML void amortizationBtn(ActionEvent event) {
        openAmortizationView();
    }

    public List<FixedRateLoan> getLoanList() {
        return loanList;
    }

    private void printFormInput(){
        System.out.println("Customer Information:");
        System.out.println("Name: " + nameTF.getText());
        System.out.println("Phone: " + phoneTF.getText());
        System.out.println("City: " + cityTF.getText());
        System.out.println("Province: " + provinceComB.getValue());

        System.out.println("\nVehicle Information:");
        System.out.println("Vehicle Type: " + ((RadioButton) vehicleTypeGroup.getSelectedToggle()).getText());
        System.out.println("Vehicle Age: " + ((RadioButton) vehicleAgeGroup.getSelectedToggle()).getText());
        System.out.println("Price: " + priceTF.getText());

        System.out.println("\nLoan Information:");
        System.out.println("Down Payment: " + downPaymentTF.getText());
        System.out.println("Interest Rate: " + ((RadioButton) interestRateGroup.getSelectedToggle()).getText());

        // Check if "Other Interest Rate" is selected and print its value
        if (interestRateGroup.getSelectedToggle() == interestOtherRB) {
            System.out.println("Other Interest Rate: " + otherInterestRateTF.getText());
        }

        System.out.println("Loan Duration: " + (int) loanDurationSlider.getValue() + " months");
        System.out.println("Payment Frequency: " + ((RadioButton) paymentFreqGroup.getSelectedToggle()).getText());
    }

    public void openAmortizationView() {
        if (!validateForm()){
            return;
        }

        setSelectedLoan();

        calculateLoan();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/workshop3/loan-amortization-view.fxml"));
            Parent root = loader.load();

            // Get the controller
            AmortizationController controller = loader.getController();

            // Generate amortization schedule and set it in the controller
            List<LoanAmortization> schedule = selectedLoan.generateAmortizationSchedule();
            controller.setAmortizationData(schedule);

            // Create a new stage (window)
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Amortization Schedule");
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
