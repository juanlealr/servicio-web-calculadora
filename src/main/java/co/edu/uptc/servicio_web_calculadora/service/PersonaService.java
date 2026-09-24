package co.edu.uptc.servicio_web_calculadora.service;

import java.io.IOException;
import org.springframework.stereotype.Service;

import co.edu.uptc.servicio_web_calculadora.dto.PaginaResponseDTO;
import co.edu.uptc.servicio_web_calculadora.repository.PersonaRepository;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public PaginaResponseDTO obtenerPagina(int page, int size) throws IOException {
        if (page < 1) {
            throw new co.edu.uptc.servicio_web_calculadora.exception.InvalidPaginationException(
                    "El número de página debe ser mayor o igual a 1.");
        }
        if (size <= 0 || size > 1000) {
            throw new co.edu.uptc.servicio_web_calculadora.exception.InvalidPaginationException(
                    "El tamaño de la página debe estar entre 1 y 1000 registros para evitar desbordamiento de memoria.");
        }

        return personaRepository.obtenerPaginaPersonas(page, size);
    }

    public boolean editarPersona(String id, String nombre, String apellido) throws IOException {
        return personaRepository.editarPersona(id, nombre, apellido);
    }
}
