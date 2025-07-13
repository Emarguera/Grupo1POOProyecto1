/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.controllers;

import com.grupo1pooproyecto1.models.Admin;
import com.grupo1pooproyecto1.models.Catalog;
import com.grupo1pooproyecto1.models.Song;
import com.grupo1pooproyecto1.system.MusicSystem;

import java.util.List;
/**
 *
 * @author Emarguera
 */
public class AdminController {
    private Admin admin;
    private MusicSystem system;

    public AdminController(Admin admin, MusicSystem system) {
        this.admin = admin;
        this.system = system;
    }

    // Upload a song to the catalog and admin collection
    public void uploadSong(Song song) {
        admin.uploadSong(song);
        system.getCatalog().addSong(song);
    }

    // Remove a song from catalog and admin collection
    public boolean eliminateSong(int index) {
        List<Song> songs = system.getCatalog().getSongs();
        if (index < 1 || index > songs.size()) return false;
        Song song = songs.get(index - 1);
        admin.eliminateSong(song);
        return system.getCatalog().removeSong(song);
    }

    public void showCatalog() {
        List<Song> songs = system.getCatalog().getSongs();
        if (songs.isEmpty()) {
            System.out.println("The catalog is empty.");
        } else {
            int index = 1;
            System.out.println("🎵 Catalog:");
            for (Song s : songs) {
                System.out.printf("%d. %s - %s ($%.2f, %.1f★)\n",
                        index++, s.getTitle(), s.getArtist(), s.getPrice(), s.getAverageRating());
            }
        }
    }

    public List<Song> getCatalogSongs() {
        return system.getCatalog().getSongs();
    }

    public void showTop5() {
        system.updateTop5Lists();
        System.out.println("\n🎵 Top 5 Rated Songs:");
        system.getTop5Lists().getTop5Rated()
                .forEach(s -> System.out.printf("- %s (%.2f★)\n", s.getTitle(), s.getAverageRating()));

        System.out.println("\n💰 Top 5 Most Purchased:");
        system.getTop5Lists().getTop5Purchased()
                .forEach(s -> System.out.printf("- %s (%d purchases)\n", s.getTitle(), s.getPurchaseCount()));

        System.out.println("\n📂 Top 5 in Playlists:");
        system.getTop5Lists().getTop5InPlaylist()
                .forEach(s -> System.out.printf("- %s (%d inclusions)\n", s.getTitle(), s.getPlaylistInclusionCount()));
    }

    public Admin getAdmin() {
        return admin;
    }

    public Catalog getCatalog() {
        return system.getCatalog();
    }

    public MusicSystem getSystem() {
        return system;
    }
}

