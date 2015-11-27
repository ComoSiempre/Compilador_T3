/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticVisitor;

import ast.*;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * clase usada para guardar las DECLARACIONES dentro de un alcance de un ejercicio.
 * @author Jonathan Vasquez - Eduardo Tapia
 */
public class Alcance {
    private int numeroAlcance; //numero actual del alcance, servira para el orden en pila.
    //tabla con todas las declaraciones en el alcance.
    //los tipos de declaraciones pueden ser: varDec, funcion, parametro, variable(para verificar operaciones).
    ArrayList<Nodo> listaDeclaraciones = new ArrayList<>();
    
    //enlace al alcanse padre.
    private Alcance padre=null;
    
    public Alcance(){
    }
    
    public Alcance(int numAlcance, Alcance padre){
        this.padre=padre;
        this.numeroAlcance=numAlcance;
    }
    /**
     * metodo que ingreso un nodo declaracion dentro del alcance en la tabla de simbolos.
     * @param nodo puede ser varDec, FunDec, Param.
     * @return TRUE: exito en ingreso, FALSE: existe duplicado en el alcanse (sin exito).
     */
    public boolean insertarDeclaracion(Nodo nodo){
        
        //Se verifica si existe el simbolo declarado en algun alcanse anidado a este. (ojo cuando el nodo es una Var).
        boolean existeNodo = existeSimbolo(nodo);
        if(existeNodo) return false; //la declaracion esta duplicada.
        this.listaDeclaraciones.add(nodo);
        return true;
    }
    
    	
    /**
     * metodo que verifica si dos listas de parametros de dos nodos funciones son iguales.
     * @param nodo1 nodo FunDec (funcion), un nodo dentro del alcance.
     * @param nodo2 nodo FunDec, nodo a evaluar.
     * @return TRUE: las listas son iguales, FALSE: listas distintas.
     */
    private boolean listasIguales(FunDec nodo1, FunDec nodo2){
        //variable usada para contar las equivalencias de los parametos de las listas.
        int equivalencias=0;
        if(nodo1.getListaParametros().size() != nodo2.getListaParametros().size()) return false; //listas distintas por tamaño.
        //se verifica los parametros uno a uno.
        //ciclo que ayudara a verificar uno a uno los parametros.
        for(int i=0; i<= nodo1.getListaParametros().size(); i++){
            //verifico que los paramtros corresponden a parametros VOID.
            if(nodo1.getParametroVoid()){
                if(nodo2.getParametroVoid()){
                    //corresnpponden a dos funciones de parametros VOID.
                    return true;
                }else{
                    //correponden a una funcion con parametro void y otra con parametros normales.
                    return false;
                }
            }else if(nodo2.getParametroVoid()){
                //corresponde a una funcion con parametros normales y la otra con parametros VOID.
                return false;
            }
            
            //ya verificado la no existencia de parametros void, que analiza la lista de parametros de las funciones.
            //verifico los parametros segun su ID, tipo.
            if(((Param)nodo1.getListaParametros().get(i)).getIDParametro().equals(((Param)nodo2.getListaParametros().get(i)).getIDParametro()) &&
                    ((Param)nodo1.getListaParametros().get(i)).getTipoParametro() == ((Param)nodo2.getListaParametros().get(i)).getTipoParametro() &&
                    ((Param)nodo1.getListaParametros().get(i)).isEsParamVector() == ((Param)nodo2.getListaParametros().get(i)).isEsParamVector()) equivalencias++;
        }
        if(nodo1.getListaParametros().size() == equivalencias) return true;//la cantidad de equivalencias coincide con la cantidad de parametros de las listas.
        return false;
    }
    /**
     * metodo que verifica el duplicado de simbolos dentro del alcance. independiente del tipo.
     * 
     * @param nodoEvaluacion: puede ser VarDec, FunDec, Param o Var.
     * @return TRUE: existe el simbolo en alcance, FALSE: no existe el simbolo en el alcance.
     */
    private boolean existeSimbolo(Nodo nodoEvaluacion){
        //recorro la lista de declaraciones.
        for(Nodo nodo : this.listaDeclaraciones){
            //si se verifican funciones.
            if(nodo instanceof FunDec && nodoEvaluacion instanceof FunDec){
                //se analizan sus nombres, tipos, y listas de parametros.
                if(((FunDec)nodo).getID().equals(((FunDec)nodoEvaluacion).getID()) &&
                        ((FunDec)nodo).getTipoIDFuntion().equals(((FunDec)nodoEvaluacion).getTipoIDFuntion())){
                            //se verifican ahora los parametros.
                            if(this.listasIguales((FunDec) nodo, (FunDec) nodoEvaluacion)) return true; //funcion duplicada
                    
                }
            }
            
            //se verifica una declaracion de variable.
            if(nodo instanceof VarDec && nodoEvaluacion instanceof VarDec){
                //se verifica que tengan el mismo ID y Tipo.
                if(((VarDec)nodo).getID().equals(((VarDec)nodoEvaluacion).getID()) && 
                        ((VarDec)nodo).getTipoID().equals(((VarDec)nodoEvaluacion).getTipoID())){
                    //se verifica si la declaracion de varible en la lista corresponde a un vector.
                    if (((VarDec) nodo).isEsVector() == true) {
                        //se verifica que la declaracion a evaluar sea tambien vector.
                        if (((VarDec) nodoEvaluacion).isEsVector() == true) {
                            //ya que los dos son vector, se verifica que los dos tengan el mismo largo declarado.
                            if (((VarDec) nodo).getNumeroVector() == ((VarDec) nodoEvaluacion).getNumeroVector()) {
                                return true; //existe la declaracion de vector en el alcance, por lo tanto, variable duplicada.
                            }
                        }
                    //verifico si el nodo a evaluar tambien no es vector.    
                    }else if(((VarDec) nodoEvaluacion).isEsVector() == false){
                        //existe una declaracion de variable con mismo tipo, ID, y los dos no son vector, 
                        //osea son declaraciones simples, por lo tanto, variable duplicada.
                        return true; 
                    }else{
                        // En este caso la variable a evaluar tiene el mismo ID y tipo que el del alcance,
                        //pero el de alcance no es vector, pero el que se esta evaluando si, por lo tanto,
                        //son dos variables distiantas.
                        return false; 
                    }
                }//fin if.
            }//fin if.
            //en caso de que se deba verificar que una variable esta declarada.
            if (nodo instanceof VarDec && nodoEvaluacion instanceof Var) {
                if (((VarDec) nodo).getID().equals(((Var) nodoEvaluacion).getID())) {
                    return true;//la variable esta declarada.
                }
            }
            //en caso de comparar una variable con un parametro de funcion.
            if (nodo instanceof Param && nodoEvaluacion instanceof Var) {
                //ya que no existen variables de tipo void guardados en alcance.
                if (!((Param) nodo).getTipoParametro().equalsIgnoreCase("void")) {
                    if (((Param) nodo).getIDParametro().equals(((Var) nodoEvaluacion).getID())) {
                        //existiria como un parametro, ya que el unico tipo posible es int.
                        return true;
                    }
                }
            }//fin IF.
        }//fin FOR.
        return false;
    }//fin funcion 'existeSimbolo' 
    /**
     * metodo usado para la impresion de declaraciones del alcance. solo para uso de desarrollo.
     */
    public void imprimirDeclaraciones() {
        for (Nodo nodo : this.listaDeclaraciones) {
            if (nodo instanceof VarDec) {
                System.out.println(((VarDec) nodo).getID() + " :: " + ((VarDec) nodo).getTipoID());
            }
            if (nodo instanceof FunDec) {
                System.out.println(((FunDec) nodo).getID() + " * " + ((FunDec) nodo).getTipoIDFuntion());
            }
            if (nodo instanceof Param) {
                if (((Param) nodo).getTipoParametro().equalsIgnoreCase("void")) {
                    System.out.println("Parametro->" + ((Param) nodo).getTipoParametro());
                } else {
                    System.out.println("Parametro->" + ((Param) nodo).getIDParametro() + " :: " + ((Param) nodo).getTipoParametro());
                }
                if (((Param) nodo).isEsParamVector()) {
                    System.out.println("Parametro->" + ((Param) nodo).getIDParametro() + " || " + ((Param) nodo).getTipoParametro() + "[]");
                }
            }
        }

    }
    /**
     * metodo que retorna el tamaño de la lista de declaraciones.
     * @return tamaño de la lista.
     */
    public int getTamañoLista(){
        return this.listaDeclaraciones.size();
    }
    
    /**
     * @return the padre
     */
    public Alcance getPadre() {
        return padre;
    }

    /**
     * @param padre the padre to set
     */
    public void setPadre(Alcance padre) {
        this.padre = padre;
    }

    /**
     * @return the numeroAlcance
     */
    public int getNumeroAlcance() {
        return numeroAlcance;
    }

    /**
     * @param numeroAlcance the numeroAlcance to set
     */
    public void setNumeroAlcance(int numeroAlcance) {
        this.numeroAlcance = numeroAlcance;
    }
}
