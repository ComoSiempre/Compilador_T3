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
 * Clase usada para la crecion del Nodo var.
 * 
 * @author Jonathan Vasquez - Eduardo Tapia.
 */
public class Var extends Nodo implements visitaNodo {

    String ID; //variable que guarda el nombre de la variable.
    Object Expression; //variable que guarda la posible expresion de sigue la variable (gramatica 21). o un Integer en caso de que sea un numero (gramatica 30).
    int numeroExpresion=0; //variable usada en el caso de que la expression sea un numero, gramatica 30, en este caso envez de subir el numero, se creara un NOdo var usando solo esta vairable.
    private String tipoVar; //variable que guarda el tipo del var aal buscarlo en la tabla de simbolos. (chequeo de tipos).
    
    
    /**
     * constructor 1, usado si la variable viene seguida por un nodo expression.
     * @param id nombre de la variable
     * @param expression nodo a seguir.
     */
    public Var(String id, Nodo expression){
        this.ID=id;
        this.Expression=expression;
    }
    /**
     * constructor 2, usado en caso de que la expresion corresponde a un numero (var ::= ID [ num ]).
     * @param id nombre del la variable.
     * @param numero el numero del vector.
     */
    public Var(String id, int numero){
        this.ID=id;
        this.Expression=numero;
    }
    /**
     * constructor 3, usado en la gramatica 21: var ::= ID.
     * @param id 
     */
    public Var(String id){
        this.ID=id;
        this.Expression=null;
    }
    /**
     * constructor 4, usado cuando se nesesita guardar un numero en un nodo. (usado solo en gramatica 30).
     * @param numero 
     */
    public Var(int numero){
        this.numeroExpresion=numero;
        this.Expression=null;
        this.ID=null;
    }
    /**
     * metodo que genera el codigo Graphviz para el nodo Var.
     * @param contNodos la cantidad de nodos visitados.
     * @return codigo Graph del nodo.
     */
    public String toGrapher(int contNodos){
        
        //condicionante usado para crar el codigo segun si la expresion corresponde a un numero o un nodo.
        if(this.Expression==null){
            return "\"nodo"+contNodos+"\"[label=\"Variable: "+this.ID+"\" "
                    + "color = orangered "
                    + "style = filled]; \n";
        }else{
            //en caso contrario, la expresion corresponde a un numero.
            return "\"nodo"+contNodos+"\"[label=\"Variable: "+this.ID+"[]\" "
                    + "color = orangered "
                    + "style = filled]; \n";
        }
           
    }
    /**
     * metodo que genera el codigo extendido Graphviz para el nodo Var.
     * @param contNodos la cantidad de nodos visitados.
     * @return codigo Graph del nodo.
     */
    public String toExtendedGrapher(int contNodos){
        
        //condicionante usado para crar el codigo segun si la expresion corresponde a un numero o un nodo.
        if(this.Expression==null){
            return "\"nodo"+contNodos+"\"[label=\"Variable: "+this.ID+", <"+this.tipoVar+">\" "
                    + "color = orangered "
                    + "style = filled]; \n";
        }else{
            //en caso contrario, la expresion corresponde a un numero.
            return "\"nodo"+contNodos+"\"[label=\"Variable: "+this.ID+"[]"+", <"+this.tipoVar+">\" "
                    + "color = orangered "
                    + "style = filled]; \n";
        }
           
    }
    /**
     * metodo que genera el codigo GRaphviz del nodo Var en caso de que guarde un numero.
     * @param contNodos la cantidad de nodos visitados.
     * @return codigo Graph del nodo.
     */
    public String toGrapherNumero(int contNodos){
        return "\"nodo"+contNodos+"\"[label=\""+this.numeroExpresion+"\" "
                + "color = orangered "
                + "style = filled]; \n";
    }
    
    //metodos GET.
    
    public String getID(){
        return this.ID;
    }
    public Object getExpression(){
        return this.Expression;
    }
    public int getNumeroExpresion(){
        return this.numeroExpresion;
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
     * @return the tipoVar
     */
    public String getTipoVar() {
        return tipoVar;
    }

    /**
     * @param tipoVar the tipoVar to set
     */
    public void setTipoVar(String tipoVar) {
        this.tipoVar = tipoVar;
    }
    
}
