package Model;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point3D;
import javafx.util.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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
    */private double alpha/*            Alpha           */=PI/6;/*
    */private double repere/*           Repere          */=1;/*
    */private double matchingLimit/*    Matching Limit  */=10000;/*
    */private int pace/*                Pace            */=10;/*
    */
    private int windowSize = 3; // (3x3 window)

    public MatrixGenerator() {
        pixelPairs = new HashMap<>();
    }

    public boolean setInputPictures(BufferedImage inputPictureLeft, BufferedImage inputPictureRight) {
        this.ipL = inputPictureLeft;
        this.ipR = inputPictureRight;

        imagesSize = new Point(ipL.getWidth(), ipL.getHeight());
        pace *= (ipL.getWidth()*ipL.getHeight()) / (233*350);

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
            - uniqueness: pair found in L-->R and in R-->L (CURRENTLY IGNORED)
            - limit: match pixels if its difference < limit S
            - order (optional)
         */
        ArrayList<Thread> threads = new ArrayList<>();

        for (int j = windowSize/2; j < imagesSize.y-(windowSize/2); j+= pace) { // no side pixel constraint
            IntegerProperty lastIrFound = new SimpleIntegerProperty(0);
            int finalJ = j;
            Thread t = new Thread(() -> {
                for (int i = windowSize / 2; i < imagesSize.x - (windowSize / 2); i += pace) { // no side pixel constraint
                    try {
                        // limit constraint
                        int iR = findBestClosePointOnRow(i, finalJ, ipL);
                        if (iR < 0) return;

                        // uniqueness & limit constraint
                        //int iL = findBestClosePointOnRow(iR, finalJ, ipR);
                        //if (abs(iL-i)>400 || iL<0) return;

                        syncronisedPointInsertion(iR, i, finalJ, lastIrFound);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            threads.add(t);
            t.start();
        }

        System.out.println("Waiting for the threads...");

        int i=0;
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized private void syncronisedPointInsertion(int iR, int iL, int j, IntegerProperty lastIrFound) throws InterruptedException {
        if(iR>=lastIrFound.getValue()) { // order constraint
            lastIrFound.setValue(iR);
          pixelPairs.put(new Point(iL,j),new Point(iR,j));
        }
    }

    private int findBestClosePointOnRow(int i, int j, BufferedImage image1) {
        double bestDiff=Double.MAX_VALUE;
        int bestIndex=-1;

        BufferedImage image2 = image1==ipL?ipR:ipL;

        for (int ie = windowSize/2; ie<imagesSize.x-(windowSize/2); ie++) {
            int[] data1 = new int[windowSize*windowSize*3],
                  data2 = new int[windowSize*windowSize*3];

            data1 = image1.getData().getPixels(
                    i-(windowSize/2),
                    j-(windowSize/2),
                    windowSize,
                    windowSize,
                    data1
            );
            data2 = image2.getData().getPixels(
                    ie-(windowSize/2),
                    j-(windowSize/2),
                    windowSize,
                    windowSize,
                    data2
            );

            double diff=0;
            for(int k=0; k<data1.length; k+=3) {
                diff+=pow(
                        (data1[k]+data1[k+1]+data1[k+2]-data2[k]-data2[k+1]-data2[k+2])/3,
                        2
                );
            }


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
                    (pair.getKey().x+pair.getValue().x)/(2*cos(alpha*repere/2)),
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
