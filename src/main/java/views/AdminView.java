package views;

import controllers.AdminController;
import enums.Genre;
import models.Song;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class AdminView {

    private AdminController adminController;

    private JFrame frame;
    private JList<String> catalogList;
    private DefaultListModel<String> catalogModel;

    private JButton addSongButton;
    private JButton removeSongButton;
    private JButton logoutButton;

    public AdminView(AdminController adminController) {
        this.adminController = adminController;
        initialize();
        loadCatalog();
    }

    private void initialize() {
        frame = new JFrame("Admin Panel - Music System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        frame.setContentPane(mainPanel);

        JLabel catalogLabel = new JLabel("Songs Catalog");
        catalogLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(catalogLabel, BorderLayout.NORTH);

        catalogModel = new DefaultListModel<>();
        catalogList = new JList<>(catalogModel);
        catalogList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(catalogList);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        addSongButton = new JButton("Add Song");
        removeSongButton = new JButton("Remove Selected Song");
        logoutButton = new JButton("Logout");

        buttonsPanel.add(addSongButton);
        buttonsPanel.add(removeSongButton);
        buttonsPanel.add(logoutButton);

        addSongButton.addActionListener(e -> addSong());
        removeSongButton.addActionListener(e -> removeSelectedSong());
        logoutButton.addActionListener(e -> logout());

        // Disable remove button if no selection
        removeSongButton.setEnabled(false);
        catalogList.addListSelectionListener(e -> {
            removeSongButton.setEnabled(catalogList.getSelectedIndex() != -1);
        });
    }

    private void loadCatalog() {
        catalogModel.clear();
        List<Song> songs = adminController.getCatalogSongs();
        for (Song song : songs) {
            catalogModel.addElement(formatSongDisplay(song));
        }
    }

    private String formatSongDisplay(Song song) {
        return String.format("%s - %s [%s] $%.2f (Genre: %s, Purchases: %d)", 
                song.getId(), song.getTitle(), song.getArtist(), song.getPrice(), song.getGenre(), song.getPurchaseCount());
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
        int selectedIndex = catalogList.getSelectedIndex();
        if (selectedIndex == -1) return;

        Song song = adminController.getCatalogSongs().get(selectedIndex);

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

    private void logout() {
        frame.dispose();
        WelcomeView welcomeView = new WelcomeView();
        welcomeView.show();
    }

    public void show() {
        frame.setVisible(true);
    }
}
