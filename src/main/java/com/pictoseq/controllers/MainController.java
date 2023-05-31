package com.pictoseq.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private GridPane SeqListGrid;
    @FXML
    private Button newSeqBtn;

    @FXML
    private void initialize() {
        // Ouvre la fenêtre de création d'un nouveau séquenciel
        newSeqBtn.setOnAction(event -> {
            System.out.println("New sequenciel");
        });

        // Ouvre la fenêtre de modification d'un séquenciel
        SeqListGrid.setOnMouseClicked(event -> {
            System.out.println("Edit sequenciel");
        });

        // Affiche la liste des séquenciels
        System.out.println("Liste des séquenciels");
    }

    @FXML
    private void openEditWindow(Sequentiel sequentiel) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/edit.fxml"));
        Parent root = loader.load();

        Stage editStage = new Stage();
        editStage.initModality(Modality.APPLICATION_MODAL);
        editStage.setTitle("Modifier un séquenciel");
        editStage.setScene(new Scene(root));
        editStage.setSequentiel(sequentiel);
        editStage.showAndWait();
    }

}
