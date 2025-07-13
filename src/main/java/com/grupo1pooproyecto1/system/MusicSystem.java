/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.system;

import com.grupo1pooproyecto1.models.User;
import com.grupo1pooproyecto1.models.Admin;
import com.grupo1pooproyecto1.models.Catalog;
import com.grupo1pooproyecto1.models.Song;
import com.grupo1pooproyecto1.services.Top5Manager;

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
    }

    public boolean registerUser(User newUser) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(newUser.getUsername())) {
                return false; // username already exists
            }
        }
        users.add(newUser);
        return true;
    }

    public User login(String username, String password) {
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
