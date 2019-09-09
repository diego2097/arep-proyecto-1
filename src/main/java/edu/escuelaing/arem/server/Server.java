/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arem.server;

import edu.escuelaing.arem.framework.Handler;
import edu.escuelaing.arem.framework.StaticMethodHandler;
import static edu.escuelaing.arem.server.Server.controlRequests;
import edu.escuelaing.arem.framework.Web;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jdk.nashorn.internal.objects.Global.load;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

/**
 *
 * @author 2125509
 */
public class Server {

    HashMap<String, StaticMethodHandler> dic = new <String, StaticMethodHandler>HashMap();
    //  private Integer port = Integer.parseInt(System.getenv("PORT"));

    /**
     * Este metodo se encarga de crear crear el servidor para que escuche por un
     * determinado puerto.
     */
    public void escuchar() {
        for (;;) {
            ServerSocket serverSocket = createServer(getPort());
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
     * Este metodo se encarga de inicializar el hashmap con los metodos de la
     * clase App que tengan la anotacion @web.
     */
    public void inicializar() {
        try {
            File f = new File(System.getProperty("user.dir") + "/src/main/java/edu/escuelaing/arem/app");
            File[] ficheros = f.listFiles();
            //Reflections reflections = new Reflections("apps", new SubTypesScanner(false));
            //Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);
            for (File fs : ficheros) {
                String name = fs.getName();
                name = "edu.escuelaing.arem.app." + name.substring(0, name.indexOf("."));
                Class<?> c = Class.forName(name);
                for (Method m : c.getMethods()) {
                    if (m.getAnnotations().length > 0) {
                        StaticMethodHandler handler = new StaticMethodHandler(m);
                        dic.put(m.getName(), handler);
                    }
                }
            }
        } catch (Exception ex) {
        }
    }

    /**
     * Este metodo se encarga de leer los datos de entrada que envia el cliente.
     *
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
     *
     * @param out PrintWriter buffer de salida para poder enviar datos al
     * cliente.
     */
    private static void index(PrintWriter out) {
        out.print("HTTP/1.1 200 OK \r");
        out.print("Content-Type: text/html \r\n");
        out.print("\r\n");
        out.print("<!DOCTYPE html>");
        out.print("<html>");
        out.print("<head>");
        out.print("<meta charset=\"UTF-8\">");
        out.print("<title>Proyecto</title>");
        out.print("</head>");
        out.print("<body>");
        out.print("<h1>Proyecto arquitectura empresarial</h1>");
        out.print("<p>Esta app es capaz de entregar paginas html e imagenes tipo PNG</p>");
        out.print("<ul>");
        out.print("<li><a href=\"/img1.PNG\">Desert Tree</a></li>");
        out.print("<li><a href=\"/img2.PNG\">Other image </a> </li>");
        out.print("<li><a href=\"/facebook.html\">Facebook</a> </li>");
        out.print("<li><a href=\"/github.html\">Github</a> </li>");
        out.print("<li><a href=\"/cuadrado.html\">App para hallar el cuadrado de un numero</a> </li>");
        out.print("<li><a href=\"/cubo.html\">App para hallar el cubo de un numero</a> </li>");
        out.print("</ul>");
        out.print("</body>");
        out.print("</html>");
        out.flush();
    }

    /**
     * Este metodo crea el servidor con el puerto definido
     *
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
     * Este metodo se encarga de esperar a que un cliente realice una peticion.
     * y conectarse a este.
     *
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
     *
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
        if (path.equals("/index.html")) {
            index(out);
        } else if (path.equals("/img1.PNG")) {
            image1(out);
        } else if (path.equals("/img2.PNG")) {
            image2(out);
        } else if (path.equals("/facebook.html")) {
            facebook(out);
        } else if (path.equals("/github.html")) {
            github(out);
        } else if (path.equals("/cuadrado.html")) {
            cuadrado(out);
        } else if (path.contains("/resultado.html")) {
            resultado(out, path,"cuadrado");
        } else if (path.equals("/cubo.html")) {
            cubo(out);
        } else if (path.contains("/resultado2.html")) {
            resultado(out, path,"cubo");
        }else {
            notFound(out);
        }

    }

    /**
     * Este metodo se encarga de desplegar una imagen en el browser.
     *
     * @param out PrintWriter buffer de salida para poder enviar datos al
     * cliente.
     */
    private void image1(PrintWriter out) {
        out.print("HTTP/1.1 200 OK \r");
        out.print("Content-Type: text/html \r\n");
        out.print("\r\n");
        out.print("<!DOCTYPE html>");
        out.print("<html>");
        out.print("<head>");
        out.print("<meta charset=\"UTF-8\">");
        out.print("<title>Proyecto</title>");
        out.print("</head>");
        out.print("<body>");
        out.print("<img src=\"https://cdn.pixabay.com/photo/2018/03/03/03/11/tree-3194803_960_720.jpg\"></img>");
        out.print("</body>");
        out.print("</html>");
        out.print("<a href=\"/index.html\">Volver</a>");
        out.flush();
    }

    /**
     * Este metodo se encarga de desplegar una imagen en el browser.
     *
     * @param out PrintWriter buffer de salida para poder enviar datos al
     * cliente.
     */
    private void image2(PrintWriter out) {
        out.print("HTTP/1.1 200 OK \r");
        out.print("Content-Type: text/html \r\n");
        out.print("\r\n");
        out.print("<!DOCTYPE html>");
        out.print("<html>");
        out.print("<head>");
        out.print("<meta charset=\"UTF-8\">");
        out.print("<title>Proyecto</title>");
        out.print("</head>");
        out.print("<body>");
        out.print("<img src=\"https://www.tekcrispy.com/wp-content/uploads/2017/12/bancos-imagenes-gratis.jpg\"></img>" + "\r\n");
        out.print("</body>");
        out.print("</html>");
        out.print("<a href=\"/index.html\">Volver</a>");
        out.flush();
    }

    /**
     * Este metodo se encarga de desplegar la pagina html de facebook.
     *
     * @param out PrintWriter buffer de salida para poder enviar datos al
     * cliente.
     */
    private void facebook(PrintWriter out) {
        out.print("HTTP/1.1 200 OK \r");
        out.print("Content-Type: text/html \r\n");
        out.print("\r\n");
        out.print("<a href=\"/index.html\">Volver</a>");
        URL url = null;
        try {
            url = new URL("https://www.facebook.com");
        } catch (MalformedURLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String inputLine = null;
            while ((inputLine = reader.readLine()) != null) {
                out.println(inputLine);
            }

        } catch (IOException x) {
        }
        out.flush();
    }

    /**
     * Este metodo se encarga de desplegar la pagina html de twitter.
     *
     * @param out PrintWriter buffer de salida para poder enviar datos al
     * cliente.
     */
    private void github(PrintWriter out) {
        out.print("HTTP/1.1 200 OK \r");
        out.print("Content-Type: text/html \r\n");
        out.print("\r\n");
        out.print("<a href=\"/index.html\">Volver</a>");
        URL url = null;
        try {
            url = new URL("https://github.com");
        } catch (MalformedURLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String inputLine = null;
            while ((inputLine = reader.readLine()) != null) {
                out.println(inputLine);
            }
        } catch (IOException x) {
        }
        out.flush();
    }

    /**
     * Este metodo se encarga de llamar a los pojos basado en la url solicitada.
     *
     * @param out PrintWriter buffer de salida para poder enviar datos al
     * cliente.
     * @param path path solicitado por el cliente.
     */
    private String services(PrintWriter out, String path, String metodo) {
        String[] url = path.split("=");
        String atributo = url[1];
        int temp = Integer.parseInt(atributo);
        Object[] param = new Object[]{temp};
        try {
            return (dic.get(metodo).procesar(param));
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


    private void notFound(PrintWriter out) {
        out.print("HTTP/1.1 200 OK \r");
        out.print("Content-Type: text/html \r\n");
        out.print("\r\n");
        out.print("<!DOCTYPE html>");
        out.print("<html>");
        out.print("<head>");
        out.print("<meta charset=\"UTF-8\">");
        out.print("<title>Proyecto</title> ");
        out.print("</head>");
        out.print("<body>");
        out.print("<h1>Page not found</h1>");
        out.print("</body>");
        out.print("</html>");
        out.flush();
    }

    private static int getPort() {

        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set (i.e.on localhost)
    }

    private void cuadrado(PrintWriter out) {
        out.print("HTTP/1.1 200 OK \r");
        out.print("Content-Type: text/html \r\n");
        out.print("\r\n");
        out.print("<!DOCTYPE html>");
        out.print("<html>");
        out.print("<head>");
        out.print("<meta charset=\"UTF-8\">");
        out.print("<title>Proyecto</title>");
        out.print("</head>");
        out.print("<body>");
        out.print("<h1>Proyecto arquitectura empresarial</h1>");
        out.print("<h2>Pojo para el cuadrado de un numero</h2>");
        out.print("<form action=\"/resultado.html\" >");
        out.print("Ingrese un numero: <input type=\"text\" name=\"numero\"><br>");
        out.print("<button type=\"submit\" value=\"Calcular\">Calcular cuadrado</button>");
        out.print("</form>");
        out.print("</body>");
        out.print("</html>");
        out.flush();
    }

    private void cubo(PrintWriter out) {
        out.print("HTTP/1.1 200 OK \r");
        out.print("Content-Type: text/html \r\n");
        out.print("\r\n");
        out.print("<!DOCTYPE html>");
        out.print("<html>");
        out.print("<head>");
        out.print("<meta charset=\"UTF-8\">");
        out.print("<title>Proyecto</title>");
        out.print("</head>");
        out.print("<body>");
        out.print("<h1>Proyecto arquitectura empresarial</h1>");
        out.print("<h2>Pojo para el cubo de un numero</h2>");
        out.print("<form action=\"/resultado2.html\" >");
        out.print("Ingrese un numero: <input type=\"text\" name=\"fname\"><br>");
        out.print("<button type=\"submit\">Calcular cubo</button>");
        out.print("</body>");
        out.print("</html>");
        out.flush();
    }

    private void resultado(PrintWriter out, String path,String pojo) {
        String resultado = services(out,path,pojo); 
        out.print("HTTP/1.1 200 OK \r");
        out.print("Content-Type: text/html \r\n");
        out.print("\r\n");
        out.print("<!DOCTYPE html>");
        out.print("<html>");
        out.print("<head>");
        out.print("<meta charset=\"UTF-8\">");
        out.print("<title>Proyecto</title> ");
        out.print("</head>");
        out.print("<body>");
        out.print("<h1>result</h1>");
        out.print(resultado);
        out.print("</body>");
        out.print("</html>");
        out.flush();

    }

}
