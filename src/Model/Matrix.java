package Model;

import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.HashMap;

public class Matrix {
    ArrayList<Point3D> map; // The map contains all the matrix's points (X,Y,Z)
    HashMap<Point3D, ArrayList<Point3D>> edges; // Edges are defines in this map

    /*
    The map contains all the matrix's points (X,Y,Z) this way:
        map[X][Y] = Z
    Indeed, every points match on (X,Y) a pixel on the pictures
     */

    public Matrix(int x, int y) {
        /*
        TODO générer la base (points et voisins)
         */
    }

    public void setPoint(int x, int y, int z) {
        /*
        TODO trouver point correspondant, et le modifier
         */
    }
}
