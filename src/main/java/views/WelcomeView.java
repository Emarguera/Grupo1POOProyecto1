package views;

import controllers.AdminController;
import controllers.FinalUserController;
import dao.AdminDAO;
import dao.LoginDAO;
import dao.RegisteredUserDAO;
import models.Admin;
import models.RegisteredUser;

import javax.swing.JOptionPane;

public class WelcomeView {

    private final AdminDAO adminDAO = new AdminDAO();
    private final RegisteredUserDAO registeredUserDAO = new RegisteredUserDAO();
    private final LoginDAO loginDAO = new LoginDAO();

    public void show() {
        String[] options = {"Login", "Register"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Welcome to the Music System",
                "Welcome",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            login();
        } else if (choice == 1) {
            UserRegistrationView registrationView = new UserRegistrationView();
            registrationView.show();
            show(); // After registration return here to login
        } else {
            System.exit(0);
        }
    }

    private void login() {
        String email = JOptionPane.showInputDialog("Enter Email:");
        String password = JOptionPane.showInputDialog("Enter Password:");

        // User cancelled one of the dialogs
        if (email == null || password == null) {
            show();
            return;
        }

        email = email.trim();
        password = password.trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Email and password are required.");
            show();
            return;
        }

        // Ask the DB who this is
        String role = loginDAO.checkLogin(email, password); // returns "ADMIN", "REGISTERED", or "INVALID"

        switch (role) {
            case "ADMIN" -> {
                // Load full Admin from DB (by email)
                Admin admin = adminDAO.getAdminByEmail(email);
                if (admin == null) {
                    JOptionPane.showMessageDialog(null, "Could not load admin profile. Please try again.");
                    show();
                    return;
                }
                AdminController adminController = new AdminController(admin);
                AdminView adminView = new AdminView(adminController);
                adminView.show();
            }

            case "REGISTERED" -> {
                // Load full RegisteredUser from DB (by email); excludes inactive users
                RegisteredUser user = registeredUserDAO.getRegisteredUserByEmail(email);
                if (user == null) {
                    JOptionPane.showMessageDialog(null, "Could not load user profile. Your account may be inactive.");
                    show();
                    return;
                }
                FinalUserController userController = new FinalUserController(user);
                RegisteredUserView userView = new RegisteredUserView(userController);
                userView.show();
            }

            default -> {
                JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.");
                show();
            }
        }
    }
}
