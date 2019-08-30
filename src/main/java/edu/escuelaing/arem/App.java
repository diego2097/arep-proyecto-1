package edu.escuelaing.arem;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
/**
 * Hello world!
 *
 */
public class App {

    
    HashMap<String, Handler> Urls;
    
       
    public void escuchar() {
    }

    public void inicializar() {

        try {
            Class c = Class.forName("apps.Prueba");
            Method m = c.getDeclaredMethod("getHtml", null);
            System.out.format("invoking %s.main()%n", c.getName());
            System.out.println(m.invoke(null, null));
           
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
