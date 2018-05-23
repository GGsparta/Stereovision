package Model;


import java.awt.image.BufferedImage;

public class MatrixGenerator {
    private BufferedImage inputPicture1, inputPicture2;
    private Matrix ouputMatrix;

    public void setInputPictures(BufferedImage inputPicture1, BufferedImage inputPicture2) {
        this.inputPicture1 = inputPicture1;
        this.inputPicture2 = inputPicture2;
    }

    public Matrix computeMatrix() {
        ouputMatrix = new Matrix(inputPicture1.getWidth(),inputPicture1.getHeight());
        /*
        Steps:    https://www.irit.fr/~Jean-Denis.Durou/ENSEIGNEMENT/VISION/COURS/co03.html
        - Calibrate
            I: P(x,y,z) visible on both pictures
            O: points Il and Ir (x,y) on the pictures (==> xleft, xright, y)
        - Matching
            I: P, Il, Ir
            O: G (scaling factor, arbitrary) and alpha (
        - Triangulate (is trying steps before to find O...)
            I: Il, Ir
            O: P
         */
        Calibrate();
        Matching();
        Triangulate();
        return ouputMatrix;
    }

    private void Calibrate() {

    }

    private void Matching() {

    }

    private void Triangulate() {

    }
}
