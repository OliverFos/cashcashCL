package com.cashcash.cashcash2;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * La classe Materiel représente un matériel avec ses détails tels que le numéro de série,
 * les dates de vente et d'installation, le prix de vente, l'emplacement, le type de matériel,
 * le client associé et le contrat associé.
 */
public class Materiel {
    private String numSerie;
    private Date dateVente;
    private Date dateInstallation;
    private float prixVente;
    private String emplacement;
    private TypeMateriel leType;
    private String numClient;
    private String numContrat;

    /**
     * Constructeur pour créer un objet Materiel avec les détails spécifiés.
     *
     * @param numSerie        Le numéro de série du matériel.
     * @param dateVente       La date de vente du matériel.
     * @param dateInstallation La date d'installation du matériel.
     * @param prixVente       Le prix de vente du matériel.
     * @param emplacement     L'emplacement du matériel.
     * @param numClient       Le numéro du client associé au matériel.
     * @param numContrat      Le numéro du contrat associé au matériel.
     * @param leType          Le type de matériel.
     */
    public Materiel(String numSerie, Date dateVente, Date dateInstallation, float prixVente, String emplacement, String numClient, String numContrat, TypeMateriel leType) {
        this.numSerie = numSerie;
        this.dateVente = dateVente;
        this.dateInstallation = dateInstallation;
        this.prixVente = prixVente;
        this.emplacement = emplacement;
        this.numClient = numClient;
        this.numContrat = numContrat;
        this.leType = leType;
    }

    /**
     * Retourne le numéro de contrat associé au matériel.
     *
     * @return Le numéro de contrat.
     */
    public String getNumContrat() {return numContrat;}


    /**
     * Retourne le numéro du client associé au matériel.
     *
     * @return Le numéro du client.
     */
    public String getNumClient() {
        return numClient;
    }

    /**
     * Retourne la date de vente du matériel.
     *
     * @return La date de vente en tant que java.sql.Date.
     */
    public java.sql.Date getDateVente() {
        return (java.sql.Date) dateVente;
    }

    /**
     * Retourne le prix de vente du matériel.
     *
     * @return Le prix de vente.
     */
    public float getPrixVente() {
        return prixVente;
    }

    /**
     * Retourne le numéro de série du matériel.
     *
     * @return Le numéro de série.
     */
    public String getNumSerie() {
        return numSerie;
    }

    /**
     * Retourne l'emplacement du matériel.
     *
     * @return L'emplacement.
     */
    public String getEmplacement() {
        return emplacement;
    }

    /**
     * Retourne le type de matériel.
     *
     * @return Le type de matériel.
     */
    public TypeMateriel getLeType() {
        return leType;
    }



    /**
     * Définit l'emplacement du matériel.
     *
     * @param emplacement Le nouvel emplacement.
     */
    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }

    /**
     * Définit le type de matériel.
     *
     * @param leType Le nouveau type de matériel.
     */
    public void setLeType(TypeMateriel leType) {
        this.leType = leType;
    }

    /**
     * Retourne la date d'installation du matériel.
     *
     * @return La date d'installation en tant que java.sql.Date.
     */
    public java.sql.Date getDateInstallation() {
        return (java.sql.Date) dateInstallation;
    }

    /**
     * Génère une représentation XML du matériel.
     *
     * @return Une chaîne de caractères contenant le XML du matériel.
     */
    public String xmlMateriel() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<materiel numSerie=\"").append(numSerie).append("\">");
        xmlBuilder.append("<type refInterne=\"").append(leType.getReferenceInterne()).append("\" libelle=\"").append(leType.getLibelleTypeMateriel()).append("\"/>");
        //xmlBuilder.append("<famille codeFamille=\"" + leType.getLaFamille().getCodeFamille() + "\" libelle=\"" + leType.getLaFamille().getLibelleFamille() + "\"/>");
        xmlBuilder.append("<date_vente>").append(formatDate(dateVente)).append("</date_vente>");
        xmlBuilder.append("<date_installation>").append(formatDate(dateInstallation)).append("</date_installation>");
        xmlBuilder.append("<prix_vente>").append(prixVente).append("</prix_vente>");
        xmlBuilder.append("<emplacement>").append(emplacement).append("</emplacement>");
        xmlBuilder.append("</materiel>");
        return xmlBuilder.toString();
    }

    /**
     * Formate une date au format "yyyy-MM-dd".
     *
     * @param date La date à formater.
     * @return Une chaîne de caractères représentant la date formatée.
     */
    private String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }
}
