package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import javafx.scene.image.ImageView ;
import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

import javafx.scene.image.Image ;


public class Controller implements Initializable{


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
    ObservableList<String> list= FXCollections.observableArrayList("Filter1","Filter2","Filter3","Filter4");
    private String firstPath;
    private String secondPath;
    public void Button1Action(ActionEvent event){
       // Image imageToImplement;
        System.out.println("chooser opened");
        FileChooser fc=new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image files","*.jpg","*.jpeg"));

        File selectedFile=fc.showOpenDialog(null);
        if(selectedFile!=null){
            try {
                firstPath=selectedFile.getAbsolutePath();

                firstimagepath.setText(firstPath);
                //imageToImplement=new Image(Controller.class.getResourceAsStream(firstPath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("NO FILE SELECTED");
            alert.setContentText("Once you open the file chooser , you better choose one xD");

            alert.showAndWait();
        }
    }

    public void Button2Action(ActionEvent event){
        System.out.println("chooser opened");
        FileChooser fc=new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image files","*.jpg","*.jpeg"));

        File selectedFile=fc.showOpenDialog(null);
        if(selectedFile!=null){
            try {
                secondPath=selectedFile.getPath();
                secondimagepath.setText(secondPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("NO FILE SELECTED");
            alert.setContentText("Once you open the file chooser , you better choose one xD");

            alert.showAndWait();
        }
    }

    public void SaveAction(ActionEvent event){
        System.out.println("Saving began");
        Image image = new Image("file:///"+firstPath);
        ImageView iv=new ImageView(image);
        iv.setFitHeight(200);
        iv.setFitWidth(200);
        firstimagepreview.getChildren().addAll(iv);
        Image image1=new Image("file:///"+secondPath);
        ImageView iv1=new ImageView(image1);
        iv1.setFitWidth(200);
        iv1.setFitHeight(200);
        secondimagepreview.getChildren().addAll(iv1);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
                filtercombo.setItems(list);
    }
}
