package com.pictoseq.controllers;

import com.pictoseq.app.Application;
import com.pictoseq.models.*;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.*;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
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
    private TilePane searchListGrid;
    @FXML
    private ScrollPane scrollPaneSequentiel;

    private Sequentiel sequentiel;

    @FXML
    private HBox headerBox;
    @FXML
    public static ColorPicker idColor;

    @FXML
    private ChoiceBox<?> idDirection;

    @FXML
    private ChoiceBox<?> idNum;

    @FXML
    private ChoiceBox<?> idText;

    @FXML
    void onRetourClick(ActionEvent event) {
        Stage editStage = (Stage) borderPane.getScene().getWindow();
        editStage.close();
    }

    @FXML
    void initialize() {
        client = HttpClient.newHttpClient();
        searchList = new SearchList(searchListGrid, client, this);

        idNum.setOnAction(event -> {
            Direction dir = Direction.getDirection(idNum.getValue().toString());
            sequentiel.changeDirectionOfNumbers(dir);
        });
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
        if (searchListGrid.getChildren().isEmpty()) return; // Si le TilePane est vide, on ne fait rien

        javafx.scene.Node lastChild = searchListGrid.getChildren().get(searchListGrid.getChildren().size() - 1);
        double lastChildBottom = lastChild.getBoundsInParent().getMaxY();
        double searchListTilePaneBottom = searchListGrid.getBoundsInParent().getMaxY();

        // Si le dernier enfant n'est pas en bas du TilePane, on ne fait rien
        if (lastChildBottom < searchListTilePaneBottom) return;

        // Si on arrive ici, c'est qu'on a scrollé jusqu'en bas
        searchList.renderNext();
    }

    public void addPictogramme(Pictograme pictograme) {
        sequentiel.addPictograme(pictograme);
        Log.println("Added pictogram to the sequentiel");
    }
    public void setSequentiel(Sequentiel sequentiel) {
        this.sequentiel = sequentiel;
        scrollPaneSequentiel.setContent(sequentiel.getVboxSequentiel());
        scrollPaneSequentiel.setPannable(true);
    }

    public void setColor(ColorPicker idColor){
        this.idColor = idColor;
    }

    public void setDirection(ChoiceBox idDirection){
        this.idDirection = idDirection;
    }

    public void setNum(ChoiceBox idNum){
        this.idNum = idNum;
    }

    public void setText(ChoiceBox idText){
        this.idText = idText;
    }
    @FXML
    public void exportSeqToPDF(ActionEvent actionEvent) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            Printer printer = job.getPrinter();
            PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.DEFAULT);
            job.getJobSettings().setPageLayout(pageLayout);

            job.printPage(scrollPaneSequentiel);
            job.endJob();
        }
    }

    @FXML
    void changeNumPos(MouseEvent event) {
        ChoiceBox choiceBox = (ChoiceBox) event.getSource();
        String value = (String) choiceBox.getValue();
        if (value.equals("En bas")){
            sequentiel.changeDirectionOfNumbers(Direction.DOWN);
        }
        else if (value.equals("En haut")){
            sequentiel.changeDirectionOfNumbers(Direction.UP);
        }
        else if (value.equals("A droite")){
            sequentiel.changeDirectionOfNumbers(Direction.RIGHT);
        }else{
            sequentiel.changeDirectionOfNumbers(Direction.LEFT);
        }
    }
}
