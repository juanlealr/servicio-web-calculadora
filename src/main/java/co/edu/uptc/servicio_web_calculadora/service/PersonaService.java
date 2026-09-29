package co.edu.uptc.servicio_web_calculadora.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import co.edu.uptc.servicio_web_calculadora.dto.PaginaResponseDTO;
import co.edu.uptc.servicio_web_calculadora.dto.PersonaDTO;
import co.edu.uptc.servicio_web_calculadora.exception.InvalidPaginationException;
import co.edu.uptc.servicio_web_calculadora.model.Persona;
import co.edu.uptc.servicio_web_calculadora.repository.PersonaDbRepository;
import co.edu.uptc.servicio_web_calculadora.repository.PersonaRepository;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository; // Lectura CSV / NFS
    private final PersonaDbRepository personaDbRepository; // Lectura PostgreSQL

    public PersonaService(PersonaRepository personaRepository, PersonaDbRepository personaDbRepository) {
        this.personaRepository = personaRepository;
        this.personaDbRepository = personaDbRepository;
    }

    // --- LÓGICA EXISTENTE PARA CSV ---
    public PaginaResponseDTO obtenerPagina(int page, int size) throws IOException {
        if (page < 1) {
            throw new InvalidPaginationException("El número de página debe ser mayor o igual a 1.");
        }
        if (size <= 0 || size > 1000) {
            throw new InvalidPaginationException("El tamaño de la página debe estar entre 1 y 1000.");
        }

        return personaRepository.obtenerPaginaPersonas(page, size);
    }

    public boolean editarPersona(String id, String nombre, String apellido) throws IOException {
        return personaRepository.editarPersona(id, nombre, apellido);
    }

    // --- NUEVA LÓGICA PARA LA BASE DE DATOS POSTGRESQL ---
    public PaginaResponseDTO obtenerPaginaDb(int page, int size) {
        if (page < 1) {
            throw new InvalidPaginationException("El número de página debe ser mayor o igual a 1.");
        }
        if (size <= 0 || size > 1000) {
            throw new InvalidPaginationException("El tamaño de la página debe estar entre 1 y 1000.");
        }

        Page<Persona> paginaDb = personaDbRepository.findAll(PageRequest.of(page - 1, size));

        List<PersonaDTO> personasDTO = paginaDb.getContent().stream()
                .map(p -> new PersonaDTO(p.getId(), p.getNombre(), p.getApellido()))
                .toList();

        String contenedorId = System.getenv().getOrDefault("HOSTNAME", "Desconocido");
        String contenedorNombre = System.getenv().getOrDefault("CONTAINER_NAME", contenedorId);
        String contenedorIp = "Desconocida";

        try {
            contenedorIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ignored) {
        }

        return new PaginaResponseDTO(
                paginaDb.getTotalElements(),
                paginaDb.getNumberOfElements(),
                page,
                paginaDb.getTotalPages(),
                contenedorId,
                contenedorNombre,
                contenedorIp,
                personasDTO);
    }
}
