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

import javax.swing.JOptionPane;
import java.time.LocalDate;
/**
 *
 * @author Emarguera
 */
public class MainView {
   public static void main(String[] args) {
        MusicSystem system = new MusicSystem(); // Shared system for both user types

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

    private static void launchAdmin(MusicSystem system) {
        String nameInput = JOptionPane.showInputDialog("👨‍💼 Nombre del Administrador:");
        if (nameInput == null || nameInput.isBlank()) return; //Para pruebas

        Admin admin = new Admin(
            "admin1",                         // id
            "admin@example.com",             // email
            "secure123",                     // password
            new Name(nameInput, ""),     // utils.Name (first, last)
            LocalDate.of(1990, 1, 1),        // fechaNacimiento
            "Costa Rica",                    // nacionalidad
            new Image("admin.jpg")           // utils.Image
        );

        AdminController controller = new AdminController(admin, system);
        AdminView view = new AdminView(controller);
        view.displayMainMenu();
    }


    private static void launchFinalUser(MusicSystem system) {
        String nameInput = JOptionPane.showInputDialog("👤 Nombre del Usuario Final:");
        if (nameInput == null || nameInput.isBlank()) return;

        FinalUser user = new FinalUser(
            "user1",                          // id
            "user@example.com",              // email
            "pass123",                       // password
            new Name(nameInput, ""),     // utils.Name (first, last)
            LocalDate.of(2000, 5, 10),       // fechaNacimiento
            "Costa Rica",                    // nacionalidad
            new Image("user.jpg")            // utils.Image
        );

        FinalUserController controller = new FinalUserController(user, system);
        FinalUserView view = new FinalUserView(controller);
        view.displayUserMenu();
    }
}

