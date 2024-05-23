package com.cashcash.cashcash2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ContratMaintenanceTest {

    private PersistanceSQL donnees;
    private ContratMaintenance unContrat;

    @BeforeEach
    void setUp() {
        donnees = new PersistanceSQL("127.0.0.1", 3306, "CashCash");
        unContrat = (ContratMaintenance) donnees.chargerDepuisBase("C0001", "ContratMaintenance");
    }

    @Test
    void getNumClient() {
        assertNotNull(unContrat, "Contract should not be null");
        String unNumClient = unContrat.getNumClient();
        assertNotNull(unNumClient, "NumClient should not be null");
        assertFalse(unNumClient.isEmpty(), "NumClient should not be empty");
        String expectedNumClient = "CL0001";
        assertEquals(expectedNumClient, unNumClient, "NumClient should match the expected value");
    }

    @Test
    void getDatePremierSignature() {
        assertNotNull(unContrat, "Contract should not be null");
        assertNotNull(unContrat.getDatePremierSignature(), "DatePremierSignature should not be null");
        // Add assertions for date comparison if applicable
    }

    @Test
    void getDateEcheance() {
        assertNotNull(unContrat, "Contract should not be null");
        assertNotNull(unContrat.getDateEcheance(), "DateEcheance should not be null");
        // Add assertions for date comparison if applicable
    }

    @Test
    void getDateSignature() {
        assertNotNull(unContrat, "Contract should not be null");
        assertNotNull(unContrat.getDateSignature(), "DateSignature should not be null");
        // Add assertions for date comparison if applicable
    }

    @Test
    void getRefTypeContrat() {
        assertNotNull(unContrat, "Contract should not be null");
        assertNotNull(unContrat.getRefTypeContrat(), "RefTypeContrat should not be null");
        assertFalse(unContrat.getRefTypeContrat().isEmpty(), "RefTypeContrat should not be empty");
        String expectedRefTypeContrat = "tc001"; // Example expected value
        assertEquals(expectedRefTypeContrat, unContrat.getRefTypeContrat(), "RefTypeContrat should match the expected value");
    }

    @Test
    void getNumContrat() {
        assertNotNull(unContrat, "Contract should not be null");
        assertNotNull(unContrat.getNumContrat(), "NumContrat should not be null");
        assertFalse(unContrat.getNumContrat().isEmpty(), "NumContrat should not be empty");
        String expectedNumContrat = "C0001"; // Example expected value
        assertEquals(expectedNumContrat, unContrat.getNumContrat(), "NumContrat should match the expected value");
    }

    @Test
    void getLesMaterielsAssures() {
        assertNotNull(unContrat, "Contract should not be null");
        ArrayList<Materiel> lesMatsAssures = unContrat.getLesMaterielsAssures();
        // Creating a list of materials for testing
        ArrayList<Materiel> expectedMateriels = new ArrayList<>();
        Materiel unMat = (Materiel) donnees.chargerDepuisBase("M0001", "Materiel");
        Materiel unMat2 = (Materiel) donnees.chargerDepuisBase("M0002", "Materiel");
        Materiel unMat3 = (Materiel) donnees.chargerDepuisBase("M0011", "Materiel");
        expectedMateriels.add(unMat);
        expectedMateriels.add(unMat2);
        expectedMateriels.add(unMat3);

        assertEquals(expectedMateriels.size(), lesMatsAssures.size(), "Number of elements should match");
        for (int i = 0; i < expectedMateriels.size(); i++) {
            assertEquals(expectedMateriels.get(i).getNumSerie(), lesMatsAssures.get(i).getNumSerie(), "Materiel at index " + i + " should match");
        }
    }


    @Test
    void estValideWithValidContract() {
        assertTrue(unContrat.estValide(), "Contract should be valid");
    }

    @Test
    void estValideWithExpiredContract() {
        // Get the current expiry date
        Date currentExpiryDate = unContrat.getDateEcheance();

        // Calculate milliseconds equivalent to 2 years
        long twoYearsInMillis = 2L * 365 * 24 * 60 * 60 * 1000; // Assuming a year is 365 days

        // Calculate the past time by subtracting 2 years from the current expiry date
        long pastTime = currentExpiryDate.getTime() - twoYearsInMillis;

        // Create a new Date object representing the past expiry date
        Date pastExpiryDate = new Date(pastTime);

        // Set the past expiry date to the contract
        unContrat.setDateEcheance(pastExpiryDate);

        // Assert that the contract is expired
        assertFalse(unContrat.estValide(), "Contract should be expired");
    }



}