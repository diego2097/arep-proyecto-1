/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arem.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 *
 * @author 2125509
 */
public class SocketServer {
       /**
     * Este metodo crea el servidor con el puerto definido
     *
     * @param i el puerto por donde se desea el servicio.
     * @return
     */
    public static ServerSocket createServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(getPort());
            return serverSocket;
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4567.");
            System.exit(1);
            return null;
        }

    }
    
    
    /**
     * Este metodo retorna el puerto por default o el que el sistema considere. 
     * @return int puerto definido
     */ 
    private static int getPort() {

        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set (i.e.on localhost)
    }
    
    

}
