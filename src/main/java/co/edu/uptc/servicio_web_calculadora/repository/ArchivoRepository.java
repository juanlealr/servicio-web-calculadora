package co.edu.uptc.servicio_web_calculadora.repository;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.stereotype.Repository;

import co.edu.uptc.servicio_web_calculadora.model.Operacion;

@Repository
public class ArchivoRepository {

    private final String RUTA_ARCHIVO = "historial_operaciones.txt";

    public void guardarOperacion(Operacion operacion){
        try (FileWriter fw = new FileWriter(RUTA_ARCHIVO, true);
            PrintWriter pw = new PrintWriter(fw)) {
            
            pw.println(operacion.toString());
            System.out.println("Operación guardada en el archivo con éxito.");
        } catch (IOException e) {
            System.err.println("Error al guardar en el archivo: " + e.getMessage());
        }
    }
}
