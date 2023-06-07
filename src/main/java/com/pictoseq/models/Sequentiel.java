package com.pictoseq.models;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.Serializable;
import java.net.http.HttpClient;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public class Sequentiel implements Serializable {
    private final List<Pictograme> pictogrameList;
    private String directionPictogrameNumber;
    private String directionSequentielTitle;
    private String directionPictogrameTitle;
    private String color;
    private boolean horizontal;
    private String name;

    public Sequentiel(String name) {
        this.pictogrameList = new LinkedList<>();
        this.directionPictogrameNumber = "En bas";
        this.directionSequentielTitle = "En haut";
        this.directionPictogrameTitle = "En haut";
        this.color = Color.GRAY.toString();
        this.horizontal = true;
        this.name = name;
    }

    // Pour faire une copie d'un séquentiel
    public Sequentiel(Sequentiel sequentiel) {
        this.pictogrameList = new LinkedList<>();
        this.horizontal = sequentiel.getHorizontal();
        this.directionPictogrameNumber = sequentiel.getDirectionPictogrameNumber();
        this.directionSequentielTitle = sequentiel.getDirectionSequentielTitle();
        this.directionPictogrameTitle = sequentiel.getDirectionPictogrameTitle();
        this.color = sequentiel.getColor();
        this.name = sequentiel.getName() + " (copie)";
        for (Pictograme pictograme : sequentiel.getPictogrameList()) {
            this.addPictograme(new Pictograme(pictograme));
        }
    }
    public void changeDirectionOfNumbers(String direction) {
        directionPictogrameNumber = direction;
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

    public String getDirectionPictogrameNumber() {
        return directionPictogrameNumber;
    }

    public String getDirectionSequentielTitle() {
        return directionSequentielTitle;
    }

    public String getDirectionPictogrameTitle() {
        return directionPictogrameTitle;
    }

    public int size() {
        return pictogrameList.size();
    }

    public void setDirectionPictogrameNumber(String directionPictogrameNumber) {
        this.directionPictogrameNumber = directionPictogrameNumber;
    }

    public String getDirectionBox() {
        if (horizontal) {
            return "Horizontal";
        } else {
            return "Vertical";
        }
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

    public void setColor(Color color) {
        this.color = color.toString();
    }

    public String getColor() {
        return this.color;
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
