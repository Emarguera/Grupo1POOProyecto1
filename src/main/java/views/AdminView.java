package views;

import controllers.AdminController;
import enums.Genre;
import models.Song;

import javax.swing.*;
import java.util.List;

public class AdminView {

    private AdminController adminController;

    public AdminView(AdminController adminController) {
        this.adminController = adminController;
    }

    public void show() {
        String[] options = {"Show Songs", "Add Song", "Remove Song", "Exit"};
        while (true) {
            int choice = JOptionPane.showOptionDialog(null, "Admin Menu", "Admin",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                    options, options[0]);

            if (choice == 0) {
                displaySongs();
            } else if (choice == 1) {
                addSong();
            } else if (choice == 2) {
                removeSong();
            } else {
                break;
            }
        }
    }

    private void displaySongs() {
        List<Song> songs = adminController.getCatalogSongs(); // Correct method

        if (songs.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No songs available.");
            return;
        }

        StringBuilder sb = new StringBuilder("Songs List:\n");
        for (Song song : songs) {
            sb.append(song.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private void addSong() {
        try {
            String id = JOptionPane.showInputDialog("Enter Song ID (3 digits):");
            String title = JOptionPane.showInputDialog("Enter Song Title:");
            String artist = JOptionPane.showInputDialog("Enter Artist Name:");
            String priceStr = JOptionPane.showInputDialog("Enter Price:");
            String[] genres = {"REGGAE", "HIPHOP", "CLASSICAL", "ROCK", "POP", "METAL"};
            String genreStr = (String) JOptionPane.showInputDialog(null, "Select Genre:", "Genre",
                    JOptionPane.QUESTION_MESSAGE, null, genres, genres[0]);

            double price = Double.parseDouble(priceStr);
            Genre genre = Genre.valueOf(genreStr);

            boolean success = adminController.addSong(id, title, artist, price, genre);

            if (success) {
                JOptionPane.showMessageDialog(null, "Song added successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add song.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
        }
    }

    private void removeSong() {
        String id = JOptionPane.showInputDialog("Enter Song ID to remove:");
        if (id != null && !id.trim().isEmpty()) {
            boolean success = adminController.removeSong(id.trim());
            if (success) {
                JOptionPane.showMessageDialog(null, "Song removed successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to remove song.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid Song ID.");
        }
    }
}
