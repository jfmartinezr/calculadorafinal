/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.calculadoraImpl;

import com.mycompany.calculadoracmd.Command;

/**
 *
 * @author f212
 */
public class ComandoSuma implements Command{



    @Override
    public double ejecutar(double op1, double op2) {
        return op1+op2;
    }

    @Override
    public double ejecutar(double op1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
