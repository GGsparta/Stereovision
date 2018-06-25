package Controller;

import Magic.ResizeHeightTranslation;
import Model.Matrix;
import Model.MatrixGenerator;
import Model.MatrixPoint3D;
import View.View3D;
import delaunay_triangulation.Delaunay_Triangulation;
import delaunay_triangulation.Point_dt;
import delaunay_triangulation.Triangle_dt;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableIntegerArray;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import javafx.scene.image.ImageView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;


public class Controller implements Initializable {


    public Label firstimageid;
    public Label secondimageid;
    public View3D resultspane;
    public SplitPane root;
    public VBox loadingpane;
    public ImageView load_gif;
    public Label load_text;
    public Pane displaymodepane;
    public Pane editmodepane;
    public ImageView eye_black;
    public ImageView eye_white;
    public ImageView grid_white;
    public ImageView grid_black;
    public ImageView arrow_up;
    public ImageView arrow_down;
    @FXML
    WebView webView;
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
    private TextField xf;
    private TextField yf;
    private TextField zf;
    private Matrix matrix;
    private TriangleMesh image3D;
    private Image image1;
    private BooleanProperty editMode = new SimpleBooleanProperty(false);
    private MeshView img3dView;
    private boolean editMode;
    @FXML
    public void onLoadHelpFile(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/help_page.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();

        stage.setTitle("Aide");
        stage.setScene(new Scene(root1));
        stage.show();

    }
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
            alert.setHeaderText("Aucun fichier seléctionné");
            alert.setContentText("Veuillez choisir une image.");

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

    public void InitEditMode() {
        Label x = new Label("X :");
        Label y = new Label("Y :");
        Label z = new Label("Z :");
        x.setMinWidth(22);
        y.setMinWidth(22);
        z.setMinWidth(22);

        xf = new TextField("0");
        xf.setPrefWidth(1000);
        xf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (matrix != null && matrix.currentPointOnEdit != null) {
                matrix.currentPointOnEdit.setPosition(
                        Double.parseDouble(newValue),
                        matrix.currentPointOnEdit.getY(),
                        matrix.currentPointOnEdit.getZ()
                );
                refresh3DPictureSurface();
            }
        });
        yf = new TextField("0");
        yf.setPrefWidth(1000);
        yf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (matrix != null && matrix.currentPointOnEdit != null) {
                matrix.currentPointOnEdit.setPosition(
                        matrix.currentPointOnEdit.getX(),
                        Double.parseDouble(newValue),
                        matrix.currentPointOnEdit.getZ()
                );
                refresh3DPictureSurface();
            }
        });
        zf = new TextField("0");
        zf.setPrefWidth(1000);
        zf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (matrix != null && matrix.currentPointOnEdit != null) {
                matrix.currentPointOnEdit.setPosition(
                        matrix.currentPointOnEdit.getX(),
                        matrix.currentPointOnEdit.getY(),
                        Double.parseDouble(newValue)
                );
                refresh3DPictureSurface();
            }
        });

        xf.setDisable(true);
        yf.setDisable(true);
        zf.setDisable(true);


        HBox xb = new HBox(10,x,xf);
        HBox yb = new HBox(10,y,yf);
        HBox zb = new HBox(10,z,zf);

        //more.setStyle("-fx-background-color: #000000");

        /*ResizeHeightTranslation rht = new ResizeHeightTranslation(Duration.millis(1000), transitionpane, more.getMinHeight());

        FadeTransition ft = new FadeTransition(Duration.millis(1000), more);
        ft.setFromValue(0);
        ft.setToValue(1);

        SequentialTransition pt = new SequentialTransition(rht, ft);

        pt.play();*/
        transitionpane.getChildren().addAll(xb,yb,zb);


    }

    public void SaveAction(ActionEvent event) {
        savebutton.setDisable(true);
        System.out.println("Saving began");
        image1 = new Image("file:///" + firstPath);
        ImageView iv1 = new ImageView(image1);
        iv1.setPreserveRatio(true);
        if (image1.getHeight() > image1.getWidth()) iv1.setFitHeight(200);
        else iv1.setFitWidth(200);
        firstimagepreview.getChildren().clear();
        firstimagepreview.getChildren().addAll(iv1);

        Image image2 = new Image("file:///" + secondPath);
        ImageView iv2 = new ImageView(image2);
        iv2.setPreserveRatio(true);
        if (image2.getHeight() > image2.getWidth()) iv2.setFitHeight(200);
        else iv2.setFitWidth(200);
        secondimagepreview.getChildren().clear();
        secondimagepreview.getChildren().addAll(iv2);
        loadingpane.setVisible(true);

        try {
            new Thread(() -> {
                MatrixGenerator matrixGenerator = new MatrixGenerator();
                if (!matrixGenerator.setInputPictures(SwingFXUtils.fromFXImage(image1, null), SwingFXUtils.fromFXImage(image2, null)))
                    return;


                matrix = matrixGenerator.computeMatrix(load_text.textProperty());


                System.out.println("Creating surface...");
                image3D = new TriangleMesh();

                refresh3DPictureSurface();

                savebutton.setDisable(false);
                System.out.println("Process finished!");
                loadingpane.setVisible(false);
            }).start();
        } catch (NullPointerException ex) {
            System.out.println(ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText("Images non trouvé");
            alert.setContentText("aucune image détéctée");

            alert.showAndWait();

        } catch (Exception e) {
            System.out.println(e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText("Erreur produite");
            alert.setContentText("erreur lors de l'éxécution du programme.");

            alert.showAndWait();
        }
    }

    private void refresh3DPictureSurface() {
        Platform.runLater(() -> resultspane.getChildren().clear());
        double minX = Double.MAX_VALUE,minY = Double.MAX_VALUE,maxX = Double.MIN_VALUE,maxY = Double.MIN_VALUE;
        HashMap<Point_dt,MatrixPoint3D> points_dts = new HashMap<>();
        for (MatrixPoint3D point : matrix) {
            if(point.getX()<minX) minX = point.getX();
            if(point.getX()>maxX) maxX = point.getX();
            if(point.getY()<minY) minY = point.getY();
            if(point.getY()>maxY) maxY = point.getY();

            Platform.runLater(() -> {
                resultspane.getChildren().add(point.view);
                if(!editMode.get()) point.view.setVisible(false);
                point.view.setOnMouseClicked(event1 -> {
                    xf.setText(point.getX()+"");
                    xf.setDisable(false);
                    yf.setText(point.getY()+"");
                    yf.setDisable(false);
                    zf.setText(point.getZ()+"");
                    zf.setDisable(false);
                    matrix.currentPointOnEdit = point;
                    point.setSelected(true);
                });
            });
            points_dts.put(new Point_dt(point.getX(),point.getY()),point);

        }
        Delaunay_Triangulation d = new Delaunay_Triangulation(points_dts.keySet().toArray(new Point_dt[0]));


        HashMap<MatrixPoint3D, Integer> point_indexes = new HashMap<>();

        float[] textures = new float[matrix.size()*2],
                points = new float[matrix.size()*3];
        var faces = FXCollections.observableIntegerArray();

        for (int i = 0; i < matrix.size(); i++) {
            point_indexes.put(matrix.get(i), i);
            textures[2*i] =
                    (float) ((matrix.get(i).getX() - minX) / (maxX - minX));
            textures[2*i+1] =
                    (float) ((matrix.get(i).getY() - minY) / (maxY - minY));
            points[3*i] = (float) matrix.get(i).getX();
            points[3*i+1] = (float) matrix.get(i).getY();
            points[3*i+2] = (float) matrix.get(i).getZ();
        }
        var it = d.trianglesIterator();
        while(it.hasNext()) {
            Triangle_dt t = it.next();
            if (t.p3() == null) continue;
            MatrixPoint3D
                    pt1 = points_dts.get(t.p1()),
                    pt2 = points_dts.get(t.p2()),
                    pt3 = points_dts.get(t.p3());
            faces.addAll(
                    point_indexes.get(pt3),
                    point_indexes.get(pt3),
                    point_indexes.get(pt2),
                    point_indexes.get(pt2),
                    point_indexes.get(pt1),
                    point_indexes.get(pt1)
            );
        }

        image3D.getTexCoords().addAll(textures);
        image3D.getPoints().addAll(points);
        image3D.getFaces().addAll(faces);

        Platform.runLater(() -> {
            img3dView = new MeshView(image3D);
            img3dView.setMaterial(new PhongMaterial(Color.WHITE,image1,null,null,null));
            resultspane.getChildren().add(0, img3dView);
            if(editMode.get()) img3dView.setVisible(false);

            img3dView.setScaleX(MatrixPoint3D.ratio.getX());
            img3dView.setScaleY(MatrixPoint3D.ratio.getY());
            img3dView.setTranslateY(img3dView.boundsInParentProperty().get().getHeight()/2);
            img3dView.setTranslateX(img3dView.boundsInParentProperty().get().getWidth()/2);
        });

        resultspane.setPrefSize(maxX-minX,maxY-minY);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filtercombo.setItems(list);

        root.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                case Z:
                    resultspane.moving[0] = true;
                    break;
                case RIGHT:
                case D:
                    resultspane.moving[1] = true;
                    break;
                case DOWN:
                case S:
                    resultspane.moving[2] = true;
                    break;
                case LEFT:
                case Q:
                    resultspane.moving[3] = true;
                    break;
            }
        });
        root.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case UP:
                case Z:
                    resultspane.moving[0] = false;
                    break;
                case RIGHT:
                case D:
                    resultspane.moving[1] = false;
                    break;
                case DOWN:
                case S:
                    resultspane.moving[2] = false;
                    break;
                case LEFT:
                case Q:
                    resultspane.moving[3] = false;
                    break;
            }
        });

        load_gif.setImage(new Image("Assets/Images/loader.gif"));
        eye_black.setImage(new Image("Assets/Images/eye_black.png"));
        eye_white.setImage(new Image("Assets/Images/eye_white.png"));
        grid_black.setImage(new Image("Assets/Images/grid_black.png"));
        grid_white.setImage(new Image("Assets/Images/grid_white.png"));
        arrow_up.setImage(new Image("Assets/Images/arrow.png"));
        arrow_up.setScaleY(-1);
        arrow_down.setImage(new Image("Assets/Images/arrow.png"));

        root.getStylesheets().add("View/style.css");
        for (Node node : root.lookupAll(".button, .combo-box"))
            node.focusedProperty().addListener((observable, oldValue, newValue) -> root.requestFocus());


        ((Pane) firstimagepreview.getParent().getParent()).widthProperty().addListener((observable, oldValue, newValue) -> {
            firstimagepreview.setMinWidth(newValue.doubleValue() / 2);
            secondimagepreview.setMinWidth(newValue.doubleValue() / 2);
            ((StackPane) resultspane.getParent()).setMinWidth(newValue.doubleValue());
        });
        ((Pane) resultspane.getParent().getParent().getParent()).heightProperty().addListener((observable, oldValue, newValue) -> {
            ((StackPane) resultspane.getParent()).setMinHeight(newValue.doubleValue() - 200);
        });

        InitEditMode();
    }

    public void SwitchMode(MouseEvent mouseEvent) {
        editMode.setValue(!editMode.get());
        editmodepane.setVisible(editMode.get());
        displaymodepane.setVisible(!editMode.get());

        resultspane.getChildren().forEach(child -> child.setVisible((child == img3dView) != editMode.get()));
        root.requestFocus();

        xf.setDisable(true);
        yf.setDisable(true);
        zf.setDisable(true);
        matrix.currentPointOnEdit.setSelected(false);
        matrix.currentPointOnEdit = null;
    }
}
