/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import java.util.ArrayList;
import semanticVisitor.ExtendedGrapherVisitor;
import semanticVisitor.ScopeAnalisisVisitor;
import semanticVisitor.TypeCheckVisitor;
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
    //variable que guarda el valor de retorno en caso de que sea un llamado a una funcion int.
    //sin uso, borrar cuando se utilize.
    private int valorRetorno=0;
    
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
        //return "\"nodo"+contNodos+"\"[label=\"Call : "+this.ID+"\" color = thistle2 style = filled]; \n";
        return "\"nodo"+contNodos+"\"[label=\"Call : "+this.ID+"\" "
                + "shape = triangle "
                + "color = thistle2 "
                + "style = filled]; \n";
    }
    @Override
    public void aceptar(GrapherVisitor v) {
        v.visitar(this);
    }

    @Override
    public void aceptar(ScopeAnalisisVisitor s) {
       s.visitar(this);
    }

    @Override
    public void aceptar(TypeCheckVisitor t) {
        t.visitar(this);
    }

    @Override
    public void aceptar(ExtendedGrapherVisitor v2) {
        v2.visitar(this);
    }

    /**
     * @return the valorRetorno
     */
    public int getValorRetorno() {
        return valorRetorno;
    }
    
}
