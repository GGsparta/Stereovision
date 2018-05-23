package Model;

public class Matrix {
    float[][] map;

    /*
    The map contains all the matrix's points (X,Y,Z) this way:
        map[X][Y] = Z
    Indeed, every points match on (X,Y) a pixel on the pictures
     */

    public Matrix(int x, int y) {
        map = new float[x][y];
    }
}
