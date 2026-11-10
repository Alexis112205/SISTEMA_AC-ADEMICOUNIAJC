package com.uniajc.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestor de conexión a la base de datos PostgreSQL / Neon.
 * Sigue el patrón Singleton para reutilizar la conexión activa.
 */
public class ConexionPostgresDatabase {

    private static Connection connection = null;

    private ConexionPostgresDatabase() {
        // Constructor privado para evitar instanciación
    }

    /**
     * Obtiene una conexión activa a la base de datos PostgreSQL (Neon).
     * Carga las credenciales desde el archivo config.properties.
     *
     * @return Connection objeto de conexión JDBC
     */
    public static Connection getConnection() {
        if (connection == null) {
            Properties properties = new Properties();
            InputStream input = null;

            try {
                // 1. Intentar cargar desde el directorio raíz del proyecto
                File configFile = new File("config.properties");
                if (configFile.exists()) {
                    input = new FileInputStream(configFile);
                } else {
                    // 2. Si no existe en la raíz, buscar en el classpath (resources)
                    input = ConexionPostgresDatabase.class.getClassLoader().getResourceAsStream("config.properties");
                }

                if (input == null) {
                    throw new FileNotFoundException("No se encontró el archivo 'config.properties' ni en la raíz ni en resources.");
                }

                properties.load(input);

                String url = properties.getProperty("URL");
                String user = properties.getProperty("USERNAME");
                String password = properties.getProperty("PASSWORD");

                if (url == null || user == null || password == null) {
                    throw new IllegalArgumentException("El archivo config.properties debe contener URL, USERNAME y PASSWORD.");
                }

                // Cargar explícitamente el driver de PostgreSQL
                Class.forName("org.postgresql.Driver");

                connection = DriverManager.getConnection(url, user, password);
                System.out.println("✅ Conexión exitosa a la base de datos PostgreSQL (Neon).");

            } catch (ClassNotFoundException e) {
                System.err.println("❌ Driver PostgreSQL no encontrado: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("❌ Error al conectar a PostgreSQL/Neon: " + e.getMessage());
            } catch (FileNotFoundException e) {
                System.err.println("❌ " + e.getMessage());
            } catch (IOException e) {
                System.err.println("❌ Error al leer config.properties: " + e.getMessage());
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return connection;
    }

    /**
     * Cierra la conexión activa si está abierta.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("🔒 Conexión cerrada exitosamente.");
            } catch (SQLException e) {
                System.err.println("❌ Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
