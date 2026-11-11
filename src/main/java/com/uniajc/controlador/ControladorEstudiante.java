package com.uniajc.controlador;

import java.util.List;
import java.util.Optional;
import com.uniajc.dao.EstudianteDao;
import com.uniajc.modelo.Estudiante;
import com.uniajc.servicios.EstudianteService;

/**
 * Capa de Controlador para la entidad Estudiante.
 * Orquesta la interacción entre las Vistas (Swing/Consola) y el Servicio de Negocio.
 */
public class ControladorEstudiante {

    private final EstudianteService estudianteService;

    public ControladorEstudiante() {
        this.estudianteService = new EstudianteService(new EstudianteDao());
    }

    public ControladorEstudiante(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    public void registrar(String nombre, String apellido, String email) {
        Estudiante nuevo = new Estudiante(nombre, apellido, email);
        estudianteService.registrarEstudiante(nuevo);
    }

    public List<Estudiante> listarTodos() {
        return estudianteService.obtenerTodosLosEstudiantes();
    }

    public Optional<Estudiante> buscarPorId(int id) {
        return estudianteService.buscarPorId(id);
    }

    public void actualizar(int id, String nombre, String apellido, String email) {
        Estudiante estudiante = new Estudiante(id, nombre, apellido, email);
        estudianteService.actualizarEstudiante(estudiante);
    }

    public void eliminar(int id) {
        estudianteService.eliminarEstudiante(id);
    }

    public EstudianteService getService() {
        return estudianteService;
    }
}
