/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tables;

import java.util.Hashtable;
import java.util.Stack;
import semanticVisitor.Alcance;
import ast.*;
/**
 * Clase que corresponde a la tabla de simbolos usada en el proceso de analisis semantico.
 * Se utilizara el patron Singleton para su uso en el compilador.
 * 
 * @author Jonathan Vasquez - Eduardo Tapia.
 */
public class SymbolTable {
    //estructura en donde se trabajara la tabla de simbolos:
    //El stack mantendra la estructura de alcance en los ejercicios.
    Stack<Alcance> ts= new Stack<>();
    private int contadorAlcance=1;//vairiable que cuenta la cnatidad de alcances.
    //variable instancia de la tabla de simbolos.
    public static SymbolTable instancia; 
    
    public SymbolTable(){
        instancia = this;
    }
    
    /**
     * Metodo que entra un nuevo alcance a tabla.
     * @return TRUE: push exitoso, FALSE: alcance nulo.
     */
    public boolean pushScope(Alcance scope){
        if(scope == null) return false;
        this.ts.push(scope);
        this.contadorAlcance++;
        return true;
    }
    
    /**
     * metodo que abandona un alcance, descartando todas las declaraciones dentro de este.
     * @return 
     */
    public Alcance popScope(){
        return this.ts.pop();
    }
    
    /**
     * metodo que añade una nueva entrada al alcance actual.
     * @return 
     */
    public boolean insertSymbol(Nodo nodo){
        if(nodo == null) return false;
        //obtengo el alcance actual.
        Alcance actual = this.ts.peek(); //obtengo el alcanse actual en la pila.
        if(actual == null) return false; //no hay nada en la pila.   
        //ingreso la declaracion al alcance.
        return actual.insertarDeclaracion(nodo);
        
    }
    
    /**
     * metodo que encuentra que elemento corresponde un nombre.
     * @return 
     */
    public boolean lookupSymbol(String nombre){
        return true;
    }

    /**
     * @return the contadorAlcance
     */
    public int getContadorAlcance() {
        return contadorAlcance;
    }

    /**
     * @param contadorAlcance the contadorAlcance to set
     */
    public void setContadorAlcance(int contadorAlcance) {
        this.contadorAlcance = contadorAlcance;
    }
    
}
