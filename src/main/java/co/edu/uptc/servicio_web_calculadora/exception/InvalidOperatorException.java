package co.edu.uptc.servicio_web_calculadora.exception;

public class InvalidOperatorException extends RuntimeException {
    public InvalidOperatorException(String message) {
        super(message);
    }
}
