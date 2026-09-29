package co.edu.uptc.servicio_web_calculadora.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uptc.servicio_web_calculadora.dto.OperacionResponseDTO;
import co.edu.uptc.servicio_web_calculadora.dto.PaginaResponseDTO;
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

    // 1. ENDPOINT ORIGINAL (Lee personas.csv vía NFS)
    @GetMapping(value = "/personas")
    public ResponseEntity<PaginaResponseDTO> obtenerPersonasPaginadas(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int size) throws IOException {

        PaginaResponseDTO respuesta = personaService.obtenerPagina(page, size);
        return ResponseEntity.ok(respuesta);
    }

    // 2. NUEVO ENDPOINT (Lee directamente la Base de Datos PostgreSQL)
    @GetMapping(value = "/personas-db")
    public ResponseEntity<PaginaResponseDTO> obtenerPersonasDbPaginadas(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int size) {

        PaginaResponseDTO respuesta = personaService.obtenerPaginaDb(page, size);
        return ResponseEntity.ok(respuesta);
    }

    // 3. EDITAR ORIGINAL (Modifica personas.csv vía NFS)
    @PutMapping(value = "/personas/editar")
    public ResponseEntity<Object> editarPersona(
            @RequestParam String id,
            @RequestParam String nombre,
            @RequestParam String apellido) throws IOException {

        String regexLetras = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$";

        if (!nombre.matches(regexLetras) || !apellido.matches(regexLetras)) {
            throw new co.edu.uptc.servicio_web_calculadora.exception.InvalidPersonaDataException(
                    "El nombre y el apellido solo pueden contener letras y espacios.");
        }

        boolean editado = personaService.editarPersona(id, nombre, apellido);

        if (!editado) {
            throw new co.edu.uptc.servicio_web_calculadora.exception.PersonaNotFoundException(
                    "No se encontró un registro con el ID especificado: " + id);
        }

        return ResponseEntity.ok(Map.of(
                "mensaje", "Registro actualizado correctamente",
                "id", id,
                "nuevoNombre", nombre,
                "nuevoApellido", apellido));
    }
}
