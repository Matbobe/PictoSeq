package com.pictoseq.models;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

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

    public Pictograme[] getPictogrameList() {
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

    public void setDirectionSequentielTitle(String directionSequentielTitle) {
        this.directionSequentielTitle = directionSequentielTitle;
    }
    public void removePictograme(Pictograme pictograme) {
        pictogrameList.remove(pictograme);
    }
    public String getDirectionPictogrameTitle() {
        return directionPictogrameTitle;
    }

    public void setDirectionPictogrameTitle(String directionPictogrameTitle) {
        this.directionPictogrameTitle = directionPictogrameTitle;
    }

    public int size() {
        return pictogrameList.size();
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

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
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

    public int indexOf(Pictograme pictograme) {
        return pictogrameList.indexOf(pictograme);
    }
    public Pictograme getPictogramme(int i) {
        return pictogrameList.get(i);
    }

    public void swapPictogrames(int index, int i) {
        Pictograme temp = pictogrameList.get(index);
        pictogrameList.set(index, pictogrameList.get(i));
        pictogrameList.set(i, temp);
    }
}
