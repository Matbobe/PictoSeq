package com.pictoseq.controllers;

import com.pictoseq.models.Sequentiel;
import com.pictoseq.models.SequentielList;
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
    private SequentielList sequentielList;

    @FXML
    private void initialize() {
        // Ouvre la fenêtre de création d'un nouveau séquenciel
        newSeqBtn.setOnAction(event -> {
            System.out.println("New sequenciel");
            sequentielList.add(new Sequentiel());
            try {
                openEditWindow(sequentielList.get(sequentielList.size() - 1));
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
        System.out.println("Liste des séquenciels");
    }

    private void openEditWindow(Sequentiel sequentiel) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/edit.fxml"));
        Parent root = loader.load();
        EditController editController = loader.getController();

        Stage editStage = new Stage();
        editStage.initModality(Modality.APPLICATION_MODAL);
        editStage.setTitle("Modifier un séquenciel");
        editStage.setScene(new Scene(root));
        editController.setSequentiel(sequentiel);
        editStage.showAndWait();
    }

    public void saveSequentielList() {
        // TODO: Sauvegarder la liste des séquenciels
    }

}
