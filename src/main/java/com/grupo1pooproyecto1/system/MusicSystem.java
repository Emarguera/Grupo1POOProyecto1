/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.system;

import com.grupo1pooproyecto1.models.User;
import com.grupo1pooproyecto1.models.Admin;
import com.grupo1pooproyecto1.models.Catalog;
import com.grupo1pooproyecto1.services.Top5Manager;
import com.grupo1pooproyecto1.utils.Image;
import com.grupo1pooproyecto1.utils.Name;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author estebanruiz
 */
public class MusicSystem {
    private List<User> users;
    private Admin admin;
    private Catalog catalog;
    private Top5Manager top5Lists;

    public MusicSystem() {
        this.users = new ArrayList<>();
        this.catalog = new Catalog();
        this.top5Lists = new Top5Manager();

        // Admin quemado (hardcoded)
        this.admin = new Admin(
            "admin1",
            "admin@example.com",
            "adminpass",
            new Name("Admin", "Root"),
            "Costa Rica",
            new Image("admin.png")
        );
    }

    public boolean registerUser(User newUser) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(newUser.getUsername())) {
                return false; // El nombre de usuario ya existe
            }
        }
        users.add(newUser);
        return true;
    }

    public User login(String username, String password) {
        if (admin != null && admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
            return admin;
        }

        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Admin getAdmin() {
        return admin;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public List<User> getUsers() {
        return users;
    }

    public Top5Manager getTop5Lists() {
        return top5Lists;
    }

    public void updateTop5Lists() {
        top5Lists.updateTop5(catalog.getSongs());
    }
}
