package Model;


import javafx.geometry.Point3D;
import javafx.util.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import static java.lang.Math.*;

public class MatrixGenerator {
    /**
     * Matrix Generation.
     * Potential new parameter(s):
     *  - alpha
     *  - matching limit
     */


    private BufferedImage
            ipL, // Input Picture Left
            ipR; // Input Picture Right
    private Point imagesSize;
    private Matrix ouputMatrix;
    private HashMap<Point,Point> pixelPairs; // matching pairs (Il,Ir)

    /*
    Parameters:
    */private double alpha/*            Alpha           */=PI/2;/*
    */private double repere/*           Repere          */=1;/*
    */private double matchingLimit/*    Matching Limit  */=0.1;/*
    */
    private int windowSize = 3; // (3x3 window)

    public MatrixGenerator() {
        pixelPairs = new HashMap<>();
    }

    public boolean setInputPictures(BufferedImage inputPictureLeft, BufferedImage inputPictureRight) {
        this.ipL = inputPictureLeft;
        this.ipR = inputPictureRight;

        imagesSize = new Point(ipL.getWidth(), ipR.getHeight());

        // if pictures are not the same size, it doesn't work.
        return ipL.getWidth()== ipR.getWidth() && ipL.getHeight()== ipR.getHeight();
    }

    public Matrix computeMatrix() {
        ouputMatrix = new Matrix();
        /*
        Steps:    https://www.irit.fr/~Jean-Denis.Durou/ENSEIGNEMENT/VISION/COURS/co03.html
        - Matching:
            Finding Ir/Il pixel pairs
        - Calibrate:
            Finding Ir, Il or alpha from P
        - Triangulate (is trying steps before to find O...)
            Find matching P to Il/Ir couples using calibration
         */
        MatchPixels();
        Triangulate();
        return ouputMatrix;
    }

    private void MatchPixels() {
        /*
        matching pixels for the best neighborhood

        4 constraints:
            - epipolar: yd=yg, no side pixels
            - uniqueness: pair found in L-->R and in R-->L
            - limit: match pixels if its difference < limit S
            - order (optional)
         */

        for (int j = windowSize/2; j < imagesSize.y-(windowSize/2); j++) { // no side pixel constraint

            // epipolar constraint
            int[] rowL = new int[imagesSize.x],
                    rowR = new int[imagesSize.x];
            ipL.getData().getPixels(0,j,imagesSize.x,1,rowL);
            ipR.getData().getPixels(0,j,imagesSize.x,1,rowR);

            int lastIrFound = 0;

            for(int i=windowSize/2; i< imagesSize.x-(windowSize/2); i++) { // no side pixel constraint
                // limit constraint
                int iR = findBestClosePointOnRow(i,j,ipL);
                if(iR<0) continue;

                // uniqueness & limit constraint
                int iL=findBestClosePointOnRow(iR,j,ipR);
                if(iL!=i) continue;

                if(iR>=lastIrFound) { // order constraint
                    lastIrFound = iR;
                    pixelPairs.put(new Point(iL,j),new Point(iR,j));
                }
            }
        }
    }

    private int findBestClosePointOnRow(int i, int j, BufferedImage image1) {
        double bestDiff=Double.MAX_VALUE;
        int bestIndex=-1;

        BufferedImage image2 = image1==ipL?ipR:ipL;

        for (int ie = windowSize/2; ie<imagesSize.x-(windowSize/2); ie++) {
            int[] data1 = new int[4*(windowSize/2)*(windowSize/2)],
                  data2 = new int[4*(windowSize/2)*(windowSize/2)];

            image1.getData().getPixels(
                    i-(windowSize/2),
                    j-(windowSize/2),
                    windowSize/2,
                    windowSize/2,
                    data1
            );
            image2.getData().getPixels(
                    ie-(windowSize/2),
                    j-(windowSize/2),
                    windowSize/2,
                    windowSize/2,
                    data2
            );

            double diff=0;
            for(int k=0; k<data1.length; k++)
                diff+=pow(data1[k]-data2[k],2);


            if(diff<matchingLimit && diff<bestDiff) {
                bestDiff=diff;
                bestIndex = ie;
            }
        }

        return bestIndex;
    }

    private void Triangulate() {
        for (HashMap.Entry<Point,Point> pair : pixelPairs.entrySet()) {
            ouputMatrix.addPoint(
                    -(pair.getKey().x+pair.getValue().x)/(2*cos(alpha*repere/2)),
                    pair.getKey().y/repere,
                    -(pair.getKey().x-pair.getValue().x)/(2*cos(alpha*repere/2))
            );
        }
    }


    private Pair<Point, Point> CalibrateI(Point3D P) {
        return new Pair<>(
                new Point(
                        (int) (repere * (-cos(alpha * P.getX() / 2) - sin(alpha * P.getZ() / 2))),
                        (int) (repere * (P.getY()))
                ),
                new Point(
                        (int) (repere * (-cos(alpha * P.getX() / 2) + sin(alpha * P.getZ() / 2))),
                        (int) (repere * (P.getY()))
                )
        );
    }

    private void CalibrateAlpha(Point3D P, HashMap.Entry<Point,Point> I) {
        alpha = 2 * atan(
                (P.getX()*(I.getKey().getX()- I.getValue().getX())) / (P.getZ()*(I.getKey().getX()+ I.getValue().getX()))
        );
    }
}
