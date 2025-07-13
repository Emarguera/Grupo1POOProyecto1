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
import com.grupo1pooproyecto1.controllers.FinalUserController;
import com.grupo1pooproyecto1.views.FinalUserView;

import java.time.LocalDate;

/**
 *
 * @author estebanruiz
 */
public class Main {
    public static void main(String[] args) {
       // Step 1: Create the system
        MusicSystem system = new MusicSystem();

        // Step 2: Add demo songs to catalog
        system.getCatalog().addSong(new Song("Storm", Genre.ROCK, "Echo Band", "A. Smith",
                LocalDate.of(2020, 3, 1), "Thunder", 1.99));

        system.getCatalog().addSong(new Song("Dreamscape", Genre.ELECTRONIC, "SynthX", "B. Jones",
                LocalDate.of(2022, 5, 15), "Waves", 2.49));

        system.getCatalog().addSong(new Song("Serenity", Genre.CLASSICAL, "J. Doe", "J. Doe",
                LocalDate.of(2018, 11, 20), "Peace", 0.99));

        // Step 3: Create and register a FinalUser
        FinalUser user = new FinalUser(
                "user1", "user1@example.com", "pass1234",
                new Name("Alice", "Walker"),
                LocalDate.of(2000, 1, 1),
                "123456789",
                new Image("avatar.png")
        );
        system.registerUser(user);

        // Step 4: Start the FinalUser session via controller/view
        FinalUserController controller = new FinalUserController(user, system);
        FinalUserView view = new FinalUserView(controller);
        view.showMenu(); // 🟢 Starts the interactive CLI menu
    }
}