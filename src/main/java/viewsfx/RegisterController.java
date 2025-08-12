package viewsfx;

import dao.RegisteredUserDAO;
import enums.Nationality;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.RegisteredUser;
import services.RegisterValidation;

public class RegisterController {

    @FXML private TextField nameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField idField;
    @FXML private ComboBox<Nationality> nationalityBox;
    @FXML private Button registerBtn;

    private final RegisteredUserDAO userDAO = new RegisteredUserDAO();

    @FXML
    public void initialize() {
        // Fill nationality
        nationalityBox.setItems(FXCollections.observableArrayList(Nationality.values()));
        if (!nationalityBox.getItems().isEmpty()) {
            nationalityBox.getSelectionModel().selectFirst();
        }

        // Auto-focus first name after scene is ready
        Platform.runLater(() -> nameField.requestFocus());

        // Live validation to enable/disable register
        addValidationListeners();
        updateRegisterDisabled();
    }

    private void addValidationListeners() {
        nameField.textProperty().addListener((o, a, b) -> updateRegisterDisabled());
        lastNameField.textProperty().addListener((o, a, b) -> updateRegisterDisabled());
        emailField.textProperty().addListener((o, a, b) -> updateRegisterDisabled());
        passwordField.textProperty().addListener((o, a, b) -> updateRegisterDisabled());
        idField.textProperty().addListener((o, a, b) -> updateRegisterDisabled());
        nationalityBox.valueProperty().addListener((o, a, b) -> updateRegisterDisabled());
    }

    private void updateRegisterDisabled() {
        boolean valid = isFormValid(false); // quick/cheap checks only
        registerBtn.setDisable(!valid);
    }

    private boolean isFormValid(boolean deep) {
        String name = t(nameField);
        String lastName = t(lastNameField);
        String email = t(emailField);
        String password = t(passwordField);
        String id = t(idField);
        Nationality nat = nationalityBox.getValue();

        boolean cheapOk =
                RegisterValidation.isNotBlank(name) &&
                RegisterValidation.isNotBlank(lastName) &&
                RegisterValidation.isValidEmail(email) &&
                RegisterValidation.isNotBlank(password) &&
                RegisterValidation.isValidID(id) &&
                nat != null;

        if (!deep || !cheapOk) return cheapOk;

        // Deep checks: duplicates (potentially DB calls)
        if (userDAO.getRegisteredUserByEmail(email) != null) return false;
        if (userDAO.getRegisteredUserById(id) != null) return false;

        return true;
    }

    @FXML
    private void onRegister() {
        // Deep validation (with duplicate checks)
        if (!isFormValid(true)) {
            // Focus the first problematic field and explain
            if (!RegisterValidation.isNotBlank(t(nameField))) {
                focusWarn(nameField, "Please enter your first name."); return;
            }
            if (!RegisterValidation.isNotBlank(t(lastNameField))) {
                focusWarn(lastNameField, "Please enter your last name."); return;
            }
            if (!RegisterValidation.isValidEmail(t(emailField))) {
                focusWarn(emailField, "Please enter a valid email."); return;
            }
            if (!RegisterValidation.isNotBlank(t(passwordField))) {
                focusWarn(passwordField, "Please enter a password."); return;
            }
            if (!RegisterValidation.isValidID(t(idField))) {
                focusWarn(idField, "ID must be exactly 9 digits."); return;
            }
            if (nationalityBox.getValue() == null) {
                nationalityBox.requestFocus();
                warn("Please select a nationality.");
                return;
            }
            if (userDAO.getRegisteredUserByEmail(t(emailField)) != null) {
                focusWarn(emailField, "That email is already registered."); return;
            }
            if (userDAO.getRegisteredUserById(t(idField)) != null) {
                focusWarn(idField, "That ID is already registered."); return;
            }
        }

        // Persist
        RegisteredUser user = new RegisteredUser(
                t(idField),
                t(nameField),
                t(lastNameField),
                t(emailField),
                t(passwordField),
                nationalityBox.getValue()
        );

        boolean ok = userDAO.createRegisteredUser(user);
        if (!ok) {
            error("Registration failed. Email/ID may already exist.");
            return;
        }

        // Success prompt; when closed, go back to Login
        Alert a = new Alert(Alert.AlertType.INFORMATION, "Registration successful! You may now login.", ButtonType.OK);
        a.setHeaderText("Success");
        a.showAndWait();
        goLogin();
    }

    @FXML
    private void onBackToLogin() { goLogin(); }

    private void goLogin() {
        try {
            Stage stage = (Stage) nameField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
            Scene scene = new Scene(loader.load(), 860, 540);
            var css = getClass().getResource("/css/styles.css");
            if (css != null) scene.getStylesheets().add(css.toExternalForm());
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            error("Could not return to login: " + e.getMessage());
        }
    }

    private static String t(TextField f) { return f == null || f.getText() == null ? "" : f.getText().trim(); }

    private void focusWarn(TextField field, String msg) {
        if (field != null) field.requestFocus();
        warn(msg);
    }

    private void warn(String m) { new Alert(Alert.AlertType.WARNING, m, ButtonType.OK).showAndWait(); }
    private void error(String m) { new Alert(Alert.AlertType.ERROR, m, ButtonType.OK).showAndWait(); }
}
