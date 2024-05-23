package com.cashcash.cashcash2;

import java.util.ArrayList;
import java.util.Date;

/**
 * La classe ContratMaintenance représente un contrat de maintenance pour un client.
 * Elle contient des informations sur le contrat, les dates importantes, et les matériels assurés.
 */
public class ContratMaintenance {
    private String numContrat; // Numéro unique du contrat
    private Date dateSignature; // Date de signature du contrat
    private Date dateEcheance; // Date d'échéance du contrat
    private Date datePremierSignature; // Date de première signature du contrat
    private ArrayList<Materiel> lesMaterielsAssures; // Liste des matériels assurés par le contrat
    private String refTypeContrat; // Référence du type de contrat
    private String numClient; // Numéro du client associé au contrat

    /**
     * Constructeur de la classe ContratMaintenance avec tous les paramètres.
     * @param numContrat Numéro unique du contrat
     * @param dateSignature Date de signature du contrat
     * @param dateEcheance Date d'échéance du contrat
     * @param datePremierSignature Date de première signature du contrat
     * @param refTypeContrat Référence du type de contrat
     * @param numClient Numéro du client associé au contrat
     * @param lesMaterielsAssures Liste des matériels assurés par le contrat
     */
    public ContratMaintenance(String numContrat, Date dateSignature, Date dateEcheance, Date datePremierSignature, String refTypeContrat, String numClient, ArrayList<Materiel> lesMaterielsAssures) {
        this.numContrat = numContrat;
        this.dateSignature = dateSignature;
        this.dateEcheance = dateEcheance;
        this.lesMaterielsAssures = lesMaterielsAssures;
        this.datePremierSignature = datePremierSignature;
        this.numClient = numClient;
        this.refTypeContrat = refTypeContrat;
    }

    // Getters pour les différents attributs du contrat de maintenance

    /**
     * Obtient Le numéro du client.
     * @return Le numéro du client.
     */
    public String getNumClient() {
        return numClient;
    }

    /**
     * Obtient la date de la première signature du contrat de maintenance.
     * @return La date de la première signature du contrat.
     */
    public java.sql.Date getDatePremierSignature() {
        return (java.sql.Date) datePremierSignature;
    }

    /**
     * Obtient la date d'échéance du contrat de maintenance.
     * @return La date d'échéance du contrat.
     */
    public java.sql.Date getDateEcheance() {
        return (java.sql.Date) dateEcheance;
    }

    /**
     * Obtient la date de signature du contrat de maintenance.
     * @return La date de signature du contrat.
     */
    public java.sql.Date getDateSignature() {
        return (java.sql.Date) dateSignature;
    }

    /**
     * Obtient la référence du type de contrat de maintenance.
     * @return La référence du type de contrat.
     */
    public String getRefTypeContrat() {
        return refTypeContrat;
    }

    /**
     * Obtient le numéro du contrat de maintenance.
     * @return Le numéro du contrat.
     */
    public String getNumContrat() {
        return numContrat;
    }

    /**
     * Obtient la liste des matériels assurés par le contrat de maintenance.
     * @return La liste des matériels assurés.
     */
    public ArrayList<Materiel> getLesMaterielsAssures() {
        return lesMaterielsAssures;
    }

    /**
     * Calcule le nombre de jours restants avant l'échéance du contrat.
     * @return Le nombre de jours restants avant l'échéance du contrat
     */
    public int getJoursRestants() {
        Date aujourdHui = new Date(); // Obtenez la date d'aujourd'hui
        long differenceEnMillisecondes = this.dateEcheance.getTime() - aujourdHui.getTime(); // Calculer la différence entre la date d'échéance et la date d'aujourd'hui en millisecondes
        long joursRestants = differenceEnMillisecondes / (24 * 60 * 60 * 1000); // Convertir la différence en millisecondes en jours
        return (int) joursRestants; // Convertir en int et retourner le nombre de jours restants
    }

    /**
     * Vérifie si le contrat est valide, c'est-à-dire si la date du jour est entre la date de signature et la date d'échéance.
     * @return Vrai si le contrat est valide, faux sinon
     */
    public boolean estValide() {
        Date aujourdHui = new Date(); // Obtenez la date d'aujourd'hui
        boolean apresDateSignature = aujourdHui.after(this.dateSignature); // Vérifiez si la date d'aujourd'hui est après la date de signature
        boolean avantDateEcheance = aujourdHui.before(this.dateEcheance); // Vérifiez si la date d'aujourd'hui est avant la date d'échéance
        return apresDateSignature && avantDateEcheance; // Le contrat est valide si la date d'aujourd'hui est après la date de signature et avant la date d'échéance
    }

    /**
     * Ajoute un matériel à la collection lesMaterielsAssures si la date de signature du contrat est antérieure à la date d'installation du matériel.
     * @param unMateriel Le matériel à ajouter
     */
    public void ajouteMaterial(Materiel unMateriel) {
        if (this.dateSignature.before(unMateriel.getDateInstallation())) { // Vérifiez si la date de signature du contrat est antérieure à la date d'installation du matériel
            this.lesMaterielsAssures.add(unMateriel); // Ajoutez le matériel à la collection des matériels assurés
        } else {
            System.out.println("Erreur : La date de signature du contrat doit être antérieure à la date d'installation du matériel.");
        }
    }

    public void setDateEcheance(Date date) {
        this.dateEcheance = date;
    }
}
