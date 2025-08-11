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

        // If user has a playlist already persisted, try to load it
        // Note: your PlaylistDAO#getPlaylistById requires an id; we create playlist when the user requests it.
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

    /**
     * Create a playlist for the user (single playlist model).
     * Returns true if created and persisted successfully.
     */
    public boolean createPlaylist(String name) {
        if (name == null || name.trim().isEmpty()) return false;

        // If user already has a playlist with id > 0 treat as already created
        if (user.getPlaylist() != null && user.getPlaylist().getId() > 0) {
            return false; // already has a playlist
        }

        // prepare playlist model and persist
        Playlist p = new Playlist();
        p.setOwnerId(user.getId());
        p.setName(name);

        int generatedId = playlistDAO.createPlaylist(p);
        if (generatedId > 0) {
            // attach to user
            user.setPlaylist(p);
            return true;
        }

        return false;
    }

    /**
     * Add a purchased song to the user's playlist.
     * Only allows songs that the user actually purchased.
     */
    public boolean addSongToPlaylist(String songId) {
        // Ensure playlist exists
        Playlist userPlaylist = user.getPlaylist();
        if (userPlaylist == null || userPlaylist.getId() <= 0) {
            // No playlist created yet
            return false;
        }

        // Make sure the song is a purchased song
        Song purchasedMatch = null;
        for (Song s : user.getPurchasedSongs()) {
            if (s.getId().equals(songId)) {
                purchasedMatch = s;
                break;
            }
        }
        if (purchasedMatch == null) {
            // song not purchased by user
            return false;
        }

        // add locally
        boolean added = userPlaylist.addSong(purchasedMatch);
        if (added) {
            boolean persisted = playlistDAO.addSongToPlaylist(userPlaylist.getId(), songId);
            if (persisted) {
                // update song stat
                purchasedMatch.incrementPlaylistAdd();
                songDAO.updateSong(purchasedMatch);
                return true;
            } else {
                // rollback local add
                userPlaylist.removeSong(purchasedMatch);
            }
        }
        return false;
    }

    public boolean removeSongFromPlaylist(String songId) {
        Playlist userPlaylist = user.getPlaylist();
        if (userPlaylist == null || userPlaylist.getId() <= 0) {
            return false;
        }

        // find corresponding Song object (preferably from playlist)
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
            if (persisted) {
                return true;
            } else {
                // rollback
                userPlaylist.addSong(toRemove);
            }
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
}
