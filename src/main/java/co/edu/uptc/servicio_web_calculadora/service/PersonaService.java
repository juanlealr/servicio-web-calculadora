package co.edu.uptc.servicio_web_calculadora.service;

import java.util.List;
import org.springframework.stereotype.Service;

import co.edu.uptc.servicio_web_calculadora.dto.PersonaDTO;
import co.edu.uptc.servicio_web_calculadora.repository.PersonaRepository;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public List<PersonaDTO> listarPersonas() {
        return personaRepository.obtenerTodas();
    }
}
