package co.edu.uptc.servicio_web_calculadora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uptc.servicio_web_calculadora.model.Operacion;
import co.edu.uptc.servicio_web_calculadora.repository.ArchivoRepository;

@Service
public class CalculadoraService {

    @Autowired
    private ArchivoRepository archivoRepository;
    
    public double realizarOperacion(double num1, double num2, String operador){

        double resultado = 0;

        switch (operador.toLowerCase()) {
            case "suma":
                resultado = num1 + num2;
                break;
            
            case "resta":
                resultado = num1 - num2;
                break;

            case "multiplicacion":
                resultado = num1 * num2;
                break;

            case "division":
                if (num2 == 0) {
                    throw new IllegalArgumentException("Error: No se puede dividir por 0.");
                }
                resultado = num1 / num2;
                break;
        
            default:
                throw new IllegalArgumentException("Error: Operación no válida. Usa suma, resta, multiplicacion o division.");
        
        }
        
        Operacion op = new Operacion(num1, num2, operador, resultado);
        
        archivoRepository.guardarOperacion(op);

        return resultado;
        
    }
}
