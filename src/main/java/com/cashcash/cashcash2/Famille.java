package com.cashcash.cashcash2;

/**
 * La classe Famille représente une famille de produits ou de services.
 * Elle contient un code unique pour identifier la famille et un libellé descriptif.
 */
public class Famille {

    private String codeFamille; // Code unique de la famille
    private String libelleFamille; // Libellé descriptif de la famille

    /**
     * Constructeur de la classe Famille.
     * @param codeFamille Le code unique de la famille.
     * @param libelleFamille Le libellé descriptif de la famille.
     */
    public Famille(String codeFamille, String libelleFamille){
        this.codeFamille = codeFamille;
        this.libelleFamille = libelleFamille;
    }

    /**
     * Obtient le code unique de la famille.
     *
     * @return Le code de la famille.
     */
    public String getCodeFamille() {
        return codeFamille;
    }

    /**
     * Obtient le libellé descriptif de la famille.
     *
     * @return Le libellé de la famille.
     */
    public String getLibelleFamille() {
        return libelleFamille;
    }
}
