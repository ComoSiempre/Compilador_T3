/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import semanticVisitor.ExtendedGrapherVisitor;
import semanticVisitor.ScopeAnalisisVisitor;
import semanticVisitor.TypeCheckVisitor;
import syntaxVisitor.GrapherVisitor;

/**
 * Clase utilizada como nodo de los parametros de funcion.
 * @author Jonathan Vasquez - Eduardo Tapia
 */
public class Param extends Nodo implements visitaNodo {
    private String tipoParametro; //variable usada para guardar el tipo del parametro.
    private String IDParametro; //variable usada para guardar el nombre del parametro.
    private boolean esParamVector; //flag si el parametro corresponde a un vector.
    
    /**
     * constructor para el nodo Param.
     * @param tipo tipo del parametro.
     * @param id nombre del parametro.
     * @param esVector flag si es parametro vector.
     */
    public Param(String tipo, String id, boolean esVector){
        this.tipoParametro=tipo;
        this.IDParametro=id;
        this.esParamVector=esVector;
        
    }

    /**
     * metodo usado para generar el codigo en graphviz.
     * @param nombrePadre el nombre del nodo padre de este nodo.
     * @param contNodos el contador de nodos creados en estos momentos.
     * @return la linea de codigo.
     */
    public String toGrapher(String nombrePadre, int contNodos){
        String linea="";
        //condicionante usado si el nodo parametro corresponde a un parametro vector.
        if(this.isEsParamVector()){
            //parametro vector.
            //ingreso el codigo de creacion a la cadeno de codigo graphviz.
            linea += "\"nodo"+contNodos+"\"[label=\"Param["+this.getIDParametro()+","+this.getTipoParametro()+"[]]\"]; \n";
            //enlazo con el padre.
            linea += nombrePadre+"->\"nodo"+contNodos+"\"; \n";
        }else{
            //parametro normal.
            linea += "\"nodo"+contNodos+"\"[label=\"Param["+this.getIDParametro()+","+this.getTipoParametro()+"]\"]; \n";
            linea += nombrePadre+"->\"nodo"+contNodos+"\"; \n";
        }
        return linea;
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
     * @return the tipoParametro
     */
    public String getTipoParametro() {
        return tipoParametro;
    }

    /**
     * @return the IDParametro
     */
    public String getIDParametro() {
        return IDParametro;
    }

    /**
     * @return the esParamVector
     */
    public boolean isEsParamVector() {
        return esParamVector;
    }
    
    
}
