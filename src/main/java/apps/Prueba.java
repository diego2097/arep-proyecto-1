/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps;

/**
 *
 * @author 2125509
 */
public class Prueba {
    
    public static String getHtml(){
        return "<!DOCTYPE html>\n" 
                + "<html>\n" 
                + "<body>\n"
                + "<h1>Calculadora de la media y desviacion estandar</h1>\n"
                + "<p>Dado un conjunto de n numeros, este programa calcula la desviacion y media del conjunto</p>\n"
                + "<p>Los numeros deben estar separados por comas</p>\n"
                + "<form action=\"/respuesta\">"
                + "Ingrese sus datos:<br>\n"
                + "<input type=\"text\" name=\"numeros\" placeholder=\"numeros\"><br>\n"
                + "<input type=\"submit\" value=\"Calcular\">\n"
                + "</form>\n"
                + "</body>\n" 
                + "</html>\n";

    }
    
}
