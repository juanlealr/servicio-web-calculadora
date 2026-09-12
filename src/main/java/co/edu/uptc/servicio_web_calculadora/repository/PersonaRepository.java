package co.edu.uptc.servicio_web_calculadora.repository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;

@Repository
public class PersonaRepository {

    private String obtenerRutaArchivo() {
        File archivoContainer = new File("/app/personas.csv");
        if (archivoContainer.exists()) {
            return "/app/personas.csv";
        }
        return "personas.csv";
    }

    public void transmitirTodasLasPersonas(OutputStream outputStream) throws IOException {
        Path path = Paths.get(obtenerRutaArchivo());

        if (!Files.exists(path)) {
            outputStream.write("[]".getBytes(StandardCharsets.UTF_8));
            return;
        }

        // Usamos BufferedWriter para transmitir por bloques directamente al cable de
        // red
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                Stream<String> lines = Files.lines(path)) {

            writer.write("[\n"); // Inicio del arreglo JSON

            Iterator<String> iterator = lines.skip(1).iterator(); // Omitir encabezado
            boolean primeraLinea = true;

            while (iterator.hasNext()) {
                String linea = iterator.next();
                if (linea.trim().isEmpty())
                    continue;

                String[] datos = linea.split(",");
                if (datos.length >= 3) {
                    if (!primeraLinea) {
                        writer.write(",\n");
                    }

                    // Escribir directamente el JSON de cada persona
                    String jsonPersona = String.format(
                            "{\"id\":\"%s\",\"nombre\":\"%s\",\"apellido\":\"%s\"}",
                            datos[0].trim(), datos[1].trim(), datos[2].trim());

                    writer.write(jsonPersona);
                    primeraLinea = false;
                }
            }

            writer.write("\n]"); // Fin del arreglo JSON
            writer.flush(); // Asegura el envío de los últimos datos
        }
    }
}
