/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arem.framework;

import edu.escuelaing.arem.framework.Handler;
import java.lang.reflect.Method;

/**
 *
 * @author 2125509
 */
public class StaticMethodHandler implements Handler {

    private Method metodo;
    
    public StaticMethodHandler(Method metodo) {
        this.metodo = metodo;
    }
    
    /**
     * Este metodo realiza la invocacion del metodo requerido
     * @return Una string con el resultado de la invocacion del metodo.
     * @throws Exception 
     */
    public String procesar() throws Exception {
        return (String) metodo.invoke(metodo, null);
    }

    /**
     * Este metodo realiza la invocacion del metodo requerido 
     * @param arg el argumento que tendra el metodo 
     * @return Una string con el resultado de la invocacion del metodo.
     * @throws Exception 
     */
    public String procesar(Object[] arg) throws Exception {
        return (String) metodo.invoke(metodo, arg);
    }

    public Method getMetodo() {
        return metodo;
    }

    public void setMetodo(Method metodo) {
        this.metodo = metodo;
    }
}
