package Magic;

import javafx.animation.Transition;
import javafx.scene.layout.Region;
import javafx.util.Duration;


public class ResizeHeightTranslation extends Transition {

    private Region region;
    private double startHeight;
    private double newHeight;
    private double heightDiff;

    public ResizeHeightTranslation( Duration duration, Region region, double newHeight ) {
        setCycleDuration(duration);
        this.region = region;
        this.newHeight = newHeight;
        this.startHeight = region.getHeight();
        this.heightDiff = newHeight - startHeight;
    }

    @Override
    protected void interpolate(double fraction) {
        region.setMinHeight( startHeight + ( heightDiff * fraction ) );
    }
}