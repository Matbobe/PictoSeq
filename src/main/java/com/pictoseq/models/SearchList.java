package com.pictoseq.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pictoseq.controllers.EditController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class SearchList {
    private final static int MAX_SIZE = 20;
    private List<Pictograme> pictogrameList;
    @FXML
    private TilePane searchListGrid;
    private final HttpClient client;

    private final EditController editController;
    private int nbRendered;
    public SearchList(TilePane searchListGrid, HttpClient client, EditController editController){
        this.searchListGrid = searchListGrid;
        this.pictogrameList = new ArrayList<>(MAX_SIZE);
        this.client = client;
        this.editController = editController;
    }

    public void searchRequest(String text, String category) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.arasaac.org/api/pictograms/fr/search/" + text))
                .GET()
                .setHeader("accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        if (response.statusCode() == 200) {
            this.pictogrameList = mapper.readValue(response.body(), new TypeReference<>() {});
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Pictograme pictograme : pictogrameList) {
            result.append(pictograme.toString()).append("\n");
        }
        return result.toString();
    }

    public void render() throws IOException, InterruptedException {
        this.nbRendered = 0;
        searchListGrid.getChildren().clear();
        if (pictogrameList == null) return;
        for (int i = 0; i < 20; i++) {
            if (i >= this.pictogrameList.size()) return;
            Pictograme pictograme = pictogrameList.get(i);
            addPictoToGrid(pictograme, i);
        }
    }

    public void renderNext() {
        int imgRendered = this.nbRendered;
        if (this.nbRendered >= this.pictogrameList.size()) return;
        int imgToRender = this.nbRendered + 4;
        if (imgToRender > this.pictogrameList.size()) imgToRender = this.pictogrameList.size();
        for (int i = imgRendered; i < imgToRender; i++) {
            if (i >= this.pictogrameList.size()) return;
            Pictograme pictograme = pictogrameList.get(i);
            addPictoToGrid(pictograme, i);
        }
    }

    private void addPictoToGrid(Pictograme pictograme, int index) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws IOException, InterruptedException {
                HttpResponse<InputStream> response = pictograme.render(client);
                if (response.statusCode() == 200) {
                    // Mettre a jour sur le thread de l'application JavaFX
                    Platform.runLater(() -> {
                        Pane pane = new Pane();
                        pane.setPrefSize(100, 100);
                        Image image = new Image(getClass().getResource("/images/plus-icon.png").toExternalForm());
                        ImageView plus = new ImageView(image);
                        plus.setFitHeight(20);
                        plus.setFitWidth(20);
                        plus.setLayoutX(40);
                        plus.setLayoutY(40);
                        plus.setVisible(false);
                        pane.getChildren().addAll(pictograme.getImageView(),plus);
                        searchListGrid.getChildren().add(pane);
                        pane.setOnMouseClicked(e -> {
                            editController.addPictogramme(new Pictograme(pictograme));
                        });
                        pane.setOnMouseEntered(e -> {
                            plus.setVisible(true);
                            pane.setStyle("-fx-background-color: #f0f0f0");
                        });
                        pane.setOnMouseExited(e -> {
                            plus.setVisible(false);
                            pane.setStyle("-fx-background-color: #ffffff");
                        });

                        setDragAndDropHandlers(pictograme, pane);


                        nbRendered++;
                    });
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private void setDragAndDropHandlers(Pictograme pictograme, Pane pane) {
        ImageView imageView = (ImageView) pictograme.getImageView();
        pane.setOnDragDetected(event -> {
            /* Début du glisser */
            Dragboard dragboard = pane.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.putImage(imageView.getImage());
            dragboard.setContent(content);
            event.consume();
        });

        pane.setOnDragDone(event -> {
            /* Fin du glisser */
            editController.addPictogramme(new Pictograme(pictograme));
            event.consume();
        });
    }

}

