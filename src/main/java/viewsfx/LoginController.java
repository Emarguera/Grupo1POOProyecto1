package viewsfx;

import dao.AdminDAO;
import dao.LoginDAO;
import dao.RegisteredUserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Admin;
import models.RegisteredUser;

import java.net.URL;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private final LoginDAO loginDAO = new LoginDAO();
    private final AdminDAO adminDAO = new AdminDAO();
    private final RegisteredUserDAO registeredUserDAO = new RegisteredUserDAO();

    @FXML
    private void onLogin() {
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String pass  = passwordField.getText() == null ? "" : passwordField.getText().trim();
        if (email.isEmpty() || pass.isEmpty()) {
            FxAlerts.info("Please enter email and password.");
            return;
        }

        String role = loginDAO.checkLogin(email, pass);

        try {
            Stage stage = (Stage) emailField.getScene().getWindow();

            switch (role) {
                case "ADMIN" -> {
                    Admin admin = adminDAO.getAdminByEmail(email);
                    if (admin == null) { FxAlerts.error("Admin profile not found."); return; }

                    URL url = getClass().getResource("/fxml/AdminDashboard.fxml");
                    if (url == null) { FxAlerts.error("AdminDashboard.fxml not found at /fxml/AdminDashboard.fxml"); return; }
                    FXMLLoader loader = new FXMLLoader(url);
                    Scene scene = new Scene(loader.load(), 1024, 640);
                    scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                    AdminDashboardController c = loader.getController();
                    c.init(admin);
                    stage.setScene(scene);
                }

                case "REGISTERED" -> {
                    RegisteredUser user = registeredUserDAO.getRegisteredUserByEmail(email);
                    if (user == null) { FxAlerts.error("User profile not found or inactive."); return; }

                    URL url = getClass().getResource("/fxml/UserDashboard.fxml");
                    if (url == null) { FxAlerts.error("UserDashboard.fxml not found at /fxml/UserDashboard.fxml"); return; }
                    FXMLLoader loader = new FXMLLoader(url);
                    Scene scene = new Scene(loader.load(), 1024, 680);
                    scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                    UserDashboardController c = loader.getController();
                    c.init(user);
                    stage.setScene(scene);
                }

                default -> FxAlerts.error("Invalid credentials.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            FxAlerts.error("Failed to open dashboard: " + ex.getMessage());
        }
    }

    @FXML
private void onRegister() {
    try {
        // Try to resolve the FXML on the classpath
        var url = getClass().getResource("/fxml/RegisterView.fxml");
        if (url == null) {
            // Extra hints to diagnose path issues
            String hint = """
                    RegisterView.fxml was not found at /fxml/RegisterView.fxml.
                    Make sure the file is here:
                      src/main/resources/fxml/RegisterView.fxml
                    Then Clean and Build so it appears at:
                      target/classes/fxml/RegisterView.fxml
                    """;
            FxAlerts.error("Could not open registration.\n\n" + hint);
            return;
        }

        FXMLLoader loader = new FXMLLoader(url);
        Scene scene = new Scene(loader.load(), 860, 540);
        var css = getClass().getResource("/css/styles.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        ((Stage) emailField.getScene().getWindow()).setScene(scene);

    } catch (Exception ex) {
        ex.printStackTrace();
        FxAlerts.error("Could not open registration:\n" + ex.getClass().getSimpleName() + ": " + ex.getMessage());
    }
}


    @FXML
    private void onHelp() {
        FxAlerts.info("Enter your credentials. Admins go to Admin Dashboard; users to User Dashboard.");
    }

    @FXML
    private void onCloseApp() {
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.close();
    }
}
