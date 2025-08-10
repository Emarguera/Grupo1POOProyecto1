package dao;

import models.Admin;
import enums.Nationality;

import java.sql.*;

public class AdminDAO {

    // Create admin
    public boolean createAdmin(Admin admin) {
        String sql = "{call ADMIN_PKG.create_admin(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionOracle.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, admin.getId());
            stmt.setString(2, admin.getName());
            stmt.setString(3, admin.getLastName());
            stmt.setString(4, admin.getEmail());
            stmt.setString(5, admin.getPassword());
            stmt.setString(6, admin.getNationality().name());

            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating admin: " + e.getMessage());
            return false;
        }
    }

    // Read admin by id (using OUT parameters)
    public Admin getAdminById(String id) {
        String sql = "{call ADMIN_PKG.read_admin(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionOracle.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, id);
            stmt.registerOutParameter(2, Types.VARCHAR); // name
            stmt.registerOutParameter(3, Types.VARCHAR); // last_name
            stmt.registerOutParameter(4, Types.VARCHAR); // email
            stmt.registerOutParameter(5, Types.VARCHAR); // password
            stmt.registerOutParameter(6, Types.VARCHAR); // nationality

            stmt.execute();

            String name = stmt.getString(2);
            if (name == null) return null; // No record found

            String lastName = stmt.getString(3);
            String email = stmt.getString(4);
            String password = stmt.getString(5);
            Nationality nationality = Nationality.valueOf(stmt.getString(6));

            return new Admin(id, name, lastName, email, password, nationality);

        } catch (SQLException e) {
            System.err.println("Error reading admin: " + e.getMessage());
            return null;
        }
    }

    // Get admin by email (to support login)
    public Admin getAdminByEmail(String email) {
        String sql = "SELECT u.id FROM users u JOIN admins a ON u.id = a.id WHERE u.email = ?";
        try (Connection conn = ConexionOracle.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("id");
                    return getAdminById(id);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching admin by email: " + e.getMessage());
        }
        return null;
    }

    // Update admin
    public boolean updateAdmin(Admin admin) {
        String sql = "{call ADMIN_PKG.update_admin(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionOracle.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, admin.getId());
            stmt.setString(2, admin.getName());
            stmt.setString(3, admin.getLastName());
            stmt.setString(4, admin.getEmail());
            stmt.setString(5, admin.getPassword());
            stmt.setString(6, admin.getNationality().name());

            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating admin: " + e.getMessage());
            return false;
        }
    }

    // Delete admin
    public boolean deleteAdmin(String id) {
        String sql = "{call ADMIN_PKG.delete_admin(?)}";
        try (Connection conn = ConexionOracle.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, id);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting admin: " + e.getMessage());
            return false;
        }
    }
}
