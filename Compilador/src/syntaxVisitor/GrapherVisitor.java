/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syntaxVisitor;

import ast.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import visitor.visitor;

/**
 * Clase usada para la generacion de codigo del arbol AST para la crecion de las imagenes en la aplicacion GraphViz.
 * @author Jonathan Vasquez - eduardo Tapia.
 */
public class GrapherVisitor implements visitor {
    String pathBase; //guardo la direccion del proyecto.
    int cantNodosVisitados; //contador de nodos visitados, nesesario para la creacion de nodos en el graphviz.
    int numeroEjercicio; //variable que guarda el numero del ejercicio a compilar, nesesario para la creacion de archivos de imagen y .dot de cada ejercicio.
    String codigoGraph; //variable que guarda el codigo nesesario para la creacion grafica del AST en graphviz.
    Stack<String> auxPadres= new Stack<>(); //pila usada para guardar los padres en las visitas, nesesario para la creacion de nodods en graphviz.
    
    /**
     * constructor dle GrapherVisitor.
     * @param pathBase la direccion base del proyecto.
     * @param numEjercicio el numero actual del ejercicio.
     */
    public GrapherVisitor(String pathBase, int numEjercicio){
        this.cantNodosVisitados=0;
        this.pathBase=pathBase;
        this.codigoGraph="";
        this.numeroEjercicio=numEjercicio;
    }
    /**
     * * metodo que genera la imagen del AST.
     * @param pathBase la direccion base del proyecto.
     */
    public void generarGraph() {
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("cod_"+this.numeroEjercicio+".dot"));
            writer.write("digraph G { \n");
            writer.write(this.codigoGraph);
            writer.flush();
            writer.write("}");
            writer.close();
            String dotPath="C:\\graphviz-2.38\\release\\bin\\dot.exe";//path del instalable del graphviz.
            String fileInputPath=pathBase+"/cod_"+this.numeroEjercicio+".dot"; //txt del codigo .dot del arbol
            String fileOutputPath = pathBase +"/img_cod_"+this.numeroEjercicio+".png"; //salida de la imagen del codigo.
            String tParam = "-Tpng";
            String tOParam = "-o";
            String [] cmd = new String[5];//variable para guardar el codigo en consola.
            cmd[0] = dotPath;
            cmd[1] = tParam;
            cmd[2] = fileInputPath;
            cmd[3] = tOParam;
            cmd[4] = fileOutputPath;
            Runtime rt = Runtime.getRuntime();
            rt.exec(cmd);
            System.out.println("Imagen creada");
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        
    }
    
    @Override
    public void visitar(Program program) {
        //obtengo el identificador dl primer nodo.
        String ident = program.toGrapher(this.cantNodosVisitados);
        //agrego el identificador al codigo.
        this.codigoGraph += ident;
        //sepero el nombre del identificador del nombre asignadodentro del nodo, posicion 0.
        String [] delimitador= ident.split("\\[");
        //obtengo la id del nodo 'sin el ;'.
        this.auxPadres.push(delimitador[0]);
        //cuento como nodo visitado.
        this.cantNodosVisitados++;
        //obtengo la lista de declaraciones dentro del nodo Program.
        ArrayList<Nodo> lista = program.getDeclaraciones();
        //visito los siguientes nodos (hijos).
        for(Nodo nodo : lista ){
            nodo.aceptar(this);
        }
        //elimino el padre actual.
        this.auxPadres.pop();
        try{
            this.generarGraph();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
    }

    @Override
    public void visitar(VarDec vardeclaration) {
        //obtengo el codigo del nodo y lo agrego a la cadena general del cosigo graph
        this.codigoGraph +=vardeclaration.toGrapher(this.auxPadres.peek(), this.cantNodosVisitados);
        //cuanto como nodo visitado.
        this.cantNodosVisitados++;
    }

    @Override
    public void visitar(FunDec function) {
        //obtengo el ident del nodo funcion.
        String ident = function.toGrapher(this.cantNodosVisitados);
        //agrego el ident a la cadena de codigo graph.
        this.codigoGraph += ident;
        String [] delimitador= ident.split("\\[");
        //creo el enlace del padre con el nuevo nodo funcion.
        String enlace = this.auxPadres.peek()+"->"+delimitador[0]+"; \n";
        //guardo el enlace en el codigo.
        this.codigoGraph += enlace;
        //guardo el nuevo nodo en la pila.
        this.auxPadres.push(delimitador[0]);
        //cuento como nodo visitado.
        this.cantNodosVisitados++;
        //visito los nodos de parametros.
        for(Nodo nodo : function.getListaParametros()){
            nodo.aceptar(this);
        }
        //visito el nodo Compound.
        function.getCompound().aceptar(this);
        //elimino el padre de la pila . (raiz del sub arbol contando desde aqui).
        this.auxPadres.pop();
    }

    @Override
    public void visitar(Param parametro) {
        //enlazo el codigo del nodo param a la cadena de codigo general del graphviz. 
        this.codigoGraph += parametro.toGrapher(this.auxPadres.peek(), this.cantNodosVisitados);
        //cuento como nodo visitado.
        this.cantNodosVisitados++;
    }

    @Override
    public void visitar(Compound componente) {
        //obtengo el codigo del nodo Compound.
        String ident = componente.toGrapher(this.cantNodosVisitados);
        //se une con la cadena general de codigo graphviz.
        this.codigoGraph += ident;
        String [] delimitador = ident.split("\\[");
        //creo el enlace del padre con el nuevo nodo Compound.
        String enlace = this.auxPadres.peek()+"->"+delimitador[0]+"; \n";
        //se une el codigo a l codigo general.
        this.codigoGraph += enlace;
        //guardo en la pila al padre.
        this.auxPadres.push(delimitador[0]);
        //cuento como nodo visitado.
        this.cantNodosVisitados++;
        //visito los nodos de variables locales.
        for(Nodo nodo : componente.getLocalVar()){
            nodo.aceptar(this);
        }
        //visito los nodos de sentencias.
        for(Nodo nodo : componente.getStatements()){
            nodo.aceptar(this);
            
        }
        
        //elimino el padre de la pila.
        this.auxPadres.pop();
    }

    @Override
    public void visitar(Expression expresion) {
        String ident= expresion.toGrapher(this.cantNodosVisitados);
        this.codigoGraph +=ident;
        String [] delimitador = ident.split("\\[");
        String enlace = this.auxPadres.peek()+"->"+delimitador[0]+"; \n";
        this.codigoGraph += enlace;
        this.auxPadres.push(delimitador[0]);
        this.cantNodosVisitados++;
        expresion.getOperador1().aceptar(this);
        expresion.getOperador2().aceptar(this);

        this.auxPadres.pop();
    }

    @Override
    public void visitar(Var var) {
        String ident="";
        //se verifica si el nodo es un terminal.
        if(var.getExpression()==null){
            //terminal.
            //Verifico si es un nodo terminal de ID (gramatica 21) o de num(gramatica 30).
            if(var.getID()==null){
                //el nodo corresponde a nodo numero.
                ident=var.toGrapherNumero(this.cantNodosVisitados);
            }else{
                //el nodo  corresponde al ID.
                ident=var.toGrapher(cantNodosVisitados);
            }
            this.codigoGraph += ident;
            String [] delimitador=ident.split("\\[");
            String enlace=this.auxPadres.peek()+"->"+delimitador[0]+"; \n";
            this.codigoGraph += enlace;
            this.cantNodosVisitados++;
        }else{
            //no terminal.
            //ahora se verifica si expresion corresponde a un numero o un Nodo.
            if(var.getID()==null){
                //la expresion corresponde a un numero.
                //se ingresa la cadena correpondiente al nodo var de numero.
                ident = var.toGrapherNumero(this.cantNodosVisitados);
            }else{
                //la espresion corresponde a un nodo.
                ident = var.toGrapher(this.cantNodosVisitados);
            }
            
            //tomo la cadena del variable vector (ya que esa gramataca dice que este nodo noes terminal).
            //ident = var.toGrapher(this.cantNodosVisitados);
            this.codigoGraph += ident;
            String[] delimitador = ident.split("\\[");
            //enlazo la cadena.
            String enlace = this.auxPadres.peek() + "->" + delimitador[0] + "; \n";
            this.codigoGraph += enlace;
            this.auxPadres.push(delimitador[0]);
            this.cantNodosVisitados++;
            if(var.getID()!=null) ((Nodo)var.getExpression()).aceptar(this);

            this.auxPadres.pop();
        }
        
        
    }

    @Override
    public void visitar(Call call) {
        //obtengo el codigo graph del nodo call.
        String ident = call.toGrapher(this.cantNodosVisitados);
        //uno al codigo general.
        this.codigoGraph += ident;
        String [] delimitador=ident.split("\\[");
        //enlazo el nodo con el padre.
        String enlace =this.auxPadres.peek()+"->"+delimitador[0]+"; \n";
        //guardo el enlace en el codigo general.
        this.codigoGraph += enlace;
        //guardo el nuevo nodo en la pila 
        this.auxPadres.push(delimitador[0]);
        //cuento como nodo visitado.
        this.cantNodosVisitados++;
        //visito los argumentos.
        for(Nodo argumento : call.getArgs()){
            argumento.aceptar(this);
        }
        //ya visto el subarbol de call, elimino la raiz .
        this.auxPadres.pop();
        
    }

    @Override
    public void visitar(Statement stmt) {
        
        //se genera el codigo del nodo.
        String ident= stmt.toGrapher(this.cantNodosVisitados);
        //se agrega al codigo general.
        this.codigoGraph += ident;
        //see obtiene el nombre ID del nodo para el enlace.
        String [] delimitador = ident.split("\\[");
        //se crea el enlace entre el nodo y su padre.
        String enlace = this.auxPadres.peek()+"->"+delimitador[0]+"; \n";
        //se agrega este enlace al codigo general.
        this.codigoGraph += enlace;
        //se agrega el nodo como el padre para la iniciacion de este sub-arbol.
        this.auxPadres.push(delimitador[0]);
        //se cuanta el nodo como visitado.
        this.cantNodosVisitados++;
        //condicionantes usadas para la visita dependiento del tipo de declaracion.
        if(stmt.getTipoDeclaracion().equalsIgnoreCase("if") && stmt.getDeclaracion_else() == null){
            //el nodo corresponde a un nodo de IF.
            stmt.getExpresion().aceptar(this);
            stmt.getDeclaracion().aceptar(this);
        }else if(stmt.getTipoDeclaracion().equalsIgnoreCase("if-else") && stmt.getDeclaracion_else() != null){
            //el nodo corresponde a un nodo IF-ELSE.
            stmt.getExpresion().aceptar(this);
            stmt.getDeclaracion().aceptar(this);
            stmt.getDeclaracion_else().aceptar(this);
        }else if(stmt.getTipoDeclaracion().equalsIgnoreCase("while")){
            //el nodo corresponde a un nodo WHILE.
            stmt.getExpresion().aceptar(this);
            stmt.getDeclaracion().aceptar(this);
            
        }else if(stmt.getTipoDeclaracion().equalsIgnoreCase("for")){
            //el nodo corresponde a un nodo FOR.
            stmt.getExpresion().aceptar(this);
            stmt.getExpresionFor2().aceptar(this);
            stmt.getExpresionFor3().aceptar(this);
            stmt.getDeclaracion().aceptar(this);
            
        }else if(stmt.getTipoDeclaracion().equalsIgnoreCase("return") && stmt.getExpresion() != null){
            //el nodo corresponde a un nodo RETURN (gramatica 2).
            stmt.getExpresion().aceptar(this);
            
        }
        //al terminar las visitas, elimino la raiz de este sub-arbol.
        this.auxPadres.pop();
    }
    
}
