package com.cashcash.cashcash2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class MaterielTest {

    private PersistanceSQL donnees;
    private Materiel unMat;

    @BeforeEach
    void setUp() {
        donnees = new PersistanceSQL("127.0.0.1", 3306, "CashCash");
        unMat = (Materiel) donnees.chargerDepuisBase("M0001", "Materiel");
    }

    @Test
    void getNumContrat() {
        assertNotNull(unMat, "Materiel should not be null");
        String unMatNumContrat = unMat.getNumContrat();
        //assertNotNull(unMatNumContrat, "NumContrat should not be null"); NumContrat can be null
        assertFalse(unMatNumContrat.isEmpty(), "NumContrat should not be empty");
        String expectedLibelle = "C0001";
        assertEquals(expectedLibelle, unMatNumContrat, "NumContrat should match the expected value");
    }

    @Test
    void getNumClient() {
        assertNotNull(unMat, "Materiel should not be null");
        String unMatNumClient = unMat.getNumClient();
        assertNotNull(unMatNumClient, "NumClient should not be null");
        assertFalse(unMatNumClient.isEmpty(), "NumClient should not be empty");
        String expectedNumClient = "CL0001";
        assertEquals(expectedNumClient, unMatNumClient, "NumClient should match the expected value");
    }

    @Test
    void getDateVente() {
        assertNotNull(unMat, "Materiel should not be null");
        Date dateVente = unMat.getDateVente();
        assertNotNull(dateVente, "DateVente should not be null");
        String expectedDateVente = "2017-02-13";
        assertEquals(expectedDateVente, dateVente.toString(), "DateVente should match the expected value");
    }

    @Test
    void getPrixVente() {
        assertNotNull(unMat, "Materiel should not be null");
        double prixVente = unMat.getPrixVente();
        double expectedPrixVente = 29.99;
        assertEquals(expectedPrixVente, prixVente, 0.01, "PrixVente should match the expected value");
    }

    @Test
    void getNumSerie() {
        assertNotNull(unMat, "Materiel should not be null");
        String numSerie = unMat.getNumSerie();
        assertNotNull(numSerie, "NumSerie should not be null");
        assertFalse(numSerie.isEmpty(), "NumSerie should not be empty");
        String expectedNumSerie = "M0001";
        assertEquals(expectedNumSerie, numSerie, "NumSerie should match the expected value");
    }

    @Test
    void getEmplacement() {
        assertNotNull(unMat, "Materiel should not be null");
        String emplacement = unMat.getEmplacement();
        assertNotNull(emplacement, "Emplacement should not be null");
        assertFalse(emplacement.isEmpty(), "Emplacement should not be empty");
        String expectedEmplacement = "acceuil";
        assertEquals(expectedEmplacement, emplacement, "Emplacement should match the expected value");
    }

    @Test
    void getLeType() {
        assertNotNull(unMat, "Materiel should not be null");
        TypeMateriel leType = unMat.getLeType();
        assertNotNull(leType, "LeType should not be null");
        String expectedTypeName = "Souris Logitech";
        assertEquals(expectedTypeName, leType.getLibelleTypeMateriel(), "LeType should match the expected value");
    }


    @Test
    void setEmplacement() {
        assertNotNull(unMat, "Materiel should not be null");
        String newEmplacement = "Warehouse B";
        unMat.setEmplacement(newEmplacement);
        assertEquals(newEmplacement, unMat.getEmplacement(), "Emplacement should be updated correctly");
    }

    @Test
    void setLeType() {
        assertNotNull(unMat, "Materiel should not be null");
        TypeMateriel newType = new TypeMateriel("T002", "Keyboard Logitech");
        unMat.setLeType(newType);
        assertEquals(newType, unMat.getLeType(), "LeType should be updated correctly");
    }

    @Test
    void getDateInstallation() {
        assertNotNull(unMat, "Materiel should not be null");
        Date dateInstallation = unMat.getDateInstallation();
        assertNotNull(dateInstallation, "DateInstallation should not be null");
        String expectedDateInstallation = "2017-02-13";
        assertEquals(expectedDateInstallation, dateInstallation.toString(), "DateInstallation should match the expected value");
    }

    @Test
    void xmlMateriel() {
        assertNotNull(unMat, "Materiel should not be null");
        String xml = unMat.xmlMateriel();
        assertNotNull(xml, "XML should not be null");
        assertFalse(xml.isEmpty(), "XML should not be empty");
        String expectedXml = "<materiel numSerie=\"M0001\"><type refInterne=\"T001\" libelle=\"Souris Logitech\"/>"
                + "<date_vente>2017-02-13</date_vente><date_installation>2017-02-13</date_installation>"
                + "<prix_vente>29.99</prix_vente><emplacement>acceuil</emplacement></materiel>";
        assertEquals(expectedXml, xml, "XML output should match the expected value");
    }

}