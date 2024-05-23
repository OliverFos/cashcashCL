package com.cashcash.cashcash2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Controller class for the TabOne.fxml file.
 */
public class TabOneController {

    @FXML
    private Text messageSucc; // Text field for success message

    @FXML
    private Text messageErreur; // Text field for error message

    private PersistanceSQL lesDonnes = new PersistanceSQL("127.0.0.1", 3306, "CashCash");

    @FXML
    private Button downloadXmlButton; // Button for downloading the XML file

    @FXML
    private TextField clientIdTextField; // Text field for entering client ID

    /**
     * Handle the event when the "Create XML" button is clicked.
     * This method retrieves the client ID from the text field,
     * generates XML for the client, and displays success or error messages accordingly.
     * @param event The action event.
     */
    @FXML
    private void handleCreateXmlButtonClick(ActionEvent event) {
        String clientId = clientIdTextField.getText();



        // Create an instance of GestionMateriels with lesDonnes
        GestionMateriels gestionMateriels = new GestionMateriels(lesDonnes);
        messageErreur.setText("");
        messageSucc.setText("");
        // Call the xmlClient method from GestionMateriels class with the clientId
        Client unClient = gestionMateriels.getClient(clientId);
        if (unClient != null) {
            String xmlClient = gestionMateriels.xmlClient(unClient);
            // Display success message
            messageSucc.setText("XML généré avec succès");
            messageSucc.setFill(Color.GREEN);
            // Enable the download button
            downloadXmlButton.setDisable(false);
        } else {
            // Display error message
            messageErreur.setText("Client not found for ID: " + clientId);
            messageErreur.setFill(Color.RED);
            // Disable the download button if client not found
            downloadXmlButton.setDisable(true);
        }
    }


    /**
     * Handle the event when the "Download XML" button is clicked.
     * This method retrieves the client ID from the text field,
     * generates XML for the client, and downloads the XML file.
     * @param event The action event.
     */
    @FXML
    private void handleDownloadXmlButtonClick(ActionEvent event) {
        String clientId = clientIdTextField.getText();

        // Create an instance of GestionMateriels with lesDonnes
        GestionMateriels gestionMateriels = new GestionMateriels(lesDonnes);

        // Call the xmlClient method from GestionMateriels class with the clientId
        Client unClient = gestionMateriels.getClient(clientId);
        if (unClient != null) {
            String xmlClient = gestionMateriels.xmlClient(unClient);
            // Call method to download XML file
            downloadXmlFile(xmlClient,clientId);
        }
    }

    /**
     * Download the XML file with the specified content and client ID.
     * This method writes the XML content to a file and opens the file explorer at the directory.
     * @param xmlContent The content of the XML file.
     * @param clientID The client ID used for naming the XML file.
     */
    private void downloadXmlFile(String xmlContent, String clientID) {
        try {
            // Specify the directory where the file will be saved
            String directoryPath = System.getProperty("user.home") + File.separator + "Downloads"; // Save in the Downloads folder
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs(); // Create the directory if it doesn't exist
            }

            // Create the file with the client ID as the filename
            File xmlFile = new File(directory, clientID + ".xml");
            FileWriter writer = new FileWriter(xmlFile);
            writer.write(xmlContent);
            writer.close();

            // Open the file explorer at the directory
            java.awt.Desktop.getDesktop().open(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
