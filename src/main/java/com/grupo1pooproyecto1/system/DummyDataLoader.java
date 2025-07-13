/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.system;

import com.grupo1pooproyecto1.enums.Genre;
import com.grupo1pooproyecto1.models.Admin;
import com.grupo1pooproyecto1.models.FinalUser;
import com.grupo1pooproyecto1.models.Song;
import com.grupo1pooproyecto1.utils.Image;
import com.grupo1pooproyecto1.utils.Name;

import java.time.LocalDate;
/**
 *
 * @author Emarguera
 */
public class DummyDataLoader {
    public static void load(MusicSystem system) {
        // Crear canciones dummy y agregarlas al catálogo
        system.getCatalog().addSong(new Song(
            "Bohemian Rhapsody",
            Genre.ROCK,
            "Queen",
            "A Night at the Opera",
            LocalDate.of(1975, 11, 21),
            "Classic rock masterpiece",
            1.99
        ));

        system.getCatalog().addSong(new Song(
            "Clair de Lune",
            Genre.CLASSICAL,  // Usa el enum correcto
            "Debussy",
            "Suite Bergamasque",
            LocalDate.of(1905, 1, 1),
            "Piano solo",
            0.99
        ));
        
        system.getCatalog().addSong(new Song(
            "Solitude is Bliss",
            Genre.POP,  // Usa el enum correcto
            "Tame Impala",
            "Inner Speaker",
            LocalDate.of(2005, 1, 1),
            "Electronic",
            0.50
        ));
        
         system.getCatalog().addSong(new Song(
            "I Am Mine",
            Genre.ROCK,  // Usa el enum correcto
            "Pearl Jam",
            "Riot Act",
            LocalDate.of(2000, 1, 1),
            "Master",
            1.50
        ));

        // Reemplazar admin quemado si deseas (opcional)
        Admin dummyAdmin = new Admin(
            "admin1",
            "admin@music.com",
            "admin123",
            new Name("Admin", "Root"),
            LocalDate.of(1990, 1, 1),
            "Costa Rica",
            new Image("admin.jpg")
        );
        system.setAdmin(dummyAdmin);

        // Registrar usuarios finales dummy usando método existente
        FinalUser dummyUser = new FinalUser(
            "user1",
            "user@music.com",
            "pass123",
            new Name("User", "Test"),
            LocalDate.of(2000, 1, 1),
            "Costa Rica",
            new Image("user.jpg")
        );
        system.registerUser(dummyUser);
    }
}
