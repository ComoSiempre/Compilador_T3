/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import syntaxVisitor.GrapherVisitor;

/**
 * Clase que representa un Nodo VarDec, usado para las declaraciones de variables en los codigo a compilar.
 * @author Jonathan Vasquez - Eduardo Tapia.
 */
public class VarDec extends Nodo implements visitaNodo {

    String tipoID; //variable que guarda el tipo de la declaracion de variable (int o void)
    String ID;  //variable que guarda el nombre de la declaracion de varialbe.
    int numeroVector; //variable que guarda el numero del tamaño del vector en caso de que la declaracion corresponda a un vector.
    boolean esVector; //variable flag que identifica la declaracion como varialbe normal o vector.
    /**
     * constructor usado en caso de que la declaracion en normal.
     * @param tipo el tipo de la variable (int o void)
     * @param id el nombre de la declaracion
     * @param normal flag variable normal (false)
     */
    public VarDec(String tipo, String id, boolean normal){
        this.tipoID=tipo;
        this.ID=id;
        this.esVector=normal;
        this.numeroVector=0;
    }
    /**
     * constructor usado en caso de que la declaracion sea vector.
     * @param tipo el tipo de la declaracion (int o void)
     * @param id nombre de la declaracion
     * @param vector flag declaracion vector (TRUE)
     * @param numeroVector tamaño del vector.
     */
    public VarDec(String tipo, String id, boolean vector,int numeroVector){
        this.tipoID=tipo;
        this.ID=id;
        this.esVector=vector;
        this.numeroVector=numeroVector;
    }
    /**
     * metodo que genera el codigo del nodo para le grapherviz.
     * @param nombrePadre el nombre del nodo padre el cual esta enlazado este actual nodo.
     * @param contNodos la cantidad de nodos visitados, de esta forma se crean los nodos segun un "id" .
     * @return la linea de comandos creado.
     */
    public String toGrapher(String nombrePadre, int contNodos){
        String lineas="";
        //condicionante segun el tipo de declaracion de variable.
        if (this.esVector==false){
            //se crea el nodo dandole el identificador.
            lineas += "\"nodo"+contNodos+"\"[label=\""+this.ID+":"+this.tipoID+"\"]; \n";
            //enlazo el nuevo nodo con su padre.
            lineas += nombrePadre+"->\"nodo"+contNodos+"\"; \n";
        }else{
            lineas += "\"nodo"+contNodos+"\"[label=\""+this.ID+":"+this.tipoID+"["+this.numeroVector+"]\"]; \n";
            lineas += nombrePadre+"->\"nodo"+contNodos+"\"; \n";
        }
        return lineas;
    }
    
    @Override
    public void aceptar(GrapherVisitor v) {
        v.visitar(this);
    }
    
    
}
