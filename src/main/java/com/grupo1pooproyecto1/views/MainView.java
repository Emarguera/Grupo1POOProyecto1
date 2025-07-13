/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.views;

import com.grupo1pooproyecto1.models.Admin;
import com.grupo1pooproyecto1.models.FinalUser;
import com.grupo1pooproyecto1.controllers.AdminController;
import com.grupo1pooproyecto1.controllers.FinalUserController;
import com.grupo1pooproyecto1.system.MusicSystem;
import com.grupo1pooproyecto1.utils.Image;
import com.grupo1pooproyecto1.utils.Name;
import com.grupo1pooproyecto1.system.DummyDataLoader;

import javax.swing.JOptionPane;
import java.time.LocalDate;
/**
 *
 * @author Emarguera
 */
public class MainView {
   public static void main(String[] args) {
        MusicSystem system = new MusicSystem(); // Shared system for both user types
        DummyDataLoader.load(system);
        
        boolean running = true;

        while (running) {
            String[] options = {"Administrador", "Usuario Final", "Salir"};
            String choice = (String) JOptionPane.showInputDialog(
                null,
                "🎶 Bienvenido a Grupo1 MusicApp\nSeleccione un tipo de usuario:",
                "Inicio",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
            );

            if (choice == null || choice.equals("Salir")) {
                running = false;
                JOptionPane.showMessageDialog(null, "👋 ¡Hasta luego!");
                break;
            }

            switch (choice) {
                case "Administrador":
                    launchAdmin(system);
                    break;
                case "Usuario Final":
                    launchFinalUser(system);
                    break;
            }
        }
    }
   private static String pedirNombreConValidacion(String mensaje) {
    String input;
    do {
        input = JOptionPane.showInputDialog(mensaje);
        if (input == null) {
            // El usuario presionó cancelar, salir o retornar null si quieres
            return null;
        }
        input = input.trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(null, "⚠️ El campo no puede estar vacío. Intenta de nuevo.");
        }
    } while (input.isEmpty());

    return input;
    }

    private static void launchAdmin(MusicSystem system) {
    String nameInput = pedirNombreConValidacion("👨‍💼 Nombre del Administrador:");
    if (nameInput == null) return; // Usuario canceló

    Admin admin = new Admin(
        "admin1",
        "admin@example.com",
        "secure123",
        new Name(nameInput, ""),
        LocalDate.of(1990, 1, 1),
        "Costa Rica",
        new Image("admin.jpg")
    );

    AdminController controller = new AdminController(admin, system);
    AdminView view = new AdminView(controller);
    view.displayMainMenu();
    }

    private static void launchFinalUser(MusicSystem system) {
        String nameInput = pedirNombreConValidacion("👤 Nombre del Usuario Final:");
        if (nameInput == null) return; // Usuario canceló

        FinalUser user = new FinalUser(
            "user1",
            "user@example.com",
            "pass123",
            new Name(nameInput, ""),
            LocalDate.of(2000, 5, 10),
            "Costa Rica",
            new Image("user.jpg")
        );

        FinalUserController controller = new FinalUserController(user, system);
        FinalUserView view = new FinalUserView(controller);
        view.displayUserMenu();
    }
}

