package Controllers;

import Models.FixedRateLoan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.Consumer;

public class SavedRateController {

    private List<FixedRateLoan> loanList;
    private Consumer<FixedRateLoan> onLoanSelected; // Callback when selecting a loan

    @FXML
    private Button closeBtn;

    @FXML
    private ListView<String> loansLV;

    @FXML
    void closeBtn(ActionEvent event) {
        closeWindow();
    }

    public void initialize() {
        loansLV.setOnMousePressed(e -> {
            if (e.getClickCount() == 2) {
                handleDoubleClick(e);
            }
        });

        closeBtn.setOnAction(e-> {
            closeWindow();
        });
    }

    public void setSavedRates(List<FixedRateLoan> loans, Consumer<FixedRateLoan> onLoanSelected) {
        this.loanList = loans;
        this.onLoanSelected = onLoanSelected; // Store callback

        ObservableList<String> loanDetails = FXCollections.observableArrayList();
        for (FixedRateLoan loan : loanList) {
            loanDetails.add(loan.toString());
        }

        loansLV.setItems(loanDetails);
    }


    public void handleDoubleClick(MouseEvent event) {
        int selectedIndex = loansLV.getSelectionModel().getSelectedIndex(); // Get index of selected item
        if (selectedIndex >= 0 && onLoanSelected != null) {
            FixedRateLoan selectedLoan = loanList.get(selectedIndex); // Get the corresponding loan from loanList
            onLoanSelected.accept(selectedLoan); // Pass the selected loan back
            closeWindow();
        }
    }

    public void closeWindow() {
        ((Stage) closeBtn.getScene().getWindow()).close();
    }
}
