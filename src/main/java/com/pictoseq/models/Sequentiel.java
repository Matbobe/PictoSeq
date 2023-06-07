package com.pictoseq.models;

import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.Serializable;
import java.net.http.HttpClient;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public class Sequentiel implements Serializable {
    private final List<Pictograme> pictogrameList;
    private transient Pane pane;
    private boolean horizontal;
    private String name;

    public Sequentiel(String name) {
        this.pictogrameList = new LinkedList<>();
        horizontal = true;
        this.pane = new HBox();
        this.name = name;
    }

    public void addPictograme(Pictograme pictograme) {
        pictogrameList.add(pictograme);
        pane.getChildren().add(pictograme.getImageView());
    }

    public void clear() {
        pictogrameList.clear();
    }

    public int size() {
        return pictogrameList.size();
    }

    @Override
    public String toString() {
        String res = "";
        for (Pictograme pictograme : pictogrameList) {
            res += pictograme;
        }
        return res;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public ImageView[] getTreePictogrameImageView(int nb) {
        HttpClient client = HttpClient.newHttpClient();

        ImageView[] imageViews = new ImageView[pictogrameList.size()];
        for (int i = 0; i < nb; i++) {
            pictogrameList.get(i).render(client);
            imageViews[i] = pictogrameList.get(i).getImageViewCopy();
        }
        return imageViews;
    }
}
