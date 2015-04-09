/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.calculadoraImpl;

/**
 *
 * @author usuario
 */
public class AnalizadorSintactico {
       
    Invocador invocador = new Invocador();
    double[] arregloNumeros = new double[10];
    char [] arregloOperadores = new char[10];
    
    public int analizadorSintactico2(String ecuacion) {
        
        char[] arregloEcuacion = ecuacion.toCharArray();
        for (int i = 0; i < arregloEcuacion.length; i++) {
           //System.out.print(arregloEcuacion[i]);
            
            double varNumDouble;
            if (arregloEcuacion[i] == '0' || arregloEcuacion[i] == '1' || arregloEcuacion[i] == '2' || arregloEcuacion[i] == '3' || arregloEcuacion[i] == '4' || arregloEcuacion[i] == '5' || arregloEcuacion[i] == '6' || arregloEcuacion[i] == '7' || arregloEcuacion[i] == '8' || arregloEcuacion[i] == '9') {
                char aux = arregloEcuacion[i];
                varNumDouble = Character.getNumericValue(aux);
                arregloNumeros[i] = varNumDouble;
                //System.out.print(arregloNumeros[i]);
            }

            if (arregloEcuacion[i] == '-' || arregloEcuacion[i] == '+' || arregloEcuacion[i] == '*' || arregloEcuacion[i] == '/' || arregloEcuacion[i] == '%' || arregloEcuacion[i] == 'r' || arregloEcuacion[i] == 'R' || arregloEcuacion[i] == '^' || arregloEcuacion[i] == 'l' || arregloEcuacion[i] == 's' || arregloEcuacion[i] == 'c' || arregloEcuacion[i] == 't') {
                char aux2 = arregloEcuacion[i];
                arregloOperadores[i] = aux2;
               // System.out.print(arregloOperadores[i]);
            }
            /*(arregloEcuacion[i] == "-" || arregloEcuacion[i] == "+" || arregloEcuacion[i] == "*" || arregloEcuacion[i] == "/" || arregloEcuacion[i] == "%" || arregloEcuacion[i] == "r" || arregloEcuacion[i] == "R" || arregloEcuacion[i] == "^" || arregloEcuacion[i] == "l" || arregloEcuacion[i] == "s" || arregloEcuacion[i] == "c" || arregloEcuacion[i] == "t") {*/
        }
        
//        for (int i = 0; i < arregloNumeros.length; i++) {
//          
//            System.out.println(arregloNumeros[i]);
//          
//        }
//         for (int i = 0; i < arregloOperadores.length; i++) {
//          
//            System.out.println(arregloOperadores[i]);
//          
//        }
         for (int i = 0; i < arregloNumeros.length; i++) {
                        
            for (int j = 0; j < arregloOperadores.length; j++) {
            
                       
                if( arregloOperadores[j]== 'r' || arregloOperadores[j] == 'R' || arregloOperadores[j] == 'l' || arregloOperadores[j] == 's' || arregloOperadores[j] == 'c' || arregloOperadores[j] == 't')
                {
                  invocador.ejecutarUnitaria(arregloNumeros[i], ""+arregloOperadores[j]);
                  System.out.println(invocador.ejecutarUnitaria(arregloNumeros[i], ""+arregloOperadores[j]));                 
                }
            }
         }
        
        
        

        return 0;
    }    

    public static void main(String[] args) {
        AnalizadorSintactico as = new AnalizadorSintactico();
        as.analizadorSintactico2("r8");
       // Invocador invocador = new Invocador();
        //System.out.println(invocador.ejecutarBinaria(1, 2, "+"));
        

    }
    
    

}
