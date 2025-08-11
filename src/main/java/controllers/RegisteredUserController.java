package controllers;

import dao.PurchaseDAO;
import dao.PlaylistDAO;
import dao.RegisteredUserDAO;
import dao.SongDAO;
import models.Catalog;
import models.Playlist;
import models.RegisteredUser;
import models.Song;

import java.util.List;

public class RegisteredUserController {

    private RegisteredUser user;
    private Catalog catalog;
    private PurchaseDAO purchaseDAO;
    private PlaylistDAO playlistDAO;
    private SongDAO songDAO;
    private RegisteredUserDAO userDAO;

    public RegisteredUserController(RegisteredUser user) {
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
            boolean success = purchaseDAO.purchaseSong(user.getId(), songId);
            if (success) {
                user.purchaseSong(song);
                user.setBalance(user.getBalance() - song.getPrice());
                userDAO.updateRegisteredUser(user);
                song.incrementPurchase();
                songDAO.updateSong(song);
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

    public boolean createPlaylist(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        if (user.getPlaylist() != null && user.getPlaylist().getId() > 0) return false;

        Playlist p = new Playlist();
        p.setOwnerId(user.getId());
        p.setName(name);

        int generatedId = playlistDAO.createPlaylist(p);
        if (generatedId > 0) {
            user.setPlaylist(p);
            return true;
        }
        return false;
    }

    public boolean addSongToPlaylist(String songId) {
        Playlist userPlaylist = user.getPlaylist();
        if (userPlaylist == null || userPlaylist.getId() <= 0) return false;

        Song purchasedMatch = null;
        for (Song s : user.getPurchasedSongs()) {
            if (s.getId().equals(songId)) {
                purchasedMatch = s;
                break;
            }
        }
        if (purchasedMatch == null) return false;

        boolean added = userPlaylist.addSong(purchasedMatch);
        if (added) {
            boolean persisted = playlistDAO.addSongToPlaylist(userPlaylist.getId(), songId);
            if (persisted) {
                purchasedMatch.incrementPlaylistAdd();
                songDAO.updateSong(purchasedMatch);
                return true;
            } else {
                userPlaylist.removeSong(purchasedMatch);
            }
        }
        return false;
    }

    public boolean removeSongFromPlaylist(String songId) {
        Playlist userPlaylist = user.getPlaylist();
        if (userPlaylist == null || userPlaylist.getId() <= 0) return false;

        Song toRemove = null;
        for (Song s : userPlaylist.getSongs()) {
            if (s.getId().equals(songId)) {
                toRemove = s;
                break;
            }
        }
        if (toRemove == null) return false;

        boolean removed = userPlaylist.removeSong(toRemove);
        if (removed) {
            boolean persisted = playlistDAO.removeSongFromPlaylist(userPlaylist.getId(), songId);
            if (persisted) return true;
            else userPlaylist.addSong(toRemove);
        }
        return false;
    }

    public List<Song> getPurchasedSongs() {
        return user.getPurchasedSongs();
    }

    public List<Song> getPlaylistSongs() {
        Playlist p = user.getPlaylist();
        if (p == null) return List.of();
        return p.getSongs();
    }

    // New: expose balance for the view
    public double getBalance() {
        return user.getBalance();
    }
}
