package com.cashcash.cashcash2;

/**
 * Represents a type of material.
 */
public class TypeMateriel {
    private String referenceInterne;
    private String libelleTypeMateriel;
    //private Famille laFamille; // Uncomment this line if Famille class is available

    /**
     * Constructs a TypeMateriel object with the specified internal reference and label.
     * @param refInterne The internal reference of the material type.
     * @param libelleType The label of the material type.
     */
    public TypeMateriel(String refInterne, String libelleType) {
        this.referenceInterne = refInterne;
        this.libelleTypeMateriel = libelleType;
        //this.laFamille = laFamille;
    }

    // Uncomment the following method if Famille class is available
    /*
    public Famille getLaFamille() {
        return laFamille;
    }
    */

    /**
     * Gets the label of the material type.
     * @return The label of the material type.
     */
    public String getLibelleTypeMateriel() {
        return libelleTypeMateriel;
    }

    /**
     * Gets the internal reference of the material type.
     * @return The internal reference of the material type.
     */
    public String getReferenceInterne() {
        return referenceInterne;
    }
}
