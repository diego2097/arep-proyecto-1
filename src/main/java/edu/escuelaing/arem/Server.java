/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arem;

import static edu.escuelaing.arem.Server.controlRequests;
import edu.escuelaing.arem.annotations.Web;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 2125509
 */
public class Server {

    HashMap<String, StaticMethodHandler> dic = new <String, StaticMethodHandler>HashMap();
    private Integer port = Integer.parseInt(System.getenv("PORT"));
    
    /**
     * Este metodo se encarga de crear crear el servidor para que escuche por un determinado puerto. 
     */
    public void escuchar() {
        while (true) {
            ServerSocket serverSocket = createServer(port);
            Socket clientSocket = getClient(serverSocket);

            String path = controlRequests(clientSocket);
            System.out.println(path);
            write(clientSocket, path);
            try {
                clientSocket.close();
                serverSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Este metodo se encarga de inicializar el hashmap con los metodos de la clase App que tengan la 
     * anotacion @web. 
     */
    public void inicializar() {
        try {
            String clase = "edu.escuelaing.arem.App";
            Class<?> c = Class.forName(clase);
            for (Method m : c.getMethods()) {
                if (m.isAnnotationPresent(Web.class)) {
                    Class[] params = m.getParameterTypes();
                    dic.put(m.getName(),
                            new StaticMethodHandler(c.getDeclaredMethod(m.getName(), params)));
                }
            }
        } catch (Exception ex) {}
    }

    /**
     * Este metodo se encarga de leer los datos de entrada que envia el cliente. 
     * @param clientSocket
     * @return 
     */
    public static String controlRequests(Socket clientSocket) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        String inputLine;
        String path = "";
        try {
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
                if (inputLine.contains("GET")) {
                    String[] get = inputLine.split(" ");
                    path = get[1];
                } else if (inputLine.contains("POST")) {
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return path;
    }

    /**
     * Este metodo se encarga de crear la pagina html index. 
     * @param out PrintWriter buffer de salida para poder enviar datos al cliente. 
     */
    private static void index(PrintWriter out) {
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html" + "\r\n");
        out.println("<!DOCTYPE html>" + "\r\n");
        out.println("<html>" + "\r\n");
        out.println("<head>" + "\r\n");
        out.println("<meta charset=\"UTF-8\">" + "\r\n");
        out.println("<title>Proyecto</title>" + "\r\n");
        out.println("</head>" + "\r\n");
        out.println("<body>" + "\r\n");
        out.println("<h1>Proyecto arquitectura empresarial</h1>" + "\r\n");
        out.println("<p>Esta app es capaz de entregar paginas html e imagenes tipo PNG</p>");
        out.println("<ul>");
        out.println("<li><a href=\"/img1\">Desert Tree</a></li>");
        out.println("<li><a href=\"/img2\">Other image </a> </li>");
        out.println("<li><a href=\"/facebook\">Facebook</a> </li>");
        out.println("<li><a href=\"/github\">Github</a> </li>");
        out.println("</ul>");
        out.println("</body>" + "\r\n");
        out.println("</html>" + "\r\n");
        out.flush();
    }

    /**
     * Este metodo crea el servidor con el puerto definido 
     * @param i el puerto por donde se desea el servicio. 
     * @return 
     */
    private ServerSocket createServer(int puerto) {
        try {
            ServerSocket serverSocket = new ServerSocket(puerto);
            return serverSocket;
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
            return null;
        }

    }

    /**
     * Este metodo se encarga de esperar a que un cliente realice una peticion. y conectarse a este.  
     * @param serverSocket el servidor. 
     * @return Socket cliente que realizo la peticion. 
     */
    private Socket getClient(ServerSocket serverSocket) {
        try {
            System.out.println("Listo para recibir ...");
            Socket clientSocket = serverSocket.accept();
            return clientSocket;
        } catch (IOException e) {
            getClient(serverSocket);
        }
        return null;
    }

    /**
     * Este metodo se encarga de dirigir cada path a su metodo correspondiente
     * @param clientSocket El puerto cliente. 
     * @param path El path que solicita el cliente. 
     */
    private void write(Socket clientSocket, String path) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (path.contains("index")) {
            index(out);
        } else if (path.contains("img1")) {
            image1(out);
        } else if (path.contains("img2")) {
            image2(out);
        } else if (path.contains("facebook")) {
            facebook(out);
        } else if (path.contains("github")) {
            github(out);
        } else if (path.contains("services")) {
            services(out, path);
        }else{
            notFound(out);
        }
        

    }

    /**
     * Este metodo se encarga de desplegar una imagen en el browser. 
     * @param out PrintWriter buffer de salida para poder enviar datos al cliente. 
     */
    private void image1(PrintWriter out) {
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html + \r\n");
        out.println("<!DOCTYPE html>+ \r\n");
        out.println("<html> + \r\n");
        out.println("<head> + \r\n");
        out.println("<meta charset=\"UTF-8\">+ \r\n");
        out.println("<title>Proyecto</title> + \r\n");
        out.println("</head> + \r\n");
        out.println("<body> + \r\n");
        out.println("<img src=\"https://cdn.pixabay.com/photo/2018/03/03/03/11/tree-3194803_960_720.jpg\"></img> + \r\n");
        out.println("</body> + \r\n");
        out.println("</html> + \r\n");
        out.println("<a href=\"/index\">Volver</a>");
        out.flush();
    }

    /**
     * Este metodo se encarga de desplegar una imagen en el browser. 
     * @param out PrintWriter buffer de salida para poder enviar datos al cliente. 
     */
    private void image2(PrintWriter out) {
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html + \r\n");
        out.println("<!DOCTYPE html> + \r\n");
        out.println("<html> + \r\n");
        out.println("<head> + \r\n");
        out.println("<meta charset=\"UTF-8\"> + \r\n");
        out.println("<title>Proyecto</title> + \r\n");
        out.println("</head> + \r\n");
        out.println("<body> + \r\n");
        out.println("<img src=\"https://www.tekcrispy.com/wp-content/uploads/2017/12/bancos-imagenes-gratis.jpg\"></img>" + "\r\n");
        out.println("</body> + \r\n");
        out.println("</html> +\r\n");
        out.println("<a href=\"/index\">Volver</a>");
        out.flush();
    }

    /**
     * Este metodo se encarga de desplegar la pagina html de facebook. 
     * @param out PrintWriter buffer de salida para poder enviar datos al cliente. 
     */
    private void facebook(PrintWriter out) {
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html + \r\n");
        out.println("<a href=\"/index\">Volver</a>");
        URL url = null;
        try {
            url = new URL("https://www.facebook.com");
        } catch (MalformedURLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String inputLine = null;
            while ((inputLine = reader.readLine()) != null) {
                out.println(inputLine + "\r\n");
            }

        } catch (IOException x) {
        }
    }

    /**
     * Este metodo se encarga de desplegar la pagina html de twitter. 
     * @param out PrintWriter buffer de salida para poder enviar datos al cliente. 
     */
    private void github(PrintWriter out) {
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html + \r\n");
        out.println("<a href=\"/index\">Volver</a>");
        URL url = null;
        try {
            url = new URL("https://github.com");
        } catch (MalformedURLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String inputLine = null;
            while ((inputLine = reader.readLine()) != null) {
                out.println(inputLine + "\r\n");
            }
        } catch (IOException x) {
        }
    }

    /**
     * Este metodo se encarga de llamar a los pojos basado en la url solicitada. 
     * @param out PrintWriter buffer de salida para poder enviar datos al cliente. 
     * @param path path solicitado por el cliente. 
     */
    private void services(PrintWriter out, String path) {
        String[] url = path.split("/");
        String methodname = url[3];
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html + \r\n");
        if (methodname.contains(":")) {
            String[] cadena = methodname.split(":");
            String method = cadena[0]; 
            int temp = Integer.parseInt(cadena[1]);
            Object[] param = new Object[]{temp};
            try {
                out.println(dic.get(method).procesar(param));
            } catch (Exception ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                out.println(dic.get(methodname).procesar());
            } catch (Exception ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void notFound(PrintWriter out) {
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html + \r\n");
        out.println("<!DOCTYPE html> + \r\n");
        out.println("<html> + \r\n");
        out.println("<head> + \r\n");
        out.println("<meta charset=\"UTF-8\"> + \r\n");
        out.println("<title>Proyecto</title> + \r\n");
        out.println("</head> + \r\n");
        out.println("<body> + \r\n");
        out.println("<h1>Page not found</h1>" + "\r\n");
        out.println("</body> + \r\n");
        out.println("</html> +\r\n");
    }
}
