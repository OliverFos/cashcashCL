package com.cashcash.cashcash2;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.collections.FXCollections;

import java.time.LocalDate;
import java.util.Date;

import java.time.LocalDate;
import java.util.Date;

/**
 * Controller class for TabTwo.fxml
 */
public class TabTwoController {

    public ChoiceBox typeContrat;

    // Database connection
    private static PersistanceSQL lesDonnes = new PersistanceSQL("127.0.0.1", 3306, "CashCash");

    public Button searchClientButton;
    public Text messageErreur;
    public Text messageSucc;
    public Button creerContrat;
    public DatePicker dateSignature;

    @FXML
    private TextField clientIDTextField;

    @FXML
    private TableView<String> serialNumbersTableView;

    @FXML
    private TableColumn<String, String> numMaterielColumn;

    /**
     * Initializes the controller class.
     */
    @FXML
    private void initialize() {
        // Set selection mode for TableView
        serialNumbersTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        // Set default value for ChoiceBox
        typeContrat.setValue("tc001");
        // Set cell value factory for TableColumn
        numMaterielColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
    }

    /**
     * Handles the event when the "Search Client" button is clicked.
     * Retrieves materials without a contract for the given client ID and displays them in the TableView.
     * @param actionEvent The action event.
     */
    @FXML
    private void handleSearchClientButtonClick(ActionEvent actionEvent) {
        // Retrieve the text from the TextField
        String clientId = clientIDTextField.getText();

        // Create an instance of GestionMateriels with lesDonnes
        GestionMateriels gestionMateriels = new GestionMateriels(lesDonnes);

        // Call the getMaterielSansContrat method from GestionMateriels class with the clientId
        String[] serialNumbers = gestionMateriels.getMaterielSansContrat(clientId);
        if (serialNumbers != null) {
            // Clear the TableView
            serialNumbersTableView.getItems().clear();
            // Add serial numbers to the TableView
            serialNumbersTableView.getItems().addAll(serialNumbers);
            serialNumbersTableView.refresh();
        }
    }

    /**
     * Handles the event when the "Create Contract" button is clicked.
     * Creates a contract for the selected materials and client ID, and updates the database accordingly.
     * @param actionEvent The action event.
     */
    @FXML
    private void handleCreateContract(ActionEvent actionEvent) {
        // Clear previous messages
        messageErreur.setText("");
        messageSucc.setText("");

        // Get the selected items from the TableView based on checkbox selection
        ObservableList<String> selectedSerialNumbers = FXCollections.observableArrayList();
        // Iterate through the selected items in the TableView
        for (String item : serialNumbersTableView.getSelectionModel().getSelectedItems()) {
            selectedSerialNumbers.add(item);
        }

        if (!selectedSerialNumbers.isEmpty()) {
            // Check if signature date is provided
            if (dateSignature.getValue() != null) {
                // Create a confirmation dialog
                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationDialog.setTitle("Confirmation");
                confirmationDialog.setHeaderText("Create Contract");
                confirmationDialog.setContentText("Are you sure you want to create the contract?");

                // Show the confirmation dialog and wait for user response
                confirmationDialog.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // If user confirms, proceed with contract creation
                        Date signatureDate = java.sql.Date.valueOf(dateSignature.getValue());
                        String numContrat = lesDonnes.getIdContrat();
                        Date echeanceDate = java.sql.Date.valueOf(dateSignature.getValue().plusYears(1));
                        String contratType = (String) typeContrat.getValue();
                        String clientID = clientIDTextField.getText();

                        // Insert contract into database
                        lesDonnes.insertContrat(numContrat, signatureDate, echeanceDate, signatureDate, contratType, clientID);
                        // Update material records
                        for (String selectedSerialNumber : selectedSerialNumbers) {
                            lesDonnes.updateMat(selectedSerialNumber, numContrat);
                            System.out.println((selectedSerialNumber));
                        }
                        messageSucc.setText("Success!");
                        serialNumbersTableView.refresh();
                        // Call the refreshTableData method after updating materials
                        refreshTableData();
                    } else {
                        // If user cancels, do nothing
                        messageErreur.setText("Contract creation cancelled.");
                    }
                });
            } else {
                messageErreur.setText("Veuillez Ajouter une date de signature");
            }

        } else {
            // If no items are selected, display an error message
            messageErreur.setText("Veuillez sélectionner un ou plusieurs matériels");
        }
    }

    /**
     * Refreshes the data displayed in the TableView based on the client ID.
     */
    private void refreshTableData() {
        // Retrieve the text from the TextField
        String clientId = clientIDTextField.getText();

        // Clear the TableView
        serialNumbersTableView.getItems().clear();

        // Create an instance of GestionMateriels with lesDonnes
        GestionMateriels gestionMateriels = new GestionMateriels(lesDonnes);

        // Call the getMaterielSansContrat method from GestionMateriels class with the clientId
        String[] serialNumbers = gestionMateriels.getMaterielSansContrat(clientId);
        if (serialNumbers != null) {
            // Add serial numbers to the TableView
            serialNumbersTableView.getItems().addAll(serialNumbers);
        }

        // Refresh the TableView to reflect the changes
        serialNumbersTableView.refresh();
    }
}