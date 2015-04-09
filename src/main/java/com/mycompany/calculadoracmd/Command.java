/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.calculadoracmd;

/**
 *
 * @author f212
 */
public interface Command {
    
    public double ejecutar(double op1,double op2);
    public double ejecutar(double op1);
}
