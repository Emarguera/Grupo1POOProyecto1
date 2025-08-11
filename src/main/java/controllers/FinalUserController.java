package controllers;

import models.RegisteredUser;
import models.Song;

import java.util.List;

/**
 * Adapter so code that constructs FinalUserController(...) keeps working.
 * Delegates to RegisteredUserController which contains the real implementation.
 */
public class FinalUserController {

    private RegisteredUserController delegate;

    public FinalUserController(RegisteredUser user) {
        this.delegate = new RegisteredUserController(user);
    }

    // Delegate methods used by RegisteredUserView
    public List<Song> getCatalogSongs() {
        return delegate.getCatalogSongs();
    }

    public boolean purchaseSong(String songId) {
        return delegate.purchaseSong(songId);
    }

    public boolean addSongToPlaylist(String songId) {
        return delegate.addSongToPlaylist(songId);
    }

    public boolean removeSongFromPlaylist(String songId) {
        return delegate.removeSongFromPlaylist(songId);
    }

    public boolean rateSong(String songId, int rating) {
        return delegate.rateSong(songId, rating);
    }

    public List<Song> getPurchasedSongs() {
        return delegate.getPurchasedSongs();
    }

    public List<Song> getPlaylistSongs() {
        return delegate.getPlaylistSongs();
    }

    // New: create playlist
    public boolean createPlaylist(String name) {
        return delegate.createPlaylist(name);
    }
}
