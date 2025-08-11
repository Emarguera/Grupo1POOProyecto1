package views;

import controllers.AdminController;
import enums.Genre;
import models.Song;
import models.RegisteredUser;
import services.Top5Manager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdminView {

    private final AdminController adminController;

    private JFrame frame;

    // Catalog
    private JList<String> catalogList;
    private DefaultListModel<String> catalogModel;
    private JButton addSongButton;
    private JButton removeSongButton;

    // Users
    private JList<String> usersList;
    private DefaultListModel<String> usersModel;
    private JButton deleteUserButton;
    private JButton addBalanceButton;

    // Top 5
    private JButton viewTop5Button;

    private JButton logoutButton;

    private final NumberFormat currencyFmt = NumberFormat.getCurrencyInstance(Locale.US);

    public AdminView(AdminController adminController) {
        this.adminController = adminController;
        initialize();
        loadCatalog();
        loadRegisteredUsers();
    }

    private void initialize() {
        frame = new JFrame("Admin Panel - Music System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(980, 560);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        frame.setContentPane(mainPanel);

        JPanel header = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Admin Panel - Music System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        header.add(titleLabel, BorderLayout.WEST);

        // Top 5 button in the header
        JPanel headerButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        viewTop5Button = new JButton("View Top 5");
        viewTop5Button.addActionListener(e -> showTop5Dialog());
        headerButtons.add(viewTop5Button);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        headerButtons.add(logoutButton);

        header.add(headerButtons, BorderLayout.EAST);

        mainPanel.add(header, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(560);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Left: Catalog
        JPanel catalogPanel = new JPanel(new BorderLayout(6,6));
        JLabel catalogLabel = new JLabel("Songs Catalog");
        catalogLabel.setFont(new Font("Arial", Font.BOLD, 16));
        catalogPanel.add(catalogLabel, BorderLayout.NORTH);

        catalogModel = new DefaultListModel<>();
        catalogList = new JList<>(catalogModel);
        catalogList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        catalogPanel.add(new JScrollPane(catalogList), BorderLayout.CENTER);

        JPanel catalogBtns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addSongButton = new JButton("Add Song");
        removeSongButton = new JButton("Remove Selected Song");
        catalogBtns.add(addSongButton);
        catalogBtns.add(removeSongButton);
        catalogPanel.add(catalogBtns, BorderLayout.SOUTH);
        splitPane.setLeftComponent(catalogPanel);

        // Right: Users
        JPanel usersPanel = new JPanel(new BorderLayout(6,6));
        JLabel usersLabel = new JLabel("Registered Users");
        usersLabel.setFont(new Font("Arial", Font.BOLD, 16));
        usersPanel.add(usersLabel, BorderLayout.NORTH);

        usersModel = new DefaultListModel<>();
        usersList = new JList<>(usersModel);
        usersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        usersPanel.add(new JScrollPane(usersList), BorderLayout.CENTER);

        JPanel usersBtns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        deleteUserButton = new JButton("Delete Selected User");
        addBalanceButton = new JButton("Add Balance");
        logoutButton = new JButton("Logout");
        usersBtns.add(deleteUserButton);
        usersBtns.add(addBalanceButton);
        usersBtns.add(logoutButton);
        usersPanel.add(usersBtns, BorderLayout.SOUTH);

        splitPane.setRightComponent(usersPanel);

        // Actions
        addSongButton.addActionListener(e -> addSong());
        removeSongButton.addActionListener(e -> removeSelectedSong());
        deleteUserButton.addActionListener(e -> deleteSelectedUser());
        addBalanceButton.addActionListener(e -> addBalanceToSelectedUser());
        logoutButton.addActionListener(e -> logout());

        // Enable/disable
        removeSongButton.setEnabled(false);
        deleteUserButton.setEnabled(false);
        addBalanceButton.setEnabled(false);

        catalogList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                removeSongButton.setEnabled(catalogList.getSelectedIndex() != -1);
            }
        });

        usersList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean sel = usersList.getSelectedIndex() != -1;
                deleteUserButton.setEnabled(sel);
                addBalanceButton.setEnabled(sel);
            }
        });
    }

    private void loadCatalog() {
        catalogModel.clear();
        List<Song> songs = adminController.getCatalogSongs();
        for (Song song : songs) {
            catalogModel.addElement(formatSongDisplay(song));
        }
    }

    private void loadRegisteredUsers() {
        usersModel.clear();
        List<RegisteredUser> users = adminController.getRegisteredUsers();
        for (RegisteredUser u : users) {
            usersModel.addElement(String.format("%s - %s %s (%s)  | Balance: %s",
                    u.getId(), u.getName(), u.getLastName(), u.getEmail(), currencyFmt.format(u.getBalance())));
        }
    }

    private String formatSongDisplay(Song song) {
        return String.format("%s - %s [%s] %s (Genre: %s, Purchases: %d, In Playlists: %d, Avg ★: %.2f)",
                song.getId(), song.getTitle(), song.getArtist(),
                currencyFmt.format(song.getPrice()), song.getGenre(),
                song.getPurchaseCount(), song.getPlaylistCount(), song.getAverageRating());
    }

    private void addSong() {
        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField artistField = new JTextField();
        JTextField priceField = new JTextField();
        JComboBox<Genre> genreComboBox = new JComboBox<>(Genre.values());

        Object[] inputs = {
                "ID (3-digit):", idField,
                "Title:", titleField,
                "Artist:", artistField,
                "Price:", priceField,
                "Genre:", genreComboBox
        };

        int result = JOptionPane.showConfirmDialog(frame, inputs, "Add New Song", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String id = idField.getText().trim();
                String title = titleField.getText().trim();
                String artist = artistField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                Genre genre = (Genre) genreComboBox.getSelectedItem();

                if (id.isEmpty() || title.isEmpty() || artist.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean success = adminController.addSong(id, title, artist, price, genre);
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Song added successfully!");
                    loadCatalog();
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to add song. Check if ID is unique.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid price format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removeSelectedSong() {
        int idx = catalogList.getSelectedIndex();
        if (idx == -1) return;

        Song song = adminController.getCatalogSongs().get(idx);

        int confirm = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to delete the song: " + song.getTitle() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = adminController.removeSong(song.getId());
            if (success) {
                JOptionPane.showMessageDialog(frame, "Song removed successfully.");
                loadCatalog();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to remove song.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedUser() {
        int idx = usersList.getSelectedIndex();
        if (idx == -1) return;

        RegisteredUser u = adminController.getRegisteredUsers().get(idx);

        int confirm = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to delete (deactivate) user: " + u.getEmail() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = adminController.deleteRegisteredUser(u.getId());
            if (success) {
                JOptionPane.showMessageDialog(frame, "User deactivated (soft deleted).");
                loadRegisteredUsers();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to delete user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addBalanceToSelectedUser() {
        int idx = usersList.getSelectedIndex();
        if (idx == -1) return;

        RegisteredUser u = adminController.getRegisteredUsers().get(idx);

        String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to ADD to balance (e.g., 10.00):");
        if (amountStr == null) return;
        try {
            double amount = Double.parseDouble(amountStr.trim());
            if (amount < 0) {
                JOptionPane.showMessageDialog(frame, "Amount must be non-negative.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean ok = adminController.addBalanceToUser(u.getId(), amount);
            if (ok) {
                JOptionPane.showMessageDialog(frame, "Balance updated.");
                loadRegisteredUsers();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to update balance.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
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
