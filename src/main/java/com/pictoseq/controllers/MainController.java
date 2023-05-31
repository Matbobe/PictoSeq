package com.pictoseq.controllers;

import com.pictoseq.models.PersistenceBySerialization;
import com.pictoseq.models.Sequentiel;
import com.pictoseq.models.SequentielList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private TilePane SeqListGrid;
    @FXML
    private Button newSeqBtn;
    @FXML
    private ScrollPane scrollPane;
    private SequentielList sequentielList;
    private PersistentModelManager persistentModelManager;

    @FXML
    private void initialize() {
        // Permet de redimensionner la liste des séquenciels en fonction de la taille de la fenêtre
        SeqListGrid.prefWidthProperty().bind(scrollPane.widthProperty());

        // Chargement de la liste des séquenciels depuis le fichier de sauvegarde
        persistentModelManager = new PersistenceBySerialization();
        sequentielList = persistentModelManager.load();

        // Ouvre la fenêtre de création d'un nouveau séquenciel
        newSeqBtn.setOnAction(event -> {
            Sequentiel sequentiel = new Sequentiel("Nouveau séquenciel n°" + (sequentielList.size() + 1));
            sequentielList.add(sequentiel);
            renderSequentielList();
            try {
                openEditWindow(sequentiel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Ouvre la fenêtre de modification d'un séquenciel
        SeqListGrid.setOnMouseClicked(event -> {
            System.out.println("Edit sequenciel");
            try {
                openEditWindow(sequentielList.get(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Affiche la liste des séquenciels
        renderSequentielList();
    }

    private void openEditWindow(Sequentiel sequentiel) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/edit.fxml"));
        Parent root = loader.load();
        EditController editController = loader.getController();

        Stage editStage = new Stage();
        editStage.initModality(Modality.APPLICATION_MODAL);
        editStage.setTitle("Modifier un séquenciel");
        editStage.setScene(new Scene(root));
        editStage.setMaximized(true);
        editController.setSequentiel(sequentiel);
        editStage.showAndWait();

        // Sauvegarder la liste des séquenciels quand la fenêtre de modification est fermée
        persistentModelManager.save(sequentielList);
        renderSequentielList();
    }

    private void renderSequentielList() {
        SeqListGrid.getChildren().clear();
        for (int i = 0; i < sequentielList.size(); i++) {
            SeqListGrid.getChildren().add(renderSequentiel(sequentielList.get(i)));
        }
    }

    private VBox renderSequentiel(Sequentiel sequentiel) {
        VBox sequentielBox = new VBox();
        sequentielBox.setPrefWidth(200);
        sequentielBox.setPrefHeight(100);
        sequentielBox.setSpacing(10);
        sequentielBox.getStyleClass().add("sequentiel");
        sequentielBox.getChildren().add(new Label(sequentiel.getName()));
        return sequentielBox;
    }
}
