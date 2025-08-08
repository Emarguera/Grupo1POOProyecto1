/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author isaac
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionOracle {
//    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE"; // DB de Isaac
    private static final String URL = "jdbc:oracle:thin:@//192.168.100.182:1521/xe"; // DB de Esteban
    private static final String USER = "system";
    private static final String PASSWORD = "Zapot3";

    public static Connection conectar() {
        Connection conexion = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver"); // Asegura que se cargue el driver
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a Oracle");
        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró el driver JDBC: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
        
        
        return conexion;
    }
        
}
