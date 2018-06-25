package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.application.ConditionalFeature.FXML;

/**
 * Created by pc on 25/06/2018.
 */
public class HelpPageController implements Initializable {

    @FXML
    WebView webView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WebEngine engine = webView.getEngine();
        URL url = this.getClass().getResource("/help/web/help.html");

        try {
            engine.load(url.toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
