package edu.escuelaing.arem.app;

import edu.escuelaing.arem.framework.Web;
import static java.lang.Math.pow;
/**
 * Hello world!
 *
 */
public class App {

    /**
     * 
     * @return Una pagina html informando que el pojo ha funcioando correctamente. 
     */
    @Web("presentacion")
    public static String presentacion(){
        return "<!DOCTYPE html>\n" + 
        		"<html>\n" + 
        		"<head>\n" + 
        		"  <meta charset=\"utf-8\" />\n" + 
        		"  <title>Proyecto</title>  \n" + 
        		"</head>\n" +
                        "<body>\n" +
        		"  <h1>Pojo funcionando!!</h1>	\n" + 
        		"</body>\n" + 
        		"</html>\n" + 
        		"";
    }
    
    /**
     * 
     * @param numero, el numero al cual se le hallara el cuadrado 
     * @return retorna el cuadrado del numero 
     */
    @Web("cuadrado")
    public static String cuadrado(Integer numero) {
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
    
    /**
     * 
     * @param numero  el numero al cual se le hallara el cubo 
     * @return una string con el cubo del numero 
     */
    @Web("cubo")
    public static String cubo(Integer numero) {
        return "<!DOCTYPE html>\n" + 
        		"<html>\n" + 
        		"<head>\n" + 
        		"  <meta charset=\"utf-8\" />\n" + 
        		"  <title>ARSW - Laboratorio 1</title>  \n" + 
        		"</head>\n" + 
        		"<body>   \n" + 
        		"<h1>"+numero+" al cubo es "+(int)pow(numero,3)+"</h1>  \n" + 
        		"</body>\n" + 
        		"</html>\n" + 
        		"";
    }
}