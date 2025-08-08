package controllers;

import models.Catalog;
import models.RegisteredUser;
import models.Song;
import services.Top5Manager;

import java.util.List;

public class FinalUserController {

    private RegisteredUser user;
    private Catalog catalog;

    public FinalUserController(RegisteredUser user) {
        this.user = user;
        this.catalog = Catalog.getInstance();  // Fixed here
    }

    public List<Song> getCatalogSongs() {
        return catalog.getAllSongs();
    }

    public boolean purchaseSong(String songId) {
        Song song = catalog.findById(songId);
        if (song != null && !user.getPurchasedSongs().contains(song) && user.getBalance() >= song.getPrice()) {
            user.purchaseSong(song);
            user.setBalance(user.getBalance() - song.getPrice());

            // Instead of Top5Manager.recordPurchase(), call directly:
            song.incrementPurchase();

            return true;
        }
        return false;
    }

    public boolean rateSong(String songId, int rating) {
        Song song = catalog.findById(songId);
        if (song != null && user.getPurchasedSongs().contains(song)) {
            song.addRating(rating);

            // Instead of Top5Manager.recordRating()
            // No separate method needed since rating is added directly on song

            return true;
        }
        return false;
    }

    public boolean addSongToPlaylist(String songId) {
        Song song = catalog.findById(songId);
        if (song != null) {
            boolean added = user.getPlaylist().addSong(song);
            if (added) {
                // Instead of Top5Manager.recordPlaylistAdd()
                song.incrementPlaylistAdd();
            }
            return added;
        }
        return false;
    }

    public boolean removeSongFromPlaylist(String songId) {
        Song song = catalog.findById(songId);
        if (song != null) {
            return user.getPlaylist().removeSong(song);
        }
        return false;
    }

    public List<Song> getPurchasedSongs() {
        return user.getPurchasedSongs();
    }

    public List<Song> getPlaylistSongs() {
        return user.getPlaylist().getSongs();
    }
}
