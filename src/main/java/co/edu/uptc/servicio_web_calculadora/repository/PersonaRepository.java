package co.edu.uptc.servicio_web_calculadora.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import co.edu.uptc.servicio_web_calculadora.dto.PersonaDTO;

@Repository
public class PersonaRepository {

    private String obtenerRutaArchivo() {
        File archivoContainer = new File("/app/personas.csv");
        if (archivoContainer.exists()) {
            return "/app/personas.csv";
        }
        return "personas.csv";
    }

    public List<PersonaDTO> obtenerTodas() {
        List<PersonaDTO> personas = new ArrayList<>();
        String ruta = obtenerRutaArchivo();
        File archivo = new File(ruta);

        if (!archivo.exists()) {
            System.err.println("El archivo CSV no existe en la ruta: " + ruta);
            return personas;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean esPrimeraLinea = true;

            while ((linea = br.readLine()) != null) {
                if (esPrimeraLinea && (linea.toLowerCase().contains("id") || linea.toLowerCase().contains("nombre"))) {
                    esPrimeraLinea = false;
                    continue;
                }
                esPrimeraLinea = false;

                if (linea.trim().isEmpty())
                    continue;

                String[] datos = linea.split(",");
                if (datos.length >= 3) {
                    personas.add(new PersonaDTO(datos[0].trim(), datos[1].trim(), datos[2].trim()));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }

        return personas;
    }
}
