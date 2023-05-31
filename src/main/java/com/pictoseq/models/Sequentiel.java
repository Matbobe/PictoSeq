package com.pictoseq.models;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public class Sequentiel implements Serializable {
    private final List<Pictograme> pictogrameList;
    private transient Pane pane;
    private boolean horizontal;
    private String name;

    public Sequentiel(BorderPane borderPane, String name) {
        this.pictogrameList = new LinkedList<>();
        horizontal = true;
        this.pane = new HBox();
        borderPane.setCenter(this.pane);
        this.name = name;
    }

    public Sequentiel(String name) {
        this.pictogrameList = new LinkedList<>();
        horizontal = true;
        this.pane = new HBox();
        this.name = name;
    }

    public Sequentiel(LinkedList<Pictograme> pictogrameList) {
        this.pictogrameList = pictogrameList;
    }

    public List<Pictograme> getPictogrameList() {
        return pictogrameList;
    }

    public void addPictograme(Pictograme pictograme) {
        pictogrameList.add(pictograme);
        pane.getChildren().add(pictograme.getImageView());
    }

    public void removePictograme(Pictograme pictograme) {
        pictogrameList.remove(pictograme);
    }

    public void removePictograme(int index) {
        pictogrameList.remove(index);
    }

    public void clear() {
        pictogrameList.clear();
    }

    public int size() {
        return pictogrameList.size();
    }

    public Pictograme getPictograme(int index) {
        return pictogrameList.get(index);
    }

    @Override
    public String toString() {
        String res = "";
        for (Pictograme pictograme : pictogrameList) {
            res += pictograme;
        }
        return res;
    }

    public Pane getPane() {
        return pane;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
