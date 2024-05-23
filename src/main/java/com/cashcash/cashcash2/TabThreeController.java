package com.cashcash.cashcash2;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.beans.property.SimpleStringProperty;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;



import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedDeque;


/**
 * Controller class for the TabThree.fxml file.
 */
public class TabThreeController {

    // Database connection
    private static PersistanceSQL lesDonnes = new PersistanceSQL("127.0.0.1", 3306, "CashCash");

    @FXML
    private Text messageErreur; // Text field for error messages

    @FXML
    private Text messageSucc; // Text field for success messages

    @FXML
    private TextField clientIDTextField; // Text field for entering client ID

    @FXML
    private TableView<String> serialNumbersTableView; // Table view for displaying serial numbers

    @FXML
    private TableColumn<String, String> numMaterielColumn; // Column for displaying serial numbers

    /**
     * Initializes the table view.
     */
    @FXML
    private void initialize() {
        serialNumbersTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        numMaterielColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
    }

    /**
     * Handle the event when the "Search Client" button is clicked.
     * Retrieves contracts for the entered client ID and populates the table view.
     * @param actionEvent The action event.
     */
    @FXML
    private void handleSearchClientButtonClick(ActionEvent actionEvent) {
        String clientId = clientIDTextField.getText();
        GestionMateriels gestionMateriels = new GestionMateriels(lesDonnes);
        ArrayList<String> numContrat = gestionMateriels.getContratEcheance(clientId);
        if (numContrat != null) {
            serialNumbersTableView.getItems().clear();
            serialNumbersTableView.getItems().addAll(numContrat);
            serialNumbersTableView.refresh();
        }
    }

    /**
     * Handle the event when the "Create PDF" button is clicked.
     * Generates PDFs for selected contracts and displays success or error messages.
     * @param actionEvent The action event.
     */
    @FXML
    private void handleCreatePDF(ActionEvent actionEvent) {
        // Clear previous messages
        messageErreur.setText("");
        messageSucc.setText("");

        // Get selected contract numbers
        ObservableList<String> selectedContractNumbers = serialNumbersTableView.getSelectionModel().getSelectedItems();

        // Proceed if at least one contract is selected
        if (!selectedContractNumbers.isEmpty()) {
            // Confirmation dialog
            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Confirmation");
            confirmationDialog.setHeaderText("Create PDF");
            confirmationDialog.setContentText("Are you sure you want to create a PDF?");

            confirmationDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Generate PDFs for selected contracts
                    ArrayList<ContratMaintenance> contrats = new ArrayList<>();
                    for (String selectedContractNumber : selectedContractNumbers) {
                        ContratMaintenance unContrat = lesDonnes.getContrat(selectedContractNumber);
                        contrats.add(unContrat);
                    }

                    // Generate PDFs and display success message
                    for (ContratMaintenance unContrat : contrats) {
                        String fileName = "pdf/" + unContrat.getNumContrat() + ".pdf";
                        generatePdf(unContrat, fileName);
                    }
                    messageSucc.setText("PDF generated successfully!");
                    serialNumbersTableView.refresh();
                    refreshTableData();
                } else {
                    messageErreur.setText("PDF creation cancelled.");
                }
            });
        } else {
            messageErreur.setText("Please select at least one contract.");
        }
    }

    /**
     * Generates a PDF file for the given contract.
     * @param unContrat The contract for which PDF is to be generated.
     * @param fileName The name of the PDF file to be generated.
     */
    private void generatePdf(ContratMaintenance unContrat, String fileName) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            document.add(new Paragraph("Contract Number: " + unContrat.getNumContrat()));
            document.add(new Paragraph("Client ID: " + unContrat.getNumClient()));
            document.add(new Paragraph("Start Date: " + unContrat.getDateSignature()));
            document.add(new Paragraph("End Date: " + unContrat.getDateEcheance()));
            for (Materiel unMat : unContrat.getLesMaterielsAssures()) {
                document.add(new Paragraph("- " + unMat.getNumSerie() + " " + unMat.getLeType().getLibelleTypeMateriel()));
            }
            // Add more contract details here
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refreshes the data in the table view.
     */
    private void refreshTableData() {
        String clientId = clientIDTextField.getText();
        serialNumbersTableView.getItems().clear();
        GestionMateriels gestionMateriels = new GestionMateriels(lesDonnes);
        ArrayList<String> numContrat = gestionMateriels.getContratEcheance(clientId);

        if (numContrat != null) {
            serialNumbersTableView.getItems().addAll(numContrat);
        }
        serialNumbersTableView.refresh();
    }

    /**
     * Handles the event when the "Create Email" button is clicked.
     * Sends PDFs of selected contracts via email and displays success or error messages.
     * @param actionEvent The action event.
     */
    @FXML
    private void handleCreateEmail(ActionEvent actionEvent) {
        // Clear previous messages
        messageErreur.setText("");
        messageSucc.setText("");

        // Get selected contract numbers and client ID
        ObservableList<String> selectedContractNumbers = serialNumbersTableView.getSelectionModel().getSelectedItems();
        String clientId = clientIDTextField.getText();
        Client unClient = (Client) lesDonnes.chargerDepuisBase(clientId, "Client");
        String email = unClient.getEmail();

        // Proceed if at least one contract is selected
        if (!selectedContractNumbers.isEmpty()) {
            // Confirmation dialog
            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Confirmation");
            confirmationDialog.setHeaderText("Send PDF");
            confirmationDialog.setContentText("Are you sure you want to send the PDFs?");

            confirmationDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Email configuration
                    String to = email; // recipient email address
                    String host = "smtp.gmail.com"; // Gmail SMTP server
                    final String username = "olidenfoster@gmail.com"; // Gmail username
                    final String password = "pakk dvtc yrst xowg"; // App-specific password

                    // Set email properties
                    Properties properties = new Properties();
                    properties.put("mail.smtp.auth", "true");
                    properties.put("mail.smtp.starttls.enable", "true");
                    properties.put("mail.smtp.host", host);
                    properties.put("mail.smtp.port", "587");

                    // Create session with authentication
                    Session session = Session.getInstance(properties,
                            new jakarta.mail.Authenticator() {
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(username, password);
                                }
                            });

                    try {
                        // Create message
                        MimeMessage message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(username));
                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                        message.setSubject("Contract Details with PDFs");

                        // Create email body
                        BodyPart messageBodyPart = new MimeBodyPart();
                        StringBuilder emailText = new StringBuilder("Voici les contrats:\n");

                        // Add selected contracts to the email body
                        for (String selectedContractNumber : selectedContractNumbers) {
                            emailText.append(" - ").append(lesDonnes.getContrat(selectedContractNumber).getNumContrat()).append("\n");
                        }
                        messageBodyPart.setText(emailText.toString());

                        // Create multipart message for attachments
                        Multipart multipart = new MimeMultipart();
                        multipart.addBodyPart(messageBodyPart);

                        // Attach PDF files
                        for (String selectedContractNumber : selectedContractNumbers) {
                            ContratMaintenance contrat = lesDonnes.getContrat(selectedContractNumber);
                            String fileName = "pdf/" + contrat.getNumContrat() + ".pdf";
                            generatePdf(contrat, fileName);
                            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                            DataSource source = new FileDataSource(fileName);
                            attachmentBodyPart.setDataHandler(new DataHandler(source));
                            attachmentBodyPart.setFileName(fileName);
                            multipart.addBodyPart(attachmentBodyPart);
                        }

                        // Set the complete message parts
                        message.setContent(multipart);

                        // Send message
                        Transport.send(message);
                        messageSucc.setText("Email with PDFs sent successfully!");
                    } catch (MessagingException mex) {
                        mex.printStackTrace();
                        messageErreur.setText("Error in sending email.");
                    }
                } else {
                    messageErreur.setText("PDF creation cancelled.");
                }
            });
        } else {
            messageErreur.setText("Please select at least one contract.");
        }
    }

}