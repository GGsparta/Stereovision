package View;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import static java.lang.Math.*;

public class View3D extends StackPane {
    public boolean[] moving;
    private double rotateSpeed = 1.5;
    private final Point2D cm = new Point2D(117, 172); // counter_margin

    private Rotate rx,ry;

    public View3D() {
        super();

        moving = new boolean[4]; // order: UP RIGHT DOWN RIGHT
        rx = new Rotate(0.1, Rotate.X_AXIS);
        ry = new Rotate(0.1, Rotate.Y_AXIS);
        getTransforms().clear();
        getTransforms().addAll(rx,ry);


        widthProperty().addListener((observable, oldValue, newValue) -> {
            rx.setPivotX(newValue.doubleValue()*3/4);
            ry.setPivotX(newValue.doubleValue()*2.5/4);
            setTranslateX(-newValue.doubleValue()/6);
        });
        heightProperty().addListener((observable, oldValue, newValue) -> {
            rx.setPivotY(newValue.doubleValue()*3/4);
            ry.setPivotY(newValue.doubleValue()*2.5/4);
            setTranslateY(-newValue.doubleValue()/3);
        });

        rx.setAngle(0);
        ry.setAngle(0); // execute a change to update view



        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(25),
                event -> {
                    if (moving[0]) rx.setAngle(min(rx.getAngle() + rotateSpeed,90-rotateSpeed));
                    if (moving[1]) ry.setAngle(min(ry.getAngle() + rotateSpeed,90-rotateSpeed));
                    if (moving[2]) rx.setAngle(max(rx.getAngle() - rotateSpeed,-90+rotateSpeed));
                    if (moving[3]) ry.setAngle(max(ry.getAngle() - rotateSpeed,-90+rotateSpeed));
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
