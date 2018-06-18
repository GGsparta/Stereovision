package Model;

import javafx.geometry.Point3D;

import java.util.ArrayList;

class MatrixPoint3D extends Point3D {
    ArrayList<MatrixPoint3D> neighbours;

    MatrixPoint3D(double x, double y, double z) {
        super(x,y,0);
    }
}
