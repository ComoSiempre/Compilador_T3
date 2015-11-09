/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import java.util.*;
import syntaxVisitor.GrapherVisitor;

/**
 * Clase que representa el nodo raiz  del AST y desde el cual comienza el recorrida por patron visitor.
 * @author Jonathan Vasquez - Eduardo Tapia.
 */
public class Program implements visitaNodo {
    
    ArrayList<Nodo> listaDeclaraciones=new ArrayList<>();
    
    /**
     * contructor default.
     */
    public Program(){
        
    }

    /**
     * metodo que retorna la lista de declaraciones.
     * @return lista de nodos .
     */
    public ArrayList<Nodo> getDeclaraciones(){
        return this.listaDeclaraciones;
    }
    /**
     * metodo que guarda en la lista de declaraciones una declaracion.
     * @param nodo nodo de declaracion.
     */
    public void agregarDeclaracion(Nodo nodo){
        this.listaDeclaraciones.add(nodo);
    }
    /**
     * metodo que genera el nodo en codigo de graphviz.
     * @param contNodos
     * @return linea de codigo.
     */
    public String toGrapher(int contNodos){
        String linea="\"nodo"+contNodos+"\"[label=\"Progama\"]; \n";
        return linea;
    }
    @Override
    public void aceptar(GrapherVisitor v) {
        v.visitar(this);
    }
    
}
