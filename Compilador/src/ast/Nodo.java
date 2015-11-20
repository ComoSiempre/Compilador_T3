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
    private int fila; //variable que guarda la fila en donde esta el nodo en el codigo.
    private int columna; //variable que guarda la columna en donde esta el nodo en el codigo.
    public Nodo(){
        super();
    }

    /**
     * @return the fila
     */
    public int getFila() {
        return fila;
    }

    /**
     * @param fila the fila to set
     */
    public void setFila(int fila) {
        this.fila = fila;
    }

    /**
     * @return the columna
     */
    public int getColumna() {
        return columna;
    }

    /**
     * @param columna the columna to set
     */
    public void setColumna(int columna) {
        this.columna = columna;
    }
    
}
