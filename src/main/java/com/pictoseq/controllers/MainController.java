package com.pictoseq.controllers;

import com.pictoseq.models.Log;
import com.pictoseq.models.PersistenceBySerialization;
import com.pictoseq.models.Sequentiel;
import com.pictoseq.models.SequentielList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
    private ImageView idImageLogo;


    @FXML
    private void initialize() {
        // Permet de redimensionner la liste des séquenciels en fonction de la taille de la fenêtre
        SeqListGrid.prefWidthProperty().bind(scrollPane.widthProperty());

        // Chargement de la liste des séquenciels depuis le fichier de sauvegarde
        persistentModelManager = new PersistenceBySerialization();
        sequentielList = persistentModelManager.load();

        //mise en place du logo
        idImageLogo.setImage(new ImageView(String.valueOf(getClass().getResource("/images/Logo_Picto_Seq-removebg-preview.png"))).getImage());

        // Ouvre la fenêtre de création d'un nouveau séquenciel
        newSeqBtn.setOnAction(event -> {
            Sequentiel sequentiel = new Sequentiel("Nouveau séquenciel n°" + (sequentielList.size() + 1));
            sequentielList.add(sequentiel);
            renderSequentielList();
            try {
                Log.println("Edit new sequenciel");
                openEditWindow(sequentiel);
            } catch (IOException e) {
                Log.printError("Error while opening edit window");
                e.printStackTrace();
            }
        });

        // Ouvre la fenêtre de modification d'un séquenciel
        SeqListGrid.setOnMouseClicked(event -> {
            try {
                Log.println("Edit sequenciel");
                Node target = (Node) event.getTarget();
                while (target != null && !SeqListGrid.getChildren().contains(target)) {
                    target = target.getParent();
                }

                if (target != null) {
                    int index = SeqListGrid.getChildren().indexOf(target);
                    if (index >= 0 && index < sequentielList.size()) {
                        openEditWindow(sequentielList.get(index));
                    }
                }
            } catch (IOException e) {
                Log.printError("Error while opening edit window");
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
        HBox spacer = new HBox();
        ImageView dupliIcon = new ImageView(String.valueOf(getClass().getResource("/images/dupli-icon.png")));
        ImageView deleteIcon = new ImageView(String.valueOf(getClass().getResource("/images/delete-icon.png")));

        sequentielBox.setPrefWidth(200);
        sequentielBox.setPrefHeight(100);
        sequentielBox.setSpacing(10);
        sequentielBox.setPadding(new Insets(3, 3, 5, 5));
        sequentielBox.getStyleClass().add("sequentiel");
        //Header
        header.setPrefHeight(30);
        header.setPrefWidth(200);
        header.setAlignment(Pos.BOTTOM_RIGHT);

        // Dupliquer
        dupliIcon.setFitHeight(25);
        dupliIcon.setFitWidth(25);
        Pane dupliPane = new Pane(dupliIcon);
        dupliPane.setPrefSize(25, 25);
        dupliPane.setOnMouseClicked(event -> {
            duplicateSequentiel(sequentiel);
            event.consume();
        });

        // Supprimer
        deleteIcon.setFitHeight(25);
        deleteIcon.setFitWidth(25);
        Pane deletePane = new Pane(deleteIcon);
        deletePane.setPrefSize(25, 25);
        deletePane.setOnMouseClicked(event -> {
            deleteSequentiel(sequentiel);
            event.consume();
        });

        header.getChildren().add(dupliPane);
        header.getChildren().add(deletePane);

        sequentielBox.getChildren().add(header);
        // Middle
        spacer.setPrefHeight(50);
        spacer.setPrefWidth(200);
        spacer.setAlignment(Pos.CENTER);
        spacer.setSpacing(5);
        spacer.setPadding(new Insets(0, 5, 0, 5));
        int size = sequentiel.size();
        if (size != 0) {
            if (size > 3) size = 3;
            ImageView[] images = sequentiel.getTreePictogrameImageView(size);
            for (int i = 0; i < size; i++) {
                images[i].setFitHeight(40);
                images[i].setFitWidth(40);
                spacer.getChildren().add(images[i]);
            }
        } else {
            spacer.getChildren().add(new Label("Aucun pictogramme"));
        }
        sequentielBox.getChildren().add(spacer);
        // Footer
        sequentielBox.getChildren().add(new Label(sequentiel.getName()));
        return sequentielBox;
    }

    private void deleteSequentiel(Sequentiel sequentiel) {
        sequentielList.remove(sequentiel);
        persistentModelManager.save(sequentielList);
        renderSequentielList();
        Log.println("Delete sequentiel");
    }

    private void duplicateSequentiel(Sequentiel sequentiel) {
        // duplicate sequentiel
        Sequentiel newSequentiel = new Sequentiel(sequentiel);
        sequentielList.add(newSequentiel);
        persistentModelManager.save(sequentielList);
        renderSequentielList();
        Log.println("Duplicate sequentiel");
    }
}
