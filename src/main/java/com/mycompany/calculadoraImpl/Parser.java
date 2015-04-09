/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.calculadoraImpl;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 *
 * @author Usuario
 */
public class Parser {
//VARIABLES PRIVADAS
//Guarda la última expresión que se tradujo a postfijo para poder evaluar en ella sin dar una nueva expresión

    private String ultimaParseada;
//CONSTRUCTORES

    public Parser() {
        ultimaParseada = "0";
    }
//FUNCIONES PUBLICAS

    /**
     * La función que parsea la expresión a notación postfija.
     *     
* @param expresion El string con la expresión a parsear.
     * @return Un String con la expresión parseada en notación postfija.
     * @exception SintaxException Error de escritura en la expresión.
     */
    public String parsear(String expresion) throws SintaxException {
        Stack PilaNumeros = new Stack(); //Pila donde se guardarán los números al parsear
        Stack PilaOperadores = new Stack(); //Pila donde se guardarán los operadores al parsear
        String expr = expresion.replaceAll(" ", ""); //La expresión sin espacios ni mayúsculas.
        String fragmento; //Guarda el fragmento de texto que se esté utilizando en el momento (ya sea un número, un operador, una función, etc.)
        int pos = 0, tamano = 0; //pos marca la posición del caracter que se está procesando actualmente en el String. tamano indica el tamaño del texto que se procesa en ese momento.
        byte cont = 1; //contador indica el número de caracteres que se sacan del string en un momento indicado, este no puede ser más de seis (la función con más caracteres tiene seis)
//Este es un arreglo de Strings que guarda todas las funciones y expresiones permitidas, incluso números, y los acomoda en cada posición de acuerdo a su tamaño
        final String funciones[] = {"1 2 3 4 5 6 7 8 9 0 ( ) x e + - * /",
            "pi",
            "ln(",
            "log( abs( sen( sin( cos( tan("};
//Todas las funciones que trabajan como paréntesis de apertura están aquí.
        final String parentesis = "( ln log sen sin cos tan";
        byte anterior = 0;
        try {
            while (pos < expr.length()) { //Haga mientras la posición sea menor al tamaño del String (mientras este dentro del string)
                tamano = 0;
                cont = 1;
                while (tamano == 0 && cont <= 6) { //Este while revisa si el pedazo del texto sacado concuerda con algo conocido
                    if (pos + cont <= expr.length() && funciones[cont - 1].indexOf(expr.substring(pos, pos + cont)) != -1) {
                        tamano = cont;
                    }
                    cont++;
                }
                if (tamano == 0) { //Si no encontró nada es por que hubo un error, se pone la ùltima parseada en cero y se lanza una excepción
                    ultimaParseada = "0";
                    throw new SintaxException("Error en la expresión");
                } else if (tamano == 1) { //Si encontró algo de tamaño uno
                    if (isNum(expr.substring(pos, pos + tamano))) { //Si es un número se encarga de sacarlo completo
                        if (anterior == 1 || anterior == 4) {//si hay una multiplicación oculta
                            sacaOperadores(PilaNumeros, PilaOperadores, "*");
                        }
                        fragmento = ""; //aquí se guardará el número sacado
                        do { //Hágalo mientras lo que siga sea un número o un punto o una coma
                            fragmento = fragmento + expr.charAt(pos);
                            pos++;
                        } while (pos < expr.length() && (isNum(expr.substring(pos, pos + tamano)) || expr.charAt(pos) == '.' || expr.charAt(pos) == ','));
                        try { //Trate de convertirlo en un número
                            Double.valueOf(fragmento);
                        } catch (NumberFormatException e) { //Si no pudo pasarlo a número hay un error
                            ultimaParseada = "0";
                            throw new SintaxException("Número mal digitado");
                        }
                        PilaNumeros.push(new String(fragmento));
                        anterior = 1;
                        pos--;
                    } else if (expr.charAt(pos) == 'x' || expr.charAt(pos) == 'e') { //Si es un número conocido
                        if (anterior == 1 || anterior == 4) {//si hay una multiplicación oculta
                            sacaOperadores(PilaNumeros, PilaOperadores, "*");
                        }
                        PilaNumeros.push(expr.substring(pos, pos + tamano));
                        anterior = 1;
                    } else if (expr.charAt(pos) == '+' || expr.charAt(pos) == '*' || expr.charAt(pos) == '/') { //Si es suma, multiplicación o división
                        if (anterior == 0 || anterior == 2 || anterior == 3)//Hay error si antes de los operadores no hay nada, hay un paréntesis de apertura o un operador
                        {
                            throw new SintaxException("Error en la expresión");
                        }
                        sacaOperadores(PilaNumeros, PilaOperadores, expr.substring(pos, pos + tamano));
                        anterior = 2;
                    } else if (expr.charAt(pos) == '-') { //Si es una resta
                        if (anterior == 0 || anterior == 2 || anterior == 3) {//si hay un menos unario
                            PilaNumeros.push(new String("-1"));
                            sacaOperadores(PilaNumeros, PilaOperadores, "*");
                        } else {//si el menos es binario
                            sacaOperadores(PilaNumeros, PilaOperadores, "-");
                        }
                        anterior = 2;
                    } else if (expr.charAt(pos) == '(') {
                        if (anterior == 1 || anterior == 4) { //si hay una multiplicación oculta
                            sacaOperadores(PilaNumeros, PilaOperadores, "*");
                        }
                        PilaOperadores.push(new String("("));
                        anterior = 3;
                    } else if (expr.charAt(pos) == ')') {
                        if (anterior != 1 && anterior != 4) //Antes de un cierre de paréntesis sólo puede haber un número u otro cierre de paréntesis, sino hay un error
                        {
                            throw new SintaxException("Error en la expresión");
                        }
                        while (!PilaOperadores.empty() && parentesis.indexOf(((String) PilaOperadores.peek())) == -1) {
                            sacaOperador(PilaNumeros, PilaOperadores);
                        }
                        if (!((String) PilaOperadores.peek()).equals("(")) {
                            PilaNumeros.push(new String(((String) PilaNumeros.pop()) + " " + ((String) PilaOperadores.pop())));
                        } else {
                            PilaOperadores.pop();
                        }
                        anterior = 4;
                    }
                } else if (tamano >= 2) { //Si lo encontrado es de tamaño dos o mayor (todas las funciones se manejan igual)
                    fragmento = expr.substring(pos, pos + tamano);
                    if (fragmento.equals("pi")) { //Si es el número pi
                        if (anterior == 1 || anterior == 4) {//si hay una multiplicación oculta
                            sacaOperadores(PilaNumeros, PilaOperadores, "*");
                        }
                        PilaNumeros.push(fragmento);
                        anterior = 1;
                    } else { //Si es cualquier otra función
                        if (anterior == 1 || anterior == 4) { //si hay una multiplicación oculta
                            sacaOperadores(PilaNumeros, PilaOperadores, "*");
                        }
                        PilaOperadores.push(fragmento.substring(0, fragmento.length() - 1)); //Se guarda en la pila de funciones sin el paréntesis de apertura (en postfijo no se necesita)
                        anterior = 3;
                    }
                }
                pos += tamano;
            }
//Procesa al final
            while (!PilaOperadores.empty()) { //Saca todos los operadores mientras la pila no esté vacía
                if (parentesis.indexOf((String) PilaOperadores.peek()) != -1) {
                    throw new SintaxException("Hay un paréntesis de más");
                }
                sacaOperador(PilaNumeros, PilaOperadores);
            }
        } catch (EmptyStackException e) { //Si en algún momento se intenta sacar de la pila y está vacía hay un error
            ultimaParseada = "0";
            throw new SintaxException("Expresión mal digitada");
        }
        ultimaParseada = ((String) PilaNumeros.pop()); //Se obtiene el resultado final
        if (!PilaNumeros.empty()) { //Si la pila de números no quedó vacía hay un error
            ultimaParseada = "0";
            throw new SintaxException("Error en la expresión");
        }
        return ultimaParseada; //Se devuelve el resultado evaluado
    }//Parsear

    public double f(String expresionParseada, double x) throws ArithmeticException {
        Stack pilaEvaluar = new Stack(); //Pila de doubles para evaluar
        double a, b; //Estos valores son los que se van sacando de la pila de doubles
        StringTokenizer tokens = new StringTokenizer(expresionParseada); //La expresión partida en tokens
        String tokenActual; //El token que se procesa actualmente
        try {
            while (tokens.hasMoreTokens()) { //Haga mientras hayan más tokens
                tokenActual = tokens.nextToken();
                if (tokenActual.equals("e")) { //Si es el número e
                    pilaEvaluar.push(new Double(Math.E));
                } else if (tokenActual.equals("pi")) {//Si es el número pi
                    pilaEvaluar.push(new Double(Math.PI));
                } else if (tokenActual.equals("x")) {//Si es una x se introduce el valor a evaluar por el usuario
                    pilaEvaluar.push(new Double(x));
                } else if (tokenActual.equals("+")) {//Si es una suma se sacan dos números y se suman
                    b = ((Double) pilaEvaluar.pop()).doubleValue();
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(a + b));
                } else if (tokenActual.equals("-")) {//Si es resta se sacan dos valores y se restan (así con todos los operadores)
                    b = ((Double) pilaEvaluar.pop()).doubleValue();
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(a - b));
                } else if (tokenActual.equals("*")) {//Multiplicación
                    b = ((Double) pilaEvaluar.pop()).doubleValue();
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(a * b));
                } else if (tokenActual.equals("/")) {//División
                    b = ((Double) pilaEvaluar.pop()).doubleValue();
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(a / b));
                } else if (tokenActual.equals("ln")) {//Si es logaritmo natural sólo se saca un valor de la pila y se evalúa
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(Math.log(a)));
                } else if (tokenActual.equals("log")) {//Logaritmo en base 10
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(Math.log(a) / Math.log(10)));
                } else if (tokenActual.equals("sen") || tokenActual.equals("sin")) { //Seno
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(Math.sin(a)));
                } else if (tokenActual.equals("cos")) {//Coseno
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(Math.cos(a)));
                } else if (tokenActual.equals("tan")) {//Tangente
                    a = ((Double) pilaEvaluar.pop()).doubleValue();
                    pilaEvaluar.push(new Double(Math.tan(a)));
                } else {//si es otra cosa tiene que ser un número, simplemente se mete en la pila
                    pilaEvaluar.push(Double.valueOf(tokenActual));
                }
            }//while
        } catch (EmptyStackException e) { //Si en algún momento se acabó la pila hay un error
            throw new ArithmeticException("Expresión mal parseada");
        } catch (NumberFormatException e) { //Si hubo error al traducir un número hay un error
            throw new ArithmeticException("Expresión mal digitada");
        } catch (ArithmeticException e) { //Cualquier otro error de división por cero o logaritmo negativo, etc.
            throw new ArithmeticException("Valor no real en la expresión");
        }
        a = ((Double) pilaEvaluar.pop()).doubleValue(); //El valor a devolver
        if (!pilaEvaluar.empty()) //Si todavía quedó otro valor en la pila hay un error
        {
            throw new ArithmeticException("Expresión mal digitada");
        }
        return a;
    }//funcion f

    public double f(double x) throws ArithmeticException {
        try {
            return f(ultimaParseada, x);
        } catch (ArithmeticException e) {
            throw e;
        }
    }//Fin de la funcion f

    private void sacaOperador(Stack Numeros, Stack operadores) throws EmptyStackException {
        String operador, a, b;
        final String operadoresBinarios = "+ - * /"; //Lista de los operadores binarios
        try {
            operador = (String) operadores.pop(); //Saca el operador que se debe evaluar
            if (operadoresBinarios.indexOf(operador) != -1) { //Si es un operador binario saca dos elementos de la pila y guarda la operación
                b = (String) Numeros.pop();
                a = (String) Numeros.pop();
                Numeros.push(new String(a + " " + b + " " + operador));
            } else { //Sino sólo saca un elemento
                a = (String) Numeros.pop();
                Numeros.push(new String(a + " " + operador));
            }
        } catch (EmptyStackException e) {
            throw e;
        }
    }//sacaOperador

    private void sacaOperadores(Stack PilaNumeros, Stack PilaOperadores, String operador) {
//Todas las funciones que se manejan como paréntesis de apertura
        final String parentesis = "( ln log sen sin cos tan";
//mientras la pila no esté vacía, el operador que sigue no sea un paréntesis de apertura, la longitud del operador sea uno (para que sea un operador), y la prioridad indique que tiene que seguir sacando elementos
        while (!PilaOperadores.empty() && parentesis.indexOf((String) PilaOperadores.peek()) == -1 && ((String) PilaOperadores.peek()).length() == 1 && prioridad(((String) PilaOperadores.peek()).charAt(0)) >= prioridad(operador.charAt(0))) {
            sacaOperador(PilaNumeros, PilaOperadores); //Saca el siguiente operador
        }
        PilaOperadores.push(operador);//Al final mete el nuevo operador luego de sacar todo lo que tenía que sacar.
    }
    /*
     *Función que devuelve la prioridad de una operacion
     */

    private int prioridad(char s) {
        if (s == '+' || s == '-') //Si es una suma o una resta devuelve cero
        {
            return 0;
        } else if (s == '*' || s == '/') //Si es multiplicación, división o resto de división devuelve uno
        {
            return 1;
        }
        return -1; //Si no fue nada de lo anterior devuelve -1
    } //Fin de la funcion prioridad
/*
     *Función que pregunta si un caracter es un número o no
     */

    private boolean isNum(String s) {
        if (s.compareTo("0") >= 0 && s.compareTo("9") <= 0) //Si el caracter está entre 0 y 9 (si es un número)
        {
            return true;
        } else {
            return false;
        }
    } //Fin de la funcion isNum
/*
     *Devuelve el signo del número dado
     */

    private double sgn(double a) {
        if (a < 0) //Si el número es negativo devuelve -1
        {
            return -1;
        } else if (a > 0)//Si es positivo devuelve 1
        {
            return 1;
        } else//Si no es negativo ni positivo devuelve cero
        {
            return 0;
        }
    }
//CLASES PRIVADAS
//Clase SintaxException
//Esta es la excepción que se lanza si hay algún error sintáctico en la expresión

    private class SintaxException extends ArithmeticException { //En realidad extiende la ArithmeticException

        public SintaxException() { //Si se llama con el mensaje por defecto
            super("Error de sintaxis en el polinomio"); //El constructor llama a la clase superior
        }

        public SintaxException(String e) { //si se llama con otro mensaje
            super(e); //El constructor llama a la clase superior
        }
    }
}
