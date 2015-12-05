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
import java.util.ArrayList;
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
     * @param scope alcance padre el cual estara ligado el nuevo alcance.
     * @return TRUE: ingreso exitoso, FALSE: ingreso fallido.status
     */
    public boolean pushScope(Alcance scopePadre){
        //creo el nuevo alcance a ser ingresado.
        Alcance scope = new Alcance(this.contadorAlcance,scopePadre);
        //cuento el nuevo alcance.
        this.contadorAlcance++;
        //scope.setNumeroAlcance(this.contadorAlcance);
        this.ts.push(scope);
        
        return true;
    }
    
    /**
     * metodo que abandona un alcance, descartando todas las declaraciones dentro de este.
     * @return el alcance eliminado (para guardarlo en el auxiliar).
     */
    public Alcance popScope(){
        return this.ts.pop();
    }
    
    /**
     * metodo que añade una nueva entrada al alcance actual.
     * @return 
     */
    public boolean insertSymbol(Nodo nodo){
        
        //obtengo el alcance actual.
        Alcance actual = this.ts.peek(); //obtengo el alcanse actual en la pila.
        if(actual == null) return false; //no hay nada en la pila.   
        //ingreso la declaracion al alcance.
        return actual.insertarDeclaracion(nodo);
        
    }
    
    /**
     * metodo que encuentra que elemento corresponde un nombre.
     * @param nombre
     * @return 
     */
    public String lookupSymbol(String nombre){
        Alcance actual =this.ts.peek();
        String tipo= actual.buscaSimbolo(nombre);
        while (tipo == null){
            actual= actual.getPadre();
            tipo = actual.buscaSimbolo(nombre);
        }
        return tipo;
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
    
    public Stack<Alcance> getTabla(){
        return this.ts;
    }
    /**
     * metodo usado para verificar si la tabla esta vacia.
     * @return TRUE: si la tabla esta vacio, FALSE: si existen alcances en la tabla.
     */
    public boolean sinAlcances(){
        if(this.ts.isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    /**
     * metodo usado para la impresion de la tabla de simbolos. solo para uso de desarrollo.
     */
    public void imprimirTabla(){
		Stack<Alcance> pilaAux=new Stack<Alcance>();
		while(!this.ts.isEmpty()){
			Alcance am= this.ts.pop();
			pilaAux.add(am);
			System.out.println("contexto: "+am.getNumeroAlcance());
			if(am.getNumeroAlcance() != 1){
				System.out.println("contexo anidado: "+am.getPadre().getNumeroAlcance());
			}
			am.imprimirDeclaraciones();
		}
		//vuelvo a ingresar los datos a la pila principal.
		while(!pilaAux.isEmpty()){
			this.ts.push(pilaAux.pop());
		}
	}
    /**
     * Verifica que esxista un simbolo en la tabla de simolos
     * @param var
     * @return 
     */
    public boolean lookupSymbol(Nodo var) {
        
        Alcance actual = this.ts.peek();
        while(actual!=null){
            if (actual.buscaSimbolo(var)){
                return true;
            }
            actual = actual.getPadre();
            
        }
        return false;
    }
}
