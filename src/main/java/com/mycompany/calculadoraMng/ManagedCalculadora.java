/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.calculadoraMng;

import com.mycompany.calculadoraImpl.Invocador;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import com.mycompany.calculadoraImpl.Parser;
import java.util.ArrayList;
import com.mycompany.*;
import com.mycompany.calculadoraImpl.AnalizadorSintactico;


/**
 *
 * @author usuario
 */
@ManagedBean
@RequestScoped
public class ManagedCalculadora {
    
    
    public String operacion;
    AnalizadorSintactico as = new AnalizadorSintactico();
    ArrayList <String> arrayHistorial= new ArrayList<String>();
    

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public void escribirEcuacion(String valor) {

        String concatenador;
        concatenador = getOperacion() + valor;
        arrayHistorial.add(concatenador);
        setOperacion(concatenador);
    }

    public void generarResultado() {
        String resultado = ""+as.analizadorSintactico2(getOperacion());
        System.out.print(resultado);
        
    }

    public void borrarCampo() {
        setOperacion("");

    }    
    
    public void borrarAtras() {
        
        String concatenador;
        concatenador = getOperacion();
        char [] charAuxiliar = concatenador.toCharArray();
        concatenador="";
           for (int i = 0 ; i < charAuxiliar.length-1 ; i++)
        {
            concatenador=concatenador+charAuxiliar[i];
                                  
        }
               
        setOperacion(concatenador);

    }
    
    public String operarUnitaria(String operador, double numero) {
        double resultado;
        Invocador i = new Invocador();
        resultado = i.ejecutarUnitaria(numero, operador);
        return resultado + "";
    }

    public String operarBinaria(String operador, double numero1, double numero2) {
        double resultado;
        Invocador i = new Invocador();
        resultado = i.ejecutarBinaria(numero1, numero2, operador);
        return resultado + "";
    }
    public String historial() {
         
        for (int i = 0; i < arrayHistorial.size(); i++) {
            
            
        }
    
        return "";
        }
    
      public String ejecuatrAnalizador() {
      //String ecuacion = getOperacion();
        String ecuacion = "38.9+25(90)";
                Parser parser = new Parser();
        System.out.println(parser.parsear(ecuacion));
        
        return ecuacion;
    }

    public static void main(String[] args) {
        ManagedCalculadora managedSuma = new ManagedCalculadora();
        //System.out.println(managedSuma.operarUnitaria("s", 90));
        //System.out.println(managedSuma.operarBinaria("+", 90, 90));
           managedSuma.ejecuatrAnalizador();
    }

}
