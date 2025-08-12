package viewsfx;

import controllers.FinalUserController;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.RegisteredUser;
import models.Song;

import java.text.NumberFormat;
import java.util.Locale;

public class UserDashboardController {

    @FXML private Label balanceLabel;

    // catalog
    @FXML private TableView<Song> catalogTable;
    @FXML private TableColumn<Song, String> colSongId, colTitle, colArtist;
    @FXML private TableColumn<Song, Number> colPrice, colAvgRating;

    // purchased
    @FXML private TableView<Song> purchasedTable;
    @FXML private TableColumn<Song, String> colPSongId, colPTitle, colPArtist;

    // playlist
    @FXML private TableView<Song> playlistTable;
    @FXML private TableColumn<Song, String> colLSongId, colLTitle, colLArtist;

    private FinalUserController controller;
    private final NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);

    public void init(RegisteredUser user) {
        this.controller = new FinalUserController(user);
        setupTables();
        refreshAll();
    }

    private void setupTables() {
        // catalog
        colSongId.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getId()));
        colTitle.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitle()));
        colArtist.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getArtist()));
        colPrice.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().getPrice()));
        colAvgRating.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().getAverageRating()));
        colPrice.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(Number v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? "" : currency.format(v.doubleValue()));
            }
        });

        // purchased
        colPSongId.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getId()));
        colPTitle.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitle()));
        colPArtist.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getArtist()));

        // playlist
        colLSongId.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getId()));
        colLTitle.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitle()));
        colLArtist.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getArtist()));

        // Placeholders
        catalogTable.setPlaceholder(new Label("No songs"));
        purchasedTable.setPlaceholder(new Label("No purchases yet"));
        playlistTable.setPlaceholder(new Label("Your playlist is empty"));

        // ✅ Constrained resize for all three tables
        catalogTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        purchasedTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        playlistTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void refreshAll() {
        catalogTable.getItems().setAll(controller.getCatalogSongs());
        purchasedTable.getItems().setAll(controller.getPurchasedSongs());
        playlistTable.getItems().setAll(controller.getPlaylistSongs());
        balanceLabel.setText(currency.format(controller.getBalance()));
    }

    @FXML
    private void onPurchase() {
        Song sel = catalogTable.getSelectionModel().getSelectedItem();
        if (sel == null) { FxAlerts.info("Select a song."); return; }
        if (controller.purchaseSong(sel.getId())) {
            FxAlerts.info("Purchased!");
            refreshAll();
        } else {
            FxAlerts.error("Purchase failed (already owned or insufficient balance).");
        }
    }

    @FXML
    private void onCreatePlaylist() {
        TextInputDialog dlg = new TextInputDialog("My Playlist");
        dlg.setHeaderText("Create playlist");
        dlg.setContentText("Name:");
        dlg.showAndWait().ifPresent(name -> {
            if (controller.createPlaylist(name)) FxAlerts.info("Playlist created.");
            else FxAlerts.error("Failed to create playlist (maybe it already exists).");
            refreshAll();
        });
    }

    @FXML
    private void onAddToPlaylist() {
        Song sel = purchasedTable.getSelectionModel().getSelectedItem();
        if (sel == null) { FxAlerts.info("Select a purchased song."); return; }
        if (controller.addSongToPlaylist(sel.getId())) {
            FxAlerts.info("Added to playlist.");
            refreshAll();
        } else {
            FxAlerts.error("Add failed. Create a playlist first.");
        }
    }

    @FXML
    private void onRemoveFromPlaylist() {
        Song sel = playlistTable.getSelectionModel().getSelectedItem();
        if (sel == null) { FxAlerts.info("Select a playlist item."); return; }
        if (controller.removeSongFromPlaylist(sel.getId())) {
            FxAlerts.info("Removed.");
            refreshAll();
        } else {
            FxAlerts.error("Remove failed.");
        }
    }

    @FXML
    private void onRatePurchased() {
        Song sel = purchasedTable.getSelectionModel().getSelectedItem();
        if (sel == null) { FxAlerts.info("Select a purchased song."); return; }
        TextInputDialog dlg = new TextInputDialog("5");
        dlg.setHeaderText("Rate " + sel.getTitle() + " (1-5)");
        dlg.setContentText("Rating:");
        dlg.showAndWait().ifPresent(s -> {
            try {
                int r = Integer.parseInt(s.trim());
                if (r < 1 || r > 5) { FxAlerts.error("1..5 only."); return; }
                if (controller.rateSong(sel.getId(), r)) {
                    FxAlerts.info("Thanks for rating!");
                    refreshAll();
                } else FxAlerts.error("Rating failed.");
            } catch (NumberFormatException ex) { FxAlerts.error("Invalid number."); }
        });
    }

    @FXML
    private void onShowTop5() {
        try {
            var loader = new FXMLLoader(getClass().getResource("/fxml/Top5Dialog.fxml"));
            var scene = new Scene(loader.load(), 700, 520);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            var s = new Stage();
            s.setTitle("Top 5");
            s.initOwner(balanceLabel.getScene().getWindow());
            s.initModality(Modality.APPLICATION_MODAL);
            s.setScene(scene);
            s.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
            FxAlerts.error("Could not open Top 5 dialog: " + ex.getMessage());
        }
    }

    @FXML
private void onLogout() {
    Stage stage = (Stage) balanceLabel.getScene().getWindow();
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
        Scene scene = new Scene(loader.load(), 860, 540);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        stage.setScene(scene);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
