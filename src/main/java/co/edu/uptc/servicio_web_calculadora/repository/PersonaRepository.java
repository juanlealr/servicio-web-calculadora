package co.edu.uptc.servicio_web_calculadora.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;

import co.edu.uptc.servicio_web_calculadora.dto.PersonaDTO;
import co.edu.uptc.servicio_web_calculadora.dto.PaginaResponseDTO;

@Repository
public class PersonaRepository {

    private String obtenerRutaArchivo() {
        File archivoContainer = new File("/app/personas.csv");
        if (archivoContainer.exists()) {
            return "/app/personas.csv";
        }
        return "personas.csv";
    }

    // 1. Método para obtener una página específica
    public PaginaResponseDTO obtenerPaginaPersonas(int page, int size) throws IOException {
        Path path = Paths.get(obtenerRutaArchivo());

        if (!Files.exists(path)) {
            return new PaginaResponseDTO(0, 0, page, 0, new ArrayList<>());
        }

        long totalRegistros = 0;

        // Contar el total de líneas (rápido gracias a los Streams)
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            totalRegistros = lines.count() - 1; // Restamos el encabezado
        }

        List<PersonaDTO> personasPagina = new ArrayList<>();
        int totalPaginas = (int) Math.ceil((double) totalRegistros / size);
        long registrosASaltar = 1 + (long) (page - 1) * size; // +1 por el encabezado

        // Leer solo la porción de datos necesaria
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            lines.skip(registrosASaltar)
                    .limit(size)
                    .forEach(linea -> {
                        if (!linea.trim().isEmpty()) {
                            String[] datos = linea.split(",");
                            if (datos.length >= 3) {
                                personasPagina.add(new PersonaDTO(datos[0].trim(), datos[1].trim(), datos[2].trim()));
                            }
                        }
                    });
        }

        return new PaginaResponseDTO(
                totalRegistros,
                personasPagina.size(),
                page,
                totalPaginas,
                personasPagina);
    }

    // 2. Método para editar un registro específico de forma segura
    public synchronized boolean editarPersona(String id, String nuevoNombre, String nuevoApellido) throws IOException {
        Path original = Paths.get(obtenerRutaArchivo());
        Path temporal = Paths.get(obtenerRutaArchivo() + ".tmp");
        boolean encontrado = false;

        if (!Files.exists(original))
            return false;

        // Leemos el original y escribimos en un temporal simultáneamente
        try (BufferedReader reader = Files.newBufferedReader(original, StandardCharsets.UTF_8);
                BufferedWriter writer = Files.newBufferedWriter(temporal, StandardCharsets.UTF_8)) {

            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty())
                    continue;

                String[] datos = linea.split(",");
                // Si encontramos el ID, escribimos la nueva línea en vez de la vieja
                if (!encontrado && datos.length >= 3 && datos[0].trim().equals(id)) {
                    writer.write(id + "," + nuevoNombre + "," + nuevoApellido + "\n");
                    encontrado = true;
                } else {
                    writer.write(linea + "\n");
                }
            }
        }

        // Si se encontró y modificó, reemplazamos el archivo original (atómico en
        // Linux/NFS)
        if (encontrado) {
            Files.move(temporal, original, StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.deleteIfExists(temporal); // Limpiamos la basura si el ID no existía
        }

        return encontrado;
    }
}
