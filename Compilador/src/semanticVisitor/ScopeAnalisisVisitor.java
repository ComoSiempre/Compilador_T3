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
import java.util.Stack;
import visitor.*;

/**
 * Clase encargada de la realizacion del primer recorrido al AST, el cual corresponde al analisis de Alcanse.
 * Dentro de este recorrido, se realizara la construccion de las declaraciones en la tabla de simbolos, de esta forma
 * se analizara los errores dentro de este recorrido.
 * 
 * @author Jonathan Vasquez - Eduardo Tapia.
 */
public class ScopeAnalisisVisitor implements visitor {
    SymbolTable tablaSimbolos;
    //pila auxiliar para guardar los alcances que son eliminados en el recorrido en la tabla de simbolos.
    //al final del recorrido, se tomaran todos los alcances del auxiliar y se asignaran a la tabla de contenidos.
    Stack<Alcance> pilaAux = new Stack<>();
    //variable auxiliar que guardara al padre.
    
    
    public ScopeAnalisisVisitor(){
        //la tabla de simbolos referenciara la tabla de simbolos creada en el compilador (Simgleton)
        this.tablaSimbolos= SymbolTable.instancia;
    }
    
    /**
     * Metodo usado para la reconstruccion de la tabla de simbolos, reingresando los alcances eliminados
     * en el proceso de recorrido del arbol AST. Esto es nesesario para la utilizacion de la tabla en el
     * chequeo de tipos (segundo recorido).
     */
    private void reconstruccionTabla(){
        //variable que representa el numero del alcance a buscar.
        int numAlcance = 2;
        //variable que requesenta la posicion del alcance en la pila auxiliar.
        int pos=0;
        
        //mistras existan elementos en la pila axuiliar.
        while(this.pilaAux.size() != 0){
            if(this.pilaAux.get(pos).getNumeroAlcance() == numAlcance){
                //si se encuentra el alcance en la pila. se ingresa a la tabla de simbolos.
                Alcance al=this.pilaAux.remove(pos);
                this.tablaSimbolos.getTabla().push(al);//'al' no  es alcance padre, por lo que no uso 'pushScope'.
                numAlcance++;
                //reseteo la busqueda en la pila.
                pos=0;
            }else{
                pos++;
            }
        }
    }
    public void imprimirTablaSimbolos(){
        //imprimo el numero de tabla
        System.out.println("num alcances: "+this.tablaSimbolos.getTabla().size());
        this.tablaSimbolos.imprimirTabla();
    }
    @Override
    public void visitar(Program program) {
        System.out.println("Iniciando primer recorrido Semantico");
        //primer alcance.
        //ingreso el alcance a la tabla de simbolos.
        this.tablaSimbolos.pushScope(null);
        //recorro el AST.
        ArrayList<Nodo> listaDec = program.getDeclaraciones();
        for(Nodo dec : listaDec){
            dec.aceptar(this);
        }
        //se reordena la pila de alcances.
        reconstruccionTabla();
        System.out.println("Primer recorrido Semantico terminado");
    }

    @Override
    public void visitar(VarDec vardeclaration) {
        if(vardeclaration.getTipoID().equalsIgnoreCase("void")){
            System.err.println("Error Semantico!. Fila: "+vardeclaration.getFila()+", Columna: "+vardeclaration.getColumna()+".\nLa variable "+vardeclaration.getID()+" es de tipo VOID.");
            System.exit(0);//termino el proceso de compilacion.
        }
        boolean resultado = this.tablaSimbolos.insertSymbol(vardeclaration);
        //condicionante usado en caso de que no se ingresara la declaracion en tabla (ya existe uno con igual id y tipo).
        if(!resultado){
            //no se ingreso, variable dubplicada.
            System.err.println("Error Semantico!. Fila: "+vardeclaration.getFila()+", Columna: "+vardeclaration.getColumna()+".\nLa variable "+vardeclaration.getID()+" ya ha sido declarada.");
            System.exit(0);//termino el proceso de compilacion.
        }
        
    }
    //kedde aqui.
    @Override
    public void visitar(FunDec function) {
        //ingreso la declacacion de funcion al alcance actual.
        boolean result = this.tablaSimbolos.insertSymbol(function);
        if(!result){
            //no se ingreso, funcion dubplicada.
            System.err.println("Error Semantico!. Fila: "+function.getFila()+", Columna: "+function.getColumna()+".\nLa funcion "+function.getID()+" ya ha sido declarada.");
            System.exit(0);//termino el proceso de compilacion.
        }
        //creo un nuevo alcance, el del interior de la funcion.
        this.tablaSimbolos.pushScope(this.tablaSimbolos.getTabla().peek());
        
        //se visitan los parametros.
        for(Nodo nodo : function.getListaParametros()){
            nodo.aceptar(this);
        }
        //usado si no existe nada el compound.
        if(function.getCompound()!=null)
            //se visita el compomente.
            function.getCompound().aceptar(this);
        //verifico que la funcion tenga return.
        if(!function.getTieneReturn() && function.getTipoIDFuntion().equalsIgnoreCase("int")){
            System.err.println("Error Semantico!. Fila: "+function.getFila()+", Columna: "+function.getColumna()+".\nLa funcion "+function.getID()+" no tiene retorno.");
            System.exit(0);//termino el proceso de compilacion.
        }
        
        //ya visitado el componente y sus variables locales, se elimina el alcance de la pila.
        Alcance scopeEliminado = this.tablaSimbolos.popScope();
        //se guarda el alcance eliminado a la pila auxiliar.
        this.pilaAux.push(scopeEliminado);
        //condicionante si la tabla queda vacio, por lo que se ingreso el ultimo alcance que corresponderia al alcance global.
        if(this.tablaSimbolos.sinAlcances()){
            this.tablaSimbolos.pushScope(scopeEliminado);
        }
    }

    @Override
    public void visitar(Param parametro) {
        //se ingresa el parametro al alcance.
        boolean result = this.tablaSimbolos.insertSymbol(parametro);
        
    }

    @Override
    public void visitar(Compound componente) {
        //creo un nuevo alcance.
        //this.tablaSimbolos.pushScope(this.tablaSimbolos.getTabla().peek());
        
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
        expresion.getOperador1().aceptar(this);
        expresion.getOperador2().aceptar(this);
    }

    @Override
    public void visitar(Var var) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visitar(Call call) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visitar(Statement stmt) {
        //condicionantes usadas para la visita dependiento del tipo de declaracion.
        if(stmt.getTipoDeclaracion().equalsIgnoreCase("if") && stmt.getDeclaracion_else() == null){
            //el nodo corresponde a un nodo de IF.
            stmt.getExpresion().aceptar(this);
            
            
            if(stmt.getDeclaracion()!= null){
                //creo un nuevo alcanc para el estamento.
                this.tablaSimbolos.pushScope(this.tablaSimbolos.getTabla().peek());
                //en caso de que hubiera un ; (expresion_stmt) en la declaracion (en este caso no se hace nodo).
                stmt.getDeclaracion().aceptar(this);
                //ya terminada la visita se elimina el alcance del if.
                Alcance scopeEliminado=this.tablaSimbolos.popScope();
                //se ingresa el alcance eliminado a la pila auxiliar.
                this.pilaAux.push(scopeEliminado);
            }
                
            
        }else if(stmt.getTipoDeclaracion().equalsIgnoreCase("if-else") && stmt.getDeclaracion_else() != null){
            //creo un alcance aux para eliminar los alcances de la tabla e ingresarlos a la pila auxiliar.
            Alcance scopeIF=null;
            //el nodo corresponde a un nodo IF-ELSE.
            stmt.getExpresion().aceptar(this);
            //obtengo el alcance padre.
            Alcance padreIF = this.tablaSimbolos.getTabla().peek();
            
            if(stmt.getDeclaracion()!= null){
                //creo el alcance del if.
                this.tablaSimbolos.pushScope(padreIF);
                
                //en caso de que hubiera un ; (expresion_stmt) en la declaracion (en este caso no se hace nodo).
                stmt.getDeclaracion().aceptar(this);
                
                //con las visitas ya hechas, se eliminan los alcances.
                scopeIF = this.tablaSimbolos.popScope();
                //se guarda el alcance eliminado en la pila axuiliar
                this.pilaAux.push(scopeIF);
            }
                
            if(stmt.getDeclaracion_else() != null){
                //creo el alcance para el ELSE.
                this.tablaSimbolos.pushScope(padreIF);
                //visito el estamento del bloque ELSE.
                stmt.getDeclaracion_else().aceptar(this);
                //con las visitas ya hechas, se eliminan los alcances.
                scopeIF=this.tablaSimbolos.popScope();
                //se guarda el alcance eliminado en la pila axuiliar
                this.pilaAux.push(scopeIF);
            }
            
            
                
        }else if(stmt.getTipoDeclaracion().equalsIgnoreCase("while")){
            //el nodo corresponde a un nodo WHILE.
            stmt.getExpresion().aceptar(this);
            
            
            if(stmt.getDeclaracion()!= null){
                //creo el alcance de while.
                this.tablaSimbolos.pushScope(this.tablaSimbolos.getTabla().peek());
                //en caso de que hubiera un ; (expresion_stmt) en la declaracion (en este caso no se hace nodo).
                stmt.getDeclaracion().aceptar(this);
                //se elimina el alcance al terminar de visitar el cilco while.
                Alcance scopeEliminado = this.tablaSimbolos.popScope();
                //se ingresa el alcance elimnado a la pila auxiliar.
                this.pilaAux.push(scopeEliminado);
            }
                
            
        }else if(stmt.getTipoDeclaracion().equalsIgnoreCase("for")){
            //el nodo corresponde a un nodo FOR.
            stmt.getExpresion().aceptar(this);
            stmt.getExpresionFor2().aceptar(this);
            stmt.getExpresionFor3().aceptar(this);
            if(stmt.getDeclaracion()!= null)
                //creo el nuevo alcance dentro del for.
                this.tablaSimbolos.pushScope(this.tablaSimbolos.getTabla().peek());
                //visito el alcance.
                //en caso de que hubiera un ; (expresion_stmt) en la declaracion (en este caso no se hace nodo).
                stmt.getDeclaracion().aceptar(this);
                //ya visitado el estamento de for. se elimina el alcance.
                Alcance scopeEliminado = this.tablaSimbolos.popScope();
                //se guarda el alcance eliminado en la pila auxiliar.
                this.pilaAux.push(scopeEliminado);
            
        }else if(stmt.getTipoDeclaracion().equalsIgnoreCase("return") && stmt.getExpresion() != null){
            //el nodo corresponde a un nodo RETURN (gramatica 2).
            stmt.getExpresion().aceptar(this);
            
        }else{
            //seria RETURN ;
            
        }
    }
    
}
