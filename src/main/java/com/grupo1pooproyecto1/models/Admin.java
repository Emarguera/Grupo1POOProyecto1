/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.models;

import com.grupo1pooproyecto1.utils.Image;
import com.grupo1pooproyecto1.utils.Name;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author estebanruiz
 */
public class Admin extends User {
    private Name fullName;
    private String idNumber;
    private Image avatar;
    private double balance;
    private List<Song> songCollection;
    private List<Playlist> playlists;

    public Admin(String username, String email, String password, Name fullName, String idNumber, Image avatar) {
        super(username, email, password);
        this.fullName = fullName;
        this.idNumber = idNumber;
        this.avatar = avatar;
        this.balance = 2.99; //Bonus de Ingreso
        this.songCollection = new ArrayList<>();
        this.playlists = new ArrayList<>();
    }
    
    /**
     * @return the fullName
     */
    public Name getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(Name fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the idNumber
     */
    public String getIdNumber() {
        return idNumber;
    }

    /**
     * @param idNumber the idNumber to set
     */
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    /**
     * @return the avatar
     */
    public Image getAvatar() {
        return avatar;
    }

    /**
     * @param avatar the avatar to set
     */
    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    /**
     * @return the balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * @return the songCollection
     */
    public List<Song> getSongCollection() {
        return songCollection;
    }

    /**
     * @param songCollection the songCollection to set
     */
    public void setSongCollection(List<Song> songCollection) {
        this.songCollection = songCollection;
    }

    /**
     * @return the playlists
     */
    public List<Playlist> getPlaylists() {
        return playlists;
    }

    /**
     * @param playlists the playlists to set
     */
    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public void register() {
        System.out.println("Admin " + username + " has registered.");
    }

    public boolean buySong(Song song) {
        if (balance >= song.getAverageRating()) {
            songCollection.add(song);
            balance -= song.getAverageRating();
            song.incrementPurchaseCount();         // Track the purchase
            return true;
        }
        return false;
    }

    public void previewSong(Song song) {
        System.out.println("Previewing: " + song.getTitle());
    }

    public void rateSong(Song song, int rating) {
        song.addRating(rating);
    }

    public void createPlaylist(String name) {
        getPlaylists().add(new Playlist(name));
    }

    public void rechargeBalance(double amount) {
        if (amount > 0) {
            setBalance(getBalance() + amount);
        }
    }

    public void uploadSong(Song song) {
        getSongCollection().add(song);
    }

    public boolean eliminateSong(Song song) {
        return getSongCollection().remove(song);
    }
}
