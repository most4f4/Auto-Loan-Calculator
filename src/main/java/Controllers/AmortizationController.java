package Controllers;

import Models.LoanAmortization;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AmortizationController {


    @FXML
    private Button closeBtn;

    @FXML
    private TableColumn<LoanAmortization, Double> paymentAmountCol;

    @FXML
    private TableColumn<LoanAmortization, Date> paymentDateCol;

    @FXML
    private TableColumn<LoanAmortization, Integer> paymentNoCol;

    @FXML
    private TableColumn<LoanAmortization, Double> interestCol;

    @FXML
    private TableColumn<LoanAmortization, Double> principalPaymentCol;

    @FXML
    private TableColumn<LoanAmortization, Double> remainingBalanceCol;

    @FXML
    private TableView<LoanAmortization> tableview;

    @FXML
    void closeBtn(ActionEvent event) {
        handleClose();
    }

    public void initialize() {

        // Payment No column
        paymentNoCol.setCellValueFactory(new PropertyValueFactory<>("paymentNo"));
        paymentNoCol.setCellFactory(column -> new TableCell<LoanAmortization, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
                setStyle("-fx-alignment: CENTER;");
            }
        });

        // Payment Date column with date formatting
        paymentDateCol.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        paymentDateCol.setCellFactory(column -> new TableCell<LoanAmortization, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
                    setText(dateFormatter.format(item));
                }
                setStyle("-fx-alignment: CENTER;");
            }
        });

        // Payment Amount column with two decimal places formatting
        paymentAmountCol.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        paymentAmountCol.setCellFactory(column -> new TableCell<LoanAmortization, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    DecimalFormat decimalFormatter = new DecimalFormat("0.00");
                    setText(decimalFormatter.format(item));
                }
                setStyle("-fx-alignment: CENTER;");
            }
        });

        // Interest Payment column with two decimal places formatting
        interestCol.setCellValueFactory(new PropertyValueFactory<>("interestPayment"));
        interestCol.setCellFactory(column -> new TableCell<LoanAmortization, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    DecimalFormat decimalFormatter = new DecimalFormat("0.00");
                    setText(decimalFormatter.format(item));
                }
                setStyle("-fx-alignment: CENTER;");
            }
        });

        // Principal Payment column with two decimal places formatting
        principalPaymentCol.setCellValueFactory(new PropertyValueFactory<>("principalPayment"));
        principalPaymentCol.setCellFactory(column -> new TableCell<LoanAmortization, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    DecimalFormat decimalFormatter = new DecimalFormat("0.00");
                    setText(decimalFormatter.format(item));
                }
                setStyle("-fx-alignment: CENTER;");
            }
        });

        // Remaining Balance column with two decimal places formatting
        remainingBalanceCol.setCellValueFactory(new PropertyValueFactory<>("remainingBalance"));
        remainingBalanceCol.setCellFactory(column -> new TableCell<LoanAmortization, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    DecimalFormat decimalFormatter = new DecimalFormat("0.00");
                    setText(decimalFormatter.format(item));
                }
                setStyle("-fx-alignment: CENTER;");
            }
        });
    }


    public void setAmortizationData(List<LoanAmortization> data) {
        // Convert List to ObservableList
        ObservableList<LoanAmortization> observableData = FXCollections.observableArrayList(data);
        // Set the table data
        tableview.getItems().setAll(observableData);
    }

    // This method is used to close the window when the close button is clicked
    public void handleClose() {
        ((Stage) closeBtn.getScene().getWindow()).close();
    }
}
