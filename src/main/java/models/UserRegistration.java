package models;

import enums.Nationality;
import models.RegisteredUser;
import services.RegisterValidation;
import java.util.List;
import java.util.ArrayList;
import models.Admin;

public class UserRegistration {

    private static List<Admin> users = new ArrayList<>(); // you need this list

    public RegisteredUser registerUser(String name, String lastName, String email, String password,
                                       String id, int age, Nationality nationality) {

        if (!RegisterValidation.isNotBlank(name) ||
            !RegisterValidation.isNotBlank(lastName) ||
            !RegisterValidation.isValidEmail(email) ||
            !RegisterValidation.isValidID(id) ||
            !RegisterValidation.isValidAge(age) ||
            !RegisterValidation.isNotBlank(password)) {
            return null;
        }

        // TODO: Check if user already exists in DB using DAO (when DAO ready)

        RegisteredUser newUser = new RegisteredUser(id, name, lastName, email, password, nationality);

        // TODO: Save to database (when DAO ready)

        return newUser;
    }

    public static void registerAdmin(Admin admin) {
        users.add(admin);
    }
}
