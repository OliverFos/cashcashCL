package com.cashcash.cashcash2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    private PersistanceSQL donnees;
    private Client unCli;

    @BeforeEach
    void setUp() {
        donnees = new PersistanceSQL("127.0.0.1", 3306, "CashCash");
        unCli = (Client) donnees.chargerDepuisBase("CL0001", "Client");
    }

    @Test
    void getNumClient() {
        assertNotNull(unCli, "Client should not be null");
        String unNumClient = unCli.getNumClient();
        assertNotNull(unNumClient, "NumClient should not be null");
        assertFalse(unNumClient.isEmpty(), "NumClient should not be empty");
        String expectedLibelle = "CL0001";
        assertEquals(expectedLibelle, unNumClient, "NumClient should match the expected value");

    }

    @Test
    void getNumAgence() {
        assertNotNull(unCli, "Client should not be null");
        String unNumAgence = unCli.getNumAgence();
        assertNotNull(unNumAgence, "unNumAgence should not be null");
        assertFalse(unNumAgence.isEmpty(), "unNumAgence should not be empty");
        String expectedLibelle = "A0001";
        assertEquals(expectedLibelle, unNumAgence, "unNumAgence should match the expected value");
    }

    @Test
    void getNomClient() {
        assertNotNull(unCli, "Client should not be null");
        String unNomClient = unCli.getNomClient();
        assertNotNull(unNomClient, "unNomClient should not be null");
        assertFalse(unNomClient.isEmpty(), "unNomClient should not be empty");
        String expectedLibelle = "Carrefour";
        assertEquals(expectedLibelle, unNomClient, "unNomClient should match the expected value");
    }

    @Test
    void getDistanceKm() {
        assertNotNull(unCli, "Client should not be null");
        BigDecimal unCliDistanceKm = unCli.getDistanceKm();
        assertNotNull(unCliDistanceKm, "unCliDistanceKm should not be null");
        assertFalse(unCliDistanceKm.compareTo(BigDecimal.ZERO) == 0, "unCliDistanceKm should not be zero");
        BigDecimal expectedDistance = new BigDecimal("10.90");
        assertEquals(expectedDistance, unCliDistanceKm, "unCliDistanceKm should match the expected value");
    }


    @Test
    void getDureeDeplacement() {
        assertNotNull(unCli, "Client should not be null");
        Time dureeDeplacement = unCli.getDureeDeplacement();
        assertNotNull(dureeDeplacement, "DureeDeplacement should not be null");
        // Check if dureeDeplacement is not null, if you expect it to be non-null
        // Assuming expected duration is "01:50:21"
        Time expectedDuration = Time.valueOf("01:50:21");
        assertEquals(expectedDuration, dureeDeplacement, "DureeDeplacement should match the expected value");
    }

    @Test
    void getAdresse() {
        assertNotNull(unCli, "Client should not be null");
        String adresse = unCli.getAdresse();
        assertNotNull(adresse, "Adresse should not be null");
        assertFalse(adresse.isEmpty(), "Adresse should not be empty");
        String expectedLibelle = "23 rue de Victor Hugo Avelin 59710";
        assertEquals(expectedLibelle, adresse, "adresse should match the expected value");
    }

    @Test
    void getCodeAPE() {
        assertNotNull(unCli, "Client should not be null");
        String codeAPE = unCli.getCodeAPE();
        assertNotNull(codeAPE, "CodeAPE should not be null");
        assertFalse(codeAPE.isEmpty(), "CodeAPE should not be empty");
        String expectedLibelle = "12345";
        assertEquals(expectedLibelle, codeAPE, "CodeAPE should match the expected value");
    }

    @Test
    void getEmail() {
        assertNotNull(unCli, "Client should not be null");
        String email = unCli.getEmail();
        assertNotNull(email, "Email should not be null");
        assertFalse(email.isEmpty(), "Email should not be empty");
        String expectedEmail = "teamultra41@gmail.com"; // Replace with the expected email
        assertEquals(expectedEmail, email, "Email should match the expected value");
    }

    @Test
    void getRaisonSociale() {
        assertNotNull(unCli, "Client should not be null");
        String raisonSociale = unCli.getRaisonSociale();
        assertNotNull(raisonSociale, "RaisonSociale should not be null");
        String expectedRaisonSociale = ""; // Replace with the expected RaisonSociale
        assertEquals(expectedRaisonSociale, raisonSociale, "RaisonSociale should match the expected value");
    }

    @Test
    void getSIREN() {
        assertNotNull(unCli, "Client should not be null");
        String SIREN = unCli.getSIREN();
        assertNotNull(SIREN, "SIREN should not be null");
        assertFalse(SIREN.isEmpty(), "SIREN should not be empty");
        String expectedSIREN = "123123123"; // Replace with the expected SIREN
        assertEquals(expectedSIREN, SIREN, "SIREN should match the expected value");
    }

    @Test
    void getTelClient() {
        assertNotNull(unCli, "Client should not be null");
        String telClient = unCli.getTelClient();
        assertNotNull(telClient, "TelClient should not be null");
        assertFalse(telClient.isEmpty(), "TelClient should not be empty");
        String expectedTelClient = "0101010101"; // Replace with the expected TelClient
        assertEquals(expectedTelClient, telClient, "TelClient should match the expected value");
    }


    @Test
    void getLesMateriels() {
        assertNotNull(unCli, "Client should not be null");
        ArrayList<Materiel> lesMateriels = unCli.getLesMateriels();
        assertNotNull(lesMateriels, "LesMateriels should not be null");

        ArrayList<Materiel> expectedMateriels = new ArrayList<>();
        Materiel unMat = (Materiel) donnees.chargerDepuisBase("M0001", "Materiel");
        Materiel unMat2 = (Materiel) donnees.chargerDepuisBase("M0002", "Materiel");
        Materiel unMat3 = (Materiel) donnees.chargerDepuisBase("M0010", "Materiel");
        Materiel unMat4 = (Materiel) donnees.chargerDepuisBase("M0011", "Materiel");
        expectedMateriels.add(unMat);
        expectedMateriels.add(unMat2);
        expectedMateriels.add(unMat3);
        expectedMateriels.add(unMat4);

        // Compare each element individually
        assertEquals(expectedMateriels.size(), lesMateriels.size(), "Number of elements should match");
        for (int i = 0; i < expectedMateriels.size(); i++) {
            assertEquals(expectedMateriels.get(i).getNumSerie(), lesMateriels.get(i).getNumSerie(), "Materiel at index " + i + " should match");
        }
    }


    @Test
    void getLesMaterielsSousContrat() {
        assertNotNull(unCli, "Client should not be null");
        ArrayList<Materiel> materielsSousContrat = unCli.getLesMaterielsSousContrat();

        ArrayList<Materiel> expectedMateriels = new ArrayList<>();
        Materiel unMat = (Materiel) donnees.chargerDepuisBase("M0001", "Materiel");
        Materiel unMat2 = (Materiel) donnees.chargerDepuisBase("M0002", "Materiel");
        Materiel unMat3 = (Materiel) donnees.chargerDepuisBase("M0011", "Materiel");
        expectedMateriels.add(unMat);
        expectedMateriels.add(unMat2);
        expectedMateriels.add(unMat3);
        assertEquals(expectedMateriels.size(), materielsSousContrat.size(), "Number of elements should match");
        for (int i = 0; i < expectedMateriels.size(); i++) {
            assertEquals(expectedMateriels.get(i).getNumSerie(), materielsSousContrat.get(i).getNumSerie(), "Materiel at index " + i + " should match");
        }

    }

    @Test
    void getLesMaterielsHorsContrat() {
        assertNotNull(unCli, "Client should not be null");
        ArrayList<Materiel> materielsHorsContrat = unCli.getLesMaterielsHorsContrat();
        Materiel unMat = (Materiel) donnees.chargerDepuisBase("M0010", "Materiel");
        ArrayList<Materiel> expectedMateriels = new ArrayList<>();
        expectedMateriels.add(unMat);

        assertEquals(expectedMateriels.size(), materielsHorsContrat.size(), "Number of elements should match");
        for (int i = 0; i < expectedMateriels.size(); i++) {
            assertEquals(expectedMateriels.get(i).getNumSerie(), materielsHorsContrat.get(i).getNumSerie(), "Materiel at index " + i + " should match");
        }
    }


    @Test
    void estAssureReturnsTrueWithValidContracts() {
        ContratMaintenance unContrat = (ContratMaintenance) donnees.chargerDepuisBase("C0001","ContratMaintenance");
        unCli.getLesContrats().add(unContrat);

        assertTrue(unCli.estAssure(), "estAssure() should return true with valid contracts");
    }

    @Test
    void estAssureReturnsFalseWithNoContracts() {

        Client unAutreCli = (Client) donnees.chargerDepuisBase("CL0035","Client");
        assertFalse(unAutreCli.estAssure(), "estAssure() should return true with valid contracts");
    }

    @Test
    void estAssureReturnsTrueWithInvalidContracts() {

        Client unAutreCli = (Client) donnees.chargerDepuisBase("CL0047","Client");
        assertFalse(unAutreCli.estAssure(), "estAssure() should return true with valid contracts");
    }

}