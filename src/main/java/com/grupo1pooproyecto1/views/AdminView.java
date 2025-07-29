/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.views;

import com.grupo1pooproyecto1.controllers.AdminController;
import com.grupo1pooproyecto1.models.Song;
import com.grupo1pooproyecto1.enums.Genre;

import java.time.LocalDate;
import javax.swing.JOptionPane;
import java.util.List;
/**
 *
 * @author Emarguera
 */
public class AdminView {
    private final AdminController controller;
    private boolean running;

    public AdminView(AdminController controller) {
        this.controller = controller;
        this.running = true;
    }

    public void displayMainMenu() {
        while (running) {
            String[] options = {
                "1. Ver catálogo",
                "2. Agregar canción",
                "3. Eliminar canción",
                "4. Ver Top 5",
                "5. Salir"
            };

            String input = (String) JOptionPane.showInputDialog(
                null,
                "Menú de Administrador:\nSeleccione una opción:",
                "Admin - Grupo1 MusicApp",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
            );

            if (input == null || input.startsWith("5")) {
                running = false;
                JOptionPane.showMessageDialog(null, "Sesión de administrador finalizada.");
                break;
            }

            handleOption(input.charAt(0));
        }
    }

    private void handleOption(char option) {
        switch (option) {
            case '1':
                showCatalog();
                break;
            case '2':
                uploadSong();
                break;
            case '3':
                eliminateSong();
                break;
            case '4':
                showTop5();
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opción inválida.");
        }
    }

    private void showCatalog() {
        List<Song> songs = controller.getCatalogSongs();
        if (songs.isEmpty()) {
            JOptionPane.showMessageDialog(null, "🎵 El catálogo está vacío.");
            return;
        }

        StringBuilder sb = new StringBuilder("🎵 Catálogo:\n");
        int index = 1;
        for (Song s : songs) {
            sb.append(String.format("%d. %s - %s ($%.2f, %.1f★)\n",
                    index++, s.getTitle(), s.getArtist(), s.getPrice(), s.getAverageRating()));
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private void uploadSong() {
        try {
        String title = JOptionPane.showInputDialog("🎵 Ingrese el título de la canción:");
        if (title == null || title.isBlank()) return;

        String[] genreOptions = {"POP", "JAZZ", "CLASSICAL", "HIPHOP", "ELECTRONICA", "COUNTRY", "REGGAE", "METAL", "OTHER"};
        String genreStr = (String) JOptionPane.showInputDialog(   
                null,
                "Seleccione el género:",
                "Género",
                JOptionPane.PLAIN_MESSAGE,
                null,
                genreOptions,
                genreOptions[0]
        );
        if (genreStr == null) return;
        Genre genre = Genre.valueOf(genreStr);

        String artist = JOptionPane.showInputDialog("Ingrese el artista:");
        if (artist == null || artist.isBlank()) return;

        String album = JOptionPane.showInputDialog("Ingrese el álbum:");
        if (album == null || album.isBlank()) return;

        String priceStr = JOptionPane.showInputDialog("Ingrese el precio:");
        if (priceStr == null) return;
        double price = Double.parseDouble(priceStr);

        Song song = new Song(title, genre, artist, album, price);
        controller.uploadSong(song);

        JOptionPane.showMessageDialog(null, "✅ Canción subida correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ Error al subir canción: " + e.getMessage());
        }
    }

    private void eliminateSong() {
        List<Song> songs = controller.getCatalogSongs();
        if (songs.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El catálogo está vacío.");
            return;
        }

        StringBuilder sb = new StringBuilder("Seleccione el número de la canción a eliminar:\n");
        int index = 1;
        for (Song s : songs) {
            sb.append(String.format("%d. %s - %s\n", index++, s.getTitle(), s.getArtist()));
        }

        String input = JOptionPane.showInputDialog(sb.toString());
        if (input == null) return;

        try {
            int songIndex = Integer.parseInt(input);
            boolean removed = controller.eliminateSong(songIndex);
            if (removed) {
                JOptionPane.showMessageDialog(null, "✅ Canción eliminada.");
            } else {
                JOptionPane.showMessageDialog(null, "❌ Índice inválido.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida.");
        }
    }

    private void showTop5() {
        // Prints to console — optional: mirror it to GUI if desired later
        controller.showTop5();
        JOptionPane.showMessageDialog(null, "🔝 Top 5 mostrado por consola.");
    }
}
