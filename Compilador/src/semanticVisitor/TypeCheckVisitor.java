/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticVisitor;

import Tables.SymbolTable;
import ast.Call;
import ast.Compound;
import ast.Expression;
import ast.FunDec;
import ast.Nodo;
import ast.Param;
import ast.Program;
import ast.Statement;
import ast.Var;
import ast.VarDec;
import java.util.ArrayList;
import visitor.*;
/**
 * Clase encargada de la realizacion del Segundo recorrido al AST, el cual corresponde al chequeo de tipos del arbol.
 * 
 * @author Jonathan Vasquez - Eduardo tapia.
 */
public class TypeCheckVisitor implements visitor {

    
    
    SymbolTable tablaSimbolos;
    /**
     * Constructor
     * Declaración de instancia de la tabla de symbolos para ubicar los tipos   
     */
    public TypeCheckVisitor() {
     
        this.tablaSimbolos = SymbolTable.instancia;
    }
    
    
    @Override
    public void visitar(Program program) {
        System.out.println("Inicio Segundo Recorrido Semantico");
        //recorro el AST.
        ArrayList<Nodo> listaDec = program.getDeclaraciones();
        for(Nodo dec : listaDec){
            dec.aceptar(this);
        }
        System.out.println("Fin del segundo recorrido semantico");
    }

    @Override
    public void visitar(VarDec vardeclaration) {
    
        
    //Asigna a la lookupSymbol buscando en la tabla de symbolos la declaracion de la variable.
        //lookupSymbol posee ahora el tipo de la variable.
       
//        //retorna por pantalla el tipo encontrado
//        System.out.println("Simbolo encontrado : "+lookupSymbol);
//        // Si no se ha encontrado un tipo entonces es un error semantico
//        if(lookupSymbol == null || lookupSymbol.isEmpty()){
//            System.err.println("Error semantico en linea :" + vardeclaration.getFila()+", columna: "+vardeclaration.getColumna());
//            System.err.println("La variable: "+vardeclaration.getID()+ "no posee un tipo definido");
//        }
    }   
    
    @Override
    public void visitar(FunDec function) {
       //visito los nodos de parametros.
        for(Nodo nodo : function.getListaParametros()){
            nodo.aceptar(this);
        }
        
         //visito el nodo Compound.
        function.getCompound().aceptar(this);
    }

    @Override
    public void visitar(Param parametro) {
        parametro.aceptar(this);
    }

    @Override
    public void visitar(Compound componente) {
        //visito los nodos de variables locales.
        for(Nodo nodo : componente.getLocalVar()){
            nodo.aceptar(this);
        }
        
        //visito los nodos de sentencias.
        for(Nodo nodo : componente.getStatements()){
            nodo.aceptar(this);
        }
    }

    @Override
    public void visitar(Expression expresion) {
        
        //relizar comparacion tipos
        // agregar tipos a nodos a Nodo Tipo.
        //  inorden
        /* Donde se agregaran los tipos de datos...  basasrse en el grapher visitor
        no se necesita visitar todos los nodos,
        
        revisar nodos en parser.
        errores semanticos---> system(0)
        declarar tabla de simbolos como una instancia en la clase 
        */
        //if (expresion.getTipo)
        if(expresion.getEsComparacion()){
           String tipo1 = null;
           String tipo2 = null;
           if(expresion.getOperador1() instanceof Var){
               Var var = (Var)expresion.getOperador1();
               tipo1 = this.tablaSimbolos.lookupSymbol(var.getID());
           }
           
           if(expresion.getOperador2() instanceof Var){
              Var var = (Var)expresion.getOperador2();
              tipo2 = this.tablaSimbolos.lookupSymbol(var.getID());
           }
           if(tipo1 == null ? tipo2 != null : !tipo1.equals(tipo2)){
               System.err.println("Error semantico : en Fila "+ expresion.getFila()+" Columna :"+ expresion.getColumna()+" los tipos son incompatibles.");
           } else {
           }
        }
        
        expresion.getOperador1().aceptar(this);
        expresion.getOperador2().aceptar(this);
        
    }

    @Override
    public void visitar(Var var) {
        boolean bool = this.tablaSimbolos.lookupSymbol(var);
        // comprobar variable no inicializada
        if (!bool && var.getID()!=null){
            System.err.println("Error Semantico! en linea: "+var.getFila() + " columna: "+var.getColumna());
            System.err.println("Variable " +var.getID()+" no inicializada");
            System.exit(0);
        }
        
    }

    @Override
    public void visitar(Call call) {
        //visito los argumentos.
        for(Nodo argumento : call.getArgs()){
            argumento.aceptar(this);
        }
        
    }

    @Override
    public void visitar(Statement stmt) {
       //condicionantes usadas para la visita dependiento del tipo de declaracion.
        if(stmt.getTipoDeclaracion().equalsIgnoreCase("if") && stmt.getDeclaracion_else() == null){
            //el nodo corresponde a un nodo de IF.
            stmt.getExpresion().aceptar(this);
            if(stmt.getDeclaracion()!= null)
                //en caso de que hubiera un ; (expresion_stmt) en la declaracion (en este caso no se hace nodo).
                stmt.getDeclaracion().aceptar(this);
        }else if(stmt.getTipoDeclaracion().equalsIgnoreCase("if-else") && stmt.getDeclaracion_else() != null){
            //el nodo corresponde a un nodo IF-ELSE.
            stmt.getExpresion().aceptar(this);
            if(stmt.getDeclaracion()!= null)
                //en caso de que hubiera un ; (expresion_stmt) en la declaracion (en este caso no se hace nodo).
                stmt.getDeclaracion().aceptar(this);
            if(stmt.getDeclaracion_else() != null)
                stmt.getDeclaracion_else().aceptar(this);
        }else if(stmt.getTipoDeclaracion().equalsIgnoreCase("while")){
            //el nodo corresponde a un nodo WHILE.
            stmt.getExpresion().aceptar(this);
            if(stmt.getDeclaracion()!= null)
                //en caso de que hubiera un ; (expresion_stmt) en la declaracion (en este caso no se hace nodo).
                stmt.getDeclaracion().aceptar(this);
            
        }else if(stmt.getTipoDeclaracion().equalsIgnoreCase("for")){
            //el nodo corresponde a un nodo FOR.
            stmt.getExpresion().aceptar(this);
            stmt.getExpresionFor2().aceptar(this);
            stmt.getExpresionFor3().aceptar(this);
            if(stmt.getDeclaracion()!= null)
                //en caso de que hubiera un ; (expresion_stmt) en la declaracion (en este caso no se hace nodo).
                stmt.getDeclaracion().aceptar(this);
            
        }else if(stmt.getTipoDeclaracion().equalsIgnoreCase("return") && stmt.getExpresion() != null){
            //el nodo corresponde a un nodo RETURN (gramatica 2).
            stmt.getExpresion().aceptar(this);
        }     
        
    }
    
}
