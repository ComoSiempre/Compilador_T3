/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visitor;

import ast.*;
/**
 * Interface entre los nodos y el GrapherVisitor.
 * nesesario para la utilizacion del patron visitor.
 * @author Jonathan Vasquez - Eduardo Tapia
 */
public interface visitor {
    public void visitar(Program program);
    public void visitar(VarDec vardeclaration);
    public void visitar(FunDec function);
    public void visitar(Param parametro);
    public void visitar(Compound componente);
    public void visitar(Expression expresion);
    public void visitar(Var var);
    public void visitar(Call call);
    public void visitar(Statement stmt);
}
