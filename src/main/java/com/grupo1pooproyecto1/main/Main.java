/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.main;

import com.grupo1pooproyecto1.enums.Genre;
import com.grupo1pooproyecto1.models.*;
import com.grupo1pooproyecto1.services.Top5Manager;
import com.grupo1pooproyecto1.system.MusicSystem;
import com.grupo1pooproyecto1.utils.Image;
import com.grupo1pooproyecto1.utils.Name;

import java.time.LocalDate;

/**
 *
 * @author estebanruiz
 */
public class Main {
    public static void main(String[] args) {
        // Step 1: Create the system
        MusicSystem system = new MusicSystem();

        // Step 2: Create songs and add to catalog
        Song s1 = new Song("Storm", Genre.ROCK, "Echo Band", "A. Smith", LocalDate.of(2020, 3, 1), "Thunder", 1.99);
        Song s2 = new Song("Dreamscape", Genre.ELECTRONIC, "SynthX", "B. Jones", LocalDate.of(2022, 5, 15), "Waves", 2.49);
        Song s3 = new Song("Serenity", Genre.CLASSICAL, "J. Doe", "J. Doe", LocalDate.of(2018, 11, 20), "Peace", 0.99);

        system.getCatalog().addSong(s1);
        system.getCatalog().addSong(s2);
        system.getCatalog().addSong(s3);

        // Step 3: Register a FinalUser
        FinalUser user = new FinalUser(
                "user1", "user1@example.com", "pass1234",
                new Name("Esteban", "Palotes"),
                LocalDate.of(2000, 1, 1),
                "123456789",
                new Image("avatar.png")
        );
        user.rechargeBalance(10.0);
        system.registerUser(user);

        // Step 4: Buy and rate songs
        user.buySong(s1);
        user.buySong(s2);

        user.rateSong(s1, 5);
        user.rateSong(s2, 4);
        user.rateSong(s3, 3); // Not purchased but rated

        // Step 5: Create a playlist and add songs
        user.createPlaylist("Chill Vibes");
        Playlist p = user.getPlaylists().get(0);
        p.addSong(s1);
        p.addSong(s2);

        // Step 6: Update and print Top 5 lists
        system.updateTop5Lists();
        Top5Manager top5 = system.getTop5Lists();

        System.out.println("\n🎵 Top 5 Rated Songs:");
        for (Song s : top5.getTop5Rated()) {
            System.out.printf("- %s (%.2f★)%n", s.getTitle(), s.getAverageRating());
        }

        System.out.println("\n💰 Top 5 Most Purchased:");
        for (Song s : top5.getTop5Purchased()) {
            System.out.printf("- %s (%d purchases)%n", s.getTitle(), s.getPurchaseCount());
        }

        System.out.println("\n📂 Top 5 in Playlists:");
        for (Song s : top5.getTop5InPlaylist()) {
            System.out.printf("- %s (%d inclusions)%n", s.getTitle(), s.getPlaylistInclusionCount());
        }
        System.out.println("\n👤 Final User Summary:");
        System.out.println(user);
    }
    
}
