package Model;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;

public class MatrixPoint3D extends Point3D {
    private final static Point3D ratio = new Point3D(0.8,0.8,0.8);
    public Sphere view;
    ArrayList<MatrixPoint3D> neighbours;

    MatrixPoint3D(double x, double y, double z, Color color) {
        super(x,y,z);
        view = new Sphere(1.5);
        view.setMaterial(new PhongMaterial(color));
        actualizeView();
    }

    public void setPosition(double x, double y, double z) {
        add(
                x-getX(),
                y-getY(),
                z-getZ()
        );
        actualizeView();
    }

    private void actualizeView() {
        view.setTranslateX(getX()*ratio.getX());
        view.setTranslateY(getY()*ratio.getY());
        view.setTranslateZ(getZ()*ratio.getZ());
    }
}
