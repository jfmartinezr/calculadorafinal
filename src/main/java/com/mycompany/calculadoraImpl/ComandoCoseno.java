/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.calculadoraImpl;
import com.mycompany.calculadoracmd.Command;

/**
 *
 * @author usuario
 */
public class ComandoCoseno implements Command{
    
    @Override
    public double ejecutar(double angulo) {
      
       return Math.cos(angulo);
      
    }

    @Override
    public double ejecutar(double op1, double op2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
