package com.uniajc;

import java.sql.Connection;
import com.uniajc.config.ConexionPostgresDatabase;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- Probando Conexión a Neon PostgreSQL ---");
        try (Connection conn = ConexionPostgresDatabase.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("🎉 ¡Conexión establecida con éxito a Neon Database!");
                System.out.println("Catalogo actual: " + conn.getCatalog());
            } else {
                System.out.println("❌ No se pudo conectar a la base de datos.");
            }
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionPostgresDatabase.closeConnection();
        }
    }
}
