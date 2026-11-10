package com.uniajc.modelo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entidad de dominio para representar a un Estudiante en el Sistema Académico UNIAJC.
 * Cumple con el estándar Java Bean (POJO) y mapea con la tabla "practica-mvc".estudiantes.
 */
public class Estudiante implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String lastname;
    private String email;

    /**
     * Constructor por defecto (requerido por especificación JavaBeans).
     */
    public Estudiante() {
    }

    /**
     * Constructor para registrar un nuevo estudiante (sin ID generado por la base de datos).
     *
     * @param name     Nombre del estudiante
     * @param lastname Apellido del estudiante
     * @param email    Correo institucional o personal
     */
    public Estudiante(String name, String lastname, String email) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
    }

    /**
     * Constructor completo (para mapeo desde la base de datos).
     *
     * @param id       Identificador único (clave primaria)
     * @param name     Nombre del estudiante
     * @param lastname Apellido del estudiante
     * @param email    Correo electrónico
     */
    public Estudiante(int id, String name, String lastname, String email) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
    }

    // ==========================================
    // GETTERS Y SETTERS PRINCIPALES
    // ==========================================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // ==========================================
    // ALIASES EN ESPAÑOL (Compatibilidad Guía UNIAJC)
    // ==========================================

    public String getNombre() {
        return name;
    }

    public void setNombre(String nombre) {
        this.name = nombre;
    }

    public String getApellido() {
        return lastname;
    }

    public void setApellido(String apellido) {
        this.lastname = apellido;
    }

    // ==========================================
    // METODOS AUXILIARES
    // ==========================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estudiante that = (Estudiante) o;
        return id == that.id && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
