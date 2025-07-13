/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.models;

import com.grupo1pooproyecto1.interfaces.Playable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author estebanruiz
 */
public class Playlist implements Playable {
    private String name;
    private List<Song> songs;
    private LocalDate creationDate;

    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
        this.creationDate = LocalDate.now();
    }

    public boolean addSong(Song song) {
        if (!songs.contains(song)) {
            songs.add(song);
            song.incrementPlaylistInclusionCount();   // Track playlist inclusion
            return true;
        }
        return false;
    }

    public boolean removeSong(Song song) {
        return getSongs().remove(song);
    }

    public double calculateRating() {
        if (getSongs().isEmpty()) return 0.0;
        double total = 0.0;
        for (Song song : getSongs()) {
            total += song.getAverageRating();
        }
        return total / getSongs().size();
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param songs the songs to set
     */
    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
    
    @Override
    public void play() {
        System.out.println("Playing playlist: " + getName());
        for (Song song : getSongs()) {
            song.play();
        }
    }

    public List<Song> getSongs() {
        return songs;
    }

    public String getName() {
        return name;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }
}