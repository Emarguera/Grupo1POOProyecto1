package views;

import controllers.FinalUserController;
import enums.Genre;
import models.Song;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RegisteredUserView {

    private FinalUserController userController;

    private JFrame frame;
    private JList<String> catalogList;
    private DefaultListModel<String> catalogModel;

    private JList<String> purchasedList;
    private DefaultListModel<String> purchasedModel;

    private JList<String> playlistList;
    private DefaultListModel<String> playlistModel;

    private JButton purchaseButton;
    private JButton addToPlaylistButton;
    private JButton removeFromPlaylistButton;
    private JButton rateButton;
    private JButton logoutButton;

    public RegisteredUserView(FinalUserController userController) {
        this.userController = userController;
        initialize();
        loadCatalog();
        loadPurchasedSongs();
        loadPlaylistSongs();
    }

    private void initialize() {
        frame = new JFrame("Registered User - Music System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);

        // Layout split pane for catalog on left, user controls on right
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(350);
        frame.getContentPane().add(splitPane);

        // Catalog panel (left)
        JPanel catalogPanel = new JPanel(new BorderLayout(5, 5));
        catalogPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        splitPane.setLeftComponent(catalogPanel);

        JLabel catalogLabel = new JLabel("Song Catalog");
        catalogLabel.setFont(new Font("Arial", Font.BOLD, 16));
        catalogPanel.add(catalogLabel, BorderLayout.NORTH);

        catalogModel = new DefaultListModel<>();
        catalogList = new JList<>(catalogModel);
        catalogList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane catalogScroll = new JScrollPane(catalogList);
        catalogPanel.add(catalogScroll, BorderLayout.CENTER);

        // Right panel for purchased songs and playlist management
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        splitPane.setRightComponent(rightPanel);

        // Purchased Songs section
        JLabel purchasedLabel = new JLabel("Purchased Songs");
        purchasedLabel.setFont(new Font("Arial", Font.BOLD, 14));
        rightPanel.add(purchasedLabel);

        purchasedModel = new DefaultListModel<>();
        purchasedList = new JList<>(purchasedModel);
        purchasedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane purchasedScroll = new JScrollPane(purchasedList);
        purchasedScroll.setPreferredSize(new Dimension(400, 150));
        rightPanel.add(purchasedScroll);

        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Playlist section
        JLabel playlistLabel = new JLabel("Playlist");
        playlistLabel.setFont(new Font("Arial", Font.BOLD, 14));
        rightPanel.add(playlistLabel);

        playlistModel = new DefaultListModel<>();
        playlistList = new JList<>(playlistModel);
        playlistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane playlistScroll = new JScrollPane(playlistList);
        playlistScroll.setPreferredSize(new Dimension(400, 150));
        rightPanel.add(playlistScroll);

        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rightPanel.add(buttonsPanel);

        purchaseButton = new JButton("Purchase Selected Song");
        addToPlaylistButton = new JButton("Add to Playlist");
        removeFromPlaylistButton = new JButton("Remove from Playlist");
        rateButton = new JButton("Rate Purchased Song");
        logoutButton = new JButton("Logout");

        buttonsPanel.add(purchaseButton);
        buttonsPanel.add(addToPlaylistButton);
        buttonsPanel.add(removeFromPlaylistButton);
        buttonsPanel.add(rateButton);
        buttonsPanel.add(logoutButton);

        // Button actions
        purchaseButton.addActionListener(e -> purchaseSelectedSong());
        addToPlaylistButton.addActionListener(e -> addSelectedSongToPlaylist());
        removeFromPlaylistButton.addActionListener(e -> removeSelectedSongFromPlaylist());
        rateButton.addActionListener(e -> ratePurchasedSong());
        logoutButton.addActionListener(e -> logout());

        // Disable buttons initially if nothing selected
        purchaseButton.setEnabled(false);
        addToPlaylistButton.setEnabled(false);
        removeFromPlaylistButton.setEnabled(false);
        rateButton.setEnabled(false);

        // List selection listeners to enable buttons appropriately
        catalogList.addListSelectionListener(e -> {
            boolean selected = catalogList.getSelectedIndex() != -1;
            purchaseButton.setEnabled(selected);
            addToPlaylistButton.setEnabled(selected);
        });

        purchasedList.addListSelectionListener(e -> {
            boolean selected = purchasedList.getSelectedIndex() != -1;
            rateButton.setEnabled(selected);
        });

        playlistList.addListSelectionListener(e -> {
            boolean selected = playlistList.getSelectedIndex() != -1;
            removeFromPlaylistButton.setEnabled(selected);
        });
    }

    private void loadCatalog() {
        catalogModel.clear();
        List<Song> songs = userController.getCatalogSongs();
        for (Song song : songs) {
            catalogModel.addElement(formatSongDisplay(song));
        }
    }

    private void loadPurchasedSongs() {
        purchasedModel.clear();
        List<Song> purchased = userController.getPurchasedSongs();
        for (Song song : purchased) {
            purchasedModel.addElement(formatSongDisplay(song));
        }
    }

    private void loadPlaylistSongs() {
        playlistModel.clear();
        List<Song> playlistSongs = userController.getPlaylistSongs();
        for (Song song : playlistSongs) {
            playlistModel.addElement(formatSongDisplay(song));
        }
    }

    private String formatSongDisplay(Song song) {
        return String.format("%s - %s [%s] $%.2f (Rating: %.2f)", 
            song.getId(), song.getTitle(), song.getArtist(), song.getPrice(), song.getAverageRating());
    }

    private void purchaseSelectedSong() {
        int index = catalogList.getSelectedIndex();
        if (index == -1) return;
        Song song = userController.getCatalogSongs().get(index);

        if (userController.purchaseSong(song.getId())) {
            JOptionPane.showMessageDialog(frame, "Song purchased successfully!");
            loadPurchasedSongs();
            loadCatalog(); // To reflect purchase count update if any
        } else {
            JOptionPane.showMessageDialog(frame, "Purchase failed. Maybe insufficient balance or already purchased.");
        }
    }

    private void addSelectedSongToPlaylist() {
        int index = catalogList.getSelectedIndex();
        if (index == -1) return;
        Song song = userController.getCatalogSongs().get(index);

        if (userController.addSongToPlaylist(song.getId())) {
            JOptionPane.showMessageDialog(frame, "Song added to playlist!");
            loadPlaylistSongs();
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to add song to playlist.");
        }
    }

    private void removeSelectedSongFromPlaylist() {
        int index = playlistList.getSelectedIndex();
        if (index == -1) return;
        Song song = userController.getPlaylistSongs().get(index);

        if (userController.removeSongFromPlaylist(song.getId())) {
            JOptionPane.showMessageDialog(frame, "Song removed from playlist!");
            loadPlaylistSongs();
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to remove song from playlist.");
        }
    }

    private void ratePurchasedSong() {
        int index = purchasedList.getSelectedIndex();
        if (index == -1) return;
        Song song = userController.getPurchasedSongs().get(index);

        String ratingStr = JOptionPane.showInputDialog(frame, "Enter rating (1-5):");
        if (ratingStr == null) return; // cancelled
        try {
            int rating = Integer.parseInt(ratingStr);
            if (rating < 1 || rating > 5) {
                JOptionPane.showMessageDialog(frame, "Rating must be between 1 and 5.");
                return;
            }
            if (userController.rateSong(song.getId(), rating)) {
                JOptionPane.showMessageDialog(frame, "Rating submitted!");
                loadCatalog(); // update average ratings in catalog
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to submit rating.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid rating input.");
        }
    }

    private void logout() {
        frame.dispose();
        WelcomeView welcomeView = new WelcomeView();
        welcomeView.show();
    }

    public void show() {
        frame.setVisible(true);
    }
}
