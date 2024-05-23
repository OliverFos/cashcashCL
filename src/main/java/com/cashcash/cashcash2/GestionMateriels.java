package com.cashcash.cashcash2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * La classe GestionMateriels gère les opérations liées aux matériels pour les clients,
 * y compris la génération de documents XML, la validation de XML, et l'interaction avec la base de données.
 */
public class GestionMateriels {
    private static PersistanceSQL donnees; // Attribut qui permet de rendre les objets métiers accessibles

    /**
     * Définit le modèle de persistance utilisé par la classe GestionMateriels.
     *
     * @param lesDonnes Le modèle de persistance à utiliser.
     */
    public static void setDonnees(PersistanceSQL lesDonnes) {
        donnees = lesDonnes;
    }

    /**
     * Constructeur de la classe GestionMateriels.
     *
     * @param lesDonnes Le modèle de persistance à utiliser.
     */
    public GestionMateriels(PersistanceSQL lesDonnes) {
        this.donnees = lesDonnes;
    }

    /**
     * Retourne l'objet Client qui possède l'identifiant idClient passé en paramètre.
     *
     * @param idClient L'identifiant du client.
     * @return Le client correspondant à l'identifiant, ou null si aucun client n'est trouvé.
     */
    public static Client getClient(String idClient) {
        Client leClient = null;
        leClient = (Client) donnees.chargerDepuisBase(idClient, "Client");
        return leClient;
    }

    /**
     * Retourne une chaîne de caractères qui représente le document XML de la liste des matériels
     * du client passé en paramètre.
     *
     * @param unClient Le client dont les matériels doivent être convertis en XML.
     * @return La chaîne de caractères représentant le document XML.
     */
    public static String xmlClient(Client unClient) {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<!DOCTYPE listeMateriel SYSTEM \"C:\\Users\\olide\\IdeaProjects\\CashCash\\listeMat.dtd\">");
        xmlBuilder.append("<listeMateriel>\n");
        xmlBuilder.append("<materiels idClient=\"").append(unClient.getNumClient()).append("\">\n");

        if (!unClient.getLesMaterielsSousContrat().isEmpty()) {
            xmlBuilder.append("<sousContrat>\n");
            for (Materiel materiel : unClient.getLesMaterielsSousContrat()) {
                xmlBuilder.append(materiel.xmlMateriel()).append("\n");
            }
            xmlBuilder.append("</sousContrat>\n");
        }

        if (!unClient.getLesMaterielsHorsContrat().isEmpty()) {
            xmlBuilder.append("<horsContrat>\n");
            for (Materiel materiel : unClient.getLesMaterielsHorsContrat()) {
                xmlBuilder.append(materiel.xmlMateriel()).append("\n");
            }
            xmlBuilder.append("</horsContrat>\n");
        }

        xmlBuilder.append("</materiels>\n");
        xmlBuilder.append("</listeMateriel>\n");

        return xmlBuilder.toString();
    }

    /**
     * Valide un contenu XML par rapport à une DTD.
     *
     * @param xmlContent Le contenu XML à valider.
     * @return true si le contenu XML est valide, false sinon.
     */
    public static boolean xmlClientValide(String xmlContent) {
        try {
            // Crée un XMLReader
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();

            // Configure la gestion des erreurs
            ErrorHandler errorHandler = new ErrorHandler() {
                @Override
                public void warning(SAXParseException e) throws SAXException {
                    throw e; // traite les avertissements comme des erreurs
                }

                @Override
                public void error(SAXParseException e) throws SAXException {
                    throw e; // traite les erreurs comme des erreurs
                }

                @Override
                public void fatalError(SAXParseException e) throws SAXException {
                    throw e; // traite les erreurs fatales comme des erreurs
                }
            };
            xmlReader.setErrorHandler(errorHandler);

            // Configure la validation DTD
            xmlReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            xmlReader.setFeature("http://xml.org/sax/features/validation", true);

            // Crée une InputSource à partir du contenu XML
            InputSource inputSource = new InputSource(new ByteArrayInputStream(xmlContent.getBytes()));

            // Analyse le contenu XML en utilisant l'InputSource
            xmlReader.parse(inputSource);

            return true; // Si la validation réussit, retourne true
        } catch (SAXException | IOException e) {
            // La validation a échoué, retourne false
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retourne les numéros de série des matériels sans contrat pour un client donné.
     *
     * @param idClient L'identifiant du client.
     * @return Un tableau de chaînes de caractères représentant les numéros de série des matériels sans contrat.
     */
    public static String[] getMaterielSansContrat(String idClient) {
        ArrayList<Materiel> lesMat = donnees.getMaterielsSansContrat(idClient);

        // Crée un tableau de chaînes pour stocker les numéros de série des matériels sans contrat
        String[] serialNumbers = new String[lesMat.size()];

        // Itère sur chaque objet Materiel dans la liste et extrait son numéro de série
        for (int i = 0; i < lesMat.size(); i++) {
            Materiel unMat = lesMat.get(i);
            serialNumbers[i] = unMat.getNumSerie();
        }

        return serialNumbers;
    }

    /**
     * Retourne l'identifiant du contrat.
     *
     * @return L'identifiant du contrat.
     */
    public String getIdContrat() {
        return donnees.getIdContrat();
    }

    /**
     * Insère un nouveau contrat dans la base de données.
     *
     * @param numContrat      Le numéro du contrat.
     * @param dateSign        La date de signature du contrat.
     * @param dateEch         La date d'échéance du contrat.
     * @param datePremSign    La date de première signature du contrat.
     * @param typeContrat     Le type de contrat.
     * @param numClient       Le numéro du client associé au contrat.
     */
    public void insertContrat(String numContrat, Date dateSign, Date dateEch, Date datePremSign, String typeContrat, String numClient) {
        donnees.insertContrat(numContrat, dateSign, dateEch, datePremSign, typeContrat, numClient);
    }

    /**
     * Met à jour le matériel pour l'associer à un contrat.
     *
     * @param numMat     Le numéro du matériel.
     * @param numContrat Le numéro du contrat auquel associer le matériel.
     */
    public void updateMat(String numMat, String numContrat) {
        donnees.updateMat(numMat, numContrat);
    }

    /**
     * Retourne les identifiants des contrats arrivant à échéance pour un client donné.
     *
     * @param numClient Le numéro du client.
     * @return Une liste de chaînes représentant les identifiants des contrats arrivant à échéance.
     */
    public static ArrayList<String> getContratEcheance(String numClient) {
        return donnees.getContratEcheance(numClient);
    }
}
