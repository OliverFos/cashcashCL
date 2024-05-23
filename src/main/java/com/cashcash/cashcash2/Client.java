package com.cashcash.cashcash2;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;

/**
 * La classe Client représente un client dans le système.
 * Elle contient des informations sur le client, ses matériels, et ses contrats de maintenance.
 */
public class Client {
    private String numClient; // Numéro unique du client
    private String raisonSociale; // Raison sociale de l'entreprise du client
    private String SIREN; // Numéro SIREN du client
    private String codeAPE; // Code APE de l'activité du client
    private String adresse; // Adresse du client
    private String telClient; // Numéro de téléphone du client
    private String email; // Adresse email du client
    private Time dureeDeplacement; // Durée de déplacement pour rejoindre le client
    private BigDecimal distanceKm; // Distance en kilomètres pour rejoindre le client
    private String numAgence; // Numéro de l'agence associée au client
    private String nomClient; // Nom du client
    private ArrayList<Materiel> lesMateriels; // Liste des matériels du client
    private ArrayList<ContratMaintenance> lesContrats; // Liste des contrats de maintenance du client

    /**
     * Constructeur de la classe Client avec tous les paramètres.
     * @param numClient Numéro unique du client
     * @param raisonSociale Raison sociale de l'entreprise du client
     * @param SIREN Numéro SIREN du client
     * @param codeAPE Code APE de l'activité du client
     * @param adresse Adresse du client
     * @param telClient Numéro de téléphone du client
     * @param email Adresse email du client
     * @param dureeDeplacement Durée de déplacement pour rejoindre le client
     * @param distanceKm Distance en kilomètres pour rejoindre le client
     * @param lesMateriels Liste des matériels du client
     * @param lesContrats Liste des contrats de maintenance du client
     * @param numAgence Numéro de l'agence associée au client
     * @param nomClient Nom du client
     */
    public Client(String numClient, String raisonSociale, String SIREN, String codeAPE, String adresse, String telClient, String email, Time dureeDeplacement, BigDecimal distanceKm, ArrayList<Materiel> lesMateriels, ArrayList<ContratMaintenance> lesContrats, String numAgence, String nomClient) {
        this.numClient = numClient;
        this.raisonSociale = raisonSociale;
        this.SIREN = SIREN;
        this.codeAPE = codeAPE;
        this.adresse = adresse;
        this.telClient = telClient;
        this.email = email;
        this.dureeDeplacement = dureeDeplacement;
        this.distanceKm = distanceKm;
        this.lesMateriels = lesMateriels;
        this.numAgence = numAgence;
        this.lesContrats = lesContrats;
        this.nomClient = nomClient;
    }


    /**
     * Setteur numClient.
     * @param numClient Le numéro du client
     */
    public void setNumClient(String numClient) {
        this.numClient = numClient;
    }

    /**
     * Obtient les contrats.
     * @return les contrats.
     */
    public ArrayList<ContratMaintenance> getLesContrats() {
        return lesContrats;
    }

    // Getters pour les différents attributs du client

    /**
     * Obtient le num du client.
     * @return Le num du client.
     */
    public String getNumClient() {
        return numClient;
    }

    /**
     * Obtient le numéro de l'agence associée au client.
     * @return Le numéro de l'agence associée au client.
     */
    public String getNumAgence() {
        return numAgence;
    }

    /**
     * Obtient le nom du client.
     * @return Le nom du client.
     */
    public String getNomClient() {
        return nomClient;
    }

    /**
     * Obtient la distance en kilomètres pour rejoindre le client.
     * @return La distance en kilomètres pour rejoindre le client.
     */
    public BigDecimal getDistanceKm() {
        return distanceKm;
    }

    /**
     * Obtient la durée de déplacement pour rejoindre le client.
     * @return La durée de déplacement pour rejoindre le client.
     */
    public Time getDureeDeplacement() {
        return dureeDeplacement;
    }

    /**
     * Obtient l'adresse du client.
     * @return L'adresse du client.
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * Obtient le code APE de l'activité du client.
     * @return Le code APE de l'activité du client.
     */
    public String getCodeAPE() {
        return codeAPE;
    }

    /**
     * Obtient l'adresse email du client.
     * @return L'adresse email du client.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Obtient la raison sociale de l'entreprise du client.
     * @return La raison sociale de l'entreprise du client.
     */
    public String getRaisonSociale() {
        return raisonSociale;
    }

    /**
     * Obtient le numéro SIREN du client.
     * @return Le numéro SIREN du client.
     */
    public String getSIREN() {
        return SIREN;
    }

    /**
     * Obtient le numéro de téléphone du client.
     * @return Le numéro de téléphone du client.
     */
    public String getTelClient() {
        return telClient;
    }

    /**
     * Obtient la liste des matériels du client.
     * @return La liste des matériels du client.
     */
    public ArrayList<Materiel> getLesMateriels() {
        return this.lesMateriels;
    }

    /**
     * Retourne l'ensemble des matériels sous contrat valide du client.
     * @return Liste des matériels sous contrat valide
     */
    public ArrayList<Materiel> getLesMaterielsSousContrat() {
        ArrayList<Materiel> materielsSousContrat = new ArrayList<>();
        for (ContratMaintenance contrat : lesContrats) {
            if (contrat.estValide()) {
                materielsSousContrat.addAll(contrat.getLesMaterielsAssures());
            }
        }
        return materielsSousContrat;
    }

    /**
     * Retourne l'ensemble des matériels sous contrat non valide du client.
     * @return Liste des matériels sous contrat non valide
     */
    public ArrayList<Materiel> getLesMaterielsHorsContrat() {
        ArrayList<Materiel> materielsHorsContrat = new ArrayList<>();
        for (ContratMaintenance contrat : lesContrats) {
            if (!contrat.estValide()) {
                materielsHorsContrat.addAll(contrat.getLesMaterielsAssures());
            }
        }
        return materielsHorsContrat;
    }

    /**
     * Vérifie si le client est assuré, c'est-à-dire s'il a au moins un contrat de maintenance valide.
     * @return Vrai si le client est assuré, faux sinon
     */
    public boolean estAssure() {
        if (lesContrats != null) {
            for (ContratMaintenance contrat : lesContrats) {
                if (contrat.estValide()) {
                    return true; // Si un contrat est valide, retourne vrai
                }
            }
        }
        return false; // Si aucun contrat valide n'est trouvé, retourne faux
    }

}
