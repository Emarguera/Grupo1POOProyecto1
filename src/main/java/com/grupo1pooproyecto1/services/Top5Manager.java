/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.services;

import com.grupo1pooproyecto1.models.Song;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author estebanruiz
 */
public class Top5Manager {
    private List<Song> top5Rated;
    private List<Song> top5Purchased;       // Placeholder: requires purchase tracking
    private List<Song> top5InPlaylist;      // Placeholder: requires inclusion tracking

    public Top5Manager() {
        this.top5Rated = new ArrayList<>();
        this.top5Purchased = new ArrayList<>();
        this.top5InPlaylist = new ArrayList<>();
    }

    public void updateTop5(List<Song> allSongs) {
        top5Rated = allSongs.stream()
            .sorted(Comparator.comparingDouble(Song::getAverageRating).reversed())
            .limit(5)
            .collect(Collectors.toList());

        top5Purchased = allSongs.stream()
            .sorted(Comparator.comparingInt(Song::getPurchaseCount).reversed())
            .limit(5)
            .collect(Collectors.toList());

        top5InPlaylist = allSongs.stream()
            .sorted(Comparator.comparingInt(Song::getPlaylistInclusionCount).reversed())
            .limit(5)
            .collect(Collectors.toList());
        // TODO: update top5Purchased and top5InPlaylist with real tracking logic
    }

    public List<Song> getTop5Rated() {
        return top5Rated;
    }

    public List<Song> getTop5Purchased() {
        return top5Purchased;
    }

    public List<Song> getTop5InPlaylist() {
        return top5InPlaylist;
    }
}
