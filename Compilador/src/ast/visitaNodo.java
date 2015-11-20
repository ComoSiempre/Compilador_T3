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
 * Interface usada para la implementacion del patron visitor para los nodos creados en el paquete ast.
 * @author Jonathan Vasquez - eduardo Tapia.
 */
public interface visitaNodo {
    public void aceptar(GrapherVisitor v);//visitar nodos para la construccion de codigo Graphviz del AST sintactico.
    public void aceptar(ScopeAnalisisVisitor s); //visitar nodos desde el primer recorrido semantico.
    public void aceptar(TypeCheckVisitor t); //visitar nodos desde el segundo recorrido semantico.
    public void aceptar(ExtendedGrapherVisitor v2); //visitar nodos para la construccion de codigo Graphviz del nuevo AST.
}
