/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticVisitor;

import ast.Call;
import ast.Compound;
import ast.Expression;
import ast.FunDec;
import ast.Param;
import ast.Program;
import ast.Statement;
import ast.Var;
import ast.VarDec;
import visitor.*;
/**
 * Clase encargada de la realizacion del Segundo recorrido al AST, el cual corresponde al chequeo de tipos del arbol.
 * 
 * @author Jonathan Vasquez - Eduardo tapia.
 */
public class TypeCheckVisitor implements visitor {

    @Override
    public void visitar(Program program) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visitar(VarDec vardeclaration) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

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
