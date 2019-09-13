/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arem.app;

import edu.escuelaing.arem.framework.Web;
import static java.lang.Math.pow;

/**
 *
 * @author 2125509
 */
public class App2 {
    @Web("cuadrado2")
    public static String cuadrado2(Integer numero) {
        return "<!DOCTYPE html>\n" + 
        		"<html>\n" + 
        		"<head>\n" + 
        		"  <meta charset=\"utf-8\" />\n" + 
        		"  <title>ARSW - Laboratorio 1</title>  \n" + 
        		"</head>\n" + 
        		"<body>   \n" + 
        		"<h1>"+numero+" al cuadrado es "+(int)pow(numero,2)+"</h1>  \n" + 
        		"</body>\n" + 
        		"</html>\n" + 
        		"";
    }
    
     @Web("coseno")
    public static String coseno(Integer numero) {
        return "<!DOCTYPE html>\n" + 
        		"<html>\n" + 
        		"<head>\n" + 
        		"  <meta charset=\"utf-8\" />\n" + 
        		"  <title>ARSW - Laboratorio 1</title>  \n" + 
        		"</head>\n" + 
        		"<body>   \n" + 
        		"<h1>"+numero+" al cuadrado es "+(int)pow(numero,2)+"</h1>  \n" + 
        		"</body>\n" + 
        		"</html>\n" + 
        		"";
    }
}
