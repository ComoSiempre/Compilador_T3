/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import ast.*;
import Tables.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java_cup.runtime.Symbol;
import parser.*;
import scanner.*;
import semanticVisitor.ExtendedGrapherVisitor;
import semanticVisitor.ScopeAnalisisVisitor;
import semanticVisitor.TypeCheckVisitor;
import syntaxVisitor.GrapherVisitor;


/**
 * Proyecto correspondiente a la integracion de la fase de analisis Semantico en la implementacion del compilador
 * para el lenguaje K*, utilizando el analizador sintactico Java CUP y el analizador Lexico Jflex.
 * @author:     Jonatham Vasquez - Eduardo Tapia
 * @version:    31/10/2015/3.1.1.
 */
public class Compilador {
    /**
     * metodo que genera el archivo java lexer.java, que contiene la nomenclatura del analisis lexico.
     * @param path la direccion donde se encuentra el archivo .flex
     */       
    public static void generarLexer(String path){
        //se crea la variable File nesesaria para la generacion del lexer
        File file=new File(path);
        //se llama al paquete Jflex para la generacion del lexer enviando como parametro el archivo FIle.
        jflex.Main.generate(file);
    }
    /**
     * metodo que genera el archivo java Parser.java, que contiene la nomenclatura del analisis sintactico.
     * @param path la direccion donde se encuentra el archivo .cup
     */
    public static void generarParser(String path){
        String[] asintactico = { "-parser", "Parser", path };
        try {
            
            java_cup.Main.main(asintactico);
        } catch (Exception ex) {
            //System.out.println(ex.getMessage());
            Logger.getLogger(Compilador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * metodo usado para trasladar los archivos generados en el analisis lexico al paquete "scanner" 
     * @param nombreLexer el nombre Lexer.java
     * @return false si el traslado no se contreto, true el caso contrario.
     */
    public static boolean moverLex(String nombreLexer){
        boolean efectuado = false;
        //creacion variable File para traslado.
        File arch = new File(nombreLexer);
        //condicionante usado para confirmar la existencia del archivo Lexer.
        if (arch.exists()) {
            System.out.println("\n*** Moviendo " + arch + " \n***");
            Path currentRelativePath = Paths.get("");
            //guardo la direccion de destino.
            String nuevoDir = currentRelativePath.toAbsolutePath().toString()
                    + File.separator + "src" + File.separator + "scanner"
                    + File.separator + arch.getName();
            //creo el archivo en la nueva direccion.
            File archViejo = new File(nuevoDir);
            //elimino el archivo viejo.
            archViejo.delete();
            //genero el archivo en el paquete.
            if (arch.renameTo(new File(nuevoDir))) {
                System.out.println("\n*** Generado " + nombreLexer + "***\n");
                efectuado = true;
            } else {
                System.out.println("\n*** No movido " + nombreLexer + " ***\n");
            }

        } else {
            System.out.println("\n*** Codigo lexer no existente ***\n");
        }
        return efectuado;
    }//fin metodo 'moverLex'
    /**
     * metodo usado para trasladar los archivos generados en el analisis sintactico al paquete "parser"  
     * @param nombreParser el nombre "Parser.java"
     * @return falso si no se concreto el traslado, true en caso contrario.
     */
    public static boolean moverParser(String nombreParser){
        boolean efectuado = false;
        File arch = new File(nombreParser);
        if (arch.exists()) {
            System.out.println("\n*** Moviendo " + arch + " \n***");
            Path currentRelativePath = Paths.get("");
            String nuevoDir = currentRelativePath.toAbsolutePath().toString()
                    + File.separator + "src" + File.separator + "parser"
                    + File.separator + arch.getName();
            File archViejo = new File(nuevoDir);
            archViejo.delete();
            if (arch.renameTo(new File(nuevoDir))) {
                System.out.println("\n*** Generado " + nombreParser + "***\n");
                efectuado = true;
            } else {
                System.out.println("\n*** No movido " + nombreParser + " ***\n");
            }

        } else {
            System.out.println("\n*** Codigo parser no existente ***\n");
        }
        return efectuado;
    }//fin metodo 'moverParser'
    
    /**
     * metodo usado para mover los archivos a sus respectivos paquetes.
     */
    public static void move(){
        boolean mvLexer = moverLex("Lexer.java");
        boolean mvParser = moverParser("Parser.java");
        boolean mvSym = moverParser("sym.java");
        //condicionante usado para confirmar el traslado de archivos, segun su existencia despue de la compilacion.
        if (mvLexer && mvParser && mvSym) {
            System.exit(0);
        }
	
    }//fin metodo move.
    
    /**
     * metodo encargado de recorrer, detectar y guardar los literales a la tabla de literales.
     * @param lex Clase lexer donde esta el conjunto de simbolos segun el ejercicio.
     * @param pathEj direccion path de los ejercicios.
     * @param numEj numero del ejercicio, nesesario para la creacion de las keys de los literales.
     */
    private static void llenarLiterales(Lexer lex,String pathEj,int numEj){
        try {
            //ciclo usado para recorrer cada token del ejercicio.
            while (true) {
                //creo la variable de simbolo.
                Symbol text;
                //obtengo el token .
                text = lex.next_token();
                //condicionante usado si el token llego a EOF (Final de archivo).
                switch(text.sym){
                    case 0:
                        System.out.println("llego al final de archivo");
                    //reseteo el lexer, de esta forma es posible volver a analizar pero en el parser.
                    lex.yyreset(new FileReader(pathEj));
                    //retorno a la compilacion..
                    return;
                    
                    case 31: //NUM.
                        //guardo el literal a la tabla de literales.
                        LiteralTable.getInstancia().ingresarLiteral((String) text.value, numEj);
                        break;
                }//fin switch
                
            }//fin while.

        } catch (IOException ex) {
            Logger.getLogger(Compilador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//fin funcion 'llenarLiterales'
    /**
     * Metodo utilizado para el proceso de compilacion de los ejercicios utilizando
     * las fases de analisis lexico y sintactico.
     * @param pathEjercicio la direccion del ejercicio a compilar
     * @param pathBase la direccion base del proyecto.
     * @param numEjercicio el numero actual del ejercicio a compilar.
     */
    public static void compilar(String pathEjercicio, String pathBase, int numEjercicio){
        System.out.println("Comienzo compilacion Ejercicio "+numEjercicio);
        File arch = new File(pathEjercicio);
        
        try{
            //generacion de la tabla de simbolos.
            SymbolTable tablaSimbolos = new SymbolTable();
            //generacion analisis lexico del ejercicio.
            Lexer lexer = new Lexer(new FileReader(arch));
            //ingreso los literales usados a la tabla de literales.
            llenarLiterales(lexer,pathEjercicio,numEjercicio);
            //generacion de parser para el analisis sintactico.
            Parser parser = new Parser(lexer);
            //generacion del arbol sintactico abstracto.
            Program programa = (Program) parser.parse().value;
            //creacion de objeto GrapherVisitor para la generacion de imagen del AST.
            GrapherVisitor grapher = new GrapherVisitor(pathBase,numEjercicio);
            //se inicia el recorrido del AST para la generacion de la imagen del arbol utilizando el patron visitor.
            grapher.visitar(programa);
            //cracion de nuevo objeto GrapherVisitor para pa generacion del AST mejorado.
            //ExtendedGrapherVisitor grapher2 = new ExtendedGrapherVisitor();
            //generacion primer recorrido semantico.
            ScopeAnalisisVisitor sp1 = new ScopeAnalisisVisitor();
            sp1.visitar(programa);
            sp1.imprimirTablaSimbolos();
            //generacion segundo recorrido semantico.
            TypeCheckVisitor sp2 = new TypeCheckVisitor();
            sp2.visitar(programa);
            ExtendedGrapherVisitor visitor = new ExtendedGrapherVisitor(pathBase,numEjercicio);
            visitor.visitar(programa);
        }catch(Exception ex){
            ex.printStackTrace();
            
        }//fin try.
    }//fin funcion compilar.
    
    /**
     * metodo Menu, usado como intefaz usuario-sistema.
     * @param dir la direccion base del proyecto, nesesario para el proceso de compilacion.
     * @param in scanner de lectura, nesesario para la lectura por teclado de las opciones.
     * @throws IOException 
     */
    public static void menu(String dir, Scanner in) throws IOException{
        boolean flag=true;
        String sel="";
        do{
            System.out.println("***********************Taller 2: Analisis Sintactico *******************************");
            System.out.println("************************************************************************************");
            System.out.println("************************************************************************************");
            System.out.println("1.- Ejercicio_1.ks");
            System.out.println("2.- Ejercicio_2.ks");
            System.out.println("3.- Ejercicio_3.ks");
            System.out.println("4.- Ejercicio_4.ks");
            System.out.println("5.- Ejercicio_5.ks");
            System.out.println("6.- Salir");
            System.out.println("Seleccionar un ejercicio a compilar:");
            sel = in.nextLine();
            char[] s = sel.toCharArray();
            switch(s[0]){
                case '1':
                    compilar(dir+"/ejercicio_1.ks",dir,1);
                    break;
                case '2':
                    compilar(dir+"/ejercicio_2.ks",dir,2);
                    break;
                case '3':
                    compilar(dir+"/ejercicio_3.ks",dir,3);
                    break;
                case '4':
                    compilar(dir+"/ejercicio_4.ks",dir,4);
                    break;
                case'5':
                    compilar(dir+"//ejercicio_5.ks",dir,5);
                    break;
                case '6':
                    flag=false;
                    break;
                default:
                    System.out.println("Seleccionar un ejemplo valido:");
                    break;
            }//fin switch.
        }while(flag);
        
    }//fin metodo 'menu'
    
    public static void menuBrian(String dir, Scanner in) throws IOException{
        boolean flag=true;
        String sel="";
        do{
            System.out.println("***********************Taller 2: Analisis Sintactico *******************************");
            System.out.println("---------------------------EJERCICIOS DE PRUEBA-------------------------------------");
            System.out.println("************************************************************************************");
            System.out.println("1.- C1_ArrayExample.ks");
            System.out.println("2.- C2_Factorial.ks");
            System.out.println("3.- C3_PrintNTimes.ks");
            System.out.println("4.- I1_SelectionSort.ks");
            System.out.println("5.- I2_WrongParameters.ks");
            System.out.println("6.- I3_WrongReturn.ks");
            System.out.println("7.- I4_UndefFunction.ks");
            System.out.println("8.- Salir");
            System.out.println("Seleccionar un ejercicio a compilar:");
            sel = in.nextLine();
            char[] s = sel.toCharArray();
            switch(s[0]){
                case '1':
                    compilar(dir+"/Codigos_de_Prueba/C1_ArrayExample.ks",dir,1);
                    break;
                case '2':
                    compilar(dir+"/Codigos_de_Prueba/C2_Factorial.ks",dir,2);
                    break;
                case '3':
                    compilar(dir+"/Codigos_de_Prueba/C3_PrintNTimes.ks",dir,3);
                    break;
                case '4':
                    compilar(dir+"/Codigos_de_Prueba/I1_SelectionSort.ks",dir,4);
                    break;
                case'5':
                    compilar(dir+"/Codigos_de_Prueba/I2_WrongParameters.ks",dir,5);
                    break;
                case '6':
                    compilar(dir+"/Codigos_de_Prueba/I3_WrongReturn.ks",dir,6);
                    break;
                case '7':
                    compilar(dir+"/Codigos_de_Prueba/I4_UndefFunction.ks",dir,7);
                    break;
                case '8':
                    flag=false;
                    break;
                default:
                    System.out.println("Seleccionar un ejemplo valido:");
                    break;
            }//fin switch.
        }while(flag);
        
    }//fin metodo 'menu'
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        //creo el objeto Scanner para la lectura desde teclado, gracias al System.in.
        Scanner in = new Scanner(System.in );
        //variable donde obtengo la direccion del proyecto.
        final String dir =System.getProperty("user.dir");
        //variable usada para guardar la direccion del archivo .flex
        
        String pathFlex=dir+System.getProperty("file.separator")+"src"+System.getProperty("file.separator")+"scanner"+System.getProperty("file.separator")+"Lexer.flex";
        String pathParser=dir+System.getProperty("file.separator")+"src"+System.getProperty("file.separator")+"parser"+System.getProperty("file.separator")+"Parser.cup";
        
        //se llama a la funcion de generacion de lexer. 
        generarLexer(pathFlex);
        //se llama a la funcion de generacion de parser.
        generarParser(pathParser);
        //se mueven los archivos a los paquetes correspondientes.
        move();
        //comienza compilacion de ejercicios.
        menu(dir, in);
    }//fin Main.
    
}//fin Compilador.
