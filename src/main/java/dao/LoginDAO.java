package dao;

import java.sql.*;

public class LoginDAO {

    /**
     * Check user login credentials and return role.
     * Possible return values: "ADMIN", "REGISTERED", "INVALID"
     */
    public String checkLogin(String email, String password) {
        String sql = "{ ? = call login_pkg.check_login(?, ?) }";
        try (Connection conn = ConexionOracle.conectar();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.registerOutParameter(1, Types.VARCHAR);
            stmt.setString(2, email);
            stmt.setString(3, password);

            stmt.execute();

            return stmt.getString(1);
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
            return "INVALID";
        }
    }
}
