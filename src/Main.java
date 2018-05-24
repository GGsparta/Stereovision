import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("View/main_window.fxml"));
        primaryStage.setTitle("Stéréovision");
        primaryStage.getIcons().add(new Image("Assets/Images/logo.png"));
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);


        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
