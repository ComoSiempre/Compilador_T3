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
 * clase usada para la crecion de nodo funcion. 
 * @author Jonathan Vasquez - Eduardo Tapia.
 */
public class FunDec extends Nodo implements visitaNodo {

    ArrayList<Nodo> listaParametros = new ArrayList<Nodo>(); //lista que guarda los parametros.
    Nodo compound_stmt; //variable nodo que guarda los compomentes dentro de la funcion.
    String ID; //variable que guarda el nombre de la funcion.
    String tipoIDFuntion; //variable que guarda el tipo de la funcion.
    private boolean parametroVoid=false;//variable flag que detecta si la funcion no tiene parametros (VOID)
    private boolean tieneReturn=true; //variable que verifica que la funcion tiene un return;
    /**
     * metodo constructor default.
     */
    public FunDec(){}
    
    /**
     * constructor usado en gramatica params, es nesesario ya que asi ascendera el nodo ya creado y solo se modifica.
     * @param parametroVoid flag si el parametro es VOID.
     */
    public FunDec(boolean parametroVoid){
        this.ID="";
        this.tipoIDFuntion="";
        this.parametroVoid=parametroVoid;
    }
    /**
     * metodo que ingresa un parametro a la lista de parametros.
     * @param parametro el nodo tipo Param.
     */
    public void agregarParametro(Nodo parametro){
        this.listaParametros.add(parametro);
    }
    
    /**
     * metodo que genera el codigo en Graphviz.
     * @param contNodos el contador de nodos creados hasta ahora.
     * @return la linea de codigo.
     */
    public String toGrapher(int contNodos){
        return "\"nodo"+contNodos+"\"[label=\"Funcion: "+this.ID+","+this.tipoIDFuntion+"\" "
                + "shape= polygon "
                + "sides = 7 "
                + "style = filled "
                + "color = lightskyblue1]; \n";
    }
    
    
    //metodos GET.
    
    public Nodo getCompound(){
        return this.compound_stmt;
    }
    public String getID(){
        return this.ID;
    }
    public String getTipoIDFuntion(){
        return this.tipoIDFuntion;
    }
    public ArrayList<Nodo> getListaParametros(){
        return this.listaParametros;
    }
    //metodods SET.
    
    public void setID(String ID){
        this.ID=ID;
    }
    public void setTipoFunction(String func){
        this.tipoIDFuntion=func;
    }
    public void setCompound_stmt(Nodo compo){
        this.compound_stmt=compo;
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
     * @return the tieneReturn
     */
    public boolean getTieneReturn() {
        return tieneReturn;
    }

    /**
     * @param tieneReturn the tieneReturn to set
     */
    public void setTieneReturn(boolean tieneReturn) {
        this.tieneReturn = tieneReturn;
    }

    /**
     * @return the parametroVoid
     */
    public boolean getParametroVoid() {
        return parametroVoid;
    }

    /**
     * @param parametroVoid the parametroVoid to set
     */
    public void setParametroVoid(boolean parametroVoid) {
        this.parametroVoid = parametroVoid;
    }

    
    
}
