/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.controllers;

import com.grupo1pooproyecto1.models.FinalUser;
import com.grupo1pooproyecto1.models.Song;
import com.grupo1pooproyecto1.models.Playlist;
import com.grupo1pooproyecto1.system.MusicSystem;

import java.util.List;
/**
 *
 * @author Emarguera
 */
public class FinalUserController {
   private FinalUser user;
    private MusicSystem system;

    public FinalUserController(FinalUser user, MusicSystem system) {
        this.user = user;
        this.system = system;
    }

    public void showCatalog() {
        List<Song> songs = system.getCatalog().getSongs();
        if (songs.isEmpty()) {
            System.out.println("The catalog is empty.");
        } else {
            System.out.println("🎵 Catalog:");
            int index = 1;
            for (Song s : songs) {
                System.out.printf("%d. %s - %s ($%.2f, %.2f★)\n",
                        index++, s.getTitle(), s.getArtist(), s.getPrice(), s.getAverageRating());
            }
        }
    }

    public boolean buySong(int songIndex) {
        List<Song> songs = system.getCatalog().getSongs();
        if (songIndex < 1 || songIndex > songs.size()) return false;

        Song song = songs.get(songIndex - 1);
        return user.buySong(song);
    }

    public boolean rateSong(int songIndex, int rating) {
        List<Song> songs = system.getCatalog().getSongs();
        if (songIndex < 1 || songIndex > songs.size()) return false;

        Song song = songs.get(songIndex - 1);
        user.rateSong(song, rating);
        return true;
    }

    public void createPlaylist(String name) {
        user.createPlaylist(name);
    }

    public boolean addSongToPlaylist(int playlistIndex, int songIndex) {
        List<Playlist> playlists = user.getPlaylists();
        List<Song> songs = system.getCatalog().getSongs();

        if (playlistIndex < 1 || playlistIndex > playlists.size() ||
            songIndex < 1 || songIndex > songs.size()) return false;

        Playlist playlist = playlists.get(playlistIndex - 1);
        Song song = songs.get(songIndex - 1);
        return playlist.addSong(song);
    }

    public List<Playlist> getUserPlaylists() {
        return user.getPlaylists();
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

    public void showUserProfile() {
        System.out.println("\n👤 Final User Summary:");
        System.out.println(user);
    }
}