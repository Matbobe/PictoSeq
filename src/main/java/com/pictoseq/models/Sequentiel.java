package com.pictoseq.models;

import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.Serializable;
import java.net.http.HttpClient;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public class Sequentiel implements Serializable {
    private final List<Pictograme> pictogrameList;
    private transient ScrollPane scrollPane;
    private boolean horizontal;
    private String name;

    public Sequentiel(String name) {
        this.pictogrameList = new LinkedList<>();
        horizontal = true;
        this.scrollPane = new ScrollPane();
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        scrollPane.setContent(hbox);
        hbox.setPadding(new javafx.geometry.Insets(300, 0, 0, 0));
        scrollPane.setPannable(true);
        this.name = name;
    }

    // Pour faire une copie d'un séquentiel
    public Sequentiel(Sequentiel sequentiel) {
           this.pictogrameList = new LinkedList<>();
            horizontal = true;
            this.scrollPane = new ScrollPane();
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER);
            scrollPane.setContent(hbox);
            hbox.setPadding(new javafx.geometry.Insets(300, 0, 0, 0));
            scrollPane.setPannable(true);
            this.name = sequentiel.getName() + " (copie)";
            for (Pictograme pictograme : sequentiel.getPictogrameList()) {
                this.addPictograme(new Pictograme(pictograme));
            }
        }
    private HBox getHbox() {
        return (HBox) scrollPane.getContent();
    }

    public Sequentiel(LinkedList<Pictograme> pictogrameList) {
        this.pictogrameList = pictogrameList;
    }

    private Pictograme[] getPictogrameList() {
        return pictogrameList.toArray(new Pictograme[0]);
    }

    public void addPictograme(Pictograme pictograme) {
        pictogrameList.add(pictograme);
        getHbox().getChildren().add(pictograme.getImageView());
        Log.println(""+ scrollPane.getScaleX());
    }

    public void clear() {
        pictogrameList.clear();
        getHbox().getChildren().clear();
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

    public ScrollPane getScrollPane() {

        return scrollPane;

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
