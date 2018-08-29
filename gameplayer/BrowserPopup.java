package gameplayer;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.Map;

/**
 * @Author Alexi Kontos
 */


public class BrowserPopup {

    private String URL;
    private Map<String,String> GAMEPLAYER_PROPERTIES;

    public BrowserPopup(String url, Map<String,String> gameplayer_properties) {
        URL = url;
        GAMEPLAYER_PROPERTIES = gameplayer_properties;
    }

    public void makePopupBrowser() {
        Stage popUpWindow = new Stage();
        popUpWindow.initModality(Modality.APPLICATION_MODAL);
        popUpWindow.setTitle(GAMEPLAYER_PROPERTIES.get("browserTitle"));
        WebView browser = new WebView();
        WebEngine engine = browser.getEngine();
        engine.load(URL);
        VBox browserView = new VBox(browser);
        Scene scene1 = new Scene(browserView, Integer.parseInt(GAMEPLAYER_PROPERTIES.get("browserWidth")), Integer.parseInt(GAMEPLAYER_PROPERTIES.get("browserHeight")));
        popUpWindow.setScene(scene1);
        popUpWindow.showAndWait();
    }
}
