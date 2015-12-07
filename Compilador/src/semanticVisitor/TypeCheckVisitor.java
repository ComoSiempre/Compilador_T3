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
 * Clase encargada de la realizacion del Segundo recorrido al AST, el cual
 * corresponde al chequeo de tipos del arbol.
 *
 * @author Jonathan Vasquez - Eduardo tapia.
 */
public class TypeCheckVisitor implements visitor {

    SymbolTable tablaSimbolos;
    String tipo;
    private FunDec funAux = null;
    Stack<Alcance> stack;
    int contextoActual = 1;

    /**
     * Constructor Declaración de instancia de la tabla de symbolos para ubicar
     * los tipos
     */
    public TypeCheckVisitor() {
        this.stack = new Stack<>();

        this.tablaSimbolos = SymbolTable.instancia;
    }

    @Override
    public void visitar(Program program) {
        System.out.println("Inicio Segundo Recorrido Semantico");
        //recorro el AST.
        ArrayList<Nodo> listaDec = program.getDeclaraciones();
        Alcance ambitoActual = this.tablaSimbolos.getAlcance(contextoActual);
        this.stack.push(ambitoActual);
        contextoActual++;
        for (Nodo dec : listaDec) {
            dec.aceptar(this);
        }

        System.out.println("Fin del segundo recorrido semantico");
    }

    @Override
    public void visitar(VarDec vardeclaration) {
        // No se permiten declaraciones de variables de tipo void
        if (vardeclaration.getTipoID().equalsIgnoreCase("void")) {
            System.err.println("Error Semantico! en linea: " + vardeclaration.getFila() + " columna:" + vardeclaration.getColumna());
            System.err.println(vardeclaration.getID() + " está declarada como un void");
            System.err.println("las variables deben ser de tipo INT!!");
            System.exit(0);
        }

    }

    @Override
    public void visitar(FunDec function) {
        //visito los nodos de parametros.
        //auxiliar para obtener tipo de funcion.
        this.funAux = function;
        for (Nodo nodo : function.getListaParametros()) {
            nodo.aceptar(this);
        }

        //visito el nodo Compound.
        function.getCompound().aceptar(this);
        // reseteo de funcion auxiliar
        this.funAux = null;

        if (this.stack.size() != 1) {
            this.stack.pop();
        }
    }

    @Override
    public void visitar(Param parametro) {
        // nada
    }

    @Override
    public void visitar(Compound componente) {

        Alcance ambitoActual = this.tablaSimbolos.getAlcance(contextoActual);
        this.stack.push(ambitoActual);
        contextoActual++;

        //visito los nodos de variables locales.
        for (Nodo nodo : componente.getLocalVar()) {
            nodo.aceptar(this);
        }

        //visito los nodos de sentencias.
        for (Nodo nodo : componente.getStatements()) {
            nodo.aceptar(this);
        }

        if (this.stack.size() != 1) {
            this.stack.pop();
        }

    }

    @Override
    public void visitar(Expression expresion) {

        //si la expresion es una comparacion 
        // debe comparar tipos iguales
        //boolean bool = this.tablaSimbolos.lookupSymbol(expresion.getOperador1());
        String tipo1 = null;
        // Si el nodo izquierdo es de tipo expresion, se visita. no deberia entrar
        if (expresion.getOperador1() instanceof Expression) {
            expresion.getOperador1().aceptar(this);
        }
        // en el caso de que sea variable
        if (expresion.getOperador1() instanceof Var) {
            if (((Var) expresion.getOperador1()).getID() == null && ((Var) expresion.getOperador1()).getExpression() == null) {
                tipo1 = "INT";
            } else {
                tipo1 = this.tablaSimbolos.retornaTipo(expresion.getOperador1(), this.stack.peek());
            }
        }
        // lado derecho
        String tipo2 = null;
        if (expresion.getOperador2() instanceof Var) {
            if (((Var) expresion.getOperador2()).getID() == null && ((Var) expresion.getOperador2()).getExpression() == null) {
                tipo2 = "INT";
            } else {
                tipo2 = this.tablaSimbolos.retornaTipo(expresion.getOperador2(), this.stack.peek());
            }
        }

        if (expresion.getOperador2() instanceof Expression) {
            expresion.getOperador2().aceptar(this);
        }

        if (expresion.getOperador2() instanceof Call) {
            expresion.getOperador2().aceptar(this);
            tipo2 = ((Call) expresion.getOperador2()).getTipoCall();
        }

        if (tipo1 != tipo2) {

            System.err.println("Error Semantico!! linea: " + expresion.getOperador1().getFila() + " columna: " + expresion.getOperador1().getColumna());
            System.err.println("Tipos de datos incompatibles");
            System.exit(0);

        }
        // si la comparacion se compara entre tipos igual entonces el resultado es de tipo booleano
        if (tipo1 == tipo2 && expresion.getEsComparacion()) {
            expresion.setTipoExpresion("bool");
        }

        // si la expresion es una asignacion
        if (tipo1 == tipo2 && expresion.getOperador().equalsIgnoreCase("::=")) {
            ((Var) expresion.getOperador1()).setTipoVar(tipo2);

        }
            // si la expresion no es ni asignacion ni una comparacion es una operacion 
        //por lo que se asigna el tipo
        if (tipo1 == tipo2 && !expresion.getEsComparacion()) {
            expresion.setTipoExpresion("INT");
        }

    }

    @Override
    public void visitar(Var var) {
        boolean bool = this.tablaSimbolos.lookupSymbol(var, this.stack.peek());
        var.setTipoVar(this.tablaSimbolos.retornaTipo(var, this.stack.peek()));
        // comprobar variable no inicializada
        if (!bool && var.getID() != null) {
            System.err.println("Error Semantico! en linea: " + var.getFila() + " columna: " + var.getColumna());
            System.err.println("Variable " + " \"" + var.getID() + " \"" + " no inicializada");
            System.exit(0);
        }

    }

    @Override
    public void visitar(Call call) {
        //Comprobacion de que se encuentre declarado
        boolean lookup = this.tablaSimbolos.lookupSymbol(call, this.stack.peek());
        if (!lookup) {
            System.err.println("Error Semantico en linea: " + call.getFila() + " columna: " + call.getColumna());
            System.err.println(call.getID() + " no se encuantra declarada");
            System.exit(0);
        }
        // se almacena el tipo de la funcion en una variable para su uso en ExtendeedGrapher
        call.setTipoCall(this.tablaSimbolos.retornaTipo(call, this.stack.peek(),call.getID()));

        
        //visito los argumentos.
        for (Nodo argumento : call.getArgs()) {
            argumento.aceptar(this);
        }

    }

    @Override
    public void visitar(Statement stmt) {
        //condicionantes usadas para la visita dependiento del tipo de declaracion.
        if (stmt.getTipoDeclaracion().equalsIgnoreCase("if") && stmt.getDeclaracion_else() == null) {
            //el nodo corresponde a un nodo de IF.
            stmt.getExpresion().aceptar(this);
            if (stmt.getDeclaracion() != null) //en caso de que hubiera un ; (expresion_stmt) en la declaracion (en este caso no se hace nodo).
            {
                stmt.getDeclaracion().aceptar(this);
            }
        } else if (stmt.getTipoDeclaracion().equalsIgnoreCase("if-else") && stmt.getDeclaracion_else() != null) {
            //el nodo corresponde a un nodo IF-ELSE.
            stmt.getExpresion().aceptar(this);
            if (stmt.getDeclaracion() != null) //en caso de que hubiera un ; (expresion_stmt) en la declaracion (en este caso no se hace nodo).
            {
                stmt.getDeclaracion().aceptar(this);
            }
            if (stmt.getDeclaracion_else() != null) {
                stmt.getDeclaracion_else().aceptar(this);
            }
        } else if (stmt.getTipoDeclaracion().equalsIgnoreCase("while")) {
            //el nodo corresponde a un nodo WHILE.
            stmt.getExpresion().aceptar(this);
            if (stmt.getDeclaracion() != null) //en caso de que hubiera un ; (expresion_stmt) en la declaracion (en este caso no se hace nodo).
            {
                stmt.getDeclaracion().aceptar(this);
            }

        } else if (stmt.getTipoDeclaracion().equalsIgnoreCase("for")) {
            //el nodo corresponde a un nodo FOR.
            stmt.getExpresion().aceptar(this);
            stmt.getExpresionFor2().aceptar(this);
            stmt.getExpresionFor3().aceptar(this);
            if (stmt.getDeclaracion() != null) //en caso de que hubiera un ; (expresion_stmt) en la declaracion (en este caso no se hace nodo).
            {
                stmt.getDeclaracion().aceptar(this);
            }

        } else if (stmt.getTipoDeclaracion().equalsIgnoreCase("return") && stmt.getExpresion() != null) {
            //el nodo corresponde a un nodo RETURN (gramatica 2).
            //se comprueba que  existan consistencia de tipos entre el return y la funcion.
            stmt.getExpresion().aceptar(this);

            if (stmt.getExpresion() instanceof Var) {
                if (((Var) stmt.getExpresion()).getExpression() == null && ((Var) stmt.getExpresion()).getID() == null) {

                    if (!this.funAux.getTipoIDFuntion().equalsIgnoreCase("INT")) {
                        System.err.println("Error Semantico!! linea: " + stmt.getFila() + " columna: " + stmt.getColumna());
                        System.err.println("Tipo de retorno no corresponde al declarado en la función.");
                        System.exit(0);
                    }
                } else {
                    if (!this.funAux.getTipoIDFuntion().equalsIgnoreCase(((Var) stmt.getExpresion()).getTipoVar())) {
                        System.err.println("Error Semantico!! linea: " + stmt.getFila() + " columna: " + stmt.getColumna());
                        System.err.println("Tipo de retorno no corresponde al declarado en la función.");
                        System.exit(0);
                    }
                }
                if (stmt.getExpresion() instanceof Call) {
                    if (!this.funAux.getTipoIDFuntion().equalsIgnoreCase(((Call) stmt.getExpresion()).getTipoCall())) {
                        System.err.println("Error Semantico!! linea: " + stmt.getFila() + " columna: " + stmt.getColumna());
                        System.err.println("Tipo de retorno no corresponde al declarado en la función.");
                        System.exit(0);
                    }
                }
                if (stmt.getExpresion() instanceof Expression) {
                    if (!this.funAux.getTipoIDFuntion().equalsIgnoreCase(((Expression) stmt.getExpresion()).getTipoExpresion())) {
                        System.err.println("Error Semantico!! linea: " + stmt.getFila() + " columna: " + stmt.getColumna());
                        System.err.println("Tipo de retorno no corresponde al declarado en la función.");
                        System.exit(0);
                    }

                }
            }

        } else if (stmt.getTipoDeclaracion().equalsIgnoreCase("return")) {
            //return tipo void
            if (!this.funAux.getTipoIDFuntion().equalsIgnoreCase("void")) {
                System.err.println("Error Semantico!! linea: " + stmt.getFila() + " columna: " + stmt.getColumna() + "\nTipo de retorno no corresponde al declarado en la función.");
                System.exit(0);
            }
        }

    }

}
