package Model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

class Matrix {
    private ArrayList<MatrixPoint3D> map; // The map contains all the matrix's points, one for each (X,Y) pair

    /*
    The map contains all the matrix's points (X,Y,Z) this way:
        map[X][Y] = Z
    Indeed, every points match on (X,Y) a pixel on the pictures
     */

    Matrix() {
        map = new ArrayList<>();

        /*// Create matrix basis
        for(int i=0; i<x; i++)
            for(int j=0; j<y; j++) {
                map.put(
                        new Pair<>(i,j),
                        new MatrixPoint3D(x,y)
                );
            }*/


    }

    void addPoint(double x, double y, double z) {
        MatrixPoint3D p = new MatrixPoint3D(x,y,z);
        map.add(p);
    }

    /*private MatrixPoint3D findPoint(int x, int y) {
        for (Pair<Integer,Integer> p : map.keySet()) if (p.getKey() == x && p.getValue() == y) return map.get(p);
        return null;
    }

    private void finalizeMatrix() {
        // Set edges
        for(Pair<Integer,Integer> p : map.keySet()) { // PAIR: key=X, value=Y
            if(p.getKey()>0) map.get(p).neighbours.add(findPoint(p.getKey()-1,p.getValue()));
            if(p.getKey()<x) map.get(p).neighbours.add(findPoint(p.getKey()+1,p.getValue()));
            if(p.getValue()>0) map.get(p).neighbours.add(findPoint(p.getKey(),p.getValue()-1));
            if(p.getValue()<y) map.get(p).neighbours.add(findPoint(p.getKey(),p.getValue()+1));
        }
    }*/
}
