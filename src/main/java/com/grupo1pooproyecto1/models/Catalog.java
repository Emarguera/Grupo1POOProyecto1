/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author estebanruiz
 */
public class Catalog {
    private List<Song> songs;

    /**
     * @return the songs
     */
    public List<Song> getSongs() {
        return songs;
    }

    /**
     * @param songs the songs to set
     */
    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public Catalog() {
        this.songs = new ArrayList<>();
    }

    public void addSong(Song song) {
        if (!songs.contains(song)) {
            getSongs().add(song);
        }
    }

    public boolean removeSong(Song song) {
        return getSongs().remove(song);
    }

    public List<Song> searchSongs(String criteria) {
        String lowerCriteria = criteria.toLowerCase();
        return getSongs().stream()
            .filter(song ->
                song.getTitle().toLowerCase().contains(lowerCriteria) ||
                song.getArtist().toLowerCase().contains(lowerCriteria) ||
                song.getComposer().toLowerCase().contains(lowerCriteria) ||
                song.getAlbum().toLowerCase().contains(lowerCriteria)
            )
            .collect(Collectors.toList());
    }


}
