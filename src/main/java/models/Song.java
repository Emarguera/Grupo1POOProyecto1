package models;

import enums.Genre;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Song in the system.
 */
public class Song {

    private String id; // 3-digit internal code
    private String title;
    private String artist;
    private double price;
    private Genre genre;

    private int purchaseCount;
    private int playlistCount;
    private int totalRating;
    private int ratingCount;

    public Song(String id, String title, String artist, double price, Genre genre) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.price = price;
        this.genre = genre;
        this.purchaseCount = 0;
        this.playlistCount = 0;
        this.totalRating = 0;
        this.ratingCount = 0;
    }

    public void addRating(int rating) {
        if (rating >= 1 && rating <= 5) {
            totalRating += rating;
            ratingCount++;
        }
    }

    public double getAverageRating() {
        if (ratingCount == 0) return 0;
        return (double) totalRating / ratingCount;
    }

    public void incrementPurchase() {
        purchaseCount++;
    }

    public void incrementPlaylistAdd() {
        playlistCount++;
    }

    // Getters & Setters

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public double getPrice() {
        return price;
    }

    public Genre getGenre() {
        return genre;
    }

    public int getPurchaseCount() {
        return purchaseCount;
    }

    public int getPlaylistCount() {
        return playlistCount;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public int getTotalRating() {
        return totalRating;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + title + " - " + artist + " ($" + price + ") - " + genre;
    }
}
