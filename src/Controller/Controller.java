package Controller;

import Magic.ResizeHeightTranslation;
import Model.MatrixGenerator;
import Model.MatrixPoint3D;
import View.View3D;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

import javafx.scene.image.Image;
import javafx.util.Duration;


public class Controller implements Initializable {


    public Label firstimageid;
    public Label secondimageid;
    public View3D resultspane;
    public SplitPane root;
    @FXML
    private Label firstimagepath;
    @FXML
    private Label secondimagepath;
    @FXML
    private Button firstimagebrowse;
    @FXML
    private Button secondimagebrowse;
    @FXML
    private Button savebutton;
    @FXML
    private Pane firstimagepreview;
    @FXML
    private Pane secondimagepreview;
    @FXML
    private ComboBox<String> filtercombo;
    @FXML
    private Button morebutton;
    @FXML
    private Pane transitionpane;
    private ObservableList<String> list = FXCollections.observableArrayList("Filter1", "Filter2", "Filter3", "Filter4");
    private String firstPath = "D:\\yoyo6\\Documents\\UTBM\\Stereovision\\examples\\MILITARY_LEFT.jpg"; // TODO remove init test path
    private String secondPath = "D:\\yoyo6\\Documents\\UTBM\\Stereovision\\examples\\MILITARY_RIGHT.jpg"; // TODO remove init test path

    public void Button1Action(ActionEvent event) {
        // Image imageToImplement;
        System.out.println("chooser opened");
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.jpeg"));

        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                firstPath = selectedFile.getAbsolutePath();

                firstimagepath.setText(firstPath);
                //imageToImplement=new Image(Controller.class.getResourceAsStream(firstPath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("NO FILE SELECTED");
            alert.setContentText("Once you open the file chooser , you better choose one xD");

            alert.showAndWait();
        }
    }

    public void Button2Action(ActionEvent event) {
        System.out.println("chooser opened");
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.jpeg"));

        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                secondPath = selectedFile.getPath();
                secondimagepath.setText(secondPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("NO FILE SELECTED");
            alert.setContentText("Once you open the file chooser , you better choose one xD");

            alert.showAndWait();
        }
    }

    public void MoreAction(ActionEvent event) {
        HBox hbox = new HBox();
        Pane spacer = new Pane();

        GridPane more = new GridPane();
        more.setMaxHeight(300);
        more.setMinHeight(100);
        Button btn = new Button("whatever");
        Label x = new Label("X :");
        Label y = new Label("Y :");
        Label z = new Label("Z :");
        TextField xf = new TextField("0");
        xf.setMaxWidth(30);
        TextField yf = new TextField("0");
        yf.setMaxWidth(30);

        TextField zf = new TextField("0");
        zf.setMaxWidth(30);

        HBox.setHgrow(spacer, Priority.ALWAYS);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(3);
        hbox.getChildren().addAll(x, xf, y, yf, z, zf, btn);

        more.getChildren().addAll(hbox);
        //more.setStyle("-fx-background-color: #000000");

        ResizeHeightTranslation rht = new ResizeHeightTranslation(Duration.millis(1000), transitionpane, more.getMinHeight());

        FadeTransition ft = new FadeTransition(Duration.millis(1000), more);
        ft.setFromValue(0);
        ft.setToValue(1);

        SequentialTransition pt = new SequentialTransition(rht, ft);

        pt.play();
        more.setAlignment(Pos.CENTER);
        transitionpane.getChildren().add(more);
    }

    public void SaveAction(ActionEvent event) {
        savebutton.setDisable(true);
        System.out.println("Saving began");
        Image image1 = new Image("file:///" + firstPath);
        ImageView iv = new ImageView(image1);
        iv.setFitHeight(200);
        iv.setFitWidth(200);
        firstimagepreview.getChildren().addAll(iv);
        Image image2 = new Image("file:///" + secondPath);
        ImageView iv1 = new ImageView(image2);
        iv1.setFitWidth(200);
        iv1.setFitHeight(200);
        secondimagepreview.getChildren().addAll(iv1);


        new Thread(() -> {
            MatrixGenerator matrixGenerator = new MatrixGenerator();
            if (!matrixGenerator.setInputPictures(SwingFXUtils.fromFXImage(image1, null), SwingFXUtils.fromFXImage(image2, null)))
                return;

            double minX = Double.MAX_VALUE,minY = Double.MAX_VALUE,maxX = Double.MIN_VALUE,maxY = Double.MIN_VALUE;
            for (MatrixPoint3D point : matrixGenerator.computeMatrix()) {
                if(point.getX()<minX) minX = point.getX();
                if(point.getX()>maxX) maxX = point.getX();
                if(point.getY()<minY) minY = point.getY();
                if(point.getY()>maxY) maxY = point.getY();
                Platform.runLater(() -> {
                    resultspane.getChildren().add(point.view);
                    System.out.println("Added point (" + point.getX() + "," + point.getY() + "," + point.getZ() + ")");
                });
            }
            resultspane.setPrefSize(maxX-minX,maxY-minY);
            savebutton.setDisable(true);
            System.out.println("Process finished!");
        }).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filtercombo.setItems(list);

        root.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:    case Z: resultspane.moving[0] = true; break;
                case RIGHT: case D: resultspane.moving[1] = true; break;
                case DOWN:  case S: resultspane.moving[2] = true; break;
                case LEFT:  case Q: resultspane.moving[3] = true; break;
            }
        });
        root.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case UP:    case Z: resultspane.moving[0] = false; break;
                case RIGHT: case D: resultspane.moving[1] = false; break;
                case DOWN:  case S: resultspane.moving[2] = false; break;
                case LEFT:  case Q: resultspane.moving[3] = false; break;
            }
        });
    }
}
