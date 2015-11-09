/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

/**
 * Clase abstracta usada para las instancias en la creacion del arbol sintactico abstracto.
 * Esta clase es la clase 'padre' de los nodos que se usan en el AST. 
 * @author jonathan Vasquez - Eduardo Tapia
 */
public abstract class Nodo implements visitaNodo {
    public Nodo(){
        super();
    }
    
}
