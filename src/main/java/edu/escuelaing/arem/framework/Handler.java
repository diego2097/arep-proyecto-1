/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arem.framework;

/**
 *
 * @author 2125509
 */
public interface Handler {

    public String procesar() throws Exception;
    public String procesar(Object[] arg) throws Exception;
}
