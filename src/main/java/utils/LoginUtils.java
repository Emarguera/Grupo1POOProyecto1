package utils;

import models.User;

public class LoginUtils {

    public static boolean credentialsMatch(User user, String email, String password) {
        if (user == null) return false;
        return user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password);
    }
}
