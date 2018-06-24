package Model;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;

public class MatrixPoint3D {
    public final static Point3D ratio = new Point3D(0.8,0.8,0.8);
    private final Color color;
    public Sphere view;
    private double x, y, z;
    ArrayList<MatrixPoint3D> neighbours;
    private boolean selected;

    MatrixPoint3D(double x, double y, double z, Color color) {
        this.x = x;
        this.y = y;
        this.z = z;
        view = new Sphere(3);
        this.color = color;
        view.setMaterial(new PhongMaterial(this.color));
        actualizeView();

        view.setOnMouseEntered(event -> {if(!selected) view.setRadius(8);});
        view.setOnMouseExited(event -> {if(!selected) view.setRadius(3);});
    }

    public void setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        actualizeView();
    }

    private void actualizeView() {
        view.setTranslateX(x*ratio.getX());
        view.setTranslateY(y*ratio.getY());
        view.setTranslateZ(z*ratio.getZ());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if(selected) view.setRadius(8);
        else view.setRadius(3);
    }
}
