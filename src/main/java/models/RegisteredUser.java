package models;

import enums.Nationality;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a registered user who can buy songs, create playlists, etc.
 */
public class RegisteredUser extends User {

    private Nationality nationality;
    private double balance;
    private Playlist playlist;
    private List<Song> purchasedSongs;

    public RegisteredUser(String id, String name, String lastName, String email, String password, Nationality nationality) {
        super(id, name, lastName, email, password);
        this.nationality = nationality;
        this.balance = 85.99; // initial balance
        this.playlist = new Playlist();
        this.purchasedSongs = new ArrayList<>();
    }

    // Nationality
    public Nationality getNationality() {
        return nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    // Balance
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    // Playlist
    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    // Purchased Songs
    public List<Song> getPurchasedSongs() {
        return purchasedSongs;
    }

    public void purchaseSong(Song song) {
        if (!purchasedSongs.contains(song)) {
            purchasedSongs.add(song);
            song.incrementPurchase();
        }
    }
}
