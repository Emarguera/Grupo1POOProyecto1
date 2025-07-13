/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.models;

import com.grupo1pooproyecto1.interfaces.Playable;

/**
 *
 * @author estebanruiz
 */
public class User {
    protected String username;
    protected String email;
    protected String password;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean login(String inputPassword) {
        return this.getPassword().equals(inputPassword);
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (this.getPassword().equals(oldPassword)) {
            this.setPassword(newPassword);
            return true;
        }
        return false;
    }

    public void searchSongs(String criteria) {
        System.out.println(getUsername() + " is searching for: " + criteria);
    }

    public void searchPlaylist(String name) {
        System.out.println(getUsername() + " is searching for playlist: " + name);
    }

    public void playSong(Song song) {
        song.play();
    }

    public void playPlaylist(Playable playlist) {
        playlist.play();
    }
}
