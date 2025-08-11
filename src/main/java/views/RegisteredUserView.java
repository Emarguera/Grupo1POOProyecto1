package views;

import controllers.FinalUserController;
import models.Song;
import services.Top5Manager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RegisteredUserView {

    private FinalUserController userController;

    private JFrame frame;
    private JLabel balanceLabel;

    private JList<String> catalogList;
    private DefaultListModel<String> catalogModel;

    private JList<String> purchasedList;
    private DefaultListModel<String> purchasedModel;

    private JList<String> playlistList;
    private DefaultListModel<String> playlistModel;

    private JButton purchaseButton;
    private JButton createPlaylistButton;
    private JButton addToPlaylistButton;
    private JButton removeFromPlaylistButton;
    private JButton rateButton;
    private JButton viewTop5Button;
    private JButton logoutButton;

    private final NumberFormat currencyFmt = NumberFormat.getCurrencyInstance(Locale.US);

    public RegisteredUserView(FinalUserController userController) {
        this.userController = userController;
        initialize();
        loadCatalog();
        loadPurchasedSongs();
        loadPlaylistSongs();
        refreshBalance();
    }

    private void initialize() {
        frame = new JFrame("Registered User - Music System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(980, 620);
        frame.setLocationRelativeTo(null);

        // Layout split pane for catalog on left, user controls on right
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(360);
        frame.getContentPane().add(splitPane);

        // Catalog panel (left)
        JPanel catalogPanel = new JPanel(new BorderLayout(5, 5));
        catalogPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        splitPane.setLeftComponent(catalogPanel);

        JPanel leftHeader = new JPanel(new BorderLayout());
        JLabel catalogLabel = new JLabel("Song Catalog");
        catalogLabel.setFont(new Font("Arial", Font.BOLD, 16));
        leftHeader.add(catalogLabel, BorderLayout.WEST);

        // View Top 5 button in left header
        viewTop5Button = new JButton("View Top 5");
        viewTop5Button.addActionListener(e -> showTop5Dialog());
        leftHeader.add(viewTop5Button, BorderLayout.EAST);

        catalogPanel.add(leftHeader, BorderLayout.NORTH);

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

        // Balance header
        JPanel balancePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        balanceLabel = new JLabel("Balance: " + currencyFmt.format(0));
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        balancePanel.add(balanceLabel);
        rightPanel.add(balancePanel);

        // Purchased Songs section
        JLabel purchasedLabel = new JLabel("Purchased Songs");
        purchasedLabel.setFont(new Font("Arial", Font.BOLD, 14));
        rightPanel.add(purchasedLabel);

        purchasedModel = new DefaultListModel<>();
        purchasedList = new JList<>(purchasedModel);
        purchasedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane purchasedScroll = new JScrollPane(purchasedList);
        purchasedScroll.setPreferredSize(new Dimension(420, 150));
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
        playlistScroll.setPreferredSize(new Dimension(420, 150));
        rightPanel.add(playlistScroll);

        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rightPanel.add(buttonsPanel);

        purchaseButton = new JButton("Purchase Selected Song");
        createPlaylistButton = new JButton("Create Playlist");
        addToPlaylistButton = new JButton("Add Purchased Song to Playlist");
        removeFromPlaylistButton = new JButton("Remove from Playlist");
        rateButton = new JButton("Rate Purchased Song");
        logoutButton = new JButton("Logout");

        buttonsPanel.add(purchaseButton);
        buttonsPanel.add(createPlaylistButton);
        buttonsPanel.add(addToPlaylistButton);
        buttonsPanel.add(removeFromPlaylistButton);
        buttonsPanel.add(rateButton);
        buttonsPanel.add(logoutButton);

        // Button actions
        purchaseButton.addActionListener(e -> purchaseSelectedSong());
        createPlaylistButton.addActionListener(e -> createPlaylist());
        addToPlaylistButton.addActionListener(e -> addSelectedSongToPlaylist());
        removeFromPlaylistButton.addActionListener(e -> removeSelectedSongFromPlaylist());
        rateButton.addActionListener(e -> ratePurchasedSong());
        logoutButton.addActionListener(e -> logout());

        // Disable buttons initially if nothing selected
        purchaseButton.setEnabled(false);
        createPlaylistButton.setEnabled(true);
        addToPlaylistButton.setEnabled(false);
        removeFromPlaylistButton.setEnabled(false);
        rateButton.setEnabled(false);

        // List selection listeners to enable buttons appropriately
        catalogList.addListSelectionListener(e -> {
            boolean selected = catalogList.getSelectedIndex() != -1;
            purchaseButton.setEnabled(selected);
        });

        purchasedList.addListSelectionListener(e -> {
            boolean selected = purchasedList.getSelectedIndex() != -1;
            rateButton.setEnabled(selected);
            addToPlaylistButton.setEnabled(selected);
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

    private void refreshBalance() {
        double bal = userController.getBalance();
        balanceLabel.setText("Balance: " + currencyFmt.format(bal));
    }

    private String formatSongDisplay(Song song) {
        return String.format("%s - %s [%s] %s (Avg ★: %.2f)",
                song.getId(), song.getTitle(), song.getArtist(), currencyFmt.format(song.getPrice()), song.getAverageRating());
    }

    private void purchaseSelectedSong() {
        int index = catalogList.getSelectedIndex();
        if (index == -1) return;
        Song song = userController.getCatalogSongs().get(index);

        if (userController.purchaseSong(song.getId())) {
            JOptionPane.showMessageDialog(frame, "Song purchased successfully!");
            loadPurchasedSongs();
            loadCatalog(); // reflect purchase count update
            refreshBalance(); // reflect new balance
        } else {
            JOptionPane.showMessageDialog(frame, "Purchase failed. Maybe insufficient balance or already purchased.");
        }
    }

    private void createPlaylist() {
        String name = JOptionPane.showInputDialog(frame, "Enter playlist name:");
        if (name == null) return; // cancelled
        if (name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Playlist name cannot be empty.");
            return;
        }
        if (userController.createPlaylist(name)) {
            JOptionPane.showMessageDialog(frame, "Playlist created!");
            loadPlaylistSongs();
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to create playlist (maybe one already exists).");
        }
    }

    private void addSelectedSongToPlaylist() {
        int index = purchasedList.getSelectedIndex();
        if (index == -1) return;
        Song song = userController.getPurchasedSongs().get(index);

        if (userController.addSongToPlaylist(song.getId())) {
            JOptionPane.showMessageDialog(frame, "Song added to playlist!");
            loadPlaylistSongs();
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to add song to playlist. Make sure you created a playlist first and only add purchased songs.");
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

    private void showTop5Dialog() {
        List<Song> mostPurchased = Top5Manager.getTop5MostPurchased();
        List<Song> mostPlaylisted = Top5Manager.getTop5MostPlaylisted();
        List<Song> highestRated = Top5Manager.getTop5HighestRated();

        StringBuilder sb = new StringBuilder();
        sb.append("Top 5 Most Purchased:\n");
        appendSongList(sb, mostPurchased, true, false, false);
        sb.append("\nTop 5 Most Added to Playlists:\n");
        appendSongList(sb, mostPlaylisted, false, true, false);
        sb.append("\nTop 5 Best Rated:\n");
        appendSongList(sb, highestRated, false, false, true);

        JTextArea area = new JTextArea(sb.toString(), 20, 65);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area);
        JOptionPane.showMessageDialog(frame, scroll, "Top 5", JOptionPane.INFORMATION_MESSAGE);
    }

    private void appendSongList(StringBuilder sb, List<Song> songs, boolean showPurch, boolean showPlaylist, boolean showRating) {
        int i = 1;
        for (Song s : songs) {
            sb.append(String.format("%d) %s - %s | %s", i++, s.getTitle(), s.getArtist(), currencyFmt.format(s.getPrice())));
            if (showPurch) sb.append(" | Purchases: ").append(s.getPurchaseCount());
            if (showPlaylist) sb.append(" | In Playlists: ").append(s.getPlaylistCount());
            if (showRating) sb.append(String.format(" | Avg ★: %.2f (%d votes)", s.getAverageRating(), s.getRatingCount()));
            sb.append("\n");
        }
        if (songs.isEmpty()) sb.append("(no data)\n");
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
