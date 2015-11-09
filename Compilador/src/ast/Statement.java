/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import syntaxVisitor.GrapherVisitor;

/**
 * Clase que sera usado para la construccion DE nodos para las gramaticas de selection_stmt, while_stmt,
 * for_stmt y return_stmt [switch_stmt]. 
 * @author Jonathan Vasquez - eduardo Tapia.
 */
public class Statement extends Nodo implements visitaNodo {

    String tipoDeclaracion="";//IF, IF-ELSE, WHILE, DO-WHILE, RETURN.
    private Nodo expresion;
    private Nodo expresionFor2; //nodo usado en la gramatica for (18).
    private Nodo expresionFor3; //nodo usado en la gramatica for (18).
    private Nodo declaracion;// nodo usado en selecion, while, for.
    private Nodo declaracion_else; //variable usado en la gramatica de if_else (gramatica 15).
    /**
     * constructor 1, usado para el nodo IF y WHILE.
     * @param tipoDec tipo declaracion, en este caso : if o while.
     * @param exp parametros del if , nodo Expresion.
     * @param stmt declaraciones del if .
     */
    public Statement(String tipoDec, Nodo exp, Nodo stmt){
        this.tipoDeclaracion=tipoDec;
        this.expresion=exp;
        this.expresionFor2=null;
        this.expresionFor3=null;
        this.declaracion=stmt;
        this.declaracion_else=null;
        
    }
    /**
     * constructor 2, usado para el nodo IF-ELSE.
     * @param tipoDec tipo declaracion, en este caso : if-else.
     * @param exp parametros del if , nodo Expresion.
     * @param stmt declaraciones del if .
     * @param stmt_else declaraciones del ELSE.
     */
    public Statement(String tipoDec, Nodo exp, Nodo stmt, Nodo stmt_else){
        this.tipoDeclaracion=tipoDec;
        this.expresion=exp;
        this.expresionFor2=null;
        this.expresionFor3=null;
        this.declaracion=stmt;
        this.declaracion_else=stmt_else;
        
    }
    /**
     * constructor 3, usado para el nodo FOR.
     * @param tipoDec tipo de declaracion, en este caso: for
     * @param exp parametros del for, nodo Expression.
     * @param exp2 segundo parametro del for, nodo Expression.
     * @param exp3 tercer parametro en for, nodo Expresion.
     * @param stmt declaracion del ciclo for.
     */
    public Statement(String tipoDec, Nodo exp, Nodo exp2, Nodo exp3, Nodo stmt){
        this.tipoDeclaracion=tipoDec;
        this.expresion=exp;
        this.expresionFor2=exp2;
        this.expresionFor3=exp3;
        this.declaracion=stmt;
        this.declaracion_else=null;
    }
    /**
     * constructor 4, usado para la creacion del nodo RETURN (gramatica 19.1).
     * @param tipoDec tipo de declaracion, en este caso: return.
     */
    public Statement(String tipoDec){
        this.tipoDeclaracion=tipoDec;
        this.declaracion=null;
        this.declaracion_else=null;
        this.expresion=null;
        this.expresionFor2=null;
        this.expresionFor3=null;
        
    }
    /**
     * constructor 5, usado para la crecion del nodo RETURN (gramatica 19.2).
     * @param tipoDec tipo de declaracion, en este caso: return.
     * @param exp sentencia del return, nodo Expression.
     */
    public Statement(String tipoDec, Nodo exp){
        this.tipoDeclaracion=tipoDec;
        this.declaracion=null;
        this.declaracion_else=null;
        this.expresion=exp;
        this.expresionFor2=null;
        this.expresionFor3=null;
        
    }
    /**
     * metodo que genera el codigo del nodo en graphviz.
     * @param contNodos la cantidad de nodos visitados, esto para crear la id unica por cada nodo en graphviz.
     * @return cadena de codigo.
     */
    public String toGrapher(int contNodos){
        return "\"nodo"+contNodos+"\"[label=\""+this.tipoDeclaracion+"\"]; \n";
    }
    public String getTipoDeclaracion(){
        return this.tipoDeclaracion;
    }
    
    @Override
    public void aceptar(GrapherVisitor v) {
        v.visitar(this);
    }

    /**
     * @return the declaracion_else
     */
    public Nodo getDeclaracion_else() {
        return this.declaracion_else;
    }

    /**
     * @return the expresion
     */
    public Nodo getExpresion() {
        return this.expresion;
    }

    /**
     * @return the expresionFor2
     */
    public Nodo getExpresionFor2() {
        return this.expresionFor2;
    }

    /**
     * @return the expresionFor3
     */
    public Nodo getExpresionFor3() {
        return this.expresionFor3;
    }

    /**
     * @return the declaracion
     */
    public Nodo getDeclaracion() {
        return this.declaracion;
    }
    
}
