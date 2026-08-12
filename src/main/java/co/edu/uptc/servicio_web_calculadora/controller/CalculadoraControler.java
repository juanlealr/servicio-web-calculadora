package co.edu.uptc.servicio_web_calculadora.controller;

import org.springframework.web.bind.annotation.RestController;

import co.edu.uptc.servicio_web_calculadora.service.CalculadoraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class CalculadoraControler {

    @Autowired
    private CalculadoraService calculadoraService;

    @GetMapping("/calcular")
    public String procesarCalculo(@RequestParam double num1, @RequestParam double num2, @RequestParam String operador) {
        try {
            double resultado = calculadoraService.realizarOperacion(num1, num2, operador);
            return "El resultado de la " + operador + " entre " + num1 + " y " + num2 + " es: " + resultado;
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }

    }
    
    
}
