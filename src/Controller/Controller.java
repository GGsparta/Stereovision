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
import javafx.collections.FXCollections;
import javafx.collections.ObservableIntegerArray;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.FileChooser;

import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.*;

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

            HashMap<Point_dt,MatrixPoint3D> points_dts = new HashMap<>();

            double minX = Double.MAX_VALUE,minY = Double.MAX_VALUE,maxX = Double.MIN_VALUE,maxY = Double.MIN_VALUE;
            var matrix = matrixGenerator.computeMatrix();
            for (MatrixPoint3D point : matrix) {
                if(point.getX()<minX) minX = point.getX();
                if(point.getX()>maxX) maxX = point.getX();
                if(point.getY()<minY) minY = point.getY();
                if(point.getY()>maxY) maxY = point.getY();

                Platform.runLater(() -> {
                    resultspane.getChildren().add(point.view);
                });


                points_dts.put(new Point_dt(point.getX(),point.getY()),point);

            }

            System.out.println("Creating surface...");
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
            System.out.println("size:"+d.size()+", "+d.trianglesSize());
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
            //faces.addAll(faces);


            /*TriangleMesh pyramidMesh = new TriangleMesh();
            pyramidMesh.getTexCoords().addAll(0,0);
            float h = 150;                    // Height
            float s = 300;                    // Side
            pyramidMesh.getPoints().addAll(
                    0,    0,    0,            // Point 0 - Top
                    0,    h,    -s/2,         // Point 1 - Front
                    -s/2, h,    0,            // Point 2 - Left
                    s/2,  h,    0,            // Point 3 - Back
                    0,    h,    s/2           // Point 4 - Right
            );
            pyramidMesh.getFaces().addAll(
                    0,0,  2,0,  1,0,          // Front left face
                    0,0,  1,0,  3,0,          // Front right face
                    0,0,  3,0,  4,0,          // Back right face
                    0,0,  4,0,  2,0,          // Back left face
                    4,0,  1,0,  2,0,          // Bottom rear face
                    4,0,  3,0,  1,0           // Bottom front face
            );*/

            TriangleMesh image3D = new TriangleMesh();
            image3D.getTexCoords().addAll(textures);
            image3D.getPoints().addAll(points);
            image3D.getFaces().addAll(faces);

            Platform.runLater(()->{
                var img3dView = new MeshView(image3D);
                img3dView.setDrawMode(DrawMode.FILL);
                img3dView.setMaterial(new PhongMaterial(Color.WHITE,image1,null,null,null));
                resultspane.getChildren().add(0,img3dView);

                var  cm = new Point2D(117, 172);
                img3dView.setScaleX(MatrixPoint3D.ratio.getX());
                img3dView.setScaleY(MatrixPoint3D.ratio.getY());
                img3dView.setTranslateY(img3dView.boundsInParentProperty().get().getHeight()/2);
                img3dView.setTranslateX(img3dView.boundsInParentProperty().get().getWidth()/2);
                resultspane.setTranslateY(-cm.getY());
                resultspane.setTranslateX(-cm.getX());
            });
            resultspane.setPrefSize(maxX-minX,maxY-minY);
            savebutton.setDisable(false);
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
