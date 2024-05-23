package com.cashcash.cashcash2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypeMaterielTest {

    private PersistanceSQL donnees;
    private TypeMateriel unType;

    @BeforeEach
    void setUp() {
        donnees = new PersistanceSQL("127.0.0.1", 3306, "CashCash");
        unType = (TypeMateriel) donnees.chargerDepuisBase("T001", "TypeMateriel");
    }

    @Test
    void getLibelleTypeMateriel() {
        assertNotNull(unType, "TypeMateriel should not be null");
        String libelle = unType.getLibelleTypeMateriel();
        assertNotNull(libelle, "LibelleTypeMateriel should not be null");
        assertFalse(libelle.isEmpty(), "LibelleTypeMateriel should not be empty");

        String expectedLibelle = "Souris Logitech";
        assertEquals(expectedLibelle, libelle, "LibelleTypeMateriel should match the expected value");
    }

    @Test
    void getReferenceInterne() {
        assertNotNull(unType, "TypeMateriel should not be null");
        String referenceInterne = unType.getReferenceInterne();
        assertNotNull(referenceInterne, "ReferenceInterne should not be null");
        assertFalse(referenceInterne.isEmpty(), "ReferenceInterne should not be empty");

        // Replace with the expected value you want to assert
        String expectedReference = "T001";
        assertEquals(expectedReference, referenceInterne, "ReferenceInterne should match the expected value");
    }
}
