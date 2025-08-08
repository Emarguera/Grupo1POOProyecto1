package views;

import controllers.UserRegistrationController;
import enums.Nationality;

import javax.swing.JOptionPane;

public class UserRegistrationView {

    private UserRegistrationController controller;

    public UserRegistrationView() {
        this.controller = new UserRegistrationController();
    }

    public void show() {
        String name = JOptionPane.showInputDialog("Enter your name:");
        String lastName = JOptionPane.showInputDialog("Enter your last name:");
        String email = JOptionPane.showInputDialog("Enter your email:");
        String password = JOptionPane.showInputDialog("Enter your password:");
        String id = JOptionPane.showInputDialog("Enter your 9-digit ID:");
        String[] nationalityOptions = new String[Nationality.values().length];
        for (int i = 0; i < Nationality.values().length; i++) {
            nationalityOptions[i] = Nationality.values()[i].name();
        }
        String nationalityStr = (String) JOptionPane.showInputDialog(null, "Select your nationality:",
                "Nationality", JOptionPane.QUESTION_MESSAGE, null, nationalityOptions, nationalityOptions[0]);

        if (name == null || lastName == null || email == null || password == null || id == null || nationalityStr == null) {
            JOptionPane.showMessageDialog(null, "Registration cancelled.");
            return;
        }

        if (id.length() != 9 || !id.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Invalid ID. It must be 9 digits.");
            return;
        }

        boolean success = controller.registerUser(name, lastName, email, password, id, Nationality.valueOf(nationalityStr));
        if (success) {
            JOptionPane.showMessageDialog(null, "Registration successful! You may now login.");
        } else {
            JOptionPane.showMessageDialog(null, "Registration failed. Possibly duplicate ID or email.");
        }
    }
}
