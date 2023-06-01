package com.pictoseq.controllers;

import com.pictoseq.models.PersistenceBySerialization;
import com.pictoseq.models.Sequentiel;
import com.pictoseq.models.SequentielList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
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
        HBox header = new HBox();
        Pane spacer = new Pane();
        ImageView dupliIcon = new ImageView(String.valueOf(getClass().getResource("/images/dupli-icon.png")));
        ImageView deleteIcon =  new ImageView(String.valueOf(getClass().getResource("/images/delete-icon.png")));

        sequentielBox.setPrefWidth(200);
        sequentielBox.setPrefHeight(100);
        sequentielBox.setSpacing(10);
        sequentielBox.setPadding(new Insets(5));
        sequentielBox.getStyleClass().add("sequentiel");
        //Header
        header.setPrefHeight(20);
        header.setPrefWidth(200);
        header.setAlignment(Pos.CENTER_RIGHT);

        dupliIcon.setFitHeight(20);
        dupliIcon.setFitWidth(20);
        // TODO: dupliquer le séquenciel (ne marche pas)
        dupliIcon.setOnMouseClicked(event -> duplicateSequentiel(sequentiel));
        deleteIcon.setFitHeight(20);
        deleteIcon.setFitWidth(20);
        // TODO: supprimer le séquenciel (ne marche pas)
        deleteIcon.setOnMouseClicked(event -> deleteSequentiel(sequentiel));
        header.getChildren().add(dupliIcon);
        header.getChildren().add(deleteIcon);
        sequentielBox.getChildren().add(header);
        // Middle
        spacer.setPrefHeight(50);
        sequentielBox.getChildren().add(spacer);
        // Footer
        sequentielBox.getChildren().add(new Label(sequentiel.getName()));
        return sequentielBox;
    }

    private void deleteSequentiel(Sequentiel sequentiel) {
        sequentielList.remove(sequentiel);
        renderSequentielList();
    }

    private void duplicateSequentiel(Sequentiel sequentiel) {
        // TODO: dupliquer le séquenciel
        Sequentiel newSequentiel = new Sequentiel(sequentiel.getName() + " (copie)");
        sequentielList.add(newSequentiel);
        renderSequentielList();
    }
}
