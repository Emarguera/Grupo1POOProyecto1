/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1pooproyecto1.dao;

import com.grupo1pooproyecto1.dao.ConexionOracle;
import java.sql.Connection;

/**
 *
 * @author Emarguera
 */
public class TestDAO {
    public static void main(String[] args) {
        Connection conn = ConexionOracle.conectar();
        if (conn != null) {
            System.out.println("Conexión a Oracle exitosa desde NetBeans.");
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No se pudo conectar a Oracle.");
        }
    }
}