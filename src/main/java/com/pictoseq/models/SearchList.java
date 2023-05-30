package com.pictoseq.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

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
    private GridPane searchListGrid;
    private final HttpClient client;
    private int nbRendered;
    public SearchList(GridPane searchListGrid, HttpClient client) {
        this.searchListGrid = searchListGrid;
        this.pictogrameList = new ArrayList<>(MAX_SIZE);
        this.client = client;
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
        for (int i = imgRendered; i < imgRendered + 20; i++) {
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
                    javafx.scene.image.Image image = new javafx.scene.image.Image(response.body());
                    javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(image);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    imageView.setOnMouseClicked(e -> {
                        System.out.println(pictograme);
                    });
                    // Mettre a jour sur le thread de l'application JavaFX
                    Platform.runLater(() -> {
                        searchListGrid.add(imageView, index % 2, index / 2);
                        nbRendered++;
                    });
                }
                return null;
            }
        };
        new Thread(task).start();
    }
}
