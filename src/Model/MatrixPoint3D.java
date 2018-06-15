package Model;

import javafx.geometry.Point3D;

import java.util.ArrayList;

public class MatrixPoint3D {
    Point3D data;
    boolean isDefined;
    ArrayList<MatrixPoint3D> neighbours;

    public MatrixPoint3D(int x, int y) {
        data = new Point3D(x,y,0);
        isDefined = false;
    }
}
