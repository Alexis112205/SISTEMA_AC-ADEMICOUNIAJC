package com.uniajc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.uniajc.config.ConexionPostgresDatabase;
import com.uniajc.modelo.Estudiante;

/**
 * Data Access Object (DAO) para la entidad {@link Estudiante}.
 * Encapsula todas las operaciones CRUD sobre la tabla "practica-mvc".estudiantes
 * en la base de datos PostgreSQL (Neon).
 */
public class EstudianteDao {

    private static final String TABLA = "\"practica-mvc\".estudiantes";

    /**
     * Inserta un nuevo estudiante en la base de datos.
     * Recupera y asigna el ID autogenerado al objeto estudiante.
     *
     * @param estudiante Objeto Estudiante con datos válidos
     * @throws RuntimeException si ocurre un error de SQL
     */
    public void guardar(Estudiante estudiante) {
        String sql = "INSERT INTO " + TABLA + " (name, lastname, email) VALUES (?, ?, ?)";

        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, estudiante.getName());
            pstmt.setString(2, estudiante.getLastname());
            pstmt.setString(3, estudiante.getEmail());

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        estudiante.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el estudiante en la base de datos: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene la lista completa de estudiantes registrados.
     *
     * @return Lista de estudiantes ordenados por ID
     */
    public List<Estudiante> obtenerTodos() {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT id, name, lastname, email FROM " + TABLA + " ORDER BY id ASC";

        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearEstudiante(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar todos los estudiantes: " + e.getMessage(), e);
        }
        return lista;
    }

    /**
     * Busca un estudiante por su clave primaria (ID).
     *
     * @param id Identificador único del estudiante
     * @return Optional con el estudiante si existe, o vacío si no se encuentra
     */
    public Optional<Estudiante> buscarPorId(int id) {
        String sql = "SELECT id, name, lastname, email FROM " + TABLA + " WHERE id = ?";

        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearEstudiante(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar estudiante por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    /**
     * Busca un estudiante por su correo electrónico.
     * Útil para validar que no existan correos duplicados.
     *
     * @param email Correo electrónico a buscar
     * @return Optional con el estudiante si existe, o vacío
     */
    public Optional<Estudiante> buscarPorEmail(String email) {
        String sql = "SELECT id, name, lastname, email FROM " + TABLA + " WHERE LOWER(email) = LOWER(?)";

        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearEstudiante(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar estudiante por email: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    /**
     * Actualiza los datos de un estudiante existente en la base de datos.
     *
     * @param estudiante Objeto Estudiante con datos actualizados y su ID existente
     * @throws RuntimeException si el estudiante no existe o hay error de SQL
     */
    public void actualizar(Estudiante estudiante) {
        String sql = "UPDATE " + TABLA + " SET name = ?, lastname = ?, email = ? WHERE id = ?";

        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, estudiante.getName());
            pstmt.setString(2, estudiante.getLastname());
            pstmt.setString(3, estudiante.getEmail());
            pstmt.setInt(4, estudiante.getId());

            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new RuntimeException("No se encontró ningún estudiante para actualizar con ID: " + estudiante.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estudiante: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un estudiante de la base de datos por su ID.
     *
     * @param id Identificador del estudiante a eliminar
     * @throws RuntimeException si el estudiante no existe o hay error de clave foránea
     */
    public void eliminar(int id) {
        String sql = "DELETE FROM " + TABLA + " WHERE id = ?";

        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new RuntimeException("No se encontró ningún estudiante para eliminar con ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar estudiante: " + e.getMessage(), e);
        }
    }

    /**
     * Mapea una fila de un ResultSet a un objeto Estudiante.
     *
     * @param rs ResultSet posicionado en la fila actual
     * @return Objeto Estudiante mapeado
     * @throws SQLException si ocurre un error al leer las columnas
     */
    private Estudiante mapearEstudiante(ResultSet rs) throws SQLException {
        return new Estudiante(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("lastname"),
                rs.getString("email")
        );
    }
}
