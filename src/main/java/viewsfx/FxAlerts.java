package viewsfx;

import javafx.scene.control.Alert;

public class FxAlerts {
    public static void info(String msg) { show(Alert.AlertType.INFORMATION, "Info", msg); }
    public static void error(String msg) { show(Alert.AlertType.ERROR, "Error", msg); }
    private static void show(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
