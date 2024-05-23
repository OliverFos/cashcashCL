package com.cashcash.cashcash2;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class PersistanceSQLTest {

    @Test
    void rangerDansBase() {
        // Create a PersistanceSQL object
        PersistanceSQL persistanceSQL = new PersistanceSQL("127.0.0.1", 3306, "cashcash");
        ArrayList<Materiel> lesMateriels = new ArrayList<Materiel>();
        ArrayList<ContratMaintenance> lesContrats = new ArrayList<ContratMaintenance>();
        // Create a test object
        Client testClient = new Client("CL0060", "", "111111111", "11111", "26 rue de Lille", "0102030405", "o@g.com", Time.valueOf("00:30:00"), BigDecimal.valueOf(10), lesMateriels, lesContrats, "A0001", "Auchan2");

        // Call the rangerDansBase method to store the test object in the database
        persistanceSQL.rangerDansBase(testClient);

        // Verify if the object is stored in the database by retrieving it
        Client retrievedClient = (Client) persistanceSQL.chargerDepuisBase(testClient.getNumClient(),"Client");

        // Verify if the retrieved client matches the test client
        assertNotNull(retrievedClient, "Client should be retrieved from the database");
        assertEquals(testClient.getNumClient(), retrievedClient.getNumClient(), "Retrieved client's ID should match the test client's ID");
        assertEquals(testClient.getRaisonSociale(), retrievedClient.getRaisonSociale(), "Retrieved client's company name should match the test client's company name");
        // Add more assertions based on the attributes you want to verify
    }

    @Test
    void chargerDepuisBase() {
        // Create a PersistanceSQL object
        PersistanceSQL persistanceSQL = new PersistanceSQL("127.0.0.1", 3306, "cashcash");

        // Create a test client object
        Client testClient = new Client("CL0061", "", "111111111", "11111", "26 rue de Lille", "0102030405", "o@g.com", Time.valueOf("00:30:00"), BigDecimal.valueOf(10), new ArrayList<>(), new ArrayList<>(), "A0001", "Auchan2");

        // Store the test client in the database
        persistanceSQL.rangerDansBase(testClient);

        // Retrieve the test client from the database using chargerDepuisBase
        Client retrievedClient = (Client) persistanceSQL.chargerDepuisBase(testClient.getNumClient(),"Client");

        // Verify if the object is retrieved from the database
        assertNotNull(retrievedClient, "Client should be retrieved from the database");

        // Verify if the retrieved client matches the test client
        assertEquals(testClient.getNumClient(), retrievedClient.getNumClient(), "Retrieved client's ID should match the test client's ID");
        assertEquals(testClient.getNomClient(), retrievedClient.getNomClient(), "Retrieved client's company name should match the test client's company name");
        // Add more assertions based on the attributes you want to verify
    }


    @Test
    void getMaterielsSansContrat() {
        // Create a PersistanceSQL object
        PersistanceSQL persistanceSQL = new PersistanceSQL("127.0.0.1", 3306, "cashcash");

        // Load a specific material from the database and add it to the list
        Materiel mat = (Materiel) persistanceSQL.chargerDepuisBase("M0005", "Materiel");
        ArrayList<Materiel> lesMatsSansContrat = new ArrayList<>();
        lesMatsSansContrat.add(mat);

        // Retrieve materials without contracts for the client
        ArrayList<Materiel> materielsSansContrat = persistanceSQL.getMaterielsSansContrat("CL0004");

        // AssertEquals on lesMatsSansContrat and materielsSansContrat
        assertEquals(lesMatsSansContrat.size(), materielsSansContrat.size(), "Size of the lists should be equal");

        // Compare each element from lesMatsSansContrat to materielsSansContrat
        for (int i = 0; i < lesMatsSansContrat.size(); i++) {
            assertEquals(lesMatsSansContrat.get(i).getNumSerie(), materielsSansContrat.get(i).getNumSerie(), "Materiel at index " + i + " should match");
        }
    }

    @Test
    void getIdContrat() {
        // Create a PersistanceSQL object
        PersistanceSQL persistanceSQL = new PersistanceSQL("127.0.0.1", 3306, "cashcash");

        // Call the getIdContrat method to retrieve a new contract ID
        String newContratNumber = persistanceSQL.getIdContrat();

        // AssertNotNull on the returned contract ID
        assertNotNull(newContratNumber, "New contract number should not be null");

        // AssertEquals on the format of the returned contract ID
        assertTrue(newContratNumber.matches("C\\d{4}"), "New contract number should be in the format 'C####'");

        // You can also add more assertions based on specific requirements
    }

    @Test
    void insertContrat() {
        // Create a PersistanceSQL object
        PersistanceSQL persistanceSQL = new PersistanceSQL("127.0.0.1", 3306, "cashcash");

        // Define test data for the new contract
        String numContrat = persistanceSQL.getIdContrat(); // Change this to the desired contract number
        Date dateSign = new Date(); // Current date as the signing date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateSign);
        calendar.add(Calendar.YEAR, 1); // Add one year to the current date
        Date dateEch = calendar.getTime(); // Plus one year
        Date datePremSign = new Date(); // Current date as the first signing date
        String typeContrat = "tc001"; // Change this to the desired contract type
        String numClient = "CL0001"; // Change this to the desired client number

        // Call the insertContrat method to insert the new contract into the database
        persistanceSQL.insertContrat(numContrat, dateSign, dateEch, datePremSign, typeContrat, numClient);

        // Retrieve the inserted contract from the database
        ContratMaintenance insertedContrat = (ContratMaintenance) persistanceSQL.chargerDepuisBase(numContrat, "ContratMaintenance");

        // AssertNotNull on the retrieved contract
        assertNotNull(insertedContrat, "Inserted contract should not be null");

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd zzz yyyy", Locale.ENGLISH);

        assertEquals(dateFormat.format(dateSign), dateFormat.format(insertedContrat.getDateSignature()), "Signing date should match");
        assertEquals(dateFormat.format(dateEch), dateFormat.format(insertedContrat.getDateEcheance()), "Expiry date should match");
        assertEquals(dateFormat.format(datePremSign), dateFormat.format(insertedContrat.getDatePremierSignature()), "First signing date should match");

        assertEquals(typeContrat, insertedContrat.getRefTypeContrat(), "Contract type should match");
        assertEquals(numClient, insertedContrat.getNumClient(), "Client number should match");
    }



    @Test
    void updateMat() {
        // Create a PersistanceSQL object
        PersistanceSQL persistanceSQL = new PersistanceSQL("127.0.0.1", 3306, "cashcash");

        // Define test data for the material and contract
        String numMat = "M0001"; // Change this to the serial number of the material you want to update
        String numContrat = "C0001"; // Change this to the new contract number

        // Call the updateMat method to update the contract number of the material in the database
        persistanceSQL.updateMat(numMat, numContrat);

        // Retrieve the updated material from the database
        Materiel updatedMat = (Materiel) persistanceSQL.chargerDepuisBase(numMat, "Materiel");

        // AssertNotNull on the retrieved material
        assertNotNull(updatedMat, "Updated material should not be null");

        // AssertEquals to verify if the contract number of the updated material matches the new contract number
        assertEquals(numContrat, updatedMat.getNumContrat(), "Contract number should match the updated contract number");
    }


    @Test
    void getContratEcheance() {
        // Create a PersistanceSQL object
        PersistanceSQL persistanceSQL = new PersistanceSQL("127.0.0.1", 3306, "cashcash");

        // Assuming you have a client with known ID
        String clientId = "CL0002";

        // Get the list of contracts nearing expiration for the client
        ArrayList<String> contratEcheance = persistanceSQL.getContratEcheance(clientId);

        assertTrue(contratEcheance.contains("C0002"), "Contract 2 should be expired");
    }


}