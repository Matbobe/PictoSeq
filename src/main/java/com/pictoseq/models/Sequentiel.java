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
    private transient VBox vboxSequentiel;
    private transient Pane boxSequentiel;
    private transient Label labelTitle;
    private Direction directionNumber;
    private Direction directionTitle;
    private boolean horizontal;
    private String name;
    private Text textPictogramme;
    private Text textNum;

    public Sequentiel(String name) {
        this.pictogrameList = new LinkedList<>();
        this.directionNumber = Direction.LEFT;
        this.directionTitle = Direction.UP;
        horizontal = true;
        this.name = name;
        this.vboxSequentiel = new VBox();
        this.boxSequentiel = new HBox();
        vboxSequentiel.getChildren().add(new Label(name));
        vboxSequentiel.getChildren().add(boxSequentiel);
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
        this.vboxSequentiel = new VBox();
        this.boxSequentiel = new HBox();
        vboxSequentiel.getChildren().add(new Label(name));
        vboxSequentiel.getChildren().add(boxSequentiel);
    }
    private Pane getBoxSequentiel() {
        return boxSequentiel;
    }

    private void voidChangeDirectionOfBox(Direction direction) {
        if (direction == Direction.UP || direction == Direction.DOWN) {
            horizontal = false;
            VBox vbox = new VBox();
            vbox.getChildren().addAll(boxSequentiel.getChildren());
            boxSequentiel = vbox;
        } else {
            horizontal = true;
            HBox hbox = new HBox();
            hbox.getChildren().addAll(boxSequentiel.getChildren());
            boxSequentiel = hbox;
        }
    }

    public void changeDirectionOfNumbers(Direction direction) {
        directionNumber = direction;
        List<Pictograme> temp = new LinkedList<>(this.pictogrameList);
        clear();
        for (Pictograme pictograme : temp) {
            addPictograme(pictograme);
        }
    }
    public Sequentiel(LinkedList<Pictograme> pictogrameList) {
        this.pictogrameList = pictogrameList;
    }

    private Pictograme[] getPictogrameList() {
        return pictogrameList.toArray(new Pictograme[0]);
    }

    private Pane getBoxIndex(int index) {
        return (Pane) boxSequentiel.getChildren().get(index);
    }
    public void addPictograme(Pictograme pictograme) {
        pictogrameList.add(pictograme);
        Pane newPane;
        if (directionNumber == Direction.UP){
            newPane = new VBox();
            newPane.getChildren().addAll(new Label(pictograme.get_id()),pictograme.getImageView());
        } else if (directionNumber == Direction.DOWN) {
            newPane = new VBox();
            newPane.getChildren().addAll(pictograme.getImageView(),new Label(pictograme.get_id()));
        } else if (directionNumber == Direction.LEFT) {
            newPane = new HBox();
            newPane.getChildren().addAll(new Label(pictograme.get_id()),pictograme.getImageView());
        } else {
            newPane = new HBox();
            newPane.getChildren().addAll(pictograme.getImageView(),new Label(pictograme.get_id()));
        }
        boxSequentiel.getChildren().add(newPane);
    }

    public void clear() {
        pictogrameList.clear();
        getBoxSequentiel().getChildren().clear();
    }

    public Direction getDirectionNumber() {
        return directionNumber;
    }

    public Direction getDirectionTitle() {
        return directionTitle;
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

    public VBox getVboxSequentiel() {
        return vboxSequentiel;
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

    public void setDirection(ChoiceBox idDirection){
        if(idDirection.equals("Horizontal")){
            horizontal = true;
        }
        else{horizontal = false;}
    }

    public void setHorizontal(boolean horizontal) {
        Rotate rotate = new Rotate();
        rotate.setAngle(rotate.getAngle() + 90);
    }

    public void setColor(Color color){
        ColorPicker idColor = EditController.idColor;
        color = idColor.getValue();
    }

    public void setText(Text text){
        if(text.equals("Désactiver")){
            textPictogramme.setDisable(true);
        } else if (text.equals("En haut")) {
            textPictogramme.setY(this.size() - (this.size()/10));
        }
        else{textPictogramme.setY(this.size()/10);}
    }

    public void setNum(Text num){
        if(num.equals("Désactiver")){
            textNum.setDisable(true);
        } else if (num.equals("En haut")) {
            textNum.setY(this.size() - (this.size()/10));
        }
        else{textNum.setY(this.size()/10);}
    }
}
