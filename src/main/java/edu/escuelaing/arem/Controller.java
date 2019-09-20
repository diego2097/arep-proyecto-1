/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arem;

import edu.escuelaing.arem.server.Server;
import edu.escuelaing.arem.socket.ClientSocket;
import edu.escuelaing.arem.socket.SocketServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Esta clase es la encarga de controlar el programa.
 *
 * @author 2125509
 */
public class Controller {
    
    
    final static ExecutorService service = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        
        ServerSocket serverSocket = SocketServer.createServer();
        Server.inicializar();
        while (true) {
            Socket clientSocket = ClientSocket.getClient(serverSocket);
            service.execute(new Server(clientSocket));
        }
    }
}
