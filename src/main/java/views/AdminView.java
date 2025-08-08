package views;

import controllers.AdminController;
import enums.Genre;
import models.Song;

import javax.swing.JOptionPane;
import java.util.List;

public class AdminView {

    private AdminController adminController;

    public AdminView(AdminController adminController) {
        this.adminController = adminController;
    }

    public void show() {
        String[] options = {"View Catalog", "Add Song", "Remove Song", "Logout"};
        while (true) {
            int choice = JOptionPane.showOptionDialog(null, "Admin Menu", "Admin",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0:
                    viewCatalog();
                    break;
                case 1:
                    addSong();
                    break;
                case 2:
                    removeSong();
                    break;
                case 3:
                default:
                    return;
            }
        }
    }

    public void viewCatalog() {
        List<Song> songs = adminController.getCatalogSongs();
        if (songs.isEmpty()) {
            JOptionPane.showMessageDialog(null, "The catalog is empty.");
            return;
        }

        StringBuilder sb = new StringBuilder("Catalog:\n");
        for (Song song : songs) {
            sb.append(song).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private void addSong() {
        String title = JOptionPane.showInputDialog("Enter song title:");
        String artist = JOptionPane.showInputDialog("Enter artist:");
        String priceStr = JOptionPane.showInputDialog("Enter price:");
        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid price.");
            return;
        }
        Genre genre = (Genre) JOptionPane.showInputDialog(null, "Select genre:",
                "Genre", JOptionPane.QUESTION_MESSAGE, null, Genre.values(), Genre.POP); // Case fixed here

        if (title != null && artist != null && genre != null) {
            boolean success = adminController.addSong(title, artist, price, genre);
            if (success) {
                JOptionPane.showMessageDialog(null, "Song added successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add song.");
            }
        }
    }

    private void removeSong() {
        String songId = JOptionPane.showInputDialog("Enter Song ID to remove:");
        if (songId != null) {
            boolean success = adminController.removeSong(songId);
            if (success) {
                JOptionPane.showMessageDialog(null, "Song removed successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Song not found.");
            }
        }
    }
}
