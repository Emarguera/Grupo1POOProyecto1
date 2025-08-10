package models;

import enums.Genre;

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

    // Constructor for creating a new song (without stats)
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

    // Constructor overload to create Song with all fields (for reading from DB)
    public Song(String id, String title, String artist, double price, Genre genre,
                int purchaseCount, int playlistCount, int totalRating, int ratingCount) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.price = price;
        this.genre = genre;
        this.purchaseCount = purchaseCount;
        this.playlistCount = playlistCount;
        this.totalRating = totalRating;
        this.ratingCount = ratingCount;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public int getPurchaseCount() {
        return purchaseCount;
    }

    public void setPurchaseCount(int purchaseCount) {
        this.purchaseCount = purchaseCount;
    }

    public int getPlaylistCount() {
        return playlistCount;
    }

    public void setPlaylistCount(int playlistCount) {
        this.playlistCount = playlistCount;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public int getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(int totalRating) {
        this.totalRating = totalRating;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + title + " - " + artist + " ($" + price + ") - " + genre;
    }
}
