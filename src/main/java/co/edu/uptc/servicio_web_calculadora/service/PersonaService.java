package co.edu.uptc.servicio_web_calculadora.service;

import java.io.IOException;
import java.io.OutputStream;
import org.springframework.stereotype.Service;

import co.edu.uptc.servicio_web_calculadora.repository.PersonaRepository;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public void transmitirPersonas(OutputStream outputStream) throws IOException {
        personaRepository.transmitirTodasLasPersonas(outputStream);
    }
}
