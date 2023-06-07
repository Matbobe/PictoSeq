package com.pictoseq.controllers;

import com.pictoseq.app.Application;
import com.pictoseq.models.*;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.concurrent.ExecutionException;

public class EditController {
    private HttpClient client;
    public SearchList searchList;
    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField searchBar;
    @FXML
    private GridPane searchListGrid;
    private Sequentiel sequentiel;

    @FXML
    void onRetourClick(ActionEvent event) {
        Stage editStage = (Stage) borderPane.getScene().getWindow();
        editStage.close();
    }

    @FXML
    void initialize() {
        client = HttpClient.newHttpClient();
        searchList = new SearchList(searchListGrid, client, this);
    }
    @FXML
    void handleTextSearch(KeyEvent event) {
        String text = searchBar.getText().toLowerCase().replace(" ", "+");
        String category = "all";
        // TODO: Gérer les catégories
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                searchList.searchRequest(text, category);
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            try {
                // Attendre la fin de la recherche avant d'appeler render()
                task.get(); // Attendre que la tâche soit terminée
                searchList.render();
            } catch (IOException | InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });

        task.setOnFailed(e -> {
            // Gérer les erreurs ici
        });

        new Thread(task).start();
    }

    @FXML
    void searchGridHandleScrollFiniched(ScrollEvent event) throws IOException, InterruptedException {
        if (event.getDeltaY() > 0) return; // On ne s'intéresse qu'au scroll vers le bas
        if (searchListGrid.getRowCount() == 0) return; // Si la grille est vide, on ne fait rien

        int lastRowIndex = searchListGrid.getRowCount() - 1;
        int lastColumnIndex = searchListGrid.getColumnCount() - 1;
        int lastCellIndex = lastRowIndex * searchListGrid.getColumnCount() + lastColumnIndex;
        javafx.scene.Node lastCell = searchListGrid.getChildren().get(lastCellIndex);
        double lastCellBottom = lastCell.getBoundsInParent().getMaxY();
        double searchListGridBottom = searchListGrid.getBoundsInParent().getMaxY();
        if (lastCellBottom < searchListGridBottom) return; // Si la dernière cellule n'est pas en bas de la grille, on ne fait rien

        searchList.renderNext();
    }

    public void addPictogramme(Pictograme pictograme) {
        sequentiel.addPictograme(pictograme);
        Log.println("Added pictogram to the sequentiel");
    }
    public void setSequentiel(Sequentiel sequentiel) {
        this.sequentiel = sequentiel;
        borderPane.setCenter(sequentiel.getScrollPane());
    }
}