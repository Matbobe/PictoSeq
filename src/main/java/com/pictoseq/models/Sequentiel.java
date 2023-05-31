package com.pictoseq.models;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.LinkedList;
import java.util.List;

public class Sequentiel {
    private final List<Pictograme> pictogrameList;
    private Pane pane;
    private boolean horizontal;
    public Sequentiel(BorderPane borderPane) {
        this.pictogrameList = new LinkedList<>();
        horizontal = true;
        this.pane = new HBox();
        borderPane.setCenter(this.pane);

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
}
