/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import java.util.ArrayList;
import syntaxVisitor.GrapherVisitor;

/**
 * Clase usada para la generacion de Nodo Compound del AST.
 * contendra las listas de variables locales y estamentos.
 * @author Jonathan Vasquez - Eduardo Tapia.
 */
public class Compound extends Nodo implements visitaNodo {

    //lista que guarda los nodos de variables locales.
    ArrayList<Nodo> listaLocalVar=new ArrayList<Nodo>();
    //lista que guarda los nodos Statements.
    ArrayList<Nodo> listaStatements = new ArrayList<Nodo>();
    
    /**
     * constructor que guarda las lista ya llenadas.
     * @param local lista de variables locales en compound.
     * @param sta lista de declaraciones en compound.
     */
    public Compound(ArrayList<Nodo> local, ArrayList<Nodo> sta){
        this.listaLocalVar=local;
        this.listaStatements=sta;
    }
    /**
     * metodo que genera el codigo del nodo compound para Graphviz.
     * @param contNodos la cantidad de nodos visitados.
     * @return codigo para Graphviz.
     */
    public String toGrapher(int contNodos){
        return "\"nodo"+contNodos+"\"[label=\"Compound Statement\", color=deepskyblue]; \n";
    }
    /**
     * metodo GET que retorna la lista de variables locales.
     * @return variables locales.
     */
    public ArrayList<Nodo> getLocalVar(){
        return this.listaLocalVar;
    }
    /**
     * metodo GET que retorna la lista de declaraciones
     * @return lista de declaraciones.
     */
    public ArrayList<Nodo> getStatements(){
        return this.listaStatements;
    }
    @Override
    public void aceptar(GrapherVisitor v) {
        v.visitar(this);
    }
    
}
