package Model;

import javafx.geometry.Point3D;

import java.util.ArrayList;

class MatrixPoint3D extends Point3D {
    private boolean isDefined;
    ArrayList<MatrixPoint3D> neighbours;

    MatrixPoint3D(int x, int y) {
        super(x,y,0);
        isDefined = false;
    }
}
