package co.edu.uptc.servicio_web_calculadora.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import co.edu.uptc.servicio_web_calculadora.service.CalculadoraService;
import co.edu.uptc.servicio_web_calculadora.service.PersonaService;

@RestController
@RequestMapping("/api")
public class CalculadoraController {

    private final CalculadoraService calculadoraService;
    private final PersonaService personaService;

    public CalculadoraController(CalculadoraService calculadoraService, PersonaService personaService) {
        this.calculadoraService = calculadoraService;
        this.personaService = personaService;
    }

    // Endpoint de la calculadora...

    @GetMapping(value = "/personas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StreamingResponseBody> obtenerPersonas() {
        // Le indicamos a Spring que transmita los datos en flujo continuo
        StreamingResponseBody stream = outputStream -> {
            personaService.transmitirPersonas(outputStream);
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"personas.json\"")
                .body(stream);
    }
}
