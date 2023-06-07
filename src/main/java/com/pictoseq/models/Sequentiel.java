package com.pictoseq.models;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import com.pictoseq.controllers.EditController;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

import java.io.Serializable;
import java.net.http.HttpClient;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public class Sequentiel implements Serializable {
    private final List<Pictograme> pictogrameList;
    private transient Label labelTitle;
    private String directionNumber;
    private String directionTitle;
    private boolean horizontal;
    private String name;
    private Text textPictogramme;
    private Text textNum;

    public Sequentiel(String name) {
        this.pictogrameList = new LinkedList<>();
        this.directionNumber = "En bas";
        this.directionTitle = "En haut";
        horizontal = true;
        this.name = name;
    }

    // Pour faire une copie d'un séquentiel
    public Sequentiel(Sequentiel sequentiel) {
        this.pictogrameList = new LinkedList<>();
        this.horizontal = sequentiel.getHorizontal();
        this.directionNumber = sequentiel.getDirectionNumber();
        this.directionTitle = sequentiel.getDirectionTitle();
        this.name = sequentiel.getName() + " (copie)";
        for (Pictograme pictograme : sequentiel.getPictogrameList()) {
            this.addPictograme(new Pictograme(pictograme));
        }
    }
    public void changeDirectionOfNumbers(String direction) {
        directionNumber = direction;
    }
    public Sequentiel(LinkedList<Pictograme> pictogrameList) {
        this.pictogrameList = pictogrameList;
    }

    private Pictograme[] getPictogrameList() {
        return pictogrameList.toArray(new Pictograme[0]);
    }

    public void addPictograme(Pictograme pictograme) {
        pictogrameList.add(pictograme);
    }

    public void clear() {
        pictogrameList.clear();
    }

    public String getDirectionNumber() {
        return directionNumber;
    }

    public String getDirectionTitle() {
        return directionTitle;
    }

    public int size() {
        return pictogrameList.size();
    }

    public void render(HttpClient client) {
        for (Pictograme pictograme : pictogrameList){
            pictograme.render(client);
        }
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

    public boolean getHorizontal() {
        return horizontal;
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
    public Pictograme getPictogramme(int i) {
        return pictogrameList.get(i);
    }

}
