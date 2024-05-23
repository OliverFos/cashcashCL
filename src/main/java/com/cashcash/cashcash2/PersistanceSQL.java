package com.cashcash.cashcash2;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

/**
 * Cette classe gère la persistance des données dans une base de données SQL.
 */
public class PersistanceSQL {
    private String ipBase = "127.0.0.1";
    private int port = 3306;
    private String nomBaseDonnee = "cashcash";

    /**
     * Constructeur de la classe PersistanceSQL.
     *
     * @param ipBase         L'adresse IP du serveur de base de données.
     * @param port           Le port d'écoute du serveur de base de données.
     * @param nomBaseDonnee  Le nom de la base de données.
     */
    public PersistanceSQL(String ipBase, int port, String nomBaseDonnee) {
        this.ipBase = ipBase;
        this.port = port;
        this.nomBaseDonnee = nomBaseDonnee;
    }

    /**
     * Méthode pour ranger un objet dans la base de données.
     *
     * @param unObjet L'objet à ranger dans la base de données.
     */
    public void rangerDansBase(Object unObjet) {
        try {
            // Établir une connexion à la base de données
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + ipBase + ":" + port + "/" + nomBaseDonnee, "cashcashClientLourd", "clientLourd123");

            // Créer un PreparedStatement pour insérer des données
            PreparedStatement preparedStatement = null;

            // Déterminer le type de l'objet et exécuter les opérations de base de données appropriées
            String tableName;
            switch (unObjet.getClass().getSimpleName()) {
                case "TypeMateriel":
                    TypeMateriel leTypeMat = (TypeMateriel) unObjet;
                    preparedStatement = connection.prepareStatement("INSERT INTO type (refInterne, libelleType) VALUES (?, ?)");
                    preparedStatement.setString(1, leTypeMat.getReferenceInterne());
                    preparedStatement.setString(2, leTypeMat.getLibelleTypeMateriel());
                    break;
                case "Materiel":
                    Materiel leMat = (Materiel) unObjet;
                    preparedStatement = connection.prepareStatement("INSERT INTO materiel (numSerie, dateVente,dateInstall,prixVente,emplacement,numClient,numContrat,refInterne) VALUES (?,?,?,?,?,?,?,?)");
                    preparedStatement.setString(1, leMat.getNumSerie());
                    preparedStatement.setDate(2, leMat.getDateVente());
                    preparedStatement.setDate(3, leMat.getDateInstallation());
                    preparedStatement.setFloat(4, leMat.getPrixVente());
                    preparedStatement.setString(5, leMat.getEmplacement());
                    preparedStatement.setString(6, leMat.getNumClient());
                    preparedStatement.setString(7, leMat.getNumContrat());
                    preparedStatement.setString(8, leMat.getLeType().getReferenceInterne());
                    break;
                case "ContratMaintenance":
                    ContratMaintenance leContrat = (ContratMaintenance) unObjet;
                    preparedStatement = connection.prepareStatement("INSERT INTO contrat (numContrat, dateSignature, dateEcheance, RefTypeContrat, numClient) VALUES (?, ?, ?, ?, ?)");
                    preparedStatement.setString(1, leContrat.getNumContrat());
                    preparedStatement.setDate(2, leContrat.getDateSignature());
                    preparedStatement.setDate(3, leContrat.getDateEcheance());
                    preparedStatement.setDate(4, leContrat.getDatePremierSignature());
                    preparedStatement.setString(5, leContrat.getRefTypeContrat());
                    preparedStatement.setString(6, leContrat.getNumClient());
                    break;
                case "Client":
                    Client leCli = (Client) unObjet;
                    preparedStatement = connection.prepareStatement("INSERT INTO client (numClient, raisonSociale, SIREN, APE, adresseClient, telephoneClient, emailClient, nomClient, distanceKilo, tempsDeplacement, numAgence) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    preparedStatement.setString(1, leCli.getNumClient());
                    preparedStatement.setString(2, leCli.getRaisonSociale());
                    preparedStatement.setString(3, leCli.getSIREN());
                    preparedStatement.setString(4, leCli.getCodeAPE());
                    preparedStatement.setString(5, leCli.getAdresse());
                    preparedStatement.setString(6, leCli.getTelClient());
                    preparedStatement.setString(7, leCli.getEmail());
                    preparedStatement.setString(8, leCli.getNomClient());
                    preparedStatement.setBigDecimal(9, leCli.getDistanceKm());
                    preparedStatement.setTime(10, leCli.getDureeDeplacement());
                    preparedStatement.setString(11, leCli.getNumAgence());
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported object type: " + unObjet.getClass().getSimpleName());
            }

            // Exécuter l'instruction préparée
            preparedStatement.executeUpdate();

            // Fermer les ressources
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les erreurs potentielles
        }
    }
    /**
     * Charge un objet depuis la base de données en fonction de son identifiant et de sa classe.
     *
     * @param id        L'identifiant de l'objet à charger depuis la base de données.
     * @param nomClasse Le nom de la classe de l'objet à charger.
     * @return L'objet chargé depuis la base de données, ou null s'il n'existe pas.
     */
    public Object chargerDepuisBase(String id, String nomClasse) {
        Object obj = null;
        switch (nomClasse) {
            case "TypeMateriel":
                obj = chargerTypeMaterielDepuisBase(id);
                break;
            case "Materiel":
                obj = chargerMaterielDepuisBase(id);
                break;
            case "Client":
                obj = chargerClientDepuisBase(id);
                break;
            case "ContratMaintenance":
                obj = chargerContratMaintenanceDepuisBase(id);
                break;
            // Gérer d'autres classes si nécessaire
        }
        return obj;
    }

    /**
     * Charge un objet de type TypeMateriel depuis la base de données en fonction de son identifiant.
     *
     * @param id L'identifiant du TypeMateriel à charger depuis la base de données.
     * @return L'objet TypeMateriel chargé depuis la base de données.
     */
    private TypeMateriel chargerTypeMaterielDepuisBase(String id) {
        TypeMateriel typeMateriel = null;
        try {
            // Établir une connexion à la base de données
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + ipBase + ":" + port + "/" + nomBaseDonnee, "cashcashClientLourd", "clientLourd123");

            // Créer un PreparedStatement pour exécuter la requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM type WHERE refInterne = ?");
            preparedStatement.setString(1, id); // Définir la valeur du paramètre dans la requête

            // Exécuter la requête et obtenir le résultat
            ResultSet resultSet = preparedStatement.executeQuery();

            // Vérifier si le résultat contient des lignes
            if (resultSet.next()) {
                // Récupérer les données du résultat et créer un objet TypeMateriel
                String refInterne = resultSet.getString("refInterne");
                String libelleType = resultSet.getString("libelleType");

                // Créer un nouvel objet TypeMateriel
                typeMateriel = new TypeMateriel(refInterne, libelleType);
            }

            // Fermer le résultat et le statement
            resultSet.close();
            preparedStatement.close();

            // Fermer la connexion à la base de données
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les erreurs potentielles
        }
        return typeMateriel;
    }

    /**
     * Charge un objet de type Materiel depuis la base de données en fonction de son identifiant.
     *
     * @param id L'identifiant du Materiel à charger depuis la base de données.
     * @return L'objet Materiel chargé depuis la base de données.
     */
    private Materiel chargerMaterielDepuisBase(String id) {
        Materiel materiel = null;
        try {
            // Établir une connexion à la base de données
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + ipBase + ":" + port + "/" + nomBaseDonnee, "cashcashClientLourd", "clientLourd123");

            // Créer un PreparedStatement pour exécuter la requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM materiel WHERE numSerie = ?");
            preparedStatement.setString(1, id); // Définir la valeur du paramètre dans la requête

            // Exécuter la requête et obtenir le résultat
            ResultSet resultSet = preparedStatement.executeQuery();

            // Vérifier si le résultat contient des lignes
            if (resultSet.next()) {
                // Récupérer les données du résultat et créer un objet Materiel
                String numSerie = resultSet.getString("numSerie");
                Date dateVente = resultSet.getDate("dateVente");
                Date dateInstallation = resultSet.getDate("dateInstall");
                float prixVente = resultSet.getFloat("prixVente");
                String emplacement = resultSet.getString("emplacement");
                String numClient = resultSet.getString("numClient");
                String numContrat = resultSet.getString("numContrat");
                String refInterneTypeMateriel = resultSet.getString("refInterne");
                // Récupérer l'objet TypeMateriel associé au Materiel
                TypeMateriel leType = chargerTypeMaterielDepuisBase(refInterneTypeMateriel);

                // Créer un nouvel objet Materiel
                materiel = new Materiel(numSerie, dateVente, dateInstallation, prixVente, emplacement, numClient, numContrat, leType);
            }

            // Fermer le résultat et le statement
            resultSet.close();
            preparedStatement.close();

            // Fermer la connexion à la base de données
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les erreurs potentielles
        }
        return materiel;
    }

    /**
     * Charge un objet de type ContratMaintenance depuis la base de données en fonction de son identifiant.
     *
     * @param id L'identifiant du ContratMaintenance à charger depuis la base de données.
     * @return L'objet ContratMaintenance chargé depuis la base de données.
     */
    private ContratMaintenance chargerContratMaintenanceDepuisBase(String id) {
        ContratMaintenance contratMaintenance = null;
        try {
            // Établir une connexion à la base de données
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + ipBase + ":" + port + "/" + nomBaseDonnee, "cashcashClientLourd", "clientLourd123");

            // Créer un PreparedStatement pour exécuter la requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM contrat WHERE numContrat = ?");
            preparedStatement.setString(1, id); // Définir la valeur du paramètre dans la requête

            // Exécuter la requête et obtenir le résultat
            ResultSet resultSet = preparedStatement.executeQuery();

            // Vérifier si le résultat contient des lignes
            if (resultSet.next()) {
                // Récupérer les données du résultat et créer un objet ContratMaintenance
                String numContrat = resultSet.getString("numContrat");
                Date dateSignature = resultSet.getDate("dateSignature");
                Date dateEcheance = resultSet.getDate("dateEcheance");
                Date datePremierSignature = resultSet.getDate("datePremierSignature");
                String refTypeContrat = resultSet.getString("RefTypeContrat");
                String numClient = resultSet.getString("numClient");

                // Récupérer la liste d'objets Materiel associés au contrat
                ArrayList<Materiel> lesMaterielsAssures = chargerLesMateriels(id, "numContrat");

                // Créer un nouvel objet ContratMaintenance
                contratMaintenance = new ContratMaintenance(numContrat, dateSignature, dateEcheance, datePremierSignature, refTypeContrat, numClient, lesMaterielsAssures);
            }

            // Fermer le résultat et le statement
            resultSet.close();
            preparedStatement.close();

            // Fermer la connexion à la base de données
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les erreurs potentielles
        }
        return contratMaintenance;
    }

    /**
     * Charge la liste des objets Materiel associés à un certain identifiant depuis la base de données.
     *
     * @param id     L'identifiant utilisé pour récupérer les objets Materiel associés.
     * @param column Le nom de la colonne à utiliser dans la condition de la requête SQL.
     * @return Une liste d'objets Materiel associés à l'identifiant spécifié.
     */
    private ArrayList<Materiel> chargerLesMateriels(String id, String column) {
        ArrayList<Materiel> lesMats = new ArrayList<>();
        try {
            // Établir une connexion à la base de données
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + ipBase + ":" + port + "/" + nomBaseDonnee, "cashcashClientLourd", "clientLourd123");

            // Créer un PreparedStatement pour exécuter la requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM materiel WHERE " + column + " = ?");
            preparedStatement.setString(1, id); // Définir la valeur du paramètre dans la requête

            // Exécuter la requête et obtenir le résultat
            ResultSet resultSet = preparedStatement.executeQuery();

            // Itérer sur le résultat et créer des objets Materiel
            while (resultSet.next()) {
                // Récupérer les données du résultat et créer un objet Materiel
                String numSerie = resultSet.getString("numSerie");
                Date dateVente = resultSet.getDate("dateVente");
                Date dateInstallation = resultSet.getDate("dateInstall");
                float prixVente = resultSet.getFloat("prixVente");
                String emplacement = resultSet.getString("emplacement");

                // Supposons que vous ayez déjà chargé l'objet TypeMateriel associé à ce Materiel depuis la base de données
                TypeMateriel leType = chargerTypeMaterielDepuisBase(resultSet.getString("refInterne"));

                // Créer un nouvel objet Materiel
                Materiel materiel = new Materiel(numSerie, dateVente, dateInstallation, prixVente, emplacement, null, id, leType);

                // Ajouter l'objet Materiel à la liste
                lesMats.add(materiel);
            }

            // Fermer le résultat et le statement
            resultSet.close();
            preparedStatement.close();

            // Fermer la connexion à la base de données
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les erreurs potentielles
        }
        return lesMats;
    }

    /**
     * Charge un objet de type Client depuis la base de données en fonction de son identifiant.
     *
     * @param id L'identifiant du Client à charger depuis la base de données.
     * @return L'objet Client chargé depuis la base de données.
     */
    private Client chargerClientDepuisBase(String id) {
        Client client = null;
        try {
            // Établir une connexion à la base de données
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + ipBase + ":" + port + "/" + nomBaseDonnee, "cashcashClientLourd", "clientLourd123");

            // Créer un PreparedStatement pour exécuter la requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM client WHERE numClient = ?");
            preparedStatement.setString(1, id); // Définir la valeur du paramètre dans la requête

            // Exécuter la requête et obtenir le résultat
            ResultSet resultSet = preparedStatement.executeQuery();

            // Vérifier si le résultat contient des lignes
            if (resultSet.next()) {
                // Récupérer les données du résultat et créer un objet Client
                String numClient = resultSet.getString("numClient");
                String raisonSociale = resultSet.getString("raisonSociale");
                String SIREN = resultSet.getString("SIREN");
                String codeAPE = resultSet.getString("APE");
                String adresse = resultSet.getString("adresseClient");
                String telClient = resultSet.getString("telephoneClient");
                String email = resultSet.getString("emailClient");
                Time dureeDeplacement = resultSet.getTime("tempsDeplacement");
                BigDecimal distanceKm = resultSet.getBigDecimal("distanceKilo");
                String numAgence = resultSet.getString("numAgence");
                String nomClient = resultSet.getString("nomClient");

                // Récupérer la liste d'objets ContratMaintenance associés au client
                ArrayList<ContratMaintenance> lesContrats = chargerLesContrats(id);

                // Supposons que vous ayez déjà chargé la liste d'objets Materiel associés au client depuis la base de données
                ArrayList<Materiel> lesMateriels = chargerLesMateriels(id, "numClient");

                // Créer un nouvel objet Client
                client = new Client(numClient, raisonSociale, SIREN, codeAPE, adresse, telClient, email, dureeDeplacement, distanceKm, lesMateriels, lesContrats, numAgence, nomClient);
            }

            // Fermer le résultat et le statement
            resultSet.close();
            preparedStatement.close();

            // Fermer la connexion à la base de données
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les erreurs potentielles
        }
        return client;
    }



    /**
     * Charge les contrats associés à un client spécifique depuis la base de données.
     *
     * @param id L'identifiant du client pour lequel charger les contrats.
     * @return Une liste d'objets ContratMaintenance associés au client spécifié.
     */
    private ArrayList<ContratMaintenance> chargerLesContrats(String id) {
        ArrayList<ContratMaintenance> lesContrats = new ArrayList<>();
        try {
            // Établir une connexion à la base de données
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + ipBase + ":" + port + "/" + nomBaseDonnee, "cashcashClientLourd", "clientLourd123");

            // Préparer la requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM contrat WHERE numClient = ?");
            preparedStatement.setString(1, id); // Définir la valeur du paramètre dans la requête

            // Exécuter la requête et obtenir le jeu de résultats
            ResultSet resultSet = preparedStatement.executeQuery();

            // Itérer sur le jeu de résultats et créer des objets ContratMaintenance
            while (resultSet.next()) {
                // Récupérer les données du jeu de résultats et créer un objet ContratMaintenance
                String numContrat = resultSet.getString("numContrat");
                Date dateSignature = resultSet.getDate("dateSignature");
                Date dateEcheance = resultSet.getDate("dateEcheance");
                Date datePremierSignature = resultSet.getDate("datePremierSignature");
                String refTypeContrat = resultSet.getString("RefTypeContrat");

                // Supposons que vous avez déjà chargé la liste des objets Materiel associés à chaque contrat depuis la base de données
                ArrayList<Materiel> lesMaterielsAssures = chargerLesMateriels(numContrat, "numContrat");

                // Créer un nouvel objet ContratMaintenance
                ContratMaintenance contratMaintenance = new ContratMaintenance(numContrat, dateSignature, dateEcheance, datePremierSignature, refTypeContrat, id, lesMaterielsAssures);

                // Ajouter l'objet ContratMaintenance à la liste
                lesContrats.add(contratMaintenance);
            }

            // Fermer le jeu de résultats et le statement
            resultSet.close();
            preparedStatement.close();

            // Fermer la connexion à la base de données
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les erreurs SQL
        }
        return lesContrats;
    }

    /**
     * Récupère les matériels associés à un client spécifique et n'ayant pas de contrat.
     *
     * @param idClient L'identifiant du client pour lequel récupérer les matériels sans contrat.
     * @return Une liste d'objets Materiel n'ayant pas de contrat et associés au client spécifié.
     */
    public ArrayList<Materiel> getMaterielsSansContrat(String idClient) {
        ArrayList<Materiel> materielsSansContrat = new ArrayList<>();
        try {
            // Établir une connexion à la base de données
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + ipBase + ":" + port + "/" + nomBaseDonnee, "cashcashClientLourd", "clientLourd123");

            // Préparer la requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM materiel WHERE numClient = ? AND numContrat IS NULL");
            preparedStatement.setString(1, idClient); // Définir la valeur du paramètre dans la requête

            // Exécuter la requête et obtenir le jeu de résultats
            ResultSet resultSet = preparedStatement.executeQuery();

            // Traiter le jeu de résultats
            while (resultSet.next()) {
                // Récupérer les données du jeu de résultats et créer un objet Materiel
                String numSerie = resultSet.getString("numSerie");
                Date dateVente = resultSet.getDate("dateVente");
                Date dateInstallation = resultSet.getDate("dateInstall");
                float prixVente = resultSet.getFloat("prixVente");
                String emplacement = resultSet.getString("emplacement");

                // Supposons que vous avez déjà chargé l'objet TypeMateriel associé à ce Materiel depuis la base de données
                TypeMateriel leType = chargerTypeMaterielDepuisBase(resultSet.getString("refInterne"));

                // Créer un nouvel objet Materiel
                Materiel materiel = new Materiel(numSerie, dateVente, dateInstallation, prixVente, emplacement, null, null, leType);

                // Ajouter l'objet Materiel à la liste
                materielsSansContrat.add(materiel);
            }

            // Fermer les ressources
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les erreurs SQL
        }
        return materielsSansContrat;
    }

    /**
     * Récupère un nouvel identifiant de contrat en s'assurant qu'il est unique.
     *
     * @return Un nouvel identifiant de contrat.
     */
    public String getIdContrat() {
        // Récupère le numéro de contrat maximal depuis la base de données
        String maxContratNumber = "";
        try {
            // Établir une connexion à la base de données
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + ipBase + ":" + port + "/" + nomBaseDonnee, "cashcashClientLourd", "clientLourd123");

            // Préparer l'instruction SQL pour récupérer le numéro de contrat maximal
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(numContrat) AS maxContrat FROM contrat");

            // Exécuter la requête et obtenir le jeu de résultats
            ResultSet resultSet = preparedStatement.executeQuery();

            // Traiter le jeu de résultats
            if (resultSet.next()) {
                // Récupérer le numéro de contrat maximal
                maxContratNumber = resultSet.getString("maxContrat");
            }

            // Fermer les ressources
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les erreurs potentielles
        }

        // Incrémenter le numéro de contrat maximal
        int maxNumber = Integer.parseInt(maxContratNumber.substring(1)); // Extraire la partie numérique et la convertir en int
        maxNumber++; // Incrémenter
        String newContratNumber = "C" + String.format("%04d", maxNumber); // Formater le nouveau numéro de contrat

        return newContratNumber;
    }

    /**
     * Insère un nouveau contrat dans la base de données.
     *
     * @param numContrat     Numéro du contrat.
     * @param dateSign       Date de signature du contrat.
     * @param dateEch        Date d'échéance du contrat.
     * @param datePremSign   Date de la première signature du contrat.
     * @param typeContrat    Type de contrat.
     * @param numClient      Numéro du client associé au contrat.
     */
    public void insertContrat(String numContrat, java.util.Date dateSign, java.util.Date dateEch, java.util.Date datePremSign, String typeContrat, String numClient) {
        try {
            // Établir une connexion à la base de données
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + ipBase + ":" + port + "/" + nomBaseDonnee, "cashcashClientLourd", "clientLourd123");

            // Préparer l'instruction SQL
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO contrat (numContrat, dateSignature, dateEcheance, datePremierSignature, RefTypeContrat, numClient) VALUES (?, ?, ?, ?, ?, ?)");

            // Définir les paramètres pour l'instruction préparée
            preparedStatement.setString(1, numContrat);
            preparedStatement.setDate(2, new java.sql.Date(dateSign.getTime()));
            preparedStatement.setDate(3, new java.sql.Date(dateEch.getTime()));
            preparedStatement.setDate(4, new java.sql.Date(datePremSign.getTime()));
            preparedStatement.setString(5, typeContrat);
            preparedStatement.setString(6, numClient);

            // Exécuter l'instruction SQL
            preparedStatement.executeUpdate();

            // Fermer la connexion et l'instruction pour libérer les ressources
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer toute exception SQL appropriée
        }
    }


    /**
     * Met à jour le numéro de contrat d'un matériel dans la base de données.
     *
     * @param numMat     Numéro de série du matériel.
     * @param numContrat Nouveau numéro de contrat.
     */
    public void updateMat(String numMat, String numContrat) {
        try {
            // Établir une connexion à la base de données
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + ipBase + ":" + port + "/" + nomBaseDonnee, "cashcashClientLourd", "clientLourd123");

            // Préparer l'instruction SQL
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE materiel SET numContrat = ? WHERE numSerie = ?");

            // Définir les paramètres pour l'instruction préparée
            preparedStatement.setString(1, numContrat);
            preparedStatement.setString(2, numMat);

            // Exécuter l'instruction SQL
            preparedStatement.executeUpdate();

            // Fermer la connexion et l'instruction pour libérer les ressources
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer toute exception SQL appropriée
        }
    }

    /**
     * Récupère les contrats échus pour un client donné.
     *
     * @param numClient Numéro du client.
     * @return Une liste de numéros de contrat échus.
     */
    public ArrayList<String> getContratEcheance(String numClient) {
        ArrayList<String> contratEcheance = new ArrayList<>();
        try {
            // Établir une connexion à la base de données
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + ipBase + ":" + port + "/" + nomBaseDonnee, "cashcashClientLourd", "clientLourd123");

            // Préparer l'instruction SQL
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT numContrat FROM contrat WHERE dateEcheance < CURRENT_DATE AND numClient = ?"
            );

            // Définir le paramètre
            preparedStatement.setString(1, numClient);

            // Exécuter la requête
            ResultSet resultSet = preparedStatement.executeQuery();

            // Traiter le jeu de résultats
            while (resultSet.next()) {
                String numContrat = resultSet.getString("numContrat");
                contratEcheance.add(numContrat);
            }

            // Fermer le jeu de résultats, l'instruction préparée et la connexion
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer toute exception SQL appropriée
        }

        return contratEcheance;
    }


    public ContratMaintenance getContrat(String numContrat){
        ContratMaintenance unContrat = null;
        unContrat = (ContratMaintenance) chargerDepuisBase(numContrat,"ContratMaintenance");
        return unContrat;
    }
}
