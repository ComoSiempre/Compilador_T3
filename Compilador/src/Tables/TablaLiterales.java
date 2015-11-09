/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tables;

import java.util.Hashtable;
import java.util.Enumeration;
/**
 * clase usada para la generacion de la tabla de literales con patron Singleton.
 * @author jonathan Vasquez - Eduardo Tapia
 */
public class TablaLiterales {
    //tabla que almacena literales
    private Hashtable<String,String> literales=new Hashtable<String,String>();
    private int serial=1;//variable usado para guardar los numeros guardados en tabla.
    
    //variable de instancia de la clase.
    private static TablaLiterales instanciaTabla;
    
    /**
     * constructor interno de la clase.
     */
    private TablaLiterales(){ 
    }
    
    /**
     * metodo donde obtengo la instancia de la clase.
     * 
     * @return instancia de la clase
     */
    public static TablaLiterales getInstancia(){
        if(instanciaTabla==null)
            instanciaTabla=new TablaLiterales();
        return instanciaTabla;
    }
    /**
     * metodo que retorna la tabla de literales
     * @return tabla hash de literales.
     */
    public Hashtable<String,String> getLiterales(){
        return literales;
    }
    /**
     * metodo que imprime el contenido de la tabla hash.
     */
    public void imprimirTabla(String nombreArch){
        System.out.println("tabla "+nombreArch);
        Enumeration<String> enumeracion= this.literales.elements();
        while(enumeracion.hasMoreElements()){
            System.out.println("Valores:"+enumeracion.nextElement());
        }
    }
    /**
     * metodo que imprime las llaves de los literales de la tabla.
     * @param nombreArch 
     */
    public void imprimirLlavesTabla(String nombreArch){
        System.out.println("tabla "+nombreArch);
        Enumeration<String> enumeracion= this.literales.keys();
        while(enumeracion.hasMoreElements()){
            System.out.println("Valores:"+enumeracion.nextElement());
        }
    }
    /**
     * metodo usado para el almacenamiento de los literales en la tabla hash.
     * 
     * @param literal la constante que se almacenara
     * @param numeroEjemplo el numero del ejemplo que esta siendo analizado y que servira para la generacion de la llave.
     *  
     */
    public void ingresarLiteral(String literal, int numeroEjemplo){
        this.literales.put(Integer.toString(numeroEjemplo)+"."+Integer.toString(serial), literal);
        this.serial++;
    }
}
