package com.pictoseq.controllers;

import com.pictoseq.models.Log;
import com.pictoseq.models.Pictograme;
import com.pictoseq.models.SearchList;
import com.pictoseq.models.Sequentiel;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.*;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class EditController {
    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField searchBar;
    @FXML
    private TilePane searchListGrid;
    @FXML
    private ScrollPane scrollPaneSequentiel;
    @FXML
    private HBox categories;
    @FXML
    private Pane contentPane;
    @FXML
    private HBox headerBox;
    @FXML
    public ColorPicker idColor;
    @FXML
    private ChoiceBox<String> idDirection;
    @FXML
    private ChoiceBox<String> idNum;
    @FXML
    private ChoiceBox<String> idText;
    @FXML
    private ChoiceBox<String> idTitle;

    public SearchList searchList;
    private HttpClient client;
    private VBox vboxSequentiel;
    private Pane boxSequentiel;
    private Label titleSequentiel;
    private Sequentiel sequentiel;

    private double dragStartX;
    private double dragStartY;
    private double scrollPositionX;
    private double scrollPositionY;
    private Scale scaleTransform = new Scale(1.0, 1.0);
    private Translate translateTransform = new Translate(0.0, 0.0);
    private static final double ZOOM_MIN = 0.5;
    private static final double ZOOM_MAX = 10.0;

    @FXML
    void onRetourClick(ActionEvent event) {
        Stage editStage = (Stage) borderPane.getScene().getWindow();
        editStage.close();
    }

    @FXML
    void initialize() {
        client = HttpClient.newHttpClient();
        searchList = new SearchList(searchListGrid, client, this);

        // Permet le zoom et le drag de la vue du séquenciel
        contentPane = new Pane();
        scrollPaneSequentiel.setContent(contentPane);

        contentPane.getTransforms().addAll(scaleTransform, translateTransform);

        // Gestionnaire d'événements pour le drag
        contentPane.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                dragStartX = event.getX();
                dragStartY = event.getY();
                scrollPositionX = translateTransform.getX();
                scrollPositionY = translateTransform.getY();
            }
        });

        contentPane.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                double offsetX = event.getX() - dragStartX;
                double offsetY = event.getY() - dragStartY;
                translateTransform.setX(scrollPositionX + offsetX);
                translateTransform.setY(scrollPositionY + offsetY);
            }
        });

        // Gestionnaire d'événements pour le zoom
        scrollPaneSequentiel.setOnScroll(event -> {
            double zoomFactor = event.getDeltaY() > 0 ? 1.1 : 0.9;
            double newScaleX = scaleTransform.getX() * zoomFactor;
            double newScaleY = scaleTransform.getY() * zoomFactor;

            // Vérifier les limites du zoom minimal et maximal
            if (newScaleX >= ZOOM_MIN && newScaleX <= ZOOM_MAX &&
                    newScaleY >= ZOOM_MIN && newScaleY <= ZOOM_MAX) {
                scaleTransform.setX(newScaleX);
                scaleTransform.setY(newScaleY);
            }

            event.consume();
        });

        idNum.setOnAction(event -> {
            sequentiel.changeDirectionOfNumbers(idNum.getValue());
            renderBoxSequentiel();
            renderVBoxSequentiel();
            contentPane.getChildren().setAll(vboxSequentiel);
        });
        idDirection.setOnAction(event -> {
            boolean val = idDirection.getValue().equals("Horizontal");
            sequentiel.setHorizontal(val);
            renderBoxSequentiel();
            renderVBoxSequentiel();
            contentPane.getChildren().setAll(vboxSequentiel);

        });
        idColor.setOnAction(event -> {
            sequentiel.setColor(idColor.getValue());
            renderBoxSequentiel();
            renderVBoxSequentiel();
            contentPane.getChildren().setAll(vboxSequentiel);
        });
        idText.setOnAction(event -> {
            sequentiel.setDirectionPictogrameTitle(idText.getValue());
            renderBoxSequentiel();
            renderVBoxSequentiel();
            contentPane.getChildren().setAll(vboxSequentiel);
        });
        idTitle.setOnAction(event -> {
            sequentiel.setDirectionSequentielTitle(idTitle.getValue());
            renderBoxSequentiel();
            renderVBoxSequentiel();
            contentPane.getChildren().setAll(vboxSequentiel);
        });

        // recherche par catégorie
        categories.getChildren().forEach((node) -> {
            node.setOnMouseClicked(event -> {
                Button button = (Button) node;
                String category = button.getText().toLowerCase();
                searchBar.setText(category);
                handleTextSearch(null);
            });
        });
    }
    @FXML
    void handleTextSearch(KeyEvent event) {
        String text = searchBar.getText().toLowerCase().replace(" ", "+");
        String category = "all";
        // TODO: Gérer les catégories
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                searchList.searchRequest(text, category);
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            try {
                // Attendre la fin de la recherche avant d'appeler render()
                task.get(); // Attendre que la tâche soit terminée
                searchList.render();
            } catch (IOException | InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });

        task.setOnFailed(e -> {
            // Gérer les erreurs ici
        });

        new Thread(task).start();
    }

    @FXML
    void searchGridHandleScrollFiniched(ScrollEvent event) throws IOException, InterruptedException {
        if (event.getDeltaY() > 0) return; // On ne s'intéresse qu'au scroll vers le bas
        if (searchListGrid.getChildren().isEmpty()) return; // Si le TilePane est vide, on ne fait rien

        javafx.scene.Node lastChild = searchListGrid.getChildren().get(searchListGrid.getChildren().size() - 1);
        double lastChildBottom = lastChild.getBoundsInParent().getMaxY();
        double searchListTilePaneBottom = searchListGrid.getBoundsInParent().getMaxY();

        // Si le dernier enfant n'est pas en bas du TilePane, on ne fait rien
        if (lastChildBottom < searchListTilePaneBottom) return;

        // Si on arrive ici, c'est qu'on a scrollé jusqu'en bas
        searchList.renderNext();
    }

    private void loadPictogramme(Pictograme pictograme) {
        Log.println("Added pictogram to the sequentiel");
        Pane basePane = new Pane();
        Pane newPane;
        Label texte;
        Label titrePicto;
        titrePicto = new Label(pictograme.getKeyword());
        titrePicto.setFont(new Font("Arial", 10));
        VBox panePicto = new VBox();
        texte = new Label(""+sequentiel.size());
        texte.setFont(new Font("Arial", 20));
        String dir = idNum.getValue().toString();
        Image removeImage = new Image(getClass().getResourceAsStream("/images/delete-icon.png"));
        ImageView removeImageView = new ImageView(removeImage);
        removeImageView.setFitHeight(20);
        removeImageView.setFitWidth(20);
        Pane paneRemoveImageView = new Pane();
        paneRemoveImageView.setOnMouseClicked(event -> {
            sequentiel.removePictograme(pictograme);
            boxSequentiel.getChildren().remove(basePane);
            updateIndexPictogrames();
        });
        paneRemoveImageView.getChildren().add(removeImageView);

        Pane paneLeftArrow = new Pane();
        Pane paneRightArrow = new Pane();
        Label leftArrow;
        Label rightArrow;
        if (sequentiel.getHorizontal()){
            leftArrow = new Label("←");
            rightArrow = new Label("→");
        }else {
            leftArrow = new Label("↑");
            rightArrow = new Label("↓");
        }

        if (sequentiel.indexOf(pictograme) == 0) {
            leftArrow.setVisible(false);
        }
        if (sequentiel.indexOf(pictograme) == sequentiel.size()-1) {
            rightArrow.setVisible(false);
        }
        paneLeftArrow.setOnMouseClicked(event -> {
            int index = sequentiel.indexOf(pictograme);
            if (index > 0) {
                sequentiel.swapPictogrames(index,index-1);
                renderBoxSequentiel();
                renderVBoxSequentiel();
                contentPane.getChildren().setAll(vboxSequentiel);
                leftArrow.setVisible(true);
                if (index == sequentiel.size()-1) {
                    rightArrow.setVisible(false);
                } else {
                    rightArrow.setVisible(true);
                }
            } else {
                leftArrow.setVisible(false);
            }
        });

        paneRightArrow.setOnMouseClicked(event -> {
            int index = sequentiel.indexOf(pictograme);
            if (index < sequentiel.size()-1) {
                sequentiel.swapPictogrames(index,index+1);
                renderBoxSequentiel();
                renderVBoxSequentiel();
                contentPane.getChildren().setAll(vboxSequentiel);
                rightArrow.setVisible(true);
                if (index == 0) {
                    leftArrow.setVisible(false);
                } else {
                    leftArrow.setVisible(true);
                }
            } else {
                rightArrow.setVisible(false);
            }
        });
        leftArrow.setFont(new Font(20));
        rightArrow.setFont(new Font(20));
        paneLeftArrow.getChildren().add(leftArrow);
        paneRightArrow.getChildren().add(rightArrow);
        paneLeftArrow.setLayoutX(80);
        paneLeftArrow.setLayoutY(0);
        paneRightArrow.setLayoutX(100);
        paneRightArrow.setLayoutY(0);

        if (sequentiel.getDirectionPictogrameTitle().equals("En haut")){
            panePicto.getChildren().addAll(titrePicto,new Pane(pictograme.getImageView(),paneRemoveImageView, paneLeftArrow, paneRightArrow));
        }else if (sequentiel.getDirectionPictogrameTitle().equals("En bas")){
            panePicto.getChildren().addAll(new Pane(pictograme.getImageView(),paneRemoveImageView, paneLeftArrow, paneRightArrow),titrePicto);
        } else{
            panePicto.getChildren().addAll(new Pane(pictograme.getImageView(),paneRemoveImageView, paneLeftArrow, paneRightArrow));
        }
        BorderStroke borderStroke = new BorderStroke(
                idColor.getValue(), BorderStrokeStyle.SOLID, null, new BorderWidths(2));
        panePicto.setBorder(new Border(borderStroke));
        panePicto.setPrefSize(120,120);
        panePicto.setMinSize(120,120);
        panePicto.setMaxSize(120,120);
        panePicto.setAlignment(Pos.CENTER);
        if (dir.equals("En haut")) {
            newPane = new VBox();
            newPane.getChildren().addAll(texte,panePicto);
            ((VBox) newPane).setAlignment(Pos.CENTER);
        } else if (dir.equals("En bas")) {
            newPane = new VBox();
            newPane.getChildren().addAll(panePicto,texte);
            ((VBox) newPane).setAlignment(Pos.CENTER);
        } else if (dir.equals("À gauche")) {
            newPane = new HBox();
            newPane.getChildren().addAll(texte,panePicto);
            ((HBox) newPane).setAlignment(Pos.CENTER);
        } else if (dir.equals("À droite")) {
            newPane = new HBox();
            newPane.getChildren().addAll(panePicto,texte);
            ((HBox) newPane).setAlignment(Pos.CENTER);
        } else {
            newPane = new HBox();
            newPane.getChildren().addAll(panePicto);
            ((HBox) newPane).setAlignment(Pos.CENTER);
        }
        if (boxSequentiel.getChildren().size() != 0){
            Pane lastPane = (Pane) boxSequentiel.getChildren().get(boxSequentiel.getChildren().size()-1);
            if (dir.equals("En haut") || dir.equals("À gauche")) {
                lastPane = (Pane) lastPane.getChildren().get(1);
            }else {
                lastPane = (Pane) lastPane.getChildren().get(0);
            }
            Pane p;
            if (sequentiel.getDirectionPictogrameTitle().equals("En haut")){
                p = (Pane) lastPane.getChildren().get(1);
            }else{
                p = (Pane) lastPane.getChildren().get(0);
            }

            Pane lastPaneRightArrow = (Pane) p.getChildren().get(3);
            Label lastPaneRightArrowLabel = (Label) lastPaneRightArrow.getChildren().get(0);
            lastPaneRightArrowLabel.setVisible(true);
        }
        newPane.setStyle("-fx-background-color: #ffffff;");
        boxSequentiel.getChildren().add(newPane);
    }

    private void updateIndexPictogrames(){
        for (int i = 0; i < sequentiel.size(); i++) {
            String index = Integer.toString(i+1);
            String direction = idNum.getValue().toString();
            Pane pane = (Pane)boxSequentiel.getChildren().get(i);
            if (direction.equals("En haut") || direction.equals("À gauche")) {
                ((Label)pane.getChildren().get(0)).setText(index);

            } else {
                ((Label)pane.getChildren().get(1)).setText(index);
            }

        }
    }
    public void addPictogramme(Pictograme pictograme) {
        sequentiel.addPictograme(pictograme);
        loadPictogramme(pictograme);
    }
    public void setSequentiel(Sequentiel sequentiel) {
        sequentiel.render(client);
        this.sequentiel = sequentiel;
        idNum.setValue(sequentiel.getDirectionPictogrameNumber());
        idText.setValue(sequentiel.getDirectionPictogrameTitle());
        idTitle.setValue(sequentiel.getDirectionSequentielTitle());
        idColor.setValue(Color.valueOf(sequentiel.getColor()));
        idDirection.setValue(sequentiel.getDirectionBox());
        renderBoxSequentiel();
        renderVBoxSequentiel();
        contentPane.getChildren().setAll(vboxSequentiel);
        addNameViewSequenciel();

    }
    @FXML
    public void exportSeqToPDF(ActionEvent actionEvent) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            Printer printer = job.getPrinter();
            PageOrientation pageOrientation = PageOrientation.PORTRAIT;
            if (sequentiel.getHorizontal()) {
                pageOrientation = PageOrientation.LANDSCAPE;
            }
            PageLayout pageLayout = printer.createPageLayout(Paper.A4, pageOrientation, Printer.MarginType.DEFAULT);
            job.getJobSettings().setPageLayout(pageLayout);

            // Prenez un instantané de la ScrollPane
            WritableImage snapshot = contentPane.snapshot(new SnapshotParameters(), null);

            // Créez une nouvelle ImageView pour imprimer
            ImageView imageView = new ImageView(snapshot);

            // Déplacer la copie en haut à gauche
            imageView.setTranslateX(-imageView.getBoundsInParent().getMinX());
            imageView.setTranslateY(-imageView.getBoundsInParent().getMinY());

            // Imprimer la copie
            job.printPage(imageView);
            job.endJob();
        }
    }

    @FXML
    void printSeqToPDF(ActionEvent event) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            Printer printer = job.getPrinter();
            PageOrientation pageOrientation = PageOrientation.PORTRAIT;
            if (sequentiel.getHorizontal()) {
                pageOrientation = PageOrientation.LANDSCAPE;
            }
            PageLayout pageLayout = printer.createPageLayout(Paper.A4, pageOrientation, Printer.MarginType.DEFAULT);
            job.getJobSettings().setPageLayout(pageLayout);

            // Prenez un instantané de la ScrollPane
            WritableImage snapshot = contentPane.snapshot(new SnapshotParameters(), null);

            // Créez une nouvelle ImageView pour imprimer
            ImageView imageView = new ImageView(snapshot);

            // Déplacer la copie en haut à gauche
            imageView.setTranslateX(-imageView.getBoundsInParent().getMinX());
            imageView.setTranslateY(-imageView.getBoundsInParent().getMinY());

            boolean proceed = job.showPrintDialog(borderPane.getScene().getWindow());
            if (proceed) {
                // Imprimer la copie
                job.printPage(imageView);
                job.endJob();
            }
        }
    }

    private void renderBoxSequentiel() {
        if (sequentiel.getHorizontal()) {
            boxSequentiel = new HBox();
        } else {
            boxSequentiel = new VBox();
        }
        for (int i = 0; i < sequentiel.size(); i++){
            loadPictogramme(sequentiel.getPictogramme(i));
        }
        if (!idNum.getValue().equals("Désactiver")) {
            updateIndexPictogrames();
        }
    }
    private void renderVBoxSequentiel() {
        this.vboxSequentiel = new VBox();
        titleSequentiel = new Label(sequentiel.getName());
        if (sequentiel.getDirectionSequentielTitle().equals("En haut")) {

            vboxSequentiel.getChildren().addAll(titleSequentiel,boxSequentiel);
        }else{
            vboxSequentiel.getChildren().addAll(boxSequentiel,titleSequentiel);
        }
    }

    private void clearBoxSequentiel() {
        boxSequentiel.getChildren().clear();
    }

    private void addNameViewSequenciel(){
        HBox container = new HBox();
        Text nameSequentiel = new Text();
        nameSequentiel.setText(sequentiel.getName());
        nameSequentiel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: black;");
        container.getChildren().add(nameSequentiel);
        Button renameSequentiel = new Button("Renommer");
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/pencilW-icon.png")));
        renameSequentiel.setLayoutX(10);
        renameSequentiel.setLayoutY(10);
        renameSequentiel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-color:#3056D3; -fx-text-fill: White");
        renameSequentiel.setGraphic(new ImageView(image));
        renameSequentiel.setOnAction(event -> {
            headerBox.getChildren().remove(0);
            HBox container1 = new HBox();
            headerBox.getChildren().add(container1);
            TextField textField = new TextField(nameSequentiel.getText());
            container1.getChildren().add(textField);
            container1.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            container1.setSpacing(10);
            textField.requestFocus();
            textField.end();
            textField.setStyle("-fx-border-width: 0; -fx-border-color: transparent; -fx-background-color: transparent;-fx-font-weight: bold; -fx-text-fill: black; -fx-font-size: 24px;");
            Button Confirme = new Button("Valider");
            Confirme.setLayoutX(10);
            Confirme.setLayoutY(10);
            Confirme.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-color:#3056D3; -fx-text-fill: White");
            Confirme.setOnAction(event1 -> {
                sequentiel.setName(textField.getText());
                titleSequentiel.setText(textField.getText());
                headerBox.getChildren().clear();
                addNameViewSequenciel();
            });
            Button Cancel = new Button("Annuler");
            Cancel.setLayoutX(10);
            Cancel.setLayoutY(10);
            Cancel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-color:White; -fx-text-fill: #3056D3");
            Cancel.setOnAction(event1 -> {
                headerBox.getChildren().clear();
                addNameViewSequenciel();
            });
            container1.getChildren().addAll(Confirme, Cancel);
        });
        container.getChildren().add(renameSequentiel);
        container.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        container.setSpacing(10);
        headerBox.getChildren().add(container);
    }

}
