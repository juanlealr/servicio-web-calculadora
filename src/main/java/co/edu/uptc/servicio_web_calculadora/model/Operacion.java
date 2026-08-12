package co.edu.uptc.servicio_web_calculadora.model;

public class Operacion {
    private double num1;
    private double num2;
    private String operador;
    private double resultado;

    public Operacion(double num1, double num2, String operador, double resultado) {
        this.num1 = num1;
        this.num2 = num2;
        this.operador = operador;
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return num1 + " | " + operador + " | " + num2 + " = " + resultado;
    }
}
