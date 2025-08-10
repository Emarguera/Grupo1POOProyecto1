package dao;

import models.RegisteredUser;
import enums.Nationality;

import java.sql.*;

public class RegisteredUserDAO {

    // Create registered user
    public boolean createRegisteredUser(RegisteredUser user) {
        String sql = "{call REGISTERED_USER_PKG.create_registered_user(?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionOracle.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, user.getId());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getLastName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPassword());
            stmt.setString(6, user.getNationality().name());
            stmt.setDouble(7, user.getBalance());

            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating registered user: " + e.getMessage());
            return false;
        }
    }

    // Read registered user by id (with OUT parameters)
    public RegisteredUser getRegisteredUserById(String id) {
        String sql = "{call REGISTERED_USER_PKG.read_registered_user(?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionOracle.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, id);
            stmt.registerOutParameter(2, Types.VARCHAR); // name
            stmt.registerOutParameter(3, Types.VARCHAR); // last_name
            stmt.registerOutParameter(4, Types.VARCHAR); // email
            stmt.registerOutParameter(5, Types.VARCHAR); // password
            stmt.registerOutParameter(6, Types.VARCHAR); // nationality
            stmt.registerOutParameter(7, Types.DOUBLE);  // balance

            stmt.execute();

            String name = stmt.getString(2);
            if (name == null) return null; // no record

            String lastName = stmt.getString(3);
            String email = stmt.getString(4);
            String password = stmt.getString(5);
            Nationality nationality = Nationality.valueOf(stmt.getString(6));
            double balance = stmt.getDouble(7);

            RegisteredUser user = new RegisteredUser(id, name, lastName, email, password, nationality);
            user.setBalance(balance);

            return user;
        } catch (SQLException e) {
            System.err.println("Error reading registered user: " + e.getMessage());
            return null;
        }
    }

    // Get registered user by email (for login)
    public RegisteredUser getRegisteredUserByEmail(String email) {
        String sql = "SELECT id FROM registered_users WHERE email = ?";
        try (Connection conn = ConexionOracle.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("id");
                    return getRegisteredUserById(id);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching registered user by email: " + e.getMessage());
        }
        return null;
    }

    // Update registered user
    public boolean updateRegisteredUser(RegisteredUser user) {
        String sql = "{call REGISTERED_USER_PKG.update_registered_user(?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionOracle.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, user.getId());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getLastName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPassword());
            stmt.setString(6, user.getNationality().name());
            stmt.setDouble(7, user.getBalance());

            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating registered user: " + e.getMessage());
            return false;
        }
    }

    // Delete registered user
    public boolean deleteRegisteredUser(String id) {
        String sql = "{call REGISTERED_USER_PKG.delete_registered_user(?)}";
        try (Connection conn = ConexionOracle.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, id);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting registered user: " + e.getMessage());
            return false;
        }
    }
}
