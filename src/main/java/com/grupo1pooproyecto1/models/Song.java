/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.models;

import com.grupo1pooproyecto1.enums.Genre;
import com.grupo1pooproyecto1.interfaces.Playable;

import java.util.ArrayList;
import java.util.List;
      
/**
 *
 * @author estebanruiz
 */
public class Song implements Playable {


    private int songId;
    private String title;
    private Genre genre;
    private String artist;
    private String album;
    private double price;
    private List<Integer> ratings;
    private int purchaseCount;
    private int playlistInclusionCount;

    public Song(int songId,String title, Genre genre, String artist, String album, double price,int purchaseCount, int playlistInclusionCount) {
        this.songId = songId;
        this.title = title;
        this.genre = genre;
        this.artist = artist;
        this.album = album;
        this.price = price;
        this.ratings = new ArrayList<>();
        this.purchaseCount = 0;
        this.playlistInclusionCount = 0;
    }
    
public Song() {

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
        /**
     * @return the songId
     */
    public int getSongId() {
        return songId;
    }

    /**
     * @param songId the songId to set
     */
    public void setSongId(int songId) {
        this.songId = songId;
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