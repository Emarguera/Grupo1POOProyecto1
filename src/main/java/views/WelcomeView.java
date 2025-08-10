package views;

import controllers.AdminController;
import controllers.FinalUserController;
import enums.Nationality;
import models.Admin;
import models.RegisteredUser;
import utils.LoginUtils;
import dao.AdminDAO;
import dao.RegisteredUserDAO;

import javax.swing.JOptionPane;

public class WelcomeView {

    private AdminDAO adminDAO = new AdminDAO();
    private RegisteredUserDAO registeredUserDAO = new RegisteredUserDAO();

    public void show() {
        String[] options = {"Login", "Register"};
        int choice = JOptionPane.showOptionDialog(null, "Welcome to the Music System", "Welcome",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

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

        Admin admin = adminDAO.getAdminByEmail(email);
        if (admin != null && LoginUtils.credentialsMatch(admin, email, password)) {
            AdminController adminController = new AdminController(admin);
            AdminView adminView = new AdminView(adminController);
            adminView.show();
            return;
        }

        RegisteredUser user = registeredUserDAO.getRegisteredUserByEmail(email);
        if (user != null && LoginUtils.credentialsMatch(user, email, password)) {
            FinalUserController userController = new FinalUserController(user);
            RegisteredUserView userView = new RegisteredUserView(userController);
            userView.show();
            return;
        }

        JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.");
        show();
    }
}
