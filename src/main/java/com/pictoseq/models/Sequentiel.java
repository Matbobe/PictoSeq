package com.pictoseq.models;

import com.pictoseq.controllers.EditController;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
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
    private transient ScrollPane scrollPane;
    private boolean horizontal;
    private String name;
    private Text textPictogramme;
    private Text textNum;

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
