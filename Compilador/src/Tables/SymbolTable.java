/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tables;

/**
 * Clase que corresponde a la tabla de simbolos usada en el proceso de analisis semantico.
 * Se utilizara el patron Singleton para su uso en el compilador.
 * 
 * @author Jonathan Vasquez - Eduardo Tapia.
 */
public class SymbolTable {
    
    //variable instancia de la tabla de simbolos.
    public static SymbolTable instancia; 
    public SymbolTable(){
        instancia = this;
    }
    
}
