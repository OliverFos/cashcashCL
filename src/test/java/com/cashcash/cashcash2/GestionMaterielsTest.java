package com.cashcash.cashcash2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GestionMaterielsTest {

    private PersistanceSQL donnees;
    private GestionMateriels lesDonnees;

    @BeforeEach
    void setUp() {
        donnees = new PersistanceSQL("127.0.0.1", 3306, "CashCash");
        GestionMateriels.setDonnees(donnees);
    }

    @Test
    void getClient() {
        // Retrieve client from GestionMateriels
        Client unClient = GestionMateriels.getClient("CL0001");

        // Retrieve client from database
        Client unClient2 = (Client) donnees.chargerDepuisBase("CL0001", "Client");

        // Assert that the clients are not null
        assertNotNull(unClient, "Client should not be null");
        assertNotNull(unClient2, "Client from database should not be null");

        // Assert that the client objects are equal
        assertEquals(unClient.getNumClient(), unClient2.getNumClient(), "Clients should be equal");
    }



    @Test
    void xmlClient() {
        Client unClient = (Client) donnees.chargerDepuisBase("CL0001","Client");
        // Generate XML for the client
        String generatedXml = GestionMateriels.xmlClient(unClient);

        // Define the expected XML
        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE listeMateriel SYSTEM \"C:\\Users\\olide\\IdeaProjects\\CashCash\\listeMat.dtd\">" +
                "<listeMateriel>\n" +
                "<materiels idClient=\"CL0001\">\n" +
                "<sousContrat>\n" +
                "<materiel numSerie=\"M0001\">" +
                "<type refInterne=\"T001\" libelle=\"Souris Logitech\"/>" +
                "<date_vente>2017-02-13</date_vente>" +
                "<date_installation>2017-02-13</date_installation>" +
                "<prix_vente>29.99</prix_vente>" +
                "<emplacement>acceuil</emplacement>" +
                "</materiel>\n" +
                "<materiel numSerie=\"M0002\">" +
                "<type refInterne=\"T002\" libelle=\"écran Dell\"/>" +
                "<date_vente>2017-02-13</date_vente>" +
                "<date_installation>2017-02-13</date_installation>" +
                "<prix_vente>300.0</prix_vente>" +
                "<emplacement>service informatique</emplacement>" +
                "</materiel>\n" +
                "<materiel numSerie=\"M0011\">" +
                "<type refInterne=\"T002\" libelle=\"écran Dell\"/>" +
                "<date_vente>2017-02-13</date_vente>" +
                "<date_installation>2017-02-13</date_installation>" +
                "<prix_vente>300.0</prix_vente>" +
                "<emplacement>service informatique</emplacement>" +
                "</materiel>\n" +
                "</sousContrat>\n" +
                "<horsContrat>\n" +
                "<materiel numSerie=\"M0010\">" +
                "<type refInterne=\"T001\" libelle=\"Souris Logitech\"/>" +
                "<date_vente>2017-02-13</date_vente>" +
                "<date_installation>2017-02-13</date_installation>" +
                "<prix_vente>29.99</prix_vente>" +
                "<emplacement>acceuil</emplacement>" +
                "</materiel>\n" +
                "</horsContrat>\n" +
                "</materiels>\n" +
                "</listeMateriel>\n";

        // Assert that the generated XML matches the expected XML
        assertEquals(expectedXml, generatedXml, "Generated XML should match expected XML");
    }



    @Test
    void getMaterielSansContrat() {
        // Assuming you have a client with known ID
        String clientId = "CL0004";

        // Get the list of materials without a contract for the client
        String[] materielsSansContrat = GestionMateriels.getMaterielSansContrat(clientId);

        assertTrue(Arrays.asList(materielsSansContrat).contains("M0005"), "Material M0005 should be without a contract");

    }

    @Test
    void getContratEcheance() {
        // Assuming you have a client with known ID
        String clientId = "CL0002";

        // Get the list of contracts nearing expiration for the client
        ArrayList<String> contratEcheance = GestionMateriels.getContratEcheance(clientId);

        assertTrue(contratEcheance.contains("C0002"), "Contract 2 should be expired");
    }
}