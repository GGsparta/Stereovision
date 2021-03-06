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
        primaryStage.setScene(new Scene(root, 800, 620));
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(620);


        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
