package com.uniajc.servicios;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import com.uniajc.dao.EstudianteDao;
import com.uniajc.modelo.Estudiante;

/**
 * Capa de Servicios para la entidad Estudiante.
 * Contiene la lógica de negocio y las validaciones de datos antes de interactuar con el DAO.
 */
public class EstudianteService {

    private static final Pattern EMAIL_PATTERN = 
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final EstudianteDao estudianteDao;

    public EstudianteService(EstudianteDao estudianteDao) {
        if (estudianteDao == null) {
            throw new IllegalArgumentException("El DAO no puede ser nulo.");
        }
        this.estudianteDao = estudianteDao;
    }

    public void registrarEstudiante(Estudiante estudiante) {
        validarEstudiante(estudiante);

        // Validar unicidad del correo
        Optional<Estudiante> existente = estudianteDao.buscarPorEmail(estudiante.getEmail());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un estudiante registrado con el correo: " + estudiante.getEmail());
        }

        estudianteDao.guardar(estudiante);
    }

    public List<Estudiante> obtenerTodosLosEstudiantes() {
        return estudianteDao.obtenerTodos();
    }

    public Optional<Estudiante> buscarEstudiantePorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a cero.");
        }
        return estudianteDao.buscarPorId(id);
    }

    public Optional<Estudiante> buscarPorId(int id) {
        return buscarEstudiantePorId(id);
    }


    public void actualizarEstudiante(Estudiante estudiante) {
        if (estudiante == null || estudiante.getId() <= 0) {
            throw new IllegalArgumentException("Estudiante inválido para actualizar.");
        }

        Optional<Estudiante> existente = estudianteDao.buscarPorId(estudiante.getId());
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("No se encontró ningún estudiante con ID: " + estudiante.getId());
        }

        validarEstudiante(estudiante);

        // Verificar que si cambia el correo, no colisione con el de otro estudiante
        Optional<Estudiante> porEmail = estudianteDao.buscarPorEmail(estudiante.getEmail());
        if (porEmail.isPresent() && porEmail.get().getId() != estudiante.getId()) {
            throw new IllegalArgumentException("El correo ya se encuentra en uso por otro estudiante.");
        }

        estudianteDao.actualizar(estudiante);
    }

    public void eliminarEstudiante(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a cero.");
        }

        Optional<Estudiante> existente = estudianteDao.buscarPorId(id);
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("No se encontró ningún estudiante con ID: " + id);
        }

        estudianteDao.eliminar(id);
    }

    private void validarEstudiante(Estudiante e) {
        if (e == null) {
            throw new IllegalArgumentException("El objeto estudiante no puede ser nulo.");
        }
        if (e.getName() == null || e.getName().trim().length() < 2) {
            throw new IllegalArgumentException("El nombre es obligatorio y debe tener al menos 2 caracteres.");
        }
        if (e.getLastname() == null || e.getLastname().trim().length() < 2) {
            throw new IllegalArgumentException("El apellido es obligatorio y debe tener al menos 2 caracteres.");
        }
        if (e.getEmail() == null || !EMAIL_PATTERN.matcher(e.getEmail().trim()).matches()) {
            throw new IllegalArgumentException("El formato del correo electrónico es inválido.");
        }
    }
}
