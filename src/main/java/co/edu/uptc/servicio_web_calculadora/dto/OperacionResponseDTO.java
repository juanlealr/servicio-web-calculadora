package co.edu.uptc.servicio_web_calculadora.dto;

public record OperacionResponseDTO(
    double num1,
    double num2,
    String operador,
    double resultado,
    String mensaje
) {}
