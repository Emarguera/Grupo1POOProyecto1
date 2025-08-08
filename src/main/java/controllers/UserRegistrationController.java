package controllers;

import enums.Nationality;
import models.RegisteredUser;
import services.RegisterValidation;

import java.util.ArrayList;
import java.util.List;

public class UserRegistrationController {

    // Simulating DB storage for users
    private static List<RegisteredUser> registeredUsers = new ArrayList<>();

    public boolean registerUser(String name, String lastName, String email, String password, String id, Nationality nationality) {
        // Validate
        if (!RegisterValidation.isAdult(id)) return false; // You can improve this by parsing DOB instead of ID alone
        if (userExists(email, id)) return false;

        RegisteredUser user = new RegisteredUser(name, lastName, email, password, id, nationality);
        registeredUsers.add(user);
        return true;
    }

    private boolean userExists(String email, String id) {
        return registeredUsers.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email) || u.getId().equals(id));
    }

    public List<RegisteredUser> getRegisteredUsers() {
        return registeredUsers;
    }
}
