/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.models;

import com.grupo1pooproyecto1.enums.Genre;
import com.grupo1pooproyecto1.interfaces.Playable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
      
/**
 *
 * @author estebanruiz
 */
public class Song implements Playable {
    private String title;
    private Genre genre;
    private String artist;
    private String composer;
    private LocalDate releaseDate;
    private String album;
    private double price;
    private List<Integer> ratings;
    private int purchaseCount;
    private int playlistInclusionCount;

    public Song(String title, Genre genre, String artist, String composer,
                LocalDate releaseDate, String album, double price) {
        this.title = title;
        this.genre = genre;
        this.artist = artist;
        this.composer = composer;
        this.releaseDate = releaseDate;
        this.album = album;
        this.price = price;
        this.ratings = new ArrayList<>();
        this.purchaseCount = 0;
        this.playlistInclusionCount = 0;
    }
    
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the genre
     */
    public Genre getGenre() {
        return genre;
    }

    /**
     * @param genre the genre to set
     */
    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    /**
     * @return the artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * @param artist the artist to set
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * @return the composer
     */
    public String getComposer() {
        return composer;
    }

    /**
     * @param composer the composer to set
     */
    public void setComposer(String composer) {
        this.composer = composer;
    }

    /**
     * @return the releaseDate
     */
    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    /**
     * @param releaseDate the releaseDate to set
     */
    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * @return the album
     */
    public String getAlbum() {
        return album;
    }

    /**
     * @param album the album to set
     */
    public void setAlbum(String album) {
        this.album = album;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the ratings
     */
    public List<Integer> getRatings() {
        return ratings;
    }

    /**
     * @param ratings the ratings to set
     */
    public void setRatings(List<Integer> ratings) {
        this.ratings = ratings;
    }

    /**
     * @return the purchaseCount
     */
    public int getPurchaseCount() {
        return purchaseCount;
    }

    /**
     * @param purchaseCount the purchaseCount to set
     */
    public void setPurchaseCount(int purchaseCount) {
        this.purchaseCount = purchaseCount;
    }

    /**
     * @return the playlistInclusionCount
     */
    public int getPlaylistInclusionCount() {
        return playlistInclusionCount;
    }

    /**
     * @param playlistInclusionCount the playlistInclusionCount to set
     */
    public void setPlaylistInclusionCount(int playlistInclusionCount) {
        this.playlistInclusionCount = playlistInclusionCount;
    }

    public void addRating(int rating) {
        if (rating >= 1 && rating <= 5) {
            getRatings().add(rating);
        }
    }

    public double getAverageRating() {
        if (getRatings().isEmpty()) return 0.0;
        double sum = 0;
        for (int r : getRatings()) {
            sum += r;
        }
        return sum / getRatings().size();
    }

    public void incrementPurchaseCount() {
        this.setPurchaseCount(this.getPurchaseCount() + 1);
    }

    public void incrementPlaylistInclusionCount() {
        this.setPlaylistInclusionCount(this.getPlaylistInclusionCount() + 1);
    }

    @Override
    public void play() {
        System.out.println("Playing song: " + getTitle() + " by " + getArtist());
    }
}
