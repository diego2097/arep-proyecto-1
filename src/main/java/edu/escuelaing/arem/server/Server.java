/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arem.server;

import edu.escuelaing.arem.framework.StaticMethodHandler;
import static edu.escuelaing.arem.server.Server.controlRequests;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
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
import javax.imageio.ImageIO;

/**
 *
 * @author 2125509
 */
public class Server {

    HashMap<String, StaticMethodHandler> dic = new <String, StaticMethodHandler>HashMap();
    private static final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
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
        if (path.contains("html")) {
            pages(out, path);
        } else if (path.contains("jpg")) {
            images(out, path, clientSocket);
        } else if (path.contains("/pojos")) {
            services(out, path);
        }
        /*else {
            notFound();
        }*/
    }

    /**
     * Este metodo se encarga de desplegar una imagen en el browser.
     *
     * @param out PrintWriter buffer de salida para poder enviar datos al
     * cliente.
     */
    private void images(PrintWriter out, String path, Socket clientSocket) {
        String urlInputLine = "";
        int img = path.indexOf('/') + 1;
        while (!urlInputLine.endsWith(".jpg") && img < path.length()) {
            urlInputLine += (path.charAt(img++));
        }
        try {
            File image = new File(classLoader.getResource(urlInputLine).getFile());
            BufferedImage bImage = ImageIO.read(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "jpg", bos);
            byte[] imagen = bos.toByteArray();
            DataOutputStream outImg = new DataOutputStream(clientSocket.getOutputStream());
            outImg.writeBytes("HTTP/1.1 200 OK \r\n");
            outImg.writeBytes("Content-Type: image/jpg\r\n");
            outImg.writeBytes("Content-Length: " + imagen.length);
            outImg.writeBytes("\r\n\r\n");
            outImg.write(imagen);
            outImg.close();
            out.println(outImg.toString());
        } catch (Exception e) {
            //notFound();
        }
    }

    /**
     * Este metodo se encarga de crear la pagina html index.
     *
     * @param out PrintWriter buffer de salida para poder enviar datos al
     * cliente.
     */
    private static void pages(PrintWriter out, String path) {
        out.print("HTTP/1.1 200 OK \r");
        out.print("Content-Type: text/html \r\n");
        out.print("\r\n");
        int pag = path.indexOf('/') + 1;
        String urlInputLine = "";
        while (!urlInputLine.endsWith(".html") && pag < path.length()) {
            urlInputLine += (path.charAt(pag++));
        }
        try {
            BufferedReader readerFile = new BufferedReader(new FileReader(classLoader.getResource(urlInputLine).getFile()));
            while (readerFile.ready()) {
                out.println(readerFile.readLine());
            }
            out.close();
        } catch (Exception e) {
            // notFound();
        }
    }

    /**
     * Este metodo se encarga de llamar a los pojos basado en la url solicitada.
     *
     * @param out PrintWriter buffer de salida para poder enviar datos al
     * cliente.
     * @param path path solicitado por el cliente.
     */
    private void services(PrintWriter out, String path) {
        out.print("HTTP/1.1 200 OK \r");
        out.print("Content-Type: text/html \r\n");
        out.print("\r\n");
        String[] url = path.split("/");
        String cad = url[2];
        String metodo = ""; 
        String param = "";
        System.out.println(cad);
        if (cad.contains("?")) {
            System.out.println("entraa");
            url = cad.split("\\?"); metodo = url[0]; cad = url[1];
            url = cad.split("=");
            param = url[1];
            int temp = Integer.parseInt(param);
            Object[] atributo = new Object[]{temp};
            try {
                out.print(dic.get(metodo).procesar(atributo));
                out.close();
            } catch (Exception ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("entra2");
            metodo = cad;
            try {
                out.print(dic.get(metodo).procesar());
                out.close();
            } catch (Exception ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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

    /*public static void readApps(PrintWriter out,String path) throws IOException {
        int idApps = path.indexOf("/pojos");
        String subStrg = "";
        System.out.println(path + "  line ");
        for (int ji = idApps; ji < path.length() && path.charAt(ji) != ' '; ++ji) {
            subStrg += path.charAt(ji);
        } 
        try {
            out.write("HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n");
            if (subStrg.contains(":")) {
                int id = subStrg.indexOf(":");
                out.write(dic.get(subStrg.substring(0, id)).process(new Object[]{subStrg.substring(id + 1)}));
            } else {
                out.write(dic.get(subStrg).process());
            }
            out.close();
        } catch (Exception e) {
            notFound();
        }
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
    }*/
}
