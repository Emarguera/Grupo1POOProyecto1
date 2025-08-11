package controllers;

import dao.SongDAO;
import dao.RegisteredUserDAO;
import models.Admin;
import models.Catalog;
import models.Song;
import models.RegisteredUser;
import enums.Genre;

import java.util.List;

public class AdminController {

    private final Admin admin;
    private final Catalog catalog;
    private final SongDAO songDAO;
    private final RegisteredUserDAO registeredUserDAO;

    public AdminController(Admin admin) {
        this.admin = admin;
        this.catalog = Catalog.getInstance();
        this.songDAO = new SongDAO();
        this.registeredUserDAO = new RegisteredUserDAO();
    }

    // Catalog
    public List<Song> getCatalogSongs() {
        return catalog.getAllSongs();
    }

    public boolean addSong(String id, String title, String artist, double price, Genre genre) {
        Song newSong = new Song(id, title, artist, price, genre);
        boolean created = songDAO.createSong(newSong);
        if (created) catalog.loadSongsFromDB();
        return created;
    }

    public boolean removeSong(String songId) {
        boolean deleted = songDAO.deleteSong(songId);
        if (deleted) catalog.loadSongsFromDB();
        return deleted;
    }

    // Registered users (active)
    public List<RegisteredUser> getRegisteredUsers() {
        return registeredUserDAO.getAllRegisteredUsers(); // active only
    }

    public boolean deleteRegisteredUser(String id) {
        // soft-delete via package
        return registeredUserDAO.deleteRegisteredUser(id);
    }

    // Add balance to a registered user (additive)
    public boolean addBalanceToUser(String userId, double amountToAdd) {
        if (amountToAdd < 0) return false;
        RegisteredUser u = registeredUserDAO.getRegisteredUserById(userId);
        if (u == null) return false;
        u.setBalance(u.getBalance() + amountToAdd);
        return registeredUserDAO.updateRegisteredUser(u);
    }
}
