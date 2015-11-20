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
    //pila auxiliar para el recorrido por el ast y antener los alcances ordenados.
    Stack<Alcance> pilaAux = new Stack<>();
    public ScopeAnalisisVisitor(){
        //la tabla de simbolos referenciara la tabla de simbolos creada en el compilador (Simgleton)
        this.tablaSimbolos= SymbolTable.instancia;
    }
    
    @Override
    public void visitar(Program program) {
        //primer alcance.
        //creo el alcance raiz.
        Alcance raiz = new Alcance(this.tablaSimbolos.getContadorAlcance(),null);
        //ingreso el alcance a la tabla de simbolos.
        this.tablaSimbolos.pushScope(raiz);
        //recorro el AST.
        ArrayList<Nodo> listaDec = program.getDeclaraciones();
        for(Nodo dec : listaDec){
            dec.aceptar(this);
        }
        //se reordena la pila de alcances.
        
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visitar(Param parametro) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visitar(Compound componente) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visitar(Expression expresion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
