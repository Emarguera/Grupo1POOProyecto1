package controllers;

import dao.PurchaseDAO;
import dao.PlaylistDAO;
import dao.RegisteredUserDAO;
import dao.SongDAO;

import models.Catalog;
import models.RegisteredUser;
import models.Song;

import java.util.List;

public class FinalUserController {

    private RegisteredUser user;
    private Catalog catalog;
    private PurchaseDAO purchaseDAO;
    private PlaylistDAO playlistDAO;
    private SongDAO songDAO;
    private RegisteredUserDAO userDAO;

    public FinalUserController(RegisteredUser user) {
        this.user = user;
        this.catalog = Catalog.getInstance();

        this.purchaseDAO = new PurchaseDAO();
        this.playlistDAO = new PlaylistDAO();
        this.songDAO = new SongDAO();
        this.userDAO = new RegisteredUserDAO();

        // Load user's purchased songs from DB on initialization
        loadPurchasedSongs();
    }

    private void loadPurchasedSongs() {
        List<Song> purchasedSongsFromDb = purchaseDAO.listPurchasedSongs(user.getId());
        user.setPurchasedSongs(purchasedSongsFromDb);
    }

    public List<Song> getCatalogSongs() {
        return catalog.getAllSongs();
    }

    public boolean purchaseSong(String songId) {
        Song song = catalog.findById(songId);
        if (song != null && !user.getPurchasedSongs().contains(song) && user.getBalance() >= song.getPrice()) {
            // Persist purchase in DB
            boolean success = purchaseDAO.purchaseSong(user.getId(), songId);
            if (success) {
                user.purchaseSong(song);               // update local state
                user.setBalance(user.getBalance() - song.getPrice());
                userDAO.updateRegisteredUser(user);   // persist updated balance

                song.incrementPurchase();
                songDAO.updateSong(song);              // persist updated purchase count

                return true;
            }
        }
        return false;
    }

    public boolean rateSong(String songId, int rating) {
        Song song = catalog.findById(songId);
        if (song != null && user.getPurchasedSongs().contains(song)) {
            song.addRating(rating);
            return songDAO.updateSong(song);
        }
        return false;
    }

    public boolean addSongToPlaylist(String songId) {
        Song song = catalog.findById(songId);
        if (song != null) {
            boolean added = user.getPlaylist().addSong(song);
            if (added) {
                boolean persisted = playlistDAO.addSongToPlaylist(user.getPlaylist().getId(), songId);
                if (persisted) {
                    song.incrementPlaylistAdd();
                    songDAO.updateSong(song);
                    return true;
                } else {
                    user.getPlaylist().removeSong(song); // rollback local add
                }
            }
        }
        return false;
    }

    public boolean removeSongFromPlaylist(String songId) {
        Song song = catalog.findById(songId);
        if (song != null) {
            boolean removed = user.getPlaylist().removeSong(song);
            if (removed) {
                boolean persisted = playlistDAO.removeSongFromPlaylist(user.getPlaylist().getId(), songId);
                if (persisted) {
                    return true;
                } else {
                    user.getPlaylist().addSong(song); // rollback local remove
                }
            }
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
