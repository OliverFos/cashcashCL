package com.cashcash.cashcash2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principale de l'application JavaFX qui charge et affiche la vue principale.
 */
public class Index extends Application {

    /**
     * Méthode principale de l'application JavaFX. Elle est appelée au démarrage de l'application.
     *
     * @param primaryStage La scène principale de l'application.
     * @throws Exception Si une erreur survient lors du chargement de la vue.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Chargement de la vue FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("index-view.fxml"));
        Parent root = loader.load();

        // Création de la scène avec la vue chargée
        Scene scene = new Scene(root, 500, 400);

        // Configuration et affichage de la scène principale
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Méthode principale de l'application qui lance l'application JavaFX.
     *
     * @param args Les arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        // Lancement de l'application JavaFX
        launch(args);
    }
}
