/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arem;

/**
 * Esta clase es la encarga de controlar el programa. 
 * @author 2125509
 */
public class Controller {
    
     public static void main( String[] args )
    {
        Server app = new Server();
        app.inicializar();
        app.escuchar();
    }
}
