package gameplayer;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GameplayerAlert {

    private static final String errorTitle = "ERROR";
    private Alert ALERT;

    public GameplayerAlert(String errorMessage) {
        ALERT = new Alert(AlertType.ERROR);
        ALERT.setTitle(errorTitle);
        ALERT.setContentText(errorMessage);
        createAlert();

    }

    private void createAlert() {
        ALERT.showAndWait();
    }
}
