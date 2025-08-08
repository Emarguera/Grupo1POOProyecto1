package views;

import controllers.FinalUserController;
import enums.Genre;
import models.Song;
import services.Top5Manager;

import javax.swing.JOptionPane;
import java.util.List;

public class RegisteredUserView {

    private FinalUserController userController;

    public RegisteredUserView(FinalUserController userController) {
        this.userController = userController;
    }

    public void show() {
        String[] options = {"View Catalog", "Buy Song", "Rate Song", "Manage Playlist", "View Top 5", "Logout"};
        while (true) {
            int choice = JOptionPane.showOptionDialog(null, "User Menu", "User",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0:
                    viewCatalog();
                    break;
                case 1:
                    buySong();
                    break;
                case 2:
                    rateSong();
                    break;
                case 3:
                    managePlaylist();
                    break;
                case 4:
                    viewTop5();
                    break;
                case 5:
                default:
                    return; // Exit to welcome or logout
            }
        }
    }

    public void viewCatalog() {
        List<Song> songs = userController.getCatalogSongs();
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

    private void buySong() {
        String songId = JOptionPane.showInputDialog("Enter Song ID to purchase:");
        if (songId != null) {
            boolean success = userController.purchaseSong(songId);
            if (success) {
                JOptionPane.showMessageDialog(null, "Song purchased successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Purchase failed. Check your balance or if you already own the song.");
            }
        }
    }

    private void rateSong() {
        String songId = JOptionPane.showInputDialog("Enter Song ID to rate:");
        if (songId == null) return;
        String ratingStr = JOptionPane.showInputDialog("Enter rating (1-5):");
        if (ratingStr == null) return;

        int rating;
        try {
            rating = Integer.parseInt(ratingStr);
            if (rating < 1 || rating > 5) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid rating.");
            return;
        }

        boolean success = userController.rateSong(songId, rating);
        if (success) {
            JOptionPane.showMessageDialog(null, "Thank you for rating!");
        } else {
            JOptionPane.showMessageDialog(null, "Rating failed. Make sure you own the song.");
        }
    }

    private void managePlaylist() {
        String[] options = {"View Playlist", "Add Song", "Remove Song", "Clear Playlist", "Back"};
        while (true) {
            int choice = JOptionPane.showOptionDialog(null, "Playlist Menu", "Playlist",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0:
                    viewPlaylist();
                    break;
                case 1:
                    addSongToPlaylist();
                    break;
                case 2:
                    removeSongFromPlaylist();
                    break;
                case 3:
                    clearPlaylist();
                    break;
                case 4:
                default:
                    return;
            }
        }
    }

    private void viewPlaylist() {
        List<Song> playlistSongs = userController.getPlaylistSongs();
        if (playlistSongs.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Your playlist is empty.");
            return;
        }
        StringBuilder sb = new StringBuilder("Your Playlist:\n");
        for (Song song : playlistSongs) {
            sb.append(song).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private void addSongToPlaylist() {
        String songId = JOptionPane.showInputDialog("Enter Song ID to add to playlist:");
        if (songId != null) {
            boolean success = userController.addSongToPlaylist(songId);
            if (success) {
                JOptionPane.showMessageDialog(null, "Song added to playlist.");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add song to playlist. Check if the song exists or is already in your playlist.");
            }
        }
    }

    private void removeSongFromPlaylist() {
        String songId = JOptionPane.showInputDialog("Enter Song ID to remove from playlist:");
        if (songId != null) {
            boolean success = userController.removeSongFromPlaylist(songId);
            if (success) {
                JOptionPane.showMessageDialog(null, "Song removed from playlist.");
            } else {
                JOptionPane.showMessageDialog(null, "Song not found in playlist.");
            }
        }
    }

    private void clearPlaylist() {
        userController.getPlaylistSongs().clear(); // Or add method in controller to clear playlist
        JOptionPane.showMessageDialog(null, "Playlist cleared.");
    }

    private void viewTop5() {
        StringBuilder sb = new StringBuilder();

        sb.append("Top 5 Most Purchased:\n");
        for (Song s : Top5Manager.getTop5MostPurchased()) {
            sb.append(s).append("\n");
        }

        sb.append("\nTop 5 Most Added to Playlist:\n");
        for (Song s : Top5Manager.getTop5MostPlaylisted()) {
            sb.append(s).append("\n");
        }

        sb.append("\nTop 5 Highest Rated:\n");
        for (Song s : Top5Manager.getTop5HighestRated()) {
            sb.append(s).append("\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }
}
