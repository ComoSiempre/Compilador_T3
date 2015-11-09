/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import java.util.ArrayList;
import syntaxVisitor.GrapherVisitor;

/**
 * Clase Nodo que guarda los datos de las gramaticas 31 a 33.
 * @author Jonathan Vasquez - Eduardo Tapia.
 */
public class Call extends Nodo implements visitaNodo {
    //variable que guarda el nombre de la funcion que se esta llamando.
    String ID;
    //Lista que guarda los argumentos.
    ArrayList<Nodo> listaArgs= new ArrayList<Nodo>(); 
    
    /**
     * constructor 1.
     */
    public Call(){}
    
    /**
     * metodo que agrega un nodo argumento en la lista de argumentos.
     * @param Expression un argumento.
     */
    public void agregarArgumento(Nodo Expression){
        this.listaArgs.add(Expression);
    }
    /**
     * metodo que modifica la id del llamado a funcion.
     * @param id el nombre del nuevo llamado.
     */
    public void setID(String id){
        this.ID=id;
    }
    /**
     * metodo que envia retorna la lista de argumentos.
     * @return lista de argumentos.
     */
    public ArrayList<Nodo> getArgs(){
        return this.listaArgs;
    }
    /**
     * metodo que genera el codigo en graphviz del nodo call.
     * @param contNodos la cantidad de nodos visitados.
     * @return codigo generado.
     */
    public String toGrapher(int contNodos){
        return "\"nodo"+contNodos+"\"[label=\"Call : "+this.ID+"\" color=thistle2]; \n";
    }
    @Override
    public void aceptar(GrapherVisitor v) {
        v.visitar(this);
    }
    
}
