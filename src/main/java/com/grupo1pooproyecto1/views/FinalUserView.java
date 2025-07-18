/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.views;

import com.grupo1pooproyecto1.controllers.FinalUserController;
import com.grupo1pooproyecto1.models.Playlist;
import com.grupo1pooproyecto1.models.Song;

import java.util.List;
import javax.swing.*;
/**
 *
 * @author Emarguera
 */
public class FinalUserView {
    private FinalUserController controller;

    public FinalUserView(FinalUserController controller) {
        this.controller = controller;
    }

    public void displayUserMenu() {
        boolean running = true;

        while (running) {
            // Build menu + catalog preview
            StringBuilder content = new StringBuilder("🎧 Final User Menu:\n");
            content.append("1. View Catalog\n");
            content.append("2. Buy Song\n");
            content.append("3. Rate Song\n");
            content.append("4. Create Playlist\n");
            content.append("5. Add Song to Playlist\n");
            content.append("6. View Top 5\n");
            content.append("7. View Profile\n");
            content.append("0. Exit\n\n");

            List<Song> songs = controller.getCatalogSongs();
            if (!songs.isEmpty()) {
                content.append("🎵 Catalog Preview:\n");
                int i = 1;
                for (Song s : songs) {
                    content.append(String.format("%d. %s - %s ($%.2f, %.1f★)\n",
                            i++, s.getTitle(), s.getArtist(), s.getPrice(), s.getAverageRating()));
                }
            }

            // Create scrollable panel
            JTextArea textArea = new JTextArea(content.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);

            String input = JOptionPane.showInputDialog(null, scrollPane, "Final User Dashboard", JOptionPane.PLAIN_MESSAGE);
            if (input == null || input.equals("0")) {
                JOptionPane.showMessageDialog(null, "👋 Goodbye!");
                break;
            }

            try {
                switch (input) {
                    case "1" -> controller.showCatalog();
                    case "2" -> handleBuySong();
                    case "3" -> handleRateSong();
                    case "4" -> handleCreatePlaylist();
                    case "5" -> handleAddSongToPlaylist();
                    case "6" -> controller.showTop5();
                    case "7" -> controller.showUserProfile();
                    default -> JOptionPane.showMessageDialog(null, "⚠ Invalid option.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "⚠ Error: " + e.getMessage());
            }
        }
    }

    private void handleBuySong() {
        try {
            int index = Integer.parseInt(JOptionPane.showInputDialog("Enter song number to buy:"));
            boolean result = controller.buySong(index);
            JOptionPane.showMessageDialog(null,
                    result ? "✅ Song purchased successfully." : "❌ Purchase failed.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "⚠ Invalid input.");
        }
    }

    private void handleRateSong() {
        try {
            int songIndex = Integer.parseInt(JOptionPane.showInputDialog("Enter song number to rate:"));
            int rating = Integer.parseInt(JOptionPane.showInputDialog("Enter rating (1–5):"));
            boolean result = controller.rateSong(songIndex, rating);
            JOptionPane.showMessageDialog(null,
                    result ? "✅ Rating submitted." : "❌ Failed to rate song.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "⚠ Invalid input.");
        }
    }

    private void handleCreatePlaylist() {
        String name = JOptionPane.showInputDialog("Enter playlist name:");
        if (name != null && !name.trim().isEmpty()) {
            controller.createPlaylist(name);
            JOptionPane.showMessageDialog(null, "✅ Playlist created.");
        } else {
            JOptionPane.showMessageDialog(null, "⚠ Playlist name cannot be empty.");
        }
    }

    private void handleAddSongToPlaylist() {
        List<Playlist> playlists = controller.getUserPlaylists();
        if (playlists.isEmpty()) {
            JOptionPane.showMessageDialog(null, "⚠ You have no playlists.");
            return;
        }

        StringBuilder menu = new StringBuilder("📂 Your Playlists:\n");
        for (int i = 0; i < playlists.size(); i++) {
            menu.append(i + 1).append(". ").append(playlists.get(i).getName()).append("\n");
        }

        try {
            int pIndex = Integer.parseInt(JOptionPane.showInputDialog(menu + "Enter playlist number:"));
            int sIndex = Integer.parseInt(JOptionPane.showInputDialog("Enter song number to add:"));
            boolean result = controller.addSongToPlaylist(pIndex, sIndex);
            JOptionPane.showMessageDialog(null,
                    result ? "✅ Song added to playlist." : "❌ Could not add song.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "⚠ Invalid input.");
        }
    }
}
