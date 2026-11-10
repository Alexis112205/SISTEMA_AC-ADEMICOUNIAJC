package com.uniajc.db;

import java.sql.Connection;
import com.uniajc.config.ConexionPostgresDatabase;

/**
 * @deprecated Utilizar {@link com.uniajc.config.ConexionPostgresDatabase} según la guía de arquitectura MVC.
 */
@Deprecated
public class ConexionDatabase {

    public static Connection getConnection() {
        return ConexionPostgresDatabase.getConnection();
    }

    public static void closeConnection() {
        ConexionPostgresDatabase.closeConnection();
    }
}
