package viewsfx;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.Song;
import services.Top5Manager;

import java.util.ArrayList;
import java.util.List;

public class Top5DialogController {

    @FXML private BorderPane root;

    @FXML private TableView<Row> tblPurchased;
    @FXML private TableColumn<Row, Number> colPIdx, colPPurchases;
    @FXML private TableColumn<Row, String> colPTitle, colPArtist;

    @FXML private TableView<Row> tblPlaylisted;
    @FXML private TableColumn<Row, Number> colLIdx, colLCount;
    @FXML private TableColumn<Row, String> colLTitle, colLArtist;

    @FXML private TableView<Row> tblRated;
    @FXML private TableColumn<Row, Number> colRIdx, colRAvg;
    @FXML private TableColumn<Row, String> colRTitle, colRArtist;

    @FXML
    public void initialize() {
        // Placeholders (nicer UX when empty)
        tblPurchased.setPlaceholder(new Label("No data"));
        tblPlaylisted.setPlaceholder(new Label("No data"));
        tblRated.setPlaceholder(new Label("No data"));

        // Cleaner table layout
        tblPurchased.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tblPlaylisted.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tblRated.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // Bind columns
        colPIdx.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().index));
        colPTitle.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().title));
        colPArtist.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().artist));
        colPPurchases.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().metricInt));

        colLIdx.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().index));
        colLTitle.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().title));
        colLArtist.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().artist));
        colLCount.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().metricInt));

        colRIdx.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().index));
        colRTitle.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().title));
        colRArtist.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().artist));
        colRAvg.setCellValueFactory(d -> new SimpleDoubleProperty(d.getValue().metricDouble));

        // Load safely
        load();
    }

    private void load() {
        try {
            List<Song> mostPurchased  = safe(Top5Manager.getTop5MostPurchased());
            List<Song> mostPlaylisted = safe(Top5Manager.getTop5MostPlaylisted());
            List<Song> bestRated      = safe(Top5Manager.getTop5HighestRated());

            tblPurchased.getItems().setAll(Row.from(mostPurchased, Row.Metric.PURCHASES));
            tblPlaylisted.getItems().setAll(Row.from(mostPlaylisted, Row.Metric.PLAYLISTS));
            tblRated.getItems().setAll(Row.from(bestRated, Row.Metric.AVG_RATING));
        } catch (Throwable t) {
            t.printStackTrace();
            FxAlerts.error("Failed to load Top 5: " + t.getMessage());
        }
    }

    private static List<Song> safe(List<Song> list) {
        return list == null ? new ArrayList<>() : list;
    }

    @FXML
    private void onClose() {
        ((Stage) root.getScene().getWindow()).close();
    }

    /** Adapter row for 3 tables. */
    static class Row {
        enum Metric { PURCHASES, PLAYLISTS, AVG_RATING }
        int index;
        String title;
        String artist;
        int metricInt;
        double metricDouble;

        static List<Row> from(List<Song> songs, Metric metric) {
            List<Row> rows = new ArrayList<>();
            int i = 1;
            for (Song s : songs) {
                Row r = new Row();
                r.index = i++;
                r.title = s.getTitle();
                r.artist = s.getArtist();
                switch (metric) {
                    case PURCHASES -> r.metricInt = s.getPurchaseCount();
                    case PLAYLISTS -> r.metricInt = s.getPlaylistCount();
                    case AVG_RATING -> r.metricDouble = s.getAverageRating();
                }
                rows.add(r);
            }
            return rows;
        }
    }
}
