package viewsfx;

import controllers.AdminController;
import enums.Genre;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Admin;
import models.RegisteredUser;
import models.Song;

import java.text.NumberFormat;
import java.util.Locale;

public class AdminDashboardController {

    @FXML private TableView<Song> catalogTable;
    @FXML private TableColumn<Song, String> colSongId, colTitle, colArtist, colGenre;
    @FXML private TableColumn<Song, Number> colPrice, colPurchases, colPlaylists, colAvgRating;

    @FXML private TableView<RegisteredUser> usersTable;
    @FXML private TableColumn<RegisteredUser, String> colUserId, colUserName, colUserEmail;
    @FXML private TableColumn<RegisteredUser, Number> colUserBalance;

    @FXML private Label statusLabel;

    private final NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
    private AdminController controller;

    public void init(Admin admin) {
        this.controller = new AdminController(admin);
        setupTables();
        refreshCatalog();
        refreshUsers();
    }

    private void setupTables() {
        // Catalog columns
        colSongId.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getId()));
        colTitle.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitle()));
        colArtist.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getArtist()));
        colGenre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getGenre().name()));
        colPrice.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().getPrice()));
        colPurchases.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getPurchaseCount()));
        colPlaylists.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getPlaylistCount()));
        colAvgRating.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().getAverageRating()));

        colPrice.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(Number v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? "" : currency.format(v.doubleValue()));
            }
        });

        // Users columns
        colUserId.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getId()));
        colUserName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getName() + " " + d.getValue().getLastName()));
        colUserEmail.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEmail()));
        colUserBalance.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().getBalance()));
        colUserBalance.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(Number v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? "" : currency.format(v.doubleValue()));
            }
        });

        // Placeholders
        catalogTable.setPlaceholder(new Label("No songs to show"));
        usersTable.setPlaceholder(new Label("No registered users"));

        // Constrained resize for cleaner widths
        catalogTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void refreshCatalog() {
        catalogTable.getItems().setAll(controller.getCatalogSongs());
    }

    private void refreshUsers() {
        usersTable.getItems().setAll(controller.getRegisteredUsers());
    }

    // ===========
    // Add Song UI
    // ===========
    @FXML
    private void onAddSong() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Song");
        dialog.setHeaderText("Enter the song details");

        ButtonType addBtnType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtnType, ButtonType.CANCEL);

        // Form controls
        TextField idField = new TextField();
        idField.setPromptText("e.g. S99");
        idField.setPrefColumnCount(12);

        TextField titleField = new TextField();
        titleField.setPromptText("Song title");

        TextField artistField = new TextField();
        artistField.setPromptText("Artist");

        TextField priceField = new TextField();
        priceField.setPromptText("e.g. 1.29");

        ComboBox<Genre> genreBox = new ComboBox<>(FXCollections.observableArrayList(Genre.values()));
        genreBox.getSelectionModel().selectFirst();

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(new Label("ID (max 3 chars):"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Title:"), 0, 1);
        grid.add(titleField, 1, 1);
        grid.add(new Label("Artist:"), 0, 2);
        grid.add(artistField, 1, 2);
        grid.add(new Label("Price:"), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(new Label("Genre:"), 0, 4);
        grid.add(genreBox, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Enable/disable Add button based on basic validation
        var addButton = dialog.getDialogPane().lookupButton(addBtnType);
        addButton.setDisable(true);

        Runnable toggle = () -> {
            boolean ok = !idField.getText().trim().isEmpty()
                      && !titleField.getText().trim().isEmpty()
                      && !artistField.getText().trim().isEmpty()
                      && !priceField.getText().trim().isEmpty();
            if (ok) {
                try {
                    Double p = Double.valueOf(priceField.getText().trim());
                    ok = p >= 0.0;
                } catch (NumberFormatException ex) {
                    ok = false;
                }
            }
            addButton.setDisable(!ok);
        };

        idField.textProperty().addListener((o, a, b) -> toggle.run());
        titleField.textProperty().addListener((o, a, b) -> toggle.run());
        artistField.textProperty().addListener((o, a, b) -> toggle.run());
        priceField.textProperty().addListener((o, a, b) -> toggle.run());
        toggle.run();

        var result = dialog.showAndWait();
        if (result.isPresent() && result.get() == addBtnType) {
            String id = idField.getText().trim();
            String title = titleField.getText().trim();
            String artist = artistField.getText().trim();
            Genre genre = genreBox.getValue();

            // Final validation (DB expects VARCHAR2(3) for song id)
            if (id.length() > 3) {
                new Alert(Alert.AlertType.ERROR, "ID must be at most 3 characters.").showAndWait();
                return;
            }
            double price;
            try {
                price = Double.parseDouble(priceField.getText().trim());
                if (price < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Invalid price.").showAndWait();
                return;
            }

            boolean ok = controller.addSong(id, title, artist, price, genre);
            if (ok) {
                refreshCatalog();
                status("Song added.");
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to add song. Check if ID is unique and fields are valid.")
                        .showAndWait();
                status("Failed to add song.");
            }
        }
    }

    @FXML
    private void onRemoveSong() {
        Song sel = catalogTable.getSelectionModel().getSelectedItem();
        if (sel == null) { status("Select a song to remove."); return; }
        if (confirm("Delete song [" + sel.getTitle() + "]?")) {
            if (controller.removeSong(sel.getId())) { refreshCatalog(); status("Song removed."); }
            else status("Failed to remove.");
        }
    }

    @FXML
    private void onAddBalance() {
        RegisteredUser u = usersTable.getSelectionModel().getSelectedItem();
        if (u == null) { status("Select a user first."); return; }
        TextInputDialog dlg = new TextInputDialog("10.00");
        dlg.setHeaderText("Add balance to: " + u.getEmail());
        dlg.setContentText("Amount to add:");
        dlg.showAndWait().ifPresent(val -> {
            try {
                double amt = Double.parseDouble(val.trim());
                if (amt < 0) { status("Amount must be ≥ 0"); return; }
                if (controller.addBalanceToUser(u.getId(), amt)) { refreshUsers(); status("Balance updated."); }
                else status("Failed to update balance.");
            } catch (NumberFormatException ex) {
                status("Invalid number.");
            }
        });
    }

    @FXML
    private void onDeactivateUser() {
        RegisteredUser u = usersTable.getSelectionModel().getSelectedItem();
        if (u == null) { status("Select a user first."); return; }
        if (confirm("Deactivate user " + u.getEmail() + "?")) {
            if (controller.deleteRegisteredUser(u.getId())) { refreshUsers(); status("User deactivated."); }
            else status("Failed to deactivate.");
        }
    }

    @FXML
    private void onShowTop5() {
        try {
            var loader = new FXMLLoader(getClass().getResource("/fxml/Top5Dialog.fxml"));
            var scene = new Scene(loader.load(), 700, 520);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            var s = new Stage();
            s.setTitle("Top 5");
            s.initOwner(catalogTable.getScene().getWindow());
            s.initModality(Modality.APPLICATION_MODAL);
            s.setScene(scene);
            s.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Could not open Top 5 dialog:\n" + ex.getMessage()).showAndWait();
            status("Could not open Top 5 dialog.");
        }
    }

    @FXML
    private void onLogout() {
        Stage stage = (Stage) catalogTable.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
            Scene scene = new Scene(loader.load(), 860, 540);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean confirm(String msg) {
        return new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.YES, ButtonType.NO)
                .showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }
    private void status(String s) { statusLabel.setText(s); }
}
