package Model;

/**
 * Created by pc on 24/06/2018.
 */
public class NullImagesException extends Exception{

    public NullImagesException(){
        super("no image selected.");
    }
}
