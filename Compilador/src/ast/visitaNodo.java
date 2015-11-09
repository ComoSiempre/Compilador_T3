/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import syntaxVisitor.GrapherVisitor;
/**
 * Interface usada para la implementacion del patron visitor para los nodos creados en el paquete ast.
 * @author Jonathan Vasquez - eduardo Tapia.
 */
public interface visitaNodo {
    public void aceptar(GrapherVisitor v);//cambiar visitor por clase Grapherviz
}
