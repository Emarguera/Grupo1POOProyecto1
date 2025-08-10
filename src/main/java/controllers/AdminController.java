package controllers;

import dao.SongDAO;
import models.Admin;
import models.Catalog;
import models.Song;
import enums.Genre;

import java.util.List;

public class AdminController {

    private Admin admin;
    private Catalog catalog;
    private SongDAO songDAO;

    public AdminController(Admin admin) {
        this.admin = admin;
        this.catalog = Catalog.getInstance();
        this.songDAO = new SongDAO();
    }

    public List<Song> getCatalogSongs() {
        return catalog.getAllSongs();
    }

    public boolean addSong(String id, String title, String artist, double price, Genre genre) {
        Song newSong = new Song(id, title, artist, price, genre);
        boolean created = songDAO.createSong(newSong);
        if (created) {
            // Reload catalog songs from DB after adding a new song
            catalog.loadSongsFromDB();
        }
        return created;
    }

    public boolean removeSong(String songId) {
        boolean deleted = songDAO.deleteSong(songId);
        if (deleted) {
            catalog.loadSongsFromDB();
        }
        return deleted;
    }
}
