package co.edu.uptc.servicio_web_calculadora.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uptc.servicio_web_calculadora.dto.OperacionResponseDTO;
import co.edu.uptc.servicio_web_calculadora.dto.PersonaDTO;
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

    @GetMapping("/calcular")
    public ResponseEntity<OperacionResponseDTO> procesarCalculo(
            @RequestParam double num1,
            @RequestParam double num2,
            @RequestParam String operador) {

        double resultado = calculadoraService.realizarOperacion(num1, num2, operador);
        String mensaje = "El resultado de la " + operador + " entre " + num1 + " y " + num2 + " es: " + resultado;

        return ResponseEntity.ok(new OperacionResponseDTO(num1, num2, operador, resultado, mensaje));
    }

    @GetMapping("/personas")
    public ResponseEntity<List<PersonaDTO>> obtenerPersonas() {
        List<PersonaDTO> personas = personaService.listarPersonas();
        return ResponseEntity.ok(personas);
    }
}
