/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.views;

import com.grupo1pooproyecto1.controllers.FinalUserController;
import com.grupo1pooproyecto1.models.Playlist;

import java.util.List;
import java.util.Scanner;
/**
 *
 * @author Emarguera
 */
public class FinalUserView {
    private FinalUserController controller;
    private Scanner scanner;

    public FinalUserView(FinalUserController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        int choice = -1;
        do {
            System.out.println("\n=== 🎧 Final User Menu ===");
            System.out.println("1. View Catalog");
            System.out.println("2. Buy Song");
            System.out.println("3. Rate Song");
            System.out.println("4. Create Playlist");
            System.out.println("5. Add Song to Playlist");
            System.out.println("6. View Top 5");
            System.out.println("7. View Profile");
            System.out.println("0. Exit");

            System.out.print("Choose an option: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> controller.showCatalog();
                    case 2 -> handleBuySong();
                    case 3 -> handleRateSong();
                    case 4 -> handleCreatePlaylist();
                    case 5 -> handleAddSongToPlaylist();
                    case 6 -> controller.showTop5();
                    case 7 -> controller.showUserProfile();
                    case 0 -> System.out.println("👋 Goodbye!");
                    default -> System.out.println("⚠ Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠ Please enter a valid number.");
            }
        } while (choice != 0);
    }

    private void handleBuySong() {
        controller.showCatalog();
        System.out.print("Enter song number to buy: ");
        int index = Integer.parseInt(scanner.nextLine());
        if (controller.buySong(index)) {
            System.out.println("✅ Song purchased successfully.");
        } else {
            System.out.println("❌ Purchase failed.");
        }
    }

    private void handleRateSong() {
        controller.showCatalog();
        System.out.print("Enter song number to rate: ");
        int songIndex = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter rating (1-5): ");
        int rating = Integer.parseInt(scanner.nextLine());

        if (controller.rateSong(songIndex, rating)) {
            System.out.println("✅ Rating submitted.");
        } else {
            System.out.println("❌ Failed to rate song.");
        }
    }

    private void handleCreatePlaylist() {
        System.out.print("Enter playlist name: ");
        String name = scanner.nextLine();
        controller.createPlaylist(name);
        System.out.println("✅ Playlist created.");
    }

    private void handleAddSongToPlaylist() {
        List<Playlist> playlists = controller.getUserPlaylists();
        if (playlists.isEmpty()) {
            System.out.println("⚠ You have no playlists.");
            return;
        }

        System.out.println("📂 Your Playlists:");
        for (int i = 0; i < playlists.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, playlists.get(i).getName());
        }

        System.out.print("Select playlist number: ");
        int pIndex = Integer.parseInt(scanner.nextLine());

        controller.showCatalog();
        System.out.print("Enter song number to add: ");
        int sIndex = Integer.parseInt(scanner.nextLine());

        if (controller.addSongToPlaylist(pIndex, sIndex)) {
            System.out.println("✅ Song added to playlist.");
        } else {
            System.out.println("❌ Could not add song.");
        }
    }
}
