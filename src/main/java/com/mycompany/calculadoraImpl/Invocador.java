/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.calculadoraImpl;

import com.mycompany.calculadoracmd.Command;
import java.util.HashMap;

/**
 *
 * @author f212
 */
public class Invocador {
    
    HashMap<String,Command> comandos;

    public Invocador() {
        
        comandos= new HashMap<>();
        comandos.put("-"    ,   new ComandoResta());
        comandos.put("+"    ,   new ComandoSuma());
        comandos.put("*"    ,   new ComandoMultiplicar());
        comandos.put("/"    ,   new ComandoDivision());
        comandos.put("%"    ,   new ComandoCociente());
        comandos.put("r"    ,   new ComandoRaiz2());
        comandos.put("R"    ,   new ComandoRaiz());
        comandos.put("^"    ,   new ComandoElevar());
        comandos.put("l"    ,   new ComandoLogaritmo10());
        comandos.put("s"    ,   new ComandoSeno());
        comandos.put("c"    ,   new ComandoCoseno());
        comandos.put("t"    ,   new ComandoTangente());       
    }
    
    public double ejecutarUnitaria(double op1, String operador){
        Command cmd=comandos.get(operador);
        return cmd.ejecutar(op1);
    }
    
    public double ejecutarBinaria(double op1, double op2, String operador){
        Command cmd=comandos.get(operador);
        return cmd.ejecutar(op1, op2);
    }    
    
}
