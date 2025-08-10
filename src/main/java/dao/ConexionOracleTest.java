package dao;

import java.sql.Connection;

public class ConexionOracleTest {
    public static void main(String[] args) {
        Connection conn = ConexionOracle.conectar();
        if (conn != null) {
            System.out.println("✅ La conexión fue exitosa.");
        } else {
            System.out.println("❌ La conexión falló.");
        }
    }
}