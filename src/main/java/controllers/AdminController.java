package controllers;

import models.Admin;
import models.Catalog;
import models.Song;

import java.util.List;

public class AdminController {

    private Admin admin;
    private Catalog catalog;

    public AdminController(Admin admin) {
        this.admin = admin;
        this.catalog = Catalog.getInstance();  // Fixed here
    }

    public List<Song> getCatalogSongs() {
        return catalog.getAllSongs();
    }

    public boolean addSong(String title, String artist, double price, enums.Genre genre) {
        return catalog.addSong(title, artist, price, genre);
    }

    public boolean removeSong(String songId) {
        return catalog.removeSong(songId);
    }
}
