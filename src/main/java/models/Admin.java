package models;

import enums.Nationality;

/**
 * Admin user with elevated permissions.
 */
public class Admin extends User {

    private Nationality nationality;

    public Admin(String id, String name, String lastName, String email, String password, Nationality nationality) {
        super(id, name, lastName, email, password);
        this.nationality = nationality;
    }

    // Nationality getter/setter

    public Nationality getNationality() {
        return nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }
}
